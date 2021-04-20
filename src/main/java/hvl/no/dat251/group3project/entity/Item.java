package hvl.no.dat251.group3project.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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

	@ManyToOne
	private User owner;

	private Double deliveryFee = 49.99;

	public Item() {
	}

	public Item(Long iID, String name, String description, Double price, String fromDate, String toDate,
			boolean available, List<String> images, Double deliveryFee) {
		this.iID = iID;
		this.name = name;
		this.description = description;
		this.price = price;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.available = available;
		this.images = images;
		this.deliveryFee = deliveryFee;
	}

	public Item(String name, String description, Double price, String fromDate, String toDate, boolean available) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.available = available;
	}

	public Item(Long iID, String name, String description, double price, boolean available) {
		this.iID = iID;
		this.name = name;
		this.description = description;
		this.price = price;
		this.available = available;
	}

	public Item(String name, String description, double price, boolean available) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.available = available;
	}
}