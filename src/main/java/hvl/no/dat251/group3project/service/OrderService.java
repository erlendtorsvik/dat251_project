package hvl.no.dat251.group3project.service;

import java.util.List;
import java.util.Random;

import com.google.cloud.firestore.CollectionReference;
import hvl.no.dat251.group3project.entity.Item;
import hvl.no.dat251.group3project.repository.IItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hvl.no.dat251.group3project.entity.Order;
import hvl.no.dat251.group3project.entity.User;
import hvl.no.dat251.group3project.firebase.FBInitialize;
import hvl.no.dat251.group3project.repository.IOrderRepository;

@Service
public class OrderService {

	private IOrderRepository orderRepository;

	private ItemService itemService;

	private IItemRepository itemRepository;

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

	//public void save(Order order) {
		//orderRepository.save(order);
	//}

	public Order save(Order newOrder) {
		if (newOrder.getOID() == null) {
			boolean generated = false;
			while (!generated) {
				Random rand = new Random();
				Long tempID = rand.nextLong();
				if (!findByIdIsPresent(tempID)) {
					generated = true;
					setOID(newOrder, tempID);
				}
			}
		}
		orderRepository.save(newOrder);
		return newOrder;
	}
	public void setOID(Order order, Long oID) {
		order.setOID(oID);
	}
}