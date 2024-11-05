import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import TermsCheckbox from "./TermsCheckbox";

describe("TermsCheckbox", () => {
  test("renders checkbox and label", () => {
    render(<TermsCheckbox checked={false} setChecked={jest.fn()} error="" />);
    expect(screen.getByLabelText(/Agree to terms/i)).toBeInTheDocument();
  });

  test("displays error message", () => {
    render(<TermsCheckbox checked={false} setChecked={jest.fn()} error="You must agree to the terms" />);
    expect(screen.getByText(/You must agree to the terms/i)).toBeInTheDocument();
  });

  test("updates checked state on change", () => {
    const setChecked = jest.fn();
    render(<TermsCheckbox checked={false} setChecked={setChecked} error="" />);
    
    fireEvent.click(screen.getByLabelText(/Agree to terms/i));
    expect(setChecked).toHaveBeenCalledWith(true);
  });
});
