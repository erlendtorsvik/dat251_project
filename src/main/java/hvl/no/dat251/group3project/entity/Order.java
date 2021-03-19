package hvl.no.dat251.group3project.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;


@Data
@Entity
@Table(name = "order_table")
public class Order {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long oID;
    @ManyToMany
    private List<Item> items = new ArrayList<>();
    private LocalDateTime date;
    private Double insurance;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    @ManyToOne
    private User seller;
    @ManyToOne
    private User loaner;

    public Order() {
    }

    public Order(Long oID, List<Item> items, LocalDateTime date, Double insurance,
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


