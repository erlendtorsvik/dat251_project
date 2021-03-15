package hvl.no.dat251.group3project;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hvl.no.dat251.group3project.entity.Item;
import hvl.no.dat251.group3project.entity.Order;
import hvl.no.dat251.group3project.service.ItemService;
import hvl.no.dat251.group3project.service.OrderService;
import hvl.no.dat251.group3project.service.UserService;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(SpringExtension.class)
@WebMvcTest
public class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserService userService;

    @MockBean
    private ItemService itemService;

    @Test
    @WithMockUser("test")
    void getAllOrders() throws Exception {
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item(1L, "Langrenn ski", "Veldig mye brukt ski", 99.99, true));
        itemList.add(new Item(2L, "Stor telt", "God og comfy telt", 69.420, false));

        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1L, itemList, LocalDateTime.now(), 100.0,
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(20)));
        when(orderService.findAll()).thenReturn(orders);

        mockMvc.perform(MockMvcRequestBuilders.get("/myOrders")).andDo(print())
                .andExpect(status().isOk()).andExpect(model().attribute("message", "OrdermessageLoL"));

        mockMvc.perform(MockMvcRequestBuilders.get("/allOrders").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1))).andDo(print());

    }

}
