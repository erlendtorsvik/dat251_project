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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import hvl.no.dat251.group3project.entity.User;
import hvl.no.dat251.group3project.services.ItemService;
import hvl.no.dat251.group3project.services.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class UserControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private ItemService itemService;

	@Test
	@WithMockUser("test")
	void SearchingForAllUsersShouldReturnAListOfUsers() throws Exception {
		List<User> userList = new ArrayList<User>();
		userList.add(new User("1", "Lompo", " ", ""));
		userList.add(new User("2", "Moneey", " ", ""));
		when(userService.findAll()).thenReturn(userList);

		mockMvc.perform(MockMvcRequestBuilders.get("/users").contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2))).andDo(print());
	}
	
	@Test
	@WithMockUser("test")
	void getAllUsers() throws Exception {
		List<User> userList = new ArrayList<User>();
		userList.add(new User("1", "Lompo", " ", ""));
		userList.add(new User("2", "Moneey", " ", ""));
		when(userService.findAll()).thenReturn(userList);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/users").contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$", hasSize(2))).andDo(print());
	}
}
