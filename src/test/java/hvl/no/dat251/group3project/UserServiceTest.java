package hvl.no.dat251.group3project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import hvl.no.dat251.group3project.entity.User;
import hvl.no.dat251.group3project.repositories.IUserRepository;
import hvl.no.dat251.group3project.services.UserService;

@SpringBootTest
public class UserServiceTest {

	@Autowired
	private IUserRepository userRepository;

	@Test
	@WithMockUser("test")
	void getAllUsers() {
		User userSample = new User("3", "User", " Sample", " 1");
		userRepository.save(userSample);
		UserService userService = new UserService(userRepository);

		List<User> userList = userService.findAll();
		User lastUser = userList.get(0);

		assertEquals(userSample.getFname(), lastUser.getFname());
		assertEquals(userSample.getLname(), lastUser.getLname());
		assertEquals(userSample.getUID(), lastUser.getUID());
	}
}
