package hvl.no.dat251.group3project.service;

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
	private OAuth2AuthorizedClientService authorizedClientService;

	mainRestController main = new mainRestController();

	public UserService(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public boolean findByIdIsPresent(String uid) {
		DocumentReference dr = fb.getFirebase().collection("Users").document(String.valueOf(uid));
		try {
			DocumentSnapshot ds = dr.get().get();
			return ds.exists();
				
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public User save(User user) {
		CollectionReference userCR = fb.getFirebase().collection("Users");
		userCR.document(String.valueOf((user.getUID()))).set(user);

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
		DocumentReference dr = fb.getFirebase().collection("Users").document(String.valueOf(uid));
		User user = new User();
		try {
			DocumentSnapshot ds = dr.get().get();
			Long age = (Long) ds.get("age");
			String fname = (String) ds.get("fname");
			String lname = (String) ds.get("lname");
			String gender = (String) ds.get("gender");
			String email = (String) ds.get("email");
			List<String> preferences = (List<String>) ds.get("preferences");
			user.setUID(uid);
			user.setFname(fname);
			user.setLname(lname);
			user.setEmail(email);
			user.setAge(age.intValue());
			user.setPreferences(preferences);
			if (gender != null)
				user.setGender(User.Gender.valueOf(gender.toUpperCase()));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
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