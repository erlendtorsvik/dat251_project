package hvl.no.dat251.group3project.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Address {

	@Id
	@GeneratedValue
	private Long aid;
	private String streetName;
	private String country;
	private int postalCode;
	private String houseNumber;
	private String county;
	private String municipality;

	public Address(Long aID, String streetName, String country, int postalCode, String houseNumber, String county,
			String municipality) {
		this.aid = aID;
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
		streetName = "";
		country = "";
		postalCode = 0;
		houseNumber = "";
		county = "";
		municipality = "";
	}
}