// Based on this guide: https://playwright.dev/docs/auth#basic-shared-account-in-all-tests
import { expect, test as setup } from '@playwright/test'
import { STORAGE_STATE } from '../playwright.config'

setup('authenticate', async ({ page }) => {
  // Perform authentication steps
  await page.goto('/')
  await page.getByRole('link', { name: 'Log in to Synapse' }).first().click()
  await page.getByRole('button', { name: 'Sign in with your email' }).click()
  await page
    .getByLabel('Username or Email Address')
    .fill(process.env.USERNAME || '')
  await page.getByLabel('Password').fill(process.env.PASSWORD || '')
  await page.getByRole('button', { name: 'Sign in' }).click()

  // Wait until the page reaches a state where all cookies are set.
  await expect(
    page.getByRole('heading', { name: 'Your Projects' }),
  ).toBeVisible()

  // End of authentication steps.

  await page.context().storageState({ path: STORAGE_STATE })
})
