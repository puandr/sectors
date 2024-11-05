import React from "react";

const NameInput = ({ value, setValue, error }) => (
  <div>
    <label htmlFor="name">Name:</label>
    <input type="text" id="name" name="name" value={value} onChange={(e) => setValue(e.target.value)} />
    {error && <div className="error">{error}</div>}
  </div>
);

export default NameInput;
