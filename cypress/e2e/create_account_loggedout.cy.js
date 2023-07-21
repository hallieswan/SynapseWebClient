describe('Create Account', () => {
  it('should show an alert when an invalid email address is used', () => {
    cy.visit('/')
    cy.contains('a', 'Register Now').click()
    cy.url().should('include', '#!RegisterAccount:0')
    cy.get('.pageHeaderTitle').contains('Create Synapse Account')
    cy.get('.form-control').first().type('test123')
    cy.get('input[placeholder="Your email address"').type('test123')
    cy.contains('Send registration info').click()
    cy.contains('Email address is not valid').should('have.length', 1)
  })
})
