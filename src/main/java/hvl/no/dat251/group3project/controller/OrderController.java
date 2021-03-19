package hvl.no.dat251.group3project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import hvl.no.dat251.group3project.entity.Order;
import hvl.no.dat251.group3project.service.ItemService;
import hvl.no.dat251.group3project.service.OrderService;
import hvl.no.dat251.group3project.service.UserService;

@Controller
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private UserService userService;

	@GetMapping("/allOrders")
	ResponseEntity<List<Order>> getAllOrders() {
		return new ResponseEntity<>(orderService.findAll(), HttpStatus.OK);
	}

	@GetMapping("/myOrders")
	public String myOrders(Model model, OAuth2AuthenticationToken authentication) {
		/*
		 * Order order = new Order(); List <Item> items = new ArrayList<>(); Item item1
		 * = new Item("Yo", "DEsc", 100.0, true); items.add(item1); Item item2 = new
		 * Item("Cris sug as!", "DEsc123", 100.0, true); items.add(item2);
		 * itemService.save(item1); itemService.save(item2);
		 * order.setLoaner(userService.getUser(authentication)); order.setItems(items);
		 * order.setDateFrom(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
		 * order.setDateTo(order.getDateFrom().plusDays(10).truncatedTo(ChronoUnit.
		 * MINUTES)); orderService.save(order);
		 */

		List<Order> myOrdersLoan = orderService.getOrdersByLoaner(userService.getUser(authentication));
		List<Order> myOrdersSell = orderService.getOrdersBySeller(userService.getUser(authentication));
		model.addAttribute("ordersLoan", myOrdersLoan);
		model.addAttribute("ordersSell", myOrdersSell);
		model.addAttribute("message", "Hello");
		return "orders";
	}

	@GetMapping("/orders/{id}")
	public String getOrder(@PathVariable Long id, Model model, OAuth2AuthenticationToken authentication) {
		Order order = orderService.findById(id);
		model.addAttribute("order", order);
		return "orderInfo";
	}
}