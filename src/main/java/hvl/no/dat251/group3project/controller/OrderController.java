package hvl.no.dat251.group3project.controller;

import hvl.no.dat251.group3project.entity.Order;
import hvl.no.dat251.group3project.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/allOrders")
    ResponseEntity<List<Order>> getAllOrders() {
        return new ResponseEntity<>(orderService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/orders")
    public String searchOrder (@RequestParam Long oID) {
        Order orders = orderService.findById(oID);

        return "orders";

    }

    @GetMapping("/searchOrder")
    public String search() {

        return "orders";
    }
}
