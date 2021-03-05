package hvl.no.dat251.group3project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import hvl.no.dat251.group3project.entity.Address;

public interface IAddressRepository extends JpaRepository<Address, Long> {

}
