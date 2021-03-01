package hvl.no.dat251.group3project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hvl.no.dat251.group3project.entity.User;

@Repository("userRepository")
public interface IUserRepository extends JpaRepository<User, String> {
}