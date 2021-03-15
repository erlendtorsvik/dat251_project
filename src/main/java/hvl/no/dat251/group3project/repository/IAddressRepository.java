package hvl.no.dat251.group3project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hvl.no.dat251.group3project.entity.Address;

@Repository("addressRepository")
public interface IAddressRepository extends JpaRepository<Address, Long> {

}
