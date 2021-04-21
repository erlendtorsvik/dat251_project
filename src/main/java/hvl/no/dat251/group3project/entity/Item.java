package hvl.no.dat251.group3project.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import hvl.no.dat251.group3project.entity.User.Preferences;
import lombok.Data;

@Data
@Entity
public class Item {

	@Id
	private Long iID;
	private String name;
	private String description;
	private Double price;
	private String fromDate;
	private String toDate;
	private boolean available;

	@ElementCollection
	private List<String> images = new ArrayList<>();

	@ElementCollection
	private List<Preferences> tags = new ArrayList<>();

	@ManyToOne
	private User owner;

	private Double deliveryFee = 49.99;

	public Item() {
	}

	public Item(Long iID, String name, String description, Double price, String fromDate, String toDate,
			boolean available, List<String> images, Double deliveryFee, List<Preferences> tags) {
		this.iID = iID;
		this.name = name;
		this.description = description;
		this.price = price;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.available = available;
		this.images = images;
		this.deliveryFee = deliveryFee;
		this.tags = tags;
	}

	public Item(String name, String description, Double price, String fromDate, String toDate, boolean available,
			List<String> images) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.available = available;
		this.images = images;
	}

	public Item(Long iID, String name, String description, Double price, boolean available) {
		this.iID = iID;
		this.name = name;
		this.description = description;
		this.price = price;
		this.available = available;
	}

	public void setTags(List<String> tags) {
		List<Preferences> tagList = new ArrayList<>();
		for (String tag : tags) {
			Preferences p = Preferences.valueOf(tag);
			if (p != Preferences.NAN) {
				tagList.add(p);
			}
		}
		this.tags.clear();
		this.tags.addAll(tagList);
	}

}