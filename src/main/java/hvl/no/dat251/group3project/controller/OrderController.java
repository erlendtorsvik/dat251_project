package hvl.no.dat251.group3project.controller;

import hvl.no.dat251.group3project.entity.Item;
import hvl.no.dat251.group3project.entity.Order;
import hvl.no.dat251.group3project.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import javax.print.attribute.standard.Chromaticity;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/allOrders")
    ResponseEntity<List<Order>> getAllOrders() { return new ResponseEntity<>(orderService.findAll(), HttpStatus.OK);}

    @GetMapping("/myOrders")
    public String myOrders(Model model){
        Order order = new Order();
        List <Item> items = new ArrayList<>();
        items.add(new Item("Yo", "DEsc", 100.0, true));
        order.setItems(items);
        /*order.getItems().get(0).getName();
        order.getItems().get(0).getPrice();*/
        order.setDateFrom(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        order.setDateTo(order.getDateFrom().plusDays(10).truncatedTo(ChronoUnit.MINUTES));
        List<Order> orders = orderService.findAll();

        model.addAttribute("orders", order);
        model.addAttribute("message", "OrdermessageLoL");

        return "orders";
    }
}
