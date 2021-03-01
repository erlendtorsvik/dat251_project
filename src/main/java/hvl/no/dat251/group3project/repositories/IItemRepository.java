package hvl.no.dat251.group3project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import hvl.no.dat251.group3project.entity.Item;

public interface IItemRepository extends JpaRepository<Item, Long> {

}
