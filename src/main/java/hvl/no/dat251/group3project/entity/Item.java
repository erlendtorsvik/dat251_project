package hvl.no.dat251.group3project.entity;

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
	private boolean available;

	@ManyToOne
	private User owner;

	public Item() {
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