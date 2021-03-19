package hvl.no.dat251.group3project.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import hvl.no.dat251.group3project.entity.Address;
import hvl.no.dat251.group3project.repository.IAddressRepository;
import hvl.no.dat251.group3project.service.AddressService;

@SpringBootTest
class AddressServiceTest {

	@Autowired
	private IAddressRepository addressRepository;

	private Address addressSample;

	private AddressService addressService;

	@BeforeEach
	void setup() {
		addressSample = new Address(1L, "gate", "land", 1324, "hus", "kommune", "fylke");
		addressRepository.save(addressSample);
		addressService = new AddressService(addressRepository);
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
}