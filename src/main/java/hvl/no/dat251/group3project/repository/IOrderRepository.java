package hvl.no.dat251.group3project.repository;

import hvl.no.dat251.group3project.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("orderRepository")
public interface IOrderRepository extends JpaRepository<Order, Long> {
}