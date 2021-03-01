package hvl.no.dat251.group3project.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class User {

	@Id
	private String uID;
	private String fname;
	private String lname;
	private String email;

	public User(String id, String fname, String lname, String email) {
		this.uID = id;
		this.fname = fname;
		this.lname = lname;
		this.email = email;
	}

	public User() {
	}

}
