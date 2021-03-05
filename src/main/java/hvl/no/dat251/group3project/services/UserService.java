package hvl.no.dat251.group3project.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;

import hvl.no.dat251.group3project.controllers.mainRestController;
import hvl.no.dat251.group3project.entity.Address;
import hvl.no.dat251.group3project.entity.User;
import hvl.no.dat251.group3project.entity.User.Gender;
import hvl.no.dat251.group3project.firebase.FBInitialize;
import hvl.no.dat251.group3project.repositories.IUserRepository;

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
		return userRepository.findById(uid).isPresent();
	}

	public User save(User user) {
		CollectionReference pollCR = fb.getFirebase().collection("Users");
		pollCR.document(String.valueOf((user.getUID()))).set(user);
		DocumentReference dr = fb.getFirebase().collection("Polls").document(String.valueOf(user.getUID()));
		dr.update("UserID", user.getUID());

		userRepository.save(user);
		return user;
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public void setFname(User user, String fname) {
		user.setFname(fname);
		userRepository.save(user);
	}

	public void setLname(User user, String lname) {
		user.setLname(lname);
		userRepository.save(user);
	}

	public void setAge(User user, int age) {
		user.setAge(age);
		userRepository.save(user);
	}

	public void setGender(User user, Gender gender) {
		user.setGender(gender);
		userRepository.save(user);
	}

	public void setAddress(User user, Address address) {
		user.setAddress(address);
		userRepository.save(user);
	}

	public User findById(String uid) {
		Optional<User> userOpt = userRepository.findById(uid);
		if (userOpt.isPresent()) {
			return userOpt.get();
		}
		return null;
	}

	public String getUserName(OAuth2AuthenticationToken authentication) {
		OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(),
				authentication.getName());
		Optional<User> userOpt = userRepository.findById(main.getUserID(client));
		if (!userOpt.isPresent()) {
			return "null";
		}
		User user = userOpt.get();
		return user.getFname() + " " + user.getLname();
	}

}