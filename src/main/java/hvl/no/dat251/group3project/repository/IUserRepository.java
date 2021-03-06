package hvl.no.dat251.group3project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import hvl.no.dat251.group3project.entity.User;

@Repository("userRepository")
public interface IUserRepository extends JpaRepository<User, String> {
	User findByFname(@Param("fname") String fname);

	User findByEmail(@Param("email") String email);
}