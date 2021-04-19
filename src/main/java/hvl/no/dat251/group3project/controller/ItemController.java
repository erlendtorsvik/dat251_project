package hvl.no.dat251.group3project.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

	@Value("${app.upload.dir}")
	public String imagesDir;

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
	public String addItem(Model model, OAuth2AuthenticationToken authentication, @RequestParam String name,
			@RequestParam String description, @RequestParam Double price, @RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam("images") MultipartFile[] multipartFiles) {
		List<String> images = new ArrayList<>();
		Arrays.asList(multipartFiles).stream().forEach(file -> {
			String fileName = itemService.uploadFb(file);
			images.add(fileName);
			itemService.downloadFb(fileName);
		});
		Item newItem = new Item(name, description, price, fromDate, toDate, true);
		User user = userService.getUser(authentication);
		itemService.setOwner(newItem, user);
		itemService.setImages(newItem, images);
		itemService.save(newItem);

		List<Item> myItems = itemService.getItemsByUser(userService.getUser(authentication));
		model.addAttribute("items", myItems);
		model.addAttribute("message", "Succesfully added Item ");
		return "myItems";
	}

	@GetMapping("/items/update/{id}")
	public String getUpdateItem(@PathVariable Long id, Model model, OAuth2AuthenticationToken authentication) {
		// model.addAttribute("name", getUser(authentication));
		Item item = itemService.findById(id);
		// checking if owner tried to edit poll
		if (!userService.getUser(authentication).getUID().equals(item.getOwner().getUID())) {
			model.addAttribute("message", "You can't edit someone elses item");
			return "index";
		}
		model.addAttribute("item", item);
		return "itemUpdate";
	}

	@PostMapping("/items/update/{id}")
	public String updateItem(@RequestParam String name, @RequestParam String description, @RequestParam Double price,
			@RequestParam String fromDate, @RequestParam String toDate, @RequestParam String isAvailable,
			@PathVariable Long id, @RequestParam("images") MultipartFile[] multipartFiles, Model model,
			OAuth2AuthenticationToken authentication) {
		// model.addAttribute("name", getUser(authentication));
		Item item = itemService.findById(id);
		List<String> images = item.getImages();
		int imgInitSize = images.size();
		boolean empty = Arrays.asList(multipartFiles).stream().filter(f -> !f.isEmpty()).count() == 0;
		if (!empty) {
			Arrays.asList(multipartFiles).stream().forEach(file -> {
				String fileName = itemService.uploadFb(file);
				images.add(fileName);
				itemService.downloadFb(fileName);
			});
		}

		if (!name.isBlank())
			itemService.setName(item, name);
		if (!description.isBlank())
			itemService.setDescription(item, description);
		if (!price.isNaN())
			itemService.setPrice(item, price);
		if (!isAvailable.isBlank())
			itemService.setAvailable(item, Boolean.parseBoolean(isAvailable));
		if (!fromDate.isBlank())
			itemService.setFromDate(item, fromDate);
		if (!toDate.isBlank())
			itemService.setToDate(item, toDate);
		itemService.setImages(item, images);
		itemService.save(item);
		model.addAttribute("item", item);
		model.addAttribute("message", "Successfully updated item " + id);

		if (images.size() != imgInitSize)
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
			}

		return "itemUpdate";
	}

	@GetMapping("/items/delete/{id}")
	public String deleteItem(@PathVariable Long id, Model model, OAuth2AuthenticationToken authentication) {
		List<String> images = itemService.findById(id).getImages();
		for (int i = 0; i < images.size(); i++) {
			String img = images.get(0);
			i--;
			deleteImage(id, img, model);
		}
		itemService.deleteById(id);
		List<Item> myItems = itemService.getItemsByUser(userService.getUser(authentication));
		model.addAttribute("items", myItems);
		model.addAttribute("message", "Successfully deleted item " + id);
		return "myItems";
	}

	@GetMapping("/items/{id}")
	public String getItem(@PathVariable Long id, Model model) {
		model.addAttribute("item", itemService.findById(id));

		return "item";
	}

	@GetMapping("/items/{id}/deleteImage/{image}")
	public String deleteImage(@PathVariable Long id, @PathVariable String image, Model model) {

		Item item = itemService.findById(id);

		List<String> itemImages = item.getImages();
		itemImages.remove(image);
		itemService.save(item);
		File f = new File(imagesDir + image);
		f.delete();
		itemService.deleteFbFile(image);

		model.addAttribute("item", item);
		model.addAttribute("message", "Successfully deleted image " + image);
		return "itemUpdate";
	}

}