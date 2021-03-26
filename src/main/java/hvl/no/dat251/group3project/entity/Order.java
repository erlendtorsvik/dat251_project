package hvl.no.dat251.group3project.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "order_table")
public class Order {

	@Id
	private Long oID;
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Item> items = new ArrayList<>();
	// private LocalDateTime date;
	private Double insurance;
	private String dateFrom;
	private String dateTo;
	private Double totalPrice;
	@ManyToOne
	private User seller;
	@ManyToOne
	private User loaner;

	public Order() {
	}

	public Order(Long oID, List<Item> items, Double insurance, String dateFrom, String dateTo) {
		this.oID = oID;
		this.items = items;
		// this.date = date;
		this.insurance = insurance;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
	}

	public Order(List<Item> items, double insurance, String dateFrom, String dateTo) {
		this.items = items;
		// this.date = date;
		this.insurance = insurance;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
	}

	public Order(Long oID, List<Item> items, Double insurance, String dateFrom, String dateTo, User seller,
			User loaner) {
		this.oID = oID;
		this.items = items;
		this.insurance = insurance;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.seller = seller;
		this.loaner = loaner;
	}

	public Order(List<Item> items, Double insurance, String dateFrom, String dateTo, User seller, User loaner) {

		this.items = items;
		this.insurance = insurance;
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.seller = seller;
		this.loaner = loaner;
	}

	public Order(List<Item> items, User seller, User loaner) {

		this.items = items;
		this.seller = seller;
		this.loaner = loaner;
	}

}