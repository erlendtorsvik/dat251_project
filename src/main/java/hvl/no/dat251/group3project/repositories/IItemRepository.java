package hvl.no.dat251.group3project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import hvl.no.dat251.group3project.entity.Item;

@Repository("itemRepository")
public interface IItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByNameContainingIgnoreCase(@Param("name") String name);
}