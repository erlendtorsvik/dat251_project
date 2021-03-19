package hvl.no.dat251.group3project.entity;

import lombok.Data;


import javax.persistence.*;
import java.time.LocalDateTime;
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
    private LocalDateTime date;
    private double insurance;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    @ManyToOne
    private User seller;
    @ManyToOne
    private User loaner;

    public Order() {
    }

    public Order(long oID, List<Item> items, LocalDateTime date, double insurance,
                 LocalDateTime dateFrom, LocalDateTime dateTo) {
        this.oID = oID;
        this.items = items;
        this.date = date;
        this.insurance = insurance;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Order(List<Item> items, LocalDateTime date, double insurance,
                 LocalDateTime dateFrom, LocalDateTime dateTo) {
        this.items = items;
        this.date = date;
        this.insurance = insurance;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
}


