package hvl.no.dat251.group3project.entity;

import lombok.Data;


import javax.persistence.*;
import java.util.*;


@Data
@Entity
@Table(name = "order_table")
public class Order {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long oID;
    @ManyToMany
    private List<Item> items = new ArrayList<>();
    private Date date;
    private double price;
    private double insurance;
    private Date dateFrom;
    private Date dateTo;

    public Order() {
    }

    public Order(long oID, List<Item> items, Date date, double price, double insurance, Date dateFrom, Date dateTo) {
        this.oID = oID;
        this.items = items;
        this.date = date;
        this.price = price;
        this.insurance = insurance;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Order(List<Item> items, Date date, double price, double insurance, Date dateFrom, Date dateTo) {
        this.items = items;
        this.date = date;
        this.price = price;
        this.insurance = insurance;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
}


