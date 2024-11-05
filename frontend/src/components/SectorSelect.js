import React from "react";

const SectorSelect = ({ sectors, selectedSectors, setSelectedSectors, error }) => {
  const renderSectorOptions = (sectors, level = 0) =>
    sectors.map((sector) => (
      <React.Fragment key={sector.id}>
        <option value={sector.id}>{`${"\u00A0".repeat(level * 4)}${sector.name}`}</option>
        {sector.children && renderSectorOptions(sector.children, level + 1)}
      </React.Fragment>
    ));

  return (
    <div>
      <label htmlFor="sectors">Sectors:</label>
      <select
        id="sectors"
        name="sectors"
        multiple
        size="5"
        value={selectedSectors}
        onChange={(e) => setSelectedSectors(Array.from(e.target.selectedOptions, (option) => option.value))}
      >
        {renderSectorOptions(sectors)}
      </select>
      {error && <div className="error">{error}</div>}
    </div>
  );
};

export default SectorSelect;
