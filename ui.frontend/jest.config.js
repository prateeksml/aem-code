module.exports = {
  clearMocks: true,
  setupFilesAfterEnv: ["<rootDir>/test/setup.js"],
  collectCoverageFrom: ["src/main/webpack/components/**/*.{ts,js,jsx,mjs}"],
  testEnvironment: "jsdom",
};
