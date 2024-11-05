import React from "react";
import { render, screen } from "@testing-library/react";
import LoadingIndicator from "./LoadingIndicator";

describe("LoadingIndicator", () => {
  test("renders loading message", () => {
    render(<LoadingIndicator />);
    expect(screen.getByText(/Loading.../i)).toBeInTheDocument();
  });
});
