package hvl.no.dat251.group3project.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class Address {

	@Id
	@GeneratedValue
	private Long aID;
	private String streetName;
	private String country;
	private int postalCode;
	private String houseNumber;
	private String county;
	private String municipality;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	List<User> users = new ArrayList<User>();

	public Address(Long aID, String streetName, String country, int postalCode, String houseNumber, String county,
			String municipality) {
		this.aID = aID;
		this.streetName = streetName;
		this.country = country;
		this.postalCode = postalCode;
		this.houseNumber = houseNumber;
		this.county = county;
		this.municipality = municipality;
	}

	public Address(String streetName, String country, int postalCode, String houseNumber, String county,
			String municipality) {
		this.streetName = streetName;
		this.country = country;
		this.postalCode = postalCode;
		this.houseNumber = houseNumber;
		this.county = county;
		this.municipality = municipality;
	}

	public Address() {
	}
}
