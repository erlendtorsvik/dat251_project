package hvl.no.dat251.group3project;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import hvl.no.dat251.group3project.controller.ItemController;
import hvl.no.dat251.group3project.entity.Item;
import hvl.no.dat251.group3project.service.ItemService;
import hvl.no.dat251.group3project.service.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ItemController.class)
class ItemControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	private ItemService itemService;

	@MockBean
	private UserService userService;

	@Test
	@WithMockUser("test")
	void getAllItems() throws Exception {
		List<Item> itemsList = new ArrayList<>();
		itemsList.add(new Item(1L, "Langrenn ski", "Veldig mye brukt ski", 99.99, true));
		itemsList.add(new Item(2L, "Stor telt", "God og comfy telt", 69.420, false));
		when(itemService.findAll()).thenReturn(itemsList);

		mockMvc.perform(MockMvcRequestBuilders.get("/allItems").contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(2))).andDo(print());
	}
	
	@Test
	@WithMockUser("test")
	void searchingItemsShouldRetrunAListofItems() throws Exception {
		List<Item> itemsList = new ArrayList<>();
		itemsList.add(new Item(1L, "Langrenn ski", "Veldig mye brukt ski", 99.99, true));
		itemsList.add(new Item(2L, "Stor telt", "God og comfy telt", 69.420, false));
		when(itemService.findAll()).thenReturn(itemsList);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/items").param("name", "ski")).andDo(print())
		.andExpect(status().isOk()).andExpect(model().attribute("message", "Succesfully searched items"));
	}
	
	@Test
	@WithMockUser("test")
	void searchingItemsShouldBeEnabledLOL() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/search")).andDo(print())
		.andExpect(status().isOk()).andExpect(model().attribute("message", "Hello"));
	}
}
