import React, { useEffect, useState } from 'react';
import axios from 'axios';

const SectorsForm = () => {
  const [name, setName] = useState('');
  const [sectors, setSectors] = useState([]);
  const [selectedSectors, setSelectedSectors] = useState([]);
  const [termsAgreed, setTermsAgreed] = useState(false);
  const [formErrors, setFormErrors] = useState({});

  useEffect(() => {
    const fetchSectors = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/sectors');
        const sectorsData = response.data;

        const childSectorIds = new Set();

        function collectChildIds(sector) {
          if (sector.children && sector.children.length > 0) {
            sector.children.forEach((child) => {
              childSectorIds.add(child.id);              
              collectChildIds(child);
            });
          }
        }

        sectorsData.forEach((sector) => {
          collectChildIds(sector);
        });

        const rootSectors = sectorsData.filter((sector) => !childSectorIds.has(sector.id));

        setSectors(rootSectors);
      } catch (error) {
        console.error('Error fetching sectors:', error);
      }
    };
    fetchSectors();
  }, []);

    const fetchUserData = async (id) => {
        try {
        const response = await axios.get(`http://localhost:8080/api/user-inputs/${id}`);
        const userData = response.data;
        setName(userData.name);
        setSelectedSectors(userData.selectedSectors.map(String));
        setTermsAgreed(userData.agreeToTerms);
        } catch (error) {
        console.error('Error fetching user data:', error);
        }
    };

    const renderSectorOptions = (sectors, level = 0) => {
    return sectors.map((sector) => {
        return (
        <React.Fragment key={sector.id}>
            <option value={sector.id}>
            {`${"\u00A0".repeat(level * 4)}${sector.name}`}
            </option>
            {sector.children &&
            sector.children.length > 0 &&
            renderSectorOptions(sector.children, level + 1)}
        </React.Fragment>
        );
    });
    };

    const validateForm = () => {
        const errors = {};
        if (!name.trim()) {
        errors.name = 'Name is required';
        }
        if (selectedSectors.length === 0) {
        errors.selectedSectors = 'At least one sector must be selected';
        }
        if (!termsAgreed) {
        errors.termsAgreed = 'You must agree to the terms';
        }
        setFormErrors(errors);
        return Object.keys(errors).length === 0;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validateForm()) { 
            return;
          }

        const userInputDto = {
        name: name,
        selectedSectors: selectedSectors.map(Number), 
        };

        try {
        const response = await axios.post('http://localhost:8080/api/user-inputs', userInputDto);
        console.log('Form successfully submitted:', response.data);

        fetchUserData(response.data.id); 
        } catch (error) {
        console.error('Error submitting form:', error);
        }
    };

  return (
    <form onSubmit={handleSubmit}>
      <p>Please enter your name and pick the sectors you are currently involved in.</p>

      <label htmlFor="name">Name:</label>
      <input
        type="text"
        id="name"
        name="name"
        value={name}
        onChange={(e) => setName(e.target.value)}
      />
       {formErrors.name && <div className="error">{formErrors.name}</div>}

      <br /><br />

      <label htmlFor="sectors">Sectors:</label>
      <select
        id="sectors"
        name="sectors"
        multiple
        size="5"
        value={selectedSectors}
        onChange={(e) =>
          setSelectedSectors(Array.from(e.target.selectedOptions, (option) => option.value))
        }
      >
        {renderSectorOptions(sectors)}
      </select>
      {formErrors.selectedSectors && <div className="error">{formErrors.selectedSectors}</div>}

      <br /><br />

      <input
        type="checkbox"
        id="terms"
        name="terms"
        checked={termsAgreed}
        onChange={(e) => setTermsAgreed(e.target.checked)}
      />
      <label htmlFor="terms">Agree to terms</label>
      {formErrors.termsAgreed && <div className="error">{formErrors.termsAgreed}</div>}

      <br /><br />

      <input type="submit" value="Save" />
    </form>
  );
};

export default SectorsForm;
