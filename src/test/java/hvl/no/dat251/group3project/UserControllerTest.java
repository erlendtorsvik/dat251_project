package hvl.no.dat251.group3project;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import hvl.no.dat251.group3project.entity.User;
import hvl.no.dat251.group3project.services.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class UserControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Test
	void getAllUsers() throws Exception {
		List<User> userList = new ArrayList<User>();
		userList.add(new User("1", "Lompo", true));
		userList.add(new User("2", "Moneey", false));
		when(userService.findAll()).thenReturn(userList);

		mockMvc.perform(MockMvcRequestBuilders.get("/users").contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2))).andDo(print());
	}

}
