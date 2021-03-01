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
		System.out.println(authentication.getAuthorizedClientRegistrationId());
		User newUser = main.saveUser(client);
		if (!userService.findByIdIsPresent(newUser.getUID()))
			userService.save(newUser);
		model.addAttribute("name", newUser.getFname() + " " + newUser.getLname());
		return "welcome";
	}
}
