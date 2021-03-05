package hvl.no.dat251.group3project.controllers;

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
import hvl.no.dat251.group3project.services.ItemService;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/allItems")
    ResponseEntity<List<Item>> getAllItems() {
        return new ResponseEntity<>(itemService.findAll(), HttpStatus.OK);
    }


    @GetMapping("/items")
    public String searchItem(@RequestParam String name, Model model, OAuth2AuthenticationToken authentication) {
        List<Item> items = itemService.findByWord(name);

        model.addAttribute("items", items);
        model.addAttribute("message", "Succesfully searched polls");
        return "items";

    }
    
    @GetMapping("/search")
    public String search(Model model, OAuth2AuthenticationToken authentication) {
    	model.addAttribute("message", "Hello");
    	return "items";
    }
}