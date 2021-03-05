package hvl.no.dat251.group3project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hvl.no.dat251.group3project.entity.User;
import hvl.no.dat251.group3project.services.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;

	OAuth2AuthorizedClient client;

	mainRestController main = new mainRestController();

	@GetMapping("/users")
	ResponseEntity<List<User>> getAllUsers() {
		return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
	}

	@GetMapping("/userAdd")
	public String userAdd(Model model, OAuth2AuthenticationToken authentication) {
		client = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(),
				authentication.getName());
		User newUser = main.saveUser(client);
		if (!userService.findByIdIsPresent(newUser.getUID()))
			userService.save(newUser);
		model.addAttribute("name", newUser.getFname() + " " + newUser.getLname());
		return "welcome";
	}
	
	@GetMapping("/user")
	public String getUser(Model model, OAuth2AuthenticationToken authentication) {
		client = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(),
				authentication.getName());
		String uID = main.getUserID(client);
		User newUser = userService.findById(uID);
		model.addAttribute("name", userService.getUserName(authentication));
		model.addAttribute("user", newUser);
		return "user";
	}
	
	@PostMapping("/user/{uID}")
	public String updateUser(@PathVariable String uID, @RequestParam String fname, @RequestParam String lname, @RequestParam int age, @RequestParam String gender,Model model, OAuth2AuthenticationToken authentication) {
		model.addAttribute("name", userService.getUserName(authentication));
		User user = userService.findById(uID);
		
		if (!fname.isBlank())
			userService.setFname(user, fname);
		if (!lname.isBlank())
			userService.setLname(user, lname);
		if (age != user.getAge())
			userService.setAge(user, age);
		if (!gender.isBlank()) {
			userService.setGender(user, User.Gender.valueOf(gender.toUpperCase()));
		}
		userService.save(user);
		model.addAttribute("user", user);
		model.addAttribute("message", "Successfully updated user");
		
		return "user";
	}
}
