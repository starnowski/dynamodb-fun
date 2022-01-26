module.exports = {
    preset: 'ts-jest',
    rootDir: "./",
    testEnvironment: "node",
    testPathIgnorePatterns: ['/dist', '<rootDir>/src/assets'],
    transform: {
        '^.+.ts?$': 'ts-jest',
    },
    moduleNameMapper: {
        '^~/(.*)$': '<rootDir>/$1',
        '@functions/(.*)$': '<rootDir>/src/functions/$1',
        '@services/(.*)$': '<rootDir>/src/services/$1',
        '@models/(.*)$': '<rootDir>/src/models/$1',
      },
    collectCoverage: true,
    verbose: true,

};