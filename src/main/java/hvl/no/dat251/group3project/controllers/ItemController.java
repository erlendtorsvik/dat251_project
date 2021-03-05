package hvl.no.dat251.group3project.controllers;

import java.util.List;

import hvl.no.dat251.group3project.entity.User;
import hvl.no.dat251.group3project.repositories.IItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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


    @GetMapping("/item")
    public String searchItem(@RequestParam String name, Model model, OAuth2AuthenticationToken authentication) {
        List<Item> items = itemService.findByWord(name);

        model.addAttribute("items", items);
        return "welcome";

    }
}