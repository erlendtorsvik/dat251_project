package hvl.no.dat251.group3project.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hvl.no.dat251.group3project.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
		orderService.gettAllFromFb();

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

	@GetMapping("/orderItem/{id}")
	public String orderItem(@PathVariable Long id, @RequestParam(defaultValue = "false") Boolean deliverHome,
			@RequestParam String toDate, Model model, OAuth2AuthenticationToken authentication) {
		List<Item> items = new ArrayList<>();
		Item item = itemService.findById(id);
		try {
			Date currentDate = new Date();
			String currentDateString = new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
			Date itemToDate = new SimpleDateFormat("yyyy-MM-dd").parse(item.getToDate());
			Date itemFromDate = new SimpleDateFormat("yyyy-MM-dd").parse(item.getFromDate());
			if (item.isAvailable() && currentDate.before(itemToDate) && currentDate.after(itemFromDate)
					&& item.getOwner() != userService.getUser(authentication)) {
				items.add(item);
				Order order = new Order(items, itemService.findById(id).getOwner(),
						userService.getUser(authentication));
				Double totalPrice = item.getPrice();
				if (deliverHome)
					totalPrice += item.getDeliveryFee();
				if (!toDate.isBlank())
					orderService.setToDate(order, toDate);
				orderService.setFromDate(order, currentDateString);
				orderService.setTotalPrice(order, totalPrice);
				itemService.save(item);
				orderService.save(order);
			} else {
				model.addAttribute(item);
				model.addAttribute("message", "Can not order item that is not available!");
				return "item";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		model.addAttribute(item);
		model.addAttribute("message", "Successfully ordered item " + id);
		return "item";
	}
}