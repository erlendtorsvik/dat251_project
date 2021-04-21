Feature: Search for items

  Scenario: Search for an item by its name
    Given I have a search field on the web page
    When I search for an item with search word "Ski"
    Then I should get a list of results containing the name "Ski"


