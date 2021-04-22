package hvl.no.dat251.group3project.CucumberTestRunner;

import hvl.no.dat251.group3project.entity.Item;
import hvl.no.dat251.group3project.service.ItemService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SearchAddUpdateCucumber extends RunnerOne {

    @Autowired
    private ItemService itemService;

    Item itemSampleUpdate;

    @Before
    public void setup()
    {
        Item itemSample2 = new Item(2L, "Skisko og staver", "Cucumber sko", 123.0, true);
        Item itemSample3 = new Item(3L, "Sykkel", "TourDeFrance", 12000.0, true);
        Item itemSample4 = new Item(4L, "Båt", "Robåt", 900.0, true);
        itemService.save(itemSample2);
        itemService.save(itemSample3);
        itemService.save(itemSample4);
    }
     public String displayProduct(String product) {
         List<Item> items = itemService.findByWord(product);
         StringBuilder pretty = new StringBuilder();
         for (Item item : items) {
             pretty.append("Product name: ").append(item.getName()).append(", Description: ")
                     .append(item.getDescription()).append(", Price: ").append(item.getPrice()).append("\n");
         }
         return pretty.toString();
     }


    @Given("I have a search field on the web page")
    public void i_have_a_search_field_on_the_web_page() {
        System.out.println("Step 1 - I am on the search page");
    }

    @When("I search for an item with search word {string} that has a description {string}")
    public void i_search_for_an_item_with_search_word(String productName, String description) {
        System.out.println("Step 2 - I search by name: " + productName);
        Item itemSample1 = new Item(1L, productName, description, 1000.0, true);
        itemService.save(itemSample1);
    }

    @Then("I should get a list of results containing the name {string}")
    public void i_should_get_a_list_of_results_containing_the_name(String productName) {
        System.out.println("Step 3 - The web page shows a page containing products with your search word\n"
                +displayProduct(productName));
    }


    @Given("I have fields to fill in with name, description, price etc. to create the item ad")
    public void i_have_fields_to_fill_with_name_description_price() {
        System.out.println("Step 1 - I am on the My items page");
    }

    @When("I create an item with name {string}, description {string} and price {string}")
    public void i_create_an_item_with_name_description_and_price(String productName, String description, String price) {
        System.out.println("Step 2 - I create the item with product name: " + productName + ", description: "
                + description + " and price: " + price);
        Item itemSample1 = new Item(1L, productName, description, Double.valueOf(price), true);
        itemService.save(itemSample1);
    }

    @Then("I should create an item containing the name {string}, description and price I chose for the item ad")
    public void i_should_have_successfully_created_the_item(String productName) {
        System.out.println("Step 3 - The item created should contain the name: "
                +productName+"\nThe item created contains "
                +displayProduct(productName));
    }

    @Given("I have an item ad with name {string}, description {string} and price {string}")
    public void i_have_an_item_ad_with_name_description_and_price(String productName, String description, String price)
    {
        System.out.println("Step 1 - I have an item with name: "+productName+", description: "+description+" and price: " +
                price);
        itemSampleUpdate = new Item(1L, productName, description, Double.valueOf(price), true);
        itemService.save(itemSampleUpdate);
    }

    @When("I update the item fields to have a name {string} and update price from 500 to {string}")
    public void i_update_the_item_fields_to_have_a_name_and_add_a_tag(String productName, String price) {
        System.out.println("Step 2 - I want to update the fields to have a new name: "+productName+" and a new price: "
                +price);
        itemSampleUpdate.setPrice(Double.valueOf(price));
        itemSampleUpdate.setName(productName);
        itemService.save(itemSampleUpdate);
    }

    @Then("I should update the name field and the tag field of the item")
    public void i_should_update_the_name_field_and_the_tag_field_of_the_item() {
        System.out.println("Step 3 - The updated item contains the new values: "+
                displayProduct(itemSampleUpdate.getName()));
         }

}
