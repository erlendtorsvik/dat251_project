package hvl.no.dat251.group3project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import hvl.no.dat251.group3project.entity.User;
import hvl.no.dat251.group3project.repositories.IUserRepository;
import hvl.no.dat251.group3project.services.UserService;

@SpringBootTest
public class UserServiceTest {

	@MockBean
	private IUserRepository userRepository;

	@Test
	void getAllUsers() {
		User userSample = new User("User Sample 1", true);
		userRepository.save(userSample);
		UserService userService = new UserService(userRepository);

		List<User> userList = userService.findAll();
		User lastUser = userList.get(userList.size() - 1);

		assertEquals(userSample.getName(), lastUser.getName());
		assertEquals(userSample.isDead(), lastUser.isDead());
		assertEquals(userSample.getId(), lastUser.getId());
	}
}
