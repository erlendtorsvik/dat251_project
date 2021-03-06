package hvl.no.dat251.group3project.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import hvl.no.dat251.group3project.entity.Address;
import hvl.no.dat251.group3project.entity.User;
import hvl.no.dat251.group3project.entity.User.Preferences;
import hvl.no.dat251.group3project.firebase.FBInitialize;
import hvl.no.dat251.group3project.repository.IAddressRepository;
import hvl.no.dat251.group3project.repository.IUserRepository;
import hvl.no.dat251.group3project.service.UserService;

@SpringBootTest
public class UserServiceTest {

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IAddressRepository addressRepository;

	private UserService userService;

	private User userSample;

	@MockBean
	private FBInitialize fb;

	@BeforeEach
	void setup() {
		userSample = new User("3", "User", " Sample", "dat251@hvl.no");
		userRepository.save(userSample);
		userService = new UserService(userRepository);
	}

	@AfterEach
	void deleteAll() {
		userRepository.deleteAll();
	}

	@Test
	void getAllUsersShouldRetrunAllUsers() {
		List<User> userList = userService.findAll();
		User lastUser = userList.get(userList.size() - 1);

		assertEquals(userSample.getFname(), lastUser.getFname());
		assertEquals(userSample.getLname(), lastUser.getLname());
		assertEquals(userSample.getUID(), lastUser.getUID());
	}

	@Test
	void getUserByIdShouldReturnUserIfExists() {
		User lastUser = userService.findById(userSample.getUID());
		User noUser = userService.findById("");

		assertEquals(userSample, lastUser);
		assertEquals(null, noUser);

	}

	@Test
	void changingUserAttributesShouldChangeAttributes() {
		Address addr = new Address("streetName", "country", 1337, "houseNumber", "county", "municipality");
		addressRepository.save(addr);

		userService.setUID(userSample, "Fin");
		userService.setFname(userSample, "User after Edit");
		userService.setLname(userSample, "Sample after Edit");
		userService.setAge(userSample, 19);
		userService.setGender(userSample, User.Gender.FEMALE);
		userService.setAddress(userSample, addr);
		userService.setEmail(userSample, "dat@251.no");
		userService.setContactByEmail(userSample, false);
		userService.setPhoneNumber(userSample, 12345678L);
		userService.save(userSample);

		User lastUser = userService.findById(userSample.getUID());

		assertEquals(userSample.getUID(), lastUser.getUID());
		assertEquals(userSample.getFname(), lastUser.getFname());
		assertEquals(userSample.getLname(), lastUser.getLname());
		assertEquals(userSample.getAge(), lastUser.getAge());
		assertEquals(userSample.getGender(), lastUser.getGender());
		assertEquals(userSample.getAddress().getAid(), lastUser.getAddress().getAid());
		assertEquals(userSample.getEmail(), lastUser.getEmail());
		assertEquals(userSample.getContactByEmail(), lastUser.getContactByEmail());
		assertEquals(userSample.getPhoneNumber(), lastUser.getPhoneNumber());
	}

	@Test
	void updatingPreferencesShouldReturnUpdatedPreferences() {
		List<String> pref = new ArrayList<>();
		pref.add("SKIING");
		pref.add("TENT");

		userService.setPreferences(userSample, pref);
		userService.save(userSample);

		List<Preferences> userPref = userService.findById("3").getPreferences();

		assertEquals(pref.get(1), userPref.get(1).toString());
	}

	@Test
	void quickEnumCheck() {
		assertEquals("female", User.Gender.FEMALE.getGender());
		assertEquals("Skiing", User.Preferences.SKIING.getPreference());
	}
}