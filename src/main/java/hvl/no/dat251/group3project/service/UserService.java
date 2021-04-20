package hvl.no.dat251.group3project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import hvl.no.dat251.group3project.entity.User.Preferences;
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
				Map<String, Object> addr = (Map<String, Object>) ds.get("address");
				Address savedAddress = addressService.saveAddress(addr);
				setAddress(tempUser, savedAddress);
				setAge(tempUser, (ds.getLong("age")).intValue());
				setFname(tempUser, ds.getString("fname"));
				setLname(tempUser, ds.getString("lname"));
				setEmail(tempUser, ds.getString("email"));
				if (ds.getString("gender") != null)
					setGender(tempUser, User.Gender.valueOf((ds.getString("gender")).toUpperCase()));
				List<String> pref = (List<String>) ds.get("preferences");
				setPreferences(tempUser, pref);
				setPhoneNumber(tempUser, ds.getLong("phoneNumber"));
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

	public User getUser(OAuth2AuthenticationToken authentication) {
		OAuth2AuthorizedClient client = authorizedClientService
				.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
		Optional<User> userOpt = userRepository.findById(main.getUserID(client));
		if (!userOpt.isPresent()) {
			return null;
		}
		return userOpt.get();
	}

	public void setContactByEmail(User user, Boolean contactByEmail) {
		user.setContactByEmail(contactByEmail);
	}

	public void setUID(User user, String uID) {
		user.setUID(uID);
	}

	public void setPhoneNumber(User user, Long phoneNumber) {
		user.setPhoneNumber(phoneNumber);
	}

	public User saveUser(Map<String, Object> owner) {
		List<String> prefStrings = (List<String>) owner.get("preferences");
		List<Preferences> prefs = new ArrayList<>();

		for (String pref : prefStrings) {
			prefs.add(User.Preferences.valueOf(pref));
		}

		Gender gender = null;
		if ((String) owner.get("gender") != null)
			gender = User.Gender.valueOf((String) owner.get("gender"));

		User user = new User((String) owner.get("uid"), (String) owner.get("fname"), (String) owner.get("lname"),
				(String) owner.get("email"), (Integer) owner.get("age"), (Boolean) owner.get("contactByEmail"), gender,
				(Long) owner.get("phoneNumber"), prefs, (Address) owner.get("address"));

		return save(user);
	}
}