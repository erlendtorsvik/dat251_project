package hvl.no.dat251.group3project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hvl.no.dat251.group3project.entity.Item;
import hvl.no.dat251.group3project.service.ItemService;
import hvl.no.dat251.group3project.service.UserService;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/allItems")
    ResponseEntity<List<Item>> getAllItems() {
        return new ResponseEntity<>(itemService.findAll(), HttpStatus.OK);
    }


    @GetMapping("/items")
    public String searchItem(@RequestParam String name, Model model) {
        List<Item> items = itemService.findByWord(name);

        model.addAttribute("items", items);
        model.addAttribute("message", "Succesfully searched items");
        return "items";
    }
    
    @GetMapping("/search")
    public String search(Model model) {
    	model.addAttribute("message", "Hello");
    	return "items";
    }
	
	@GetMapping("/myItems")
	public String getMyItems(Model model, OAuth2AuthenticationToken authentication) {
		List<Item> myItems = itemService.getItemsByUser(userService.getUser(authentication));
		model.addAttribute("items", myItems);
		model.addAttribute("message", "Hello");
		return "items";
	}
}