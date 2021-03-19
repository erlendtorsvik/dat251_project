package hvl.no.dat251.group3project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hvl.no.dat251.group3project.entity.Item;
import hvl.no.dat251.group3project.entity.User;
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
		itemService.gettAllFromFb();
		List<Item> myItems = itemService.getItemsByUser(userService.getUser(authentication));
		model.addAttribute("items", myItems);
		model.addAttribute("message", "Hello");
		return "myItems";
	}

	@PostMapping("/addItem")
	public String addItem(Model model, OAuth2AuthenticationToken authentication,
						  @RequestParam String name, @RequestParam String description, @RequestParam Double price) {
		Item newItem = new Item(name, description, price, true);
		User user = userService.getUser(authentication);
		itemService.setOwner(newItem, user);
		itemService.save(newItem);

		List<Item> myItems = itemService.getItemsByUser(userService.getUser(authentication));
		model.addAttribute("items", myItems);
		model.addAttribute("message", "Succesfully added Item");
		return "myItems";
	}

	@GetMapping("/items/update/{id}")
	public String getUpdatePoll(@PathVariable Long id, Model model, OAuth2AuthenticationToken authentication) {
		//model.addAttribute("name", getUser(authentication));
		Item item = itemService.findById(id);
		// checking if owner tried to edit poll
		if (!userService.getUser(authentication).getUID().equals(item.getOwner().getUID())) {
			model.addAttribute("message", "You can't edit someone elses poll");
			return "index";
		}
		model.addAttribute("item", item);
		return "itemUpdate";
	}

	@PostMapping("/items/update/{id}")
	public String updateItem(@RequestParam String name, @RequestParam String description,
							 @RequestParam Double price, @RequestParam String isAvailable, @PathVariable Long id,
							 Model model, OAuth2AuthenticationToken authentication) {
		//model.addAttribute("name", getUser(authentication));
		Item item = itemService.findById(id);

		if (!name.isBlank())
			itemService.setName(item, name);
		if (!description.isBlank())
			itemService.setDescription(item, description);
		if (!price.isNaN())
			itemService.setPrice(item, price);
		if (!isAvailable.isBlank())
			itemService.setAvailable(item, Boolean.parseBoolean(isAvailable));
		itemService.save(item);
		model.addAttribute("item", item);
		model.addAttribute("message", "Successfully updated item" + id);

		return "itemUpdate";
	}

	@GetMapping("/items/delete/{id}")
	public String deleteItem(@PathVariable Long id, Model model, OAuth2AuthenticationToken authentication) {
		itemService.deleteById(id);

		List<Item> myItems = itemService.getItemsByUser(userService.getUser(authentication));
		model.addAttribute("items", myItems);
		model.addAttribute("message", "Successfully deleted item" + id);
		return "myitems";
	}

	@GetMapping("/items/{id}")
	public String getItem(@PathVariable Long id, Model model) {
		model.addAttribute("item",itemService.findById(id));

		return "item";
	}

}