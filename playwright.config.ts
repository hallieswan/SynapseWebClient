import { defineConfig, devices } from '@playwright/test'
import dotenv from 'dotenv'

export const STORAGE_STATE = 'playwright/.auth/user.json'

/**
 * Read environment variables from file.
 * https://github.com/motdotla/dotenv
 */
dotenv.config()

/**
 * See https://playwright.dev/docs/test-configuration.
 */
export default defineConfig({
  testDir: './e2e',
  /* Run tests in files in parallel */
  fullyParallel: true,
  /* Fail the build on CI if you accidentally left test.only in the source code. */
  forbidOnly: !!process.env.CI,
  /* Limit the number of failures on CI to save resources */
  maxFailures: process.env.CI ? 10 : undefined,
  /* Retry on CI only */
  retries: process.env.CI ? 2 : 0,
  /* Opt out of parallel tests on CI. */
  workers: process.env.CI ? 1 : undefined,
  /* Reporter to use. See https://playwright.dev/docs/test-reporters */
  reporter: 'html',
  /* Shared settings for all the projects below. See https://playwright.dev/docs/api/class-testoptions. */
  use: {
    /* Base URL to use in actions like `await page.goto('/')`. */
    baseURL: 'http://127.0.0.1:8888/Portal.html',

    /* Collect trace when retrying the failed test. See https://playwright.dev/docs/trace-viewer */
    trace: process.env.CI ? 'on-first-retry' : 'retain-on-failure',
  },

  /* Configure projects for major browsers */
  projects: [
    // Setup project
    { name: 'setup', testMatch: /.*\.setup\.ts/ },

    {
      name: 'chromium - logged out',
      use: {
        ...devices['Desktop Chrome'],
      },
      testIgnore: ['**/*_loggedin.spec.ts', '**/*.setup.ts'],
    },

    {
      name: 'chromium - logged in',
      use: {
        ...devices['Desktop Chrome'],
        // Use prepared auth state.
        storageState: STORAGE_STATE,
      },
      testIgnore: ['**/*_loggedout.spec.ts'],
      dependencies: ['setup'],
    },

    /* {
      name: 'firefox',
      use: { ...devices['Desktop Firefox'] },
    },

    {
      name: 'webkit',
      use: { ...devices['Desktop Safari'] },
    }, */

    /* Test against mobile viewports. */
    // {
    //   name: 'Mobile Chrome',
    //   use: { ...devices['Pixel 5'] },
    // },
    // {
    //   name: 'Mobile Safari',
    //   use: { ...devices['iPhone 12'] },
    // },

    /* Test against branded browsers. */
    // {
    //   name: 'Microsoft Edge',
    //   use: { ...devices['Desktop Edge'], channel: 'msedge' },
    // },
    // {
    //   name: 'Google Chrome',
    //   use: { ...devices['Desktop Chrome'], channel: 'chrome' },
    // },
  ],

  /* Run your local dev server before starting the tests */
  webServer: {
    command: 'mvn gwt:run',
    url: 'http://127.0.0.1:8888/',
    reuseExistingServer: !process.env.CI,
  },
})
