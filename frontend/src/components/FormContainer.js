import React, { useEffect, useState } from "react";
import axios from "axios";
import NameInput from "./NameInput";
import SectorSelect from "./SectorSelect";
import TermsCheckbox from "./TermsCheckbox";
import LoadingIndicator from "./LoadingIndicator";
import './FormContainer.css';

const FormContainer = () => {
  const [name, setName] = useState("");
  const [sectors, setSectors] = useState([]);
  const [selectedSectors, setSelectedSectors] = useState([]);
  const [termsAgreed, setTermsAgreed] = useState(false);
  const [formErrors, setFormErrors] = useState({});
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchSectors = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/sectors");
        const sectorsData = response.data;
        setSectors(processSectors(sectorsData));
      } catch (error) {
        console.error("Error fetching sectors:", error);
      }
    };
    fetchSectors();
  }, []);

  const processSectors = (sectorsData) => {
    const childSectorIds = new Set();
    const collectChildIds = (sector) => {
      if (sector.children) {
        sector.children.forEach((child) => {
          childSectorIds.add(child.id);
          collectChildIds(child);
        });
      }
    };
    sectorsData.forEach(collectChildIds);
    return sectorsData.filter((sector) => !childSectorIds.has(sector.id));
  };

  const validateForm = () => {
    const errors = {};
    if (!name.trim()) errors.name = "Name is required";
    if (selectedSectors.length === 0) errors.selectedSectors = "At least one sector must be selected";
    if (!termsAgreed) errors.termsAgreed = "You must agree to the terms";
    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    const userInputDto = { name, selectedSectors: selectedSectors.map(Number), agreeToTerms: termsAgreed };

    try {
      setLoading(true);
      const userId = sessionStorage.getItem("userId");
      const response = userId
        ? await axios.put(`http://localhost:8080/api/user-inputs/${userId}`, userInputDto)
        : await axios.post("http://localhost:8080/api/user-inputs", userInputDto);
      sessionStorage.setItem("userId", response.data.id);
    } catch (error) {
      console.error("Error submitting form:", error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <LoadingIndicator />;

  return (
    <form onSubmit={handleSubmit}>
      <p>Please enter your name and pick the sectors you are currently involved in.</p>
      <NameInput value={name} setValue={setName} error={formErrors.name} />
      <SectorSelect sectors={sectors} selectedSectors={selectedSectors} setSelectedSectors={setSelectedSectors} error={formErrors.selectedSectors} />
      <TermsCheckbox checked={termsAgreed} setChecked={setTermsAgreed} error={formErrors.termsAgreed} />
      <input type="submit" value="Save" />
    </form>
  );
};

export default FormContainer;
