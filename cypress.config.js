const { defineConfig } = require('cypress')
const env = require('dotenv').config().parsed

module.exports = defineConfig({
  e2e: {
    baseUrl: 'http://127.0.0.1:8888',
    env: {
      ...env,
    },
    experimentalStudio: true,
    setupNodeEvents(on, config) {},
  },
})
