package hvl.no.dat251.group3project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import hvl.no.dat251.group3project.controller.mainRestController;
import hvl.no.dat251.group3project.entity.Address;
import hvl.no.dat251.group3project.entity.User;
import hvl.no.dat251.group3project.entity.User.Gender;
import hvl.no.dat251.group3project.firebase.FBInitialize;
import hvl.no.dat251.group3project.repository.IUserRepository;

@Service
public class UserService {

	private IUserRepository userRepository;

	@Autowired
	private FBInitialize fb;

	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;

	mainRestController main = new mainRestController();

	public UserService(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public boolean findByIdIsPresent(String uid) {
		return userRepository.existsById(uid);
	}

	public User save(User user) {
		userRepository.save(user);
		return user;
	}

	public void setFname(User user, String fname) {
		user.setFname(fname);
	}

	public void setLname(User user, String lname) {
		user.setLname(lname);
	}

	public void setAge(User user, int age) {
		user.setAge(age);
	}

	public void setGender(User user, Gender gender) {
		user.setGender(gender);
	}

	public void setAddress(User user, Address address) {
		user.setAddress(address);
	}

	public void setPreferences(User user, List<String> preferences) {
		user.setPreferences(preferences);
	}

	public User findById(String uid) {
		if (findByIdIsPresent(uid)) {
			return userRepository.findById(uid).get();
		}
		return null;
	}

	public String getUserName(OAuth2AuthenticationToken authentication) {
		OAuth2AuthorizedClient client = authorizedClientService
				.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
		Optional<User> userOpt = userRepository.findById(main.getUserID(client));
		if (!userOpt.isPresent()) {
			return "null";
		}
		User user = userOpt.get();
		return user.getFname() + " " + user.getLname();
	}

}