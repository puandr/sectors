import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import NameInput from "./NameInput";

describe("NameInput", () => {
  test("renders Name input", () => {
    render(<NameInput value="" setValue={jest.fn()} error="" />);
    expect(screen.getByLabelText(/Name:/i)).toBeInTheDocument();
  });

  test("displays error message", () => {
    render(<NameInput value="" setValue={jest.fn()} error="Name is required" />);
    expect(screen.getByText(/Name is required/i)).toBeInTheDocument();
  });

  test("updates value on input change", () => {
    const setValue = jest.fn();
    render(<NameInput value="" setValue={setValue} error="" />);
    
    fireEvent.change(screen.getByLabelText(/Name:/i), { target: { value: "John Doe" } });
    expect(setValue).toHaveBeenCalledWith("John Doe");
  });
});
