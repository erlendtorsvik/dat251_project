package hvl.no.dat251.group3project.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class User {

	@Id
	private String uID;

	private String fname;
	private String lname;
	private String email;
	private int age;
	private Boolean contactByEmail;
	private Gender gender;
	private Long phoneNumber;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<Preferences> preferences = new ArrayList<>();

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Address address;

	public enum Gender {
		FEMALE("female"), MALE("male"), UNSPECIFIED("unspecified");

		private String gender;

		Gender(String gender) {
			this.gender = gender;
		}

		public String getGender() {
			return this.gender;
		}
	}

	public enum Preferences {
		SKIING("Skiing"), TENT("Tent"), CANOEING("Canoeing"), HIKING("Hiking"), CYCLING("Cycling"),
		CLIMBING("Climbing"), EXTREME("Extreme"), NAN("");

		private String preference;

		Preferences(String preference) {
			this.preference = preference;
		}

		public String getPreference() {
			return this.preference;
		}
	}

	public User(String id, String fname, String lname, String email) {
		this.uID = id;
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.gender = Gender.UNSPECIFIED;
	}

	public User(String uID, String fname, String lname, String email, int age, Boolean contactByEmail, Gender gender,
			Long phoneNumber, List<Preferences> preferences, Address address) {
		this.uID = uID;
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.age = age;
		this.contactByEmail = contactByEmail;
		this.gender = gender;
		this.phoneNumber = phoneNumber;
		this.preferences = preferences;
		this.address = address;
	}

	public User() {
	}

	public void setPreferences(List<String> preferences) {
		List<Preferences> prefList = new ArrayList<>();
		for (String pref : preferences) {
			Preferences p = Preferences.valueOf(pref);
			if (p != Preferences.NAN) {
				prefList.add(p);
			}
		}
		this.preferences.clear();
		this.preferences.addAll(prefList);
	}
}