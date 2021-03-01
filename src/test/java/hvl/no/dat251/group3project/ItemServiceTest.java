package hvl.no.dat251.group3project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import hvl.no.dat251.group3project.entity.Item;
import hvl.no.dat251.group3project.repositories.IItemRepository;
import hvl.no.dat251.group3project.services.ItemService;

@SpringBootTest
class ItemServiceTest {

	@Autowired
	private IItemRepository itemRepository;

	@Test
	void getAllItems() {
		Item itemSample = new Item("Sokker", "gode gamle sokker", 1337.0, true);
		itemRepository.save(itemSample);
		ItemService itemService = new ItemService(itemRepository);

		Item firstResult = itemService.findAll().get(0);

		assertEquals(itemSample.getIID(), firstResult.getIID());
		assertEquals(itemSample.getName(), firstResult.getName());
		assertEquals(itemSample.getDescription(), firstResult.getDescription());
		assertEquals(itemSample.getPrice(), firstResult.getPrice());
		assertEquals(itemSample.isAvailable(), firstResult.isAvailable());

	}

}
