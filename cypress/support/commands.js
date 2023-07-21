import '@testing-library/cypress/add-commands'

// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
Cypress.Commands.add('login', (username, password) => {
  cy.session(
    [username],
    () => {
      cy.visit('/')
      cy.contains('a', 'Log in to Synapse').click()
      cy.url().should('include', '#!LoginPlace:0')
      cy.contains('Sign in with your email').click()
      cy.get('#username').type(username)
      cy.get('#current-password').type(password)
      cy.contains('Sign in').should('have.length', 1).click()
      cy.contains('Your Projects')
    },
    {
      cacheAcrossSpecs: true,
    },
  )
})
Cypress.Commands.add('loginUser', () => {
  cy.login(Cypress.env('USERNAME'), Cypress.env('PASSWORD'))
})

//
//
// -- This is a child command --
// Cypress.Commands.add('drag', { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add('dismiss', { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite('visit', (originalFn, url, options) => { ... })
