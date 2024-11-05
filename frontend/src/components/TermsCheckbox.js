import React from "react";
import './TermsCheckbox.css';

const TermsCheckbox = ({ checked, setChecked, error }) => (
    <div className="checkbox-container">
      <div className="checkbox-wrapper">
        <input
          type="checkbox"
          id="terms"
          name="terms"
          checked={checked}
          onChange={(e) => setChecked(e.target.checked)}
        />
        <label htmlFor="terms">Agree to terms</label>
      </div>
      {error && <div className="error">{error}</div>}
    </div>
  );

export default TermsCheckbox;