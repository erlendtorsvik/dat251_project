package hvl.no.dat251.group3project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import hvl.no.dat251.group3project.entity.Item;
import hvl.no.dat251.group3project.repository.IItemRepository;
import hvl.no.dat251.group3project.service.ItemService;

@SpringBootTest
class ItemServiceTest {

	@Autowired
	private IItemRepository itemRepository;
	private ItemService itemService;
	private Item itemSample;
	private Item itemSample1;
	private Item itemSample2;
	private Item itemSample3;
	private Item itemSample4;


	@BeforeEach
	void setup() {
		itemSample1 = new Item("Ski", "Slalomski", 1000, true);
		itemRepository.save(itemSample1);
		itemSample2 = new Item("Kajakk", "tomannskajakk", 1000, true);
		itemRepository.save(itemSample2);
		itemSample3 = new Item("Sykkel", "Terrengsykkel", 500, true);
		itemRepository.save(itemSample3);
		itemSample4 = new Item("Ski", "Langrenn", 100, false);
		itemRepository.save(itemSample4);

		itemService = new ItemService(itemRepository);
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
}