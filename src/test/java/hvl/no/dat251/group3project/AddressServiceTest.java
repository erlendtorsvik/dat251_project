package hvl.no.dat251.group3project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import hvl.no.dat251.group3project.entity.Address;
import hvl.no.dat251.group3project.repository.IAddressRepository;
import hvl.no.dat251.group3project.repository.IUserRepository;
import hvl.no.dat251.group3project.service.AddressService;
import hvl.no.dat251.group3project.service.UserService;

@SpringBootTest
class AddressServiceTest {

	@Autowired
	private IAddressRepository addressRepository;

	@Autowired
	private IUserRepository userRepository;

	private Address addressSample;

	private AddressService addressService;

	private UserService userService;

	@BeforeEach
	void setup() {
		addressSample = new Address(1L, "gate", "land", 1324, "hus", "kommune", "fylke");
		addressRepository.save(addressSample);
		addressService = new AddressService(addressRepository);
		userService = new UserService(userRepository);
	}

	@Test
	void getAllAddressesShouldRetrunAllAddresses() {
		List<Address> addressList = addressService.findAll();
		Address firstAddress = addressList.get(0);

		assertEquals(addressSample.getAid(), firstAddress.getAid());
		assertEquals(addressSample.getStreetName(), firstAddress.getStreetName());
		assertEquals(addressSample.getCountry(), firstAddress.getCountry());
		assertEquals(addressSample.getPostalCode(), firstAddress.getPostalCode());
		assertEquals(addressSample.getHouseNumber(), firstAddress.getHouseNumber());
		assertEquals(addressSample.getCounty(), firstAddress.getCounty());
		assertEquals(addressSample.getMunicipality(), firstAddress.getMunicipality());
	}

	@Test
	void getAddressByIdShouldReturnAddressIfExists() {
		Address lastAddr = addressService.findById(addressSample.getAid());
		Address noAddr = addressService.findById(-1L);

		assertEquals(addressSample.getAid(), lastAddr.getAid());
		assertEquals(null, noAddr);
	}

	@Test
	void chaningAddressAttributesShouldChangeAttributes() {
		addressService.setCountry(addressSample, "norge");
		addressService.setCounty(addressSample, "vestlandet");
		addressService.setHouseNumber(addressSample, "2b");
		addressService.setMunicipality(addressSample, "bergen kommune");
		addressService.setPostalCode(addressSample, 1234);
		addressService.setStreetName(addressSample, "alegaten");
		addressService.save(addressSample);

		Address lastAddress = addressService.findById(addressSample.getAid());

		assertEquals(addressSample.getAid(), lastAddress.getAid());
		assertEquals(addressSample.getStreetName(), lastAddress.getStreetName());
		assertEquals(addressSample.getCountry(), lastAddress.getCountry());
		assertEquals(addressSample.getPostalCode(), lastAddress.getPostalCode());
		assertEquals(addressSample.getHouseNumber(), lastAddress.getHouseNumber());
		assertEquals(addressSample.getCounty(), lastAddress.getCounty());
		assertEquals(addressSample.getMunicipality(), lastAddress.getMunicipality());
	}

//	@Test
//	void newAddressShouldHaveNoUsers() {
//		assertEquals(0, addressSample.getUsers().size());
//	}

//	@Test
//	void newUserWithAddedAddressShouldAddToUsersList() {
//		User user = new User("3", "User", " Sample", "dat251@hvl.no");
//		userService.setAddress(user, addressSample);
//		userService.save(user);
//		addressService.addUser(addressSample, user);
//		addressService.save(addressSample);
//
//		Address lastAddress = addressService.findById(addressSample.getAid());
//
//		assertEquals(1, lastAddress.getUsers().size());
//	}
}
