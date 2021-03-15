package hvl.no.dat251.group3project;


import hvl.no.dat251.group3project.entity.Item;
import hvl.no.dat251.group3project.entity.Order;
import hvl.no.dat251.group3project.repository.IOrderRepository;
import hvl.no.dat251.group3project.service.ItemService;
import hvl.no.dat251.group3project.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private IOrderRepository orderRepository;
    private OrderService orderService;
    private Order orderSample1;
    private Order orderSample2;
    private Order orderSample3;
    private Order orderSample4;


  /*  @BeforeEach
    void setup() {
        itemSample1 = new Item("Ski", "Slalomski", 1000, true);
        itemRepository.save(itemSample1);
        itemSample2 = new Item("Kajakk", "tomannskajakk", 1000, true);
        itemRepository.save(itemSample2);
        itemSample3 = new Item("Sykkel", "Terrengsykkel", 500, true);
        itemRepository.save(itemSample3);
        itemSample4 = new Item("Ski", "Langrenn", 100, false);
        itemRepository.save(itemSample4);

        itemService = new ItemService(itemRepository);
    }
*/}
