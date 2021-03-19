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

import hvl.no.dat251.group3project.entity.Address;
import hvl.no.dat251.group3project.service.AddressService;

@Controller
public class AddressController {

	@Autowired
	private AddressService addressService;

	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;

	OAuth2AuthorizedClient client;

	mainRestController main = new mainRestController();

	@GetMapping("/addresses")
	ResponseEntity<List<Address>> getAllAddresses() {
		return new ResponseEntity<>(addressService.findAll(), HttpStatus.OK);
	}

	@GetMapping("/map")
	public String getUser(Model model, OAuth2AuthenticationToken authentication) {
		return "map";
	}
}
// Cris is col