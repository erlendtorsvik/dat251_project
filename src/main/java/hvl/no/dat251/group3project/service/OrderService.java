package hvl.no.dat251.group3project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;

import hvl.no.dat251.group3project.entity.Item;
import hvl.no.dat251.group3project.entity.Order;
import hvl.no.dat251.group3project.entity.User;
import hvl.no.dat251.group3project.firebase.FBInitialize;
import hvl.no.dat251.group3project.repository.IOrderRepository;

@Service
public class OrderService {

	private IOrderRepository orderRepository;

	@Autowired
	private ItemService itemService;
	@Autowired
	private UserService userService;

	@Autowired
	private FBInitialize fb;

	public void gettAllFromFb() {
		CollectionReference orderCR = fb.getFirebase().collection("Orders");
		Order tempOrder = new Order();
		for (DocumentReference doc : orderCR.listDocuments()) {
			Long oID = Long.parseLong(doc.getId());
			if (!findByIdIsPresent(oID)) {
				try {
					DocumentSnapshot ds = orderCR.document(String.valueOf(oID)).get().get();
					setOID(tempOrder, oID);
					setInsurance(tempOrder, ds.getDouble("insurance"));
					setFromDate(tempOrder, ds.getString("dateFrom"));
					setToDate(tempOrder, ds.getString("dateTo"));
					setTotalPrice(tempOrder, ds.getDouble("totalPrice"));
					// Get seller from Firebase and save him
					HashMap seller = (HashMap) ds.get("seller");
					User savedSeller = new User((String) seller.get("uid"), (String) seller.get("fname"),
							(String) seller.get("lname"), (String) seller.get("email"));
					if (!userService.findByIdIsPresent(savedSeller.getUID()))
						userService.save(savedSeller);
					setSeller(tempOrder, savedSeller);
					// Get loaner from Firebase and save him
					HashMap loaner = (HashMap) ds.get("loaner");
					User savedLoaner = new User((String) loaner.get("uid"), (String) loaner.get("fname"),
							(String) loaner.get("lname"), (String) loaner.get("email"));
					if (!userService.findByIdIsPresent(savedLoaner.getUID()))
						userService.save(savedLoaner);
					setLoaner(tempOrder, savedLoaner);

					List<HashMap> itemsHash = (List<HashMap>) ds.get("items");
					List<Item> items = new ArrayList<>();
					Item tempItem = new Item();
					for (HashMap i : itemsHash) {
						tempItem = new Item((Long) i.get("iid"), (String) i.get("name"), (String) i.get("description"),
								(Double) i.get("price"), (String) i.get("fromDate"), (String) i.get("toDate"),
								(Boolean) i.get("available"), (List<String>) i.get("images"));
						// Get item onwer and save
						HashMap owner = (HashMap) i.get("owner");
						User savedOwner = new User((String) owner.get("uid"), (String) owner.get("fname"),
								(String) owner.get("lname"), (String) owner.get("email"));
						if (!userService.findByIdIsPresent(savedOwner.getUID()))
							userService.save(savedOwner);
						itemService.setOwner(tempItem, savedOwner);
						if (!itemService.findByIdIsPresent(tempItem.getIID()))
							itemService.save(tempItem);
						items.add(tempItem);
					}
					setItems(tempOrder, items);

					save(tempOrder);
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void setItems(Order order, List<Item> items) {
		order.setItems(items);
	}

	private void setLoaner(Order order, User loaner) {
		order.setLoaner(loaner);
	}

	private void setSeller(Order order, User seller) {
		order.setSeller(seller);
	}

	private void setInsurance(Order order, Double insurance) {
		order.setInsurance(insurance);
	}

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
		if (fb != null) {
			CollectionReference userCR = fb.getFirebase().collection("Orders");
			userCR.document(String.valueOf((newOrder.getOID()))).set(newOrder);
		}
		return newOrder;
	}

	public void setTotalPrice(Order order, Double totalPrice) {
		order.setTotalPrice(totalPrice);
	}

	public void setOID(Order order, Long oID) {
		order.setOID(oID);
	}

	public void setToDate(Order order, String toDate) {
		order.setDateTo(toDate);
	}

	public void setFromDate(Order order, String fromDate) {
		order.setDateFrom(fromDate);
	}
}