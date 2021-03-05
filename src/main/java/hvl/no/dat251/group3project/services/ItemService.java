package hvl.no.dat251.group3project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hvl.no.dat251.group3project.entity.Item;
import hvl.no.dat251.group3project.repositories.IItemRepository;

@Service
public class ItemService {

	@Autowired
    IItemRepository itemRepository;



    public ItemService(IItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public List<Item> findByWord(String itemWord) {
        return itemRepository.findByNameContainingIgnoreCase(itemWord);
    }
}