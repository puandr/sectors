// FormContainer.test.js
import React from "react";
import { render, screen, waitFor, fireEvent } from "@testing-library/react";
import FormContainer from "./FormContainer";
import axios from "axios";
import '@testing-library/jest-dom';

jest.mock("axios");

describe("FormContainer", () => {
  test("fetches and displays sectors data", async () => {
    // Mock axios.get to return a specific sectors structure
    axios.get.mockResolvedValueOnce({
      data: [
        { id: "1", name: "IT", children: [{ id: "2", name: "Development" }] },
        { id: "3", name: "HR" },
      ],
    });

    render(<FormContainer />);

    // Wait for the component to finish fetching
    await waitFor(() => expect(axios.get).toHaveBeenCalledTimes(1));

    // Additional checks for sector data rendering if needed
    expect(screen.getByLabelText(/Sectors:/i)).toBeInTheDocument();
  });

  test("submits form with valid data", async () => {
    // Mock the sectors API
    axios.get.mockResolvedValueOnce({
      data: [
        { id: "1", name: "IT", children: [] },
        { id: "2", name: "HR", children: [] },
      ],
    });

    // Mock the form submission (POST) response
    axios.post.mockResolvedValueOnce({
      data: { id: "123" },
    });

    render(<FormContainer />);

    // Fill in the form fields
    fireEvent.change(screen.getByLabelText(/Name:/i), { target: { value: "John Doe" } });
    fireEvent.change(screen.getByLabelText(/Sectors:/i), { target: { value: ["1"] } });
    fireEvent.click(screen.getByLabelText(/Agree to terms/i));

    // Submit the form
    fireEvent.click(screen.getByText(/Save/i));

    // Check that axios.post was called
    await waitFor(() => expect(axios.post).toHaveBeenCalledTimes(1));
    expect(sessionStorage.getItem("userId")).toBe("123");
  });
});




// import React from "react";
// import { render, screen, fireEvent, waitFor } from "@testing-library/react";
// import FormContainer from "./FormContainer";
// import axios from "axios";

// jest.mock("axios");

// describe("FormContainer", () => {
//   test("renders form elements correctly", () => {
//     render(<FormContainer />);
//     expect(screen.getByLabelText(/Name:/i)).toBeInTheDocument();
//     expect(screen.getByLabelText(/Sectors:/i)).toBeInTheDocument();
//     expect(screen.getByLabelText(/Agree to terms/i)).toBeInTheDocument();
//     expect(screen.getByText(/Save/i)).toBeInTheDocument();
//   });

//   test("displays error messages for validation", async () => {
//     render(<FormContainer />);
//     fireEvent.click(screen.getByText(/Save/i));

//     await waitFor(() => {
//       expect(screen.getByText(/Name is required/i)).toBeInTheDocument();
//       expect(screen.getByText(/At least one sector must be selected/i)).toBeInTheDocument();
//       expect(screen.getByText(/You must agree to the terms/i)).toBeInTheDocument();
//     });
//   });

//   test("submits form with valid data", async () => {
//     axios.post.mockResolvedValue({ data: { id: "123" } });
//     render(<FormContainer />);

//     fireEvent.change(screen.getByLabelText(/Name:/i), { target: { value: "John Doe" } });
//     fireEvent.change(screen.getByLabelText(/Sectors:/i), { target: { value: ["1"] } });
//     fireEvent.click(screen.getByLabelText(/Agree to terms/i));

//     fireEvent.click(screen.getByText(/Save/i));

//     await waitFor(() => expect(axios.post).toHaveBeenCalledTimes(1));
//     expect(sessionStorage.getItem("userId")).toBe("123");
//   });
// });
