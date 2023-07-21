describe('Projects', () => {
  it('should show alert when creating a new project with an existing name', () => {
    cy.loginUser()
    cy.visit('/')
    cy.contains('a', 'View Your Dashboard').click()
    cy.contains('Your Projects')
    cy.findByRole('button', { name: 'Projects' }).click()
    cy.get('.createProjectLink').click()
    cy.findByLabelText('Project Name').type('new project')
    cy.findByRole('button', { name: 'Save' }).click()
    cy.findByText(/an entity with the name.* already exists/i, { exact: false })
  })
})
