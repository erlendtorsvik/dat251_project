Feature: Search for items

  Scenario: Search for an item by its name
    Given I have a search field on the web page
    When I search for an item with search word "Ski" that has a description "Slalomski"
    Then I should get a list of results containing the name "Ski"

  Scenario: Want to create a new item for others to loan
    Given I have fields to fill in with name, description, price etc. to create the item ad
    When I create an item with name "Canoe", description "Two man canoe" and price "700.0"
    Then I should create an item containing the name "Canoe", description and price I chose for the item ad

    Scenario: Want to update an item ad
      Given I have an item ad with name "Bicycle", description "For grown man" and price "500"
      When I update the item fields to have a name "BMX Bicycle" and update price from 500 to "950"
      Then I should update the name field and the tag field of the item