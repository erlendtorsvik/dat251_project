package hvl.no.dat251.group3project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import hvl.no.dat251.group3project.entity.Order;
import hvl.no.dat251.group3project.entity.User;

@Repository("orderRepository")
public interface IOrderRepository extends JpaRepository<Order, Long> {
	List<Order> findBySeller(@Param("seller") User user);

	List<Order> findByLoaner(@Param("loaner") User user);
}