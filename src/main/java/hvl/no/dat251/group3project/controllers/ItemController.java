package hvl.no.dat251.group3project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import hvl.no.dat251.group3project.entity.Item;
import hvl.no.dat251.group3project.services.ItemService;

@RestController
public class ItemController {

	@Autowired
	private ItemService itemService;

	@GetMapping("/items")
	ResponseEntity<List<Item>> getAllItems() {
		return new ResponseEntity<>(itemService.findAll(), HttpStatus.OK);
	}
}
