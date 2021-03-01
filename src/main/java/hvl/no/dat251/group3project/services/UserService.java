package hvl.no.dat251.group3project.services;

import java.util.List;

import org.springframework.stereotype.Service;

import hvl.no.dat251.group3project.entity.User;
import hvl.no.dat251.group3project.repositories.IUserRepository;

@Service
public class UserService {

	private IUserRepository userRepository;

	public UserService(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

}
