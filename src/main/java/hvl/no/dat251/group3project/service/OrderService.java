package hvl.no.dat251.group3project.service;

import java.util.List;

import hvl.no.dat251.group3project.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hvl.no.dat251.group3project.entity.Order;
import hvl.no.dat251.group3project.entity.User;
import hvl.no.dat251.group3project.firebase.FBInitialize;
import hvl.no.dat251.group3project.repository.IOrderRepository;

@Service
public class OrderService {

	private IOrderRepository orderRepository;

	@Autowired
	private FBInitialize fb;

	public OrderService(IOrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	public Order findById(Long id) {
		if (findByIdIsPresent(id))
			return orderRepository.findById(id).get();
		return null;
	}

	public boolean findByIdIsPresent(Long oID) {
		return orderRepository.existsById(oID);
	}

	public List<Order> getOrdersBySeller(User user) {
		return orderRepository.findBySeller(user);
	}

	public List<Order> getOrdersByLoaner(User user) {
		return orderRepository.findByLoaner(user);
	}

	public void save(Order order) {
		orderRepository.save(order);
	}

}