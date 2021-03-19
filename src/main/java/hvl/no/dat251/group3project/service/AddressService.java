package hvl.no.dat251.group3project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import hvl.no.dat251.group3project.entity.Address;
import hvl.no.dat251.group3project.repository.IAddressRepository;

@Service
public class AddressService {

	IAddressRepository addressRepository;

	UserService userService;

	public AddressService(IAddressRepository addressRepository) {
		this.addressRepository = addressRepository;
	}

	public List<Address> findAll() {
		return addressRepository.findAll();
	}

	public boolean findByIdIsPresent(Long aid) {
		return addressRepository.existsById(aid);
	}

	public Address findById(Long aid) {
		if (findByIdIsPresent(aid)) {
			return addressRepository.findById(aid).get();
		}
		return null;
	}

	public void save(Address addressSample) {
		addressRepository.save(addressSample);
	}

	public void setStreetName(Address addressSample, String streetName) {
		addressSample.setStreetName(streetName);
	}

	public void setCountry(Address addressSample, String country) {
		addressSample.setCountry(country);
	}

	public void setPostalCode(Address addressSample, int postalCode) {
		addressSample.setPostalCode(postalCode);
	}

	public void setHouseNumber(Address addressSample, String houseNumber) {
		addressSample.setHouseNumber(houseNumber);
	}

	public void setCounty(Address addressSample, String county) {
		addressSample.setCounty(county);
	}

	public void setMunicipality(Address addressSample, String municipality) {
		addressSample.setMunicipality(municipality);
	}
}