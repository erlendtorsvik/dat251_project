package hvl.no.dat251.group3project.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;

import hvl.no.dat251.group3project.entity.Item;
import hvl.no.dat251.group3project.entity.User;
import hvl.no.dat251.group3project.entity.User.Preferences;
import hvl.no.dat251.group3project.firebase.FBInitialize;
import hvl.no.dat251.group3project.repository.IItemRepository;

@Service
public class ItemService {

	private IItemRepository itemRepository;

	@Autowired
	private FBInitialize fb;

	@Autowired
	private UserService userService;

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
					setTags(tempItem, (List<String>) ds.get("tags"));
					// Get owner from Firebase and save him
					Map<String, Object> owner = (Map<String, Object>) ds.get("owner");
					String uid = (String) owner.get("uid");
					User savedOwner;
					if (!userService.findByIdIsPresent(uid))
						savedOwner = userService.saveUser(owner);
					else
						savedOwner = userService.findById(uid);
					setOwner(tempItem, savedOwner);
					save(tempItem);
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

	public String uploadFb(MultipartFile file) {
		return fb.uploadFile(file);
	}

	public void deleteFbFile(String fileName) {
		fb.deleteFile(fileName);
	}

	public void setImages(Item item, List<String> images) {
		item.setImages(images);
	}

	public void setTags(Item item, List<String> tags) {
		item.setTags(tags);
	}

	public URL getImgUrl(String fileName) {
		return fb.getUrl(fileName);
	}

	public Item saveItem(Map<String, Object> item) {
		Map<String, Object> ownerMap = (Map<String, Object>) item.get("owner");
		String ownerID = (String) ownerMap.get("uid");
		User owner;
		if (!userService.findByIdIsPresent(ownerID))
			owner = userService.saveUser(ownerMap);
		else
			owner = userService.findById(ownerID);
		List<String> tagsStrings = (List<String>) item.get("preferences");
		List<Preferences> tags = new ArrayList<>();

		for (String tag : tagsStrings) {
			tags.add(User.Preferences.valueOf(tag));
		}
		Item savedItem = new Item((Long) item.get("iid"), (String) item.get("name"), (String) item.get("description"),
				(Double) item.get("price"), (String) item.get("fromDate"), (String) item.get("toDate"),
				(Boolean) item.get("available"), (List<String>) item.get("images"), (Double) item.get("deliveryFee"),
				tags);
		savedItem.setOwner(owner);
		return save(savedItem);
	}

	public List<Item> findByTagsUserPref(User user) {
		List<Item> items = itemRepository.findByTagsIn(user.getPreferences());
		Set<Item> s = new HashSet<>(items);
		items.clear();
		items.addAll(s);
		return items;
	}

	public Model imgsAndUrls(Model model, Item item) {
		List<URL> urls = new ArrayList<>();
		List<String> imgs = new ArrayList<>();
		for (String img : item.getImages()) {
			urls.add(getImgUrl(img));
			imgs.add(img);
		}
		model.addAttribute("imgs", imgs);
		model.addAttribute("imgUrls", urls);
		return model;
	}

	public Model oneImgAndUrl(Model model, List<Item> items) {
		List<String> imgs = new ArrayList<>();
		List<URL> urls = new ArrayList<>();
		try {
			for (Item item : items) {
				String img;
				URL url;
				if (!item.getImages().isEmpty()) {
					img = item.getImages().get(0);
					url = getImgUrl(img);
				} else {
					img = "placeholder";
					url = new URL(
							"https://firebasestorage.googleapis.com/v0/b/dat251-project.appspot.com/o/placeholderIMG.jpg?alt=media&token=9bd865dd-b77f-43f1-99e7-d57fa404f85b");
				}
				urls.add(url);
				imgs.add(img);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		model.addAttribute("imgs", imgs);
		model.addAttribute("imgUrls", urls);
		return model;
	}
}