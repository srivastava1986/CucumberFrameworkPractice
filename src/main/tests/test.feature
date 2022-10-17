Feature: Amazon
  Scenario: Verify that the right product is added in the cart
    Given I launch Chrome Browser
    When I open Amazon homepage
    Then I add product into the cart
    And close Browser