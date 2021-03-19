package hvl.no.dat251.group3project.controller;

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

import hvl.no.dat251.group3project.entity.Address;
import hvl.no.dat251.group3project.entity.User;
import hvl.no.dat251.group3project.service.AddressService;
import hvl.no.dat251.group3project.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AddressService addresService;

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
		userService.gettAllFromFb();
		if (authentication != null)
			client = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(),
					authentication.getName());
		User newUser = main.saveUser(client);
		if (!userService.findByIdIsPresent(newUser.getUID())) {
			Address addr = new Address();
			userService.setAddress(newUser, addr);
			addresService.save(addr);
			userService.save(newUser);
		}
		model.addAttribute("name", newUser.getFname() + " " + newUser.getLname());
		return "index";
	}

	@GetMapping("/user")
	public String getUser(Model model, OAuth2AuthenticationToken authentication) {
		client = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(),
				authentication.getName());
		String uID = main.getUserID(client);
		User newUser = userService.findById(uID);
		model.addAttribute("name", userService.getUser(authentication).getFname());
		model.addAttribute("user", newUser);
		return "user";
	}

	@PostMapping("/user/{uID}")
	public String updateUser(@PathVariable String uID, @RequestParam String fname, @RequestParam String lname,
			@RequestParam String email, @RequestParam int age, @RequestParam String gender,
			@RequestParam List<String> preferences, @RequestParam String streetName, @RequestParam String municipality,
			@RequestParam String county, @RequestParam String houseNumber, @RequestParam int postalCode,
			@RequestParam String country, @RequestParam(defaultValue = "false") Boolean contactByEmail, Model model,
			OAuth2AuthenticationToken authentication) {
		model.addAttribute("name", userService.getUser(authentication).getFname());
		User user = userService.findById(uID);

		if (!fname.isBlank())
			userService.setFname(user, fname);
		if (!lname.isBlank())
			userService.setLname(user, lname);
		if (!email.isBlank())
			userService.setEmail(user, email);
		if (age != user.getAge())
			userService.setAge(user, age);
		if (!gender.isBlank()) {
			userService.setGender(user, User.Gender.valueOf(gender.toUpperCase()));
		}
		if (!preferences.isEmpty())
			userService.setPreferences(user, preferences);
		Address addr = new Address();
		if (!streetName.isBlank())
			addresService.setStreetName(addr, streetName);
		if (!municipality.isBlank())
			addresService.setMunicipality(addr, municipality);
		if (!county.isBlank())
			addresService.setCounty(addr, county);
		if (!houseNumber.isBlank())
			addresService.setHouseNumber(addr, houseNumber);
		if (!country.isBlank())
			addresService.setCountry(addr, country);
		if (contactByEmail)
			userService.setContactByEmail(user, true);
		else
			userService.setContactByEmail(user, false);

		addresService.setPostalCode(addr, postalCode);
		userService.setAddress(user, addr);

		userService.save(user);
		model.addAttribute("user", user);
		model.addAttribute("message", "Succesfully updated user");

		return "user";
	}
}