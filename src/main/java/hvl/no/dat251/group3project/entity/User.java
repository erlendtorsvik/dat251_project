package hvl.no.dat251.group3project.entity;

import javax.persistence.Entity;
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
	private Gender gender;

	@ManyToOne
	private Address address;

	public enum Gender {
		FEMALE("female"), MALE("male"), NaN("NaN");

		private String gender;

		Gender(String gender) {
			this.gender = gender;
		}

		public String getGender() {
			return this.gender;
		}
	}

	public User(String id, String fname, String lname, String email) {
		this.uID = id;
		this.fname = fname;
		this.lname = lname;
		this.email = email;
	}

	public User() {
	}
}
