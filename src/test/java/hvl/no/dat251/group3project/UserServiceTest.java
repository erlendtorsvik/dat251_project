package hvl.no.dat251.group3project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import hvl.no.dat251.group3project.entity.Address;
import hvl.no.dat251.group3project.entity.User;
import hvl.no.dat251.group3project.repositories.IAddressRepository;
import hvl.no.dat251.group3project.repositories.IUserRepository;
import hvl.no.dat251.group3project.services.UserService;

@SpringBootTest
public class UserServiceTest {

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IAddressRepository addressRepository;

	private UserService userService;

	private User userSample;

	@BeforeEach
	void setup() {

		userSample = new User("3", "User", " Sample", "dat251@hvl.no");
		userRepository.save(userSample);
		userService = new UserService(userRepository);
	}

	@Test
	void getAllUsersShouldRetrunAllUsers() {
		List<User> userList = userService.findAll();
		User lastUser = userList.get(0);

		assertEquals(userSample.getFname(), lastUser.getFname());
		assertEquals(userSample.getLname(), lastUser.getLname());
		assertEquals(userSample.getUID(), lastUser.getUID());
	}
	
	@Test
	void getUserByIdShouldReturnUser() {
		User lastUser = userService.findById(userSample.getUID());
		
		assertEquals(userSample.getEmail(), lastUser.getEmail());
	}

	@Test
	void getUserByEmailShouldRetrunUser() {
		User lastUser = userService.findByEmail(userSample.getEmail());

		assertEquals(userSample.getFname(), lastUser.getFname());
		assertEquals(userSample.getLname(), lastUser.getLname());
		assertEquals(userSample.getEmail(), lastUser.getEmail());
		assertEquals(userSample.getUID(), lastUser.getUID());
	}

	@Test
	void changingUserAttributesShouldChangeAttributes() {
		Address addr = new Address("streetName", "country", 1337, "houseNumber", "county", "municipality");
		addressRepository.save(addr);

		userService.setFname(userSample, "User after Edit");
		userService.setLname(userSample, "Sample after Edit");
		userService.setAge(userSample, 19);
		userService.setGender(userSample, User.Gender.FEMALE);
		userService.setAddress(userSample, addr);

		User lastUser = userService.findByEmail(userSample.getEmail());

		assertEquals(userSample.getFname(), lastUser.getFname());
		assertEquals(userSample.getLname(), lastUser.getLname());
		assertEquals(userSample.getAge(), lastUser.getAge());
		assertEquals(userSample.getGender(), lastUser.getGender());
		assertEquals(userSample.getAddress().getAID(), lastUser.getAddress().getAID());
	}

}