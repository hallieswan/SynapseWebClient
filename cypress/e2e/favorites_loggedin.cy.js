describe('Favorites', () => {
  it('should be visible', () => {
    cy.loginUser()
    cy.visit('/')
    cy.contains('a', 'View Your Dashboard').click()
    cy.contains('Your Projects')
    cy.get('[aria-label="Favorites"]').click()
    cy.contains('Your Favorites')
  })
})
