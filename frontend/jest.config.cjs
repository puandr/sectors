// module.exports = {
//     transform: {
//     //   "^.+\\.jsx?$": "babel-jest", // Transforms JavaScript files using babel-jest
//     },
//     testEnvironment: "jsdom", // Ensures a DOM-like environment for React components
//     moduleNameMapper: {
//       "\\.(css|scss)$": "identity-obj-proxy" // Optional: mock CSS imports
//     },
//     extensionsToTreatAsEsm: [".ts", ".tsx", ".jsx"],
//     // testEnvironment: "jest-environment-node",
//     testEnvironment: "jest-environment-jsdom",
//   };
module.exports = {
  transform: {
    "^.+\\.jsx?$": "babel-jest" // Use babel-jest to transform .js and .jsx files
  },
  testEnvironment: "jsdom", // Set the environment to jsdom for React components
};
