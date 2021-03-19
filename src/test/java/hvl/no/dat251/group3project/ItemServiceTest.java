package hvl.no.dat251.group3project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import hvl.no.dat251.group3project.entity.Item;
import hvl.no.dat251.group3project.entity.User;
import hvl.no.dat251.group3project.repository.IItemRepository;
import hvl.no.dat251.group3project.repository.IUserRepository;
import hvl.no.dat251.group3project.service.ItemService;
import hvl.no.dat251.group3project.service.UserService;

@SpringBootTest
class ItemServiceTest {

	@Autowired
	private IItemRepository itemRepository;
	
	@Autowired
	private IUserRepository userRepository;
	
	private UserService userService;
	
	private ItemService itemService;
	
	private Item itemSample;
	private Item itemSample1;
	private Item itemSample2;
	private Item itemSample3;
	private Item itemSample4;

	@BeforeEach
	void setup() {
		itemService = new ItemService(itemRepository);
		userService = new UserService(userRepository);
		
		itemSample1 = new Item(1L, "Ski", "Slalomski", 1000, true);
		itemService.save(itemSample1);
		itemSample2 = new Item(2L, "Kajakk", "tomannskajakk", 1000, true);
		itemService.save(itemSample2);
		itemSample3 = new Item(3L, "Sykkel", "Terrengsykkel", 500, true);
		itemService.save(itemSample3);
		itemSample4 = new Item("Ski", "Langrenn", 100, false);
		itemService.save(itemSample4);
	}

	@AfterEach
	void deleteAll() {
		itemRepository.deleteAll();
	}

	@Test
	@WithMockUser("test")
	void getAllItems() {

		List<Item> itemSampleList = itemService.findAll();

		Item firstResult = itemSampleList.get(0);
		Item lastResult = itemSampleList.get(itemSampleList.size() - 1);

		assertEquals(firstResult.getIID(), firstResult.getIID());
		assertEquals(firstResult.getName(), itemSample1.getName());
		assertEquals(firstResult.getDescription(), itemSample1.getDescription());
		assertEquals(firstResult.getPrice(), 1000);
		assertEquals(firstResult.isAvailable(), true);
		assertEquals(lastResult.getIID(), itemSample4.getIID());
		assertEquals(lastResult.getName(), lastResult.getName());
		assertEquals(lastResult.getDescription(), lastResult.getDescription());
		assertEquals(lastResult.getPrice(), lastResult.getPrice());
		assertEquals(lastResult.isAvailable(), lastResult.isAvailable());
	}

	@Test
	void getItemBySearchWord() {
		List<Item> itemSampleList = itemService.findByWord("Ski");
		List<Item> allItems = itemService.findAll();

		Item resultFromAll1 = allItems.get(0);
		Item resultFromAll2 = allItems.get(3);

		Item result1 = itemSampleList.get(0);
		Item result2 = itemSampleList.get(1);

		assertEquals(result1.getIID(), resultFromAll1.getIID());
		assertEquals(result1.getName(), "Ski");
		assertEquals(result1.getDescription(), "Slalomski");
		assertEquals(result1.getPrice(), 1000);
		assertEquals(result1.isAvailable(), true);

		assertEquals(result2.getIID(), resultFromAll2.getIID());
		assertEquals(result2.getName(), "Ski");
		assertEquals(result2.getDescription(), "Langrenn");
		assertEquals(result2.getPrice(), 100);
		assertEquals(result2.isAvailable(), false);

	}
	
	@Test
	void updatingItemShouldUpdateItemAttributes() {
		itemService.setAvailable(itemSample1, false);
		itemService.setDescription(itemSample1, "DuDE");
		itemService.setIID(itemSample1, 43L);
		itemService.setName(itemSample1, "U Man");
		User tempUser = new User("user", "cmon", "dude", "yolo");
		userService.save(tempUser);
		itemService.setOwner(itemSample1, tempUser);
		itemService.setPrice(itemSample1, 1337.69);
		itemService.save(itemSample1);
		
		Item lastItem = itemService.findById(itemSample1.getIID());
		assertEquals(itemSample1.isAvailable(), lastItem.isAvailable());
		assertEquals(itemSample1.getDescription(), lastItem.getDescription());
		assertEquals(itemSample1.getName(), lastItem.getName());
		assertEquals(itemSample1.getOwner(), lastItem.getOwner());
		assertEquals(itemSample1.getPrice(), lastItem.getPrice());
	}
	
	@Test
	void searchingByIdShouldRetrunIdOrNull() {
		assertEquals(null, itemService.findById(-1L));
		assertEquals(itemSample1, itemService.findById(itemSample1.getIID()));
	}
	
	@Test
	void searchingItemsByUserShouldReturnUsersItems() {
		List<Item> items = new ArrayList<>();
		User tempUser = new User("user", "cmon", "dude", "yolo");
		userService.save(tempUser);
		itemSample1.setOwner(tempUser);
		itemService.save(itemSample1);
		itemSample2.setOwner(tempUser);
		itemService.save(itemSample2);
		
		List<Item> usersItems = itemService.getItemsByUser(tempUser);
		
		assertTrue(usersItems.contains(itemSample1));
		assertTrue(usersItems.contains(itemSample2));
		assertFalse(usersItems.contains(itemSample3));
	}
}