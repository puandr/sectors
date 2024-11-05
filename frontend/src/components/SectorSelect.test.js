import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import SectorSelect from "./SectorSelect";

const mockSectors = [
  { id: "1", name: "IT", children: [{ id: "2", name: "Development" }] },
  { id: "3", name: "HR" },
];

describe("SectorSelect", () => {
  test("renders sector options", () => {
    render(<SectorSelect sectors={mockSectors} selectedSectors={[]} setSelectedSectors={jest.fn()} error="" />);
    
    expect(screen.getByText("IT")).toBeInTheDocument();
    expect(screen.getByText("Development")).toBeInTheDocument();
    expect(screen.getByText("HR")).toBeInTheDocument();
  });

  test("displays error message", () => {
    render(<SectorSelect sectors={mockSectors} selectedSectors={[]} setSelectedSectors={jest.fn()} error="Please select at least one sector" />);
    expect(screen.getByText(/Please select at least one sector/i)).toBeInTheDocument();
  });

  test("updates selected sectors on change", () => {
    const setSelectedSectors = jest.fn();
    render(<SectorSelect sectors={mockSectors} selectedSectors={[]} setSelectedSectors={setSelectedSectors} error="" />);
    
    fireEvent.change(screen.getByLabelText(/Sectors:/i), { target: { options: [{ value: "1", selected: true }] } });
    expect(setSelectedSectors).toHaveBeenCalledWith(["1"]);
  });
});
