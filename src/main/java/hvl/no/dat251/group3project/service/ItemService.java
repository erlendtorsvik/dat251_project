package hvl.no.dat251.group3project.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;

import hvl.no.dat251.group3project.entity.Item;
import hvl.no.dat251.group3project.entity.User;
import hvl.no.dat251.group3project.firebase.FBInitialize;
import hvl.no.dat251.group3project.repository.IItemRepository;

@Service
public class ItemService {

	private IItemRepository itemRepository;

	@Autowired
	private FBInitialize fb;

	@Autowired
	private UserService userService;

	public void gettAllFromFb() {
		CollectionReference itemCR = fb.getFirebase().collection("Items");
		Item tempItem = new Item();
		for (DocumentReference doc : itemCR.listDocuments()) {
			Long iID = Long.parseLong(doc.getId());
			if (!findByIdIsPresent(iID)) {
				try {
					DocumentSnapshot ds = itemCR.document(String.valueOf(iID)).get().get();
					setIID(tempItem, iID);
					setName(tempItem, ds.getString("name"));
					setDescription(tempItem, ds.getString("description"));
					setPrice(tempItem, ds.getDouble("price"));
					setFromDate(tempItem, ds.getString("fromDate"));
					setToDate(tempItem, ds.getString("toDate"));
					setAvailable(tempItem, ds.getBoolean("available"));
					setImages(tempItem, (List<String>) ds.get("images"));
					// Get owner from Firebase and save him
					HashMap owner = (HashMap) ds.get("owner");
					User savedOwner = new User((String) owner.get("uid"), (String) owner.get("fname"),
							(String) owner.get("lname"), (String) owner.get("email"));
					if (!userService.findByIdIsPresent(savedOwner.getUID()))
						userService.save(savedOwner);
					setOwner(tempItem, savedOwner);
					save(tempItem);

					for (String img : tempItem.getImages()) {
						System.out.println(img);
						downloadFb(img);
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setName(Item item, String name) {
		item.setName(name);
	}

	public void setDescription(Item item, String description) {
		item.setDescription(description);
	}

	public void setPrice(Item item, Double price) {
		item.setPrice(price);
	}

	public void setAvailable(Item item, Boolean available) {
		item.setAvailable(available);
	}

	public void setFromDate(Item item, String fromDate) {
		item.setFromDate(fromDate);
	}

	public void setToDate(Item item, String toDate) {
		item.setToDate(toDate);
	}

	public ItemService(IItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	public List<Item> findAll() {
		return itemRepository.findAll();
	}

	public List<Item> findByWord(String itemWord) {
		return itemRepository.findByNameContainingIgnoreCase(itemWord);
	}

	public List<Item> getItemsByUser(User user) {
		return itemRepository.findByOwner(user);
	}

	public void setOwner(Item newItem, User user) {
		newItem.setOwner(user);
	}

	public Item save(Item newItem) {
		if (newItem.getIID() == null) {
			boolean generated = false;
			while (!generated) {
				Random rand = new Random();
				Long tempID = rand.nextLong();
				if (!findByIdIsPresent(tempID)) {
					generated = true;
					setIID(newItem, tempID);
				}
			}
		}
		itemRepository.save(newItem);
		if (fb != null) {
			CollectionReference userCR = fb.getFirebase().collection("Items");
			userCR.document(String.valueOf((newItem.getIID()))).set(newItem);
		}
		return newItem;
	}

	public Item findById(Long id) {
		if (findByIdIsPresent(id))
			return itemRepository.findById(id).get();
		return null;
	}

	public boolean findByIdIsPresent(Long id) {
		return itemRepository.findById(id).isPresent();
	}

	public void deleteById(Long id) {
		itemRepository.deleteById(id);
		if (fb != null) {
			CollectionReference userCR = fb.getFirebase().collection("Items");
			userCR.document(String.valueOf(id)).delete();
		}
	}

	public void setIID(Item item, Long iID) {
		item.setIID(iID);
	}

	@Value("${app.upload.dir}")
	public String uploadDir;

	public String uploadFb(MultipartFile file) {
		return fb.uploadFile(file);
	}

	public void downloadFb(String fileName) {
		try {
			fb.downloadFile(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteFbFile(String fileName) {
		fb.deleteFile(fileName);
	}

	public void setImages(Item item, List<String> images) {
		item.setImages(images);
	}
}