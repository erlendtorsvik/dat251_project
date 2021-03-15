package hvl.no.dat251.group3project.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;

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
	private AddressService addressService;

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

	public User findById(String uid) {
		if (findByIdIsPresent(uid)) {
			return userRepository.findById(uid).get();
		}
		return null;
	}

	public void gettAllFromFb() {
		CollectionReference userCR = fb.getFirebase().collection("Users");
		User tempUser = new User();
		for (DocumentReference doc : userCR.listDocuments()) {
			String uID = doc.getId();
			try {
				DocumentSnapshot ds = userCR.document(String.valueOf(uID)).get().get();
				tempUser.setUID(uID);
				HashMap addr = (HashMap) ds.get("address");
				Address savedAddress =new Address((String)addr.get("streetName"),(String)addr.get("country"),
						((Long)addr.get("postalCode")).intValue(),(String)addr.get("houseNumber"),
						(String)addr.get("county"),(String)addr.get("municipality"));
				setAddress(tempUser,savedAddress);
				addressService.save(savedAddress);
				setAge(tempUser, (ds.getLong("age")).intValue());
				setFname(tempUser, ds.getString("fname"));
				setLname(tempUser, ds.getString("lname"));
				setEmail(tempUser, ds.getString("email"));
				if (ds.getString("gender") != null)
					setGender(tempUser, User.Gender.valueOf((ds.getString("gender")).toUpperCase()));
				List<String> pref = (List<String>) ds.get("preferences");
				setPreferences(tempUser, pref);
				save(tempUser);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	public User save(User user) {
		userRepository.save(user);
		if (fb != null) {
			CollectionReference userCR = fb.getFirebase().collection("Users");
			userCR.document(String.valueOf((user.getUID()))).set(user);
		}
		return user;
	}

	public void delete(User userSample) {
		userRepository.delete(userSample);
	}

	public void setEmail(User user, String email) {
		user.setEmail(email);
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