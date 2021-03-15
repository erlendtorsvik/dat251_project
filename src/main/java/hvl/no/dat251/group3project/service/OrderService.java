package hvl.no.dat251.group3project.service;

import hvl.no.dat251.group3project.entity.Order;
import hvl.no.dat251.group3project.firebase.FBInitialize;
import hvl.no.dat251.group3project.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private IOrderRepository orderRepository;

    @Autowired
    private FBInitialize fb;

    public OrderService(IOrderRepository orderRepository) {
       this.orderRepository = orderRepository;
    }

    public List<Order> findAll() { return orderRepository.findAll(); }

    public Order findById(Long oID) {
        if(findByIdIsPresent(oID)){

            return orderRepository.findById(oID).get();
        }
        return null;

    }

    public boolean findByIdIsPresent(Long oID) {
        return orderRepository.existsById(oID);
    }

}