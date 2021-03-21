package hvl.no.dat251.group3project.serviceTest;

import ch.qos.logback.core.net.SyslogOutputStream;
import hvl.no.dat251.group3project.entity.Item;
import hvl.no.dat251.group3project.entity.User;
import hvl.no.dat251.group3project.repository.IItemRepository;
import hvl.no.dat251.group3project.repository.IUserRepository;
import hvl.no.dat251.group3project.service.ItemService;
import hvl.no.dat251.group3project.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import hvl.no.dat251.group3project.entity.Order;
import hvl.no.dat251.group3project.repository.IOrderRepository;
import hvl.no.dat251.group3project.service.OrderService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class OrderServiceTest {

	@Autowired
	private IOrderRepository orderRepository;

	@Autowired
	private IItemRepository itemRepository;

	@Autowired
	private IUserRepository userRepository;

	private UserService userService;
	private OrderService orderService;
	private Order orderSample;
	private Order orderSample1;
	private Order orderSample2;
	private Order orderSample3;
	private Order orderSample4;

	private User tempSeller;
	private User tempLoaner;

	@MockBean
	private ItemService itemService;


	private Item itemSample1;
	private Item itemSample2;
	private List<Item> itemSampleList = new ArrayList();

	@BeforeEach
	void setup()
	{
		userService = new UserService(userRepository);
		itemService = new ItemService(itemRepository);
		orderService = new OrderService(orderRepository);
		tempLoaner = new User("userLoaner", "Ole", "Loaner", "Ole.Loaner@mail.com");
		userService.save(tempLoaner);
		tempSeller = new User("userSeller1", "Pelle", "Seller", "Pelle.Seller@mail.com");
		userService.save(tempSeller);

		itemSample1 = new Item(1L, "Ski", "Slalomski", 1000, true);
		itemSample1.setOwner(tempLoaner);
		itemService.save(itemSample1);
		itemSample2 = new Item(2L, "Kajakk", "tomannskajakk", 1000, true);
		itemSample2.setOwner(tempSeller);
		itemService.save(itemSample2);
		itemSampleList.add(itemSample1);
		itemSampleList.add(itemSample2);
		LocalDateTime fromTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
		LocalDateTime toTime = LocalDateTime.now().plusDays(10).truncatedTo(ChronoUnit.MINUTES);

		orderSample = new Order(1L, itemSampleList, 100.0, fromTime,
				toTime,tempSeller, tempLoaner);
		orderService.save(orderSample);
	}

	@AfterEach
	void deleteAllAfter(){orderRepository.deleteAll();}

	@Test
	void findOrderByIdShouldReturnOrderID(){
		assertEquals(null, orderService.findById(-1L));
		assertEquals(orderSample, orderService.findById(orderSample.getOID()));
	}

	@Test
	void getOrderByLoanerAndSeller(){
		List<Order> orders = new ArrayList<>();
		orders.add(orderSample);
		assertEquals(orders, orderService.getOrdersByLoaner(tempLoaner));
		assertEquals(orders, orderService.getOrdersBySeller(tempSeller));
	}
}