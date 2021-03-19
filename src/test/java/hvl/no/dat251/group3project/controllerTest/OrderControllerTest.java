package hvl.no.dat251.group3project.controllerTest;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import hvl.no.dat251.group3project.service.AddressService;
import hvl.no.dat251.group3project.service.ItemService;
import hvl.no.dat251.group3project.service.OrderService;
import hvl.no.dat251.group3project.service.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class OrderControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	private OrderService orderService;

	@MockBean
	private UserService userService;

	@MockBean
	private ItemService itemService;

	@MockBean
	private AddressService addressService;

	/*
	 * @Test
	 * 
	 * @WithMockUser("test") void getAllOrders() throws Exception { List<Item>
	 * itemList = new ArrayList<>(); itemList.add(new Item(1L, "Langrenn ski",
	 * "Veldig mye brukt ski", 99.99, true)); itemList.add(new Item(2L, "Stor telt",
	 * "God og comfy telt", 69.420, false));
	 * 
	 * List<Order> orders = new ArrayList<>(); orders.add(new Order(1L, itemList,
	 * LocalDateTime.now(), 100.0, LocalDateTime.now().plusDays(10),
	 * LocalDateTime.now().plusDays(20)));
	 * when(orderService.findAll()).thenReturn(orders);
	 * 
	 * mockMvc.perform(MockMvcRequestBuilders.get("/myOrders")).andDo(print())
	 * .andExpect(status().isOk()).andExpect(model().attribute("message",
	 * "OrdermessageLoL"));
	 * 
	 * mockMvc.perform(MockMvcRequestBuilders.get("/allOrders").contentType(
	 * MediaType.APPLICATION_JSON)) .andExpect(jsonPath("$",
	 * hasSize(1))).andDo(print());
	 * 
	 * }
	 */
}