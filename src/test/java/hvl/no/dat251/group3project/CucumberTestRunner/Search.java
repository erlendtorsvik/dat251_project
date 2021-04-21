package hvl.no.dat251.group3project.CucumberTestRunner;

import hvl.no.dat251.group3project.entity.Item;
import hvl.no.dat251.group3project.repository.IItemRepository;
import hvl.no.dat251.group3project.service.ItemService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


public class Search extends RunnerOne {
    @Autowired
    private IItemRepository itemRepository;
    @Autowired
    private ItemService itemService;

    private Item itemSample1;

    /* @Before
     void setup(){
         itemSample1 = new Item(1L, "Ski", "Slalomski", 1000.0, true);
         itemService.save(itemSample1);
     }*/

     public String displayProduct(String product){
         return itemService.findByWord(product).get(0).getName();
     }


    @Given("I have a search field on the web page")
    public void i_have_a_search_field_on_the_web_page() {
        System.out.println("Step 1 - I am on the search page");
    }

    @When("I search for an item with search word {string}")
    public void i_search_for_an_item_with_search_word(String productName) {
        System.out.println("Step 2 - I search by name: " + productName);
        itemSample1 = new Item(1L, productName, "Slalomski", 1000.0, true);
        itemService.save(itemSample1);
    }

    @Then("I should get a list of results containing the name {string}")
    public void i_should_get_a_list_of_results_containing_the_name(String productName) {
        System.out.println(displayProduct(productName));
    }

}
