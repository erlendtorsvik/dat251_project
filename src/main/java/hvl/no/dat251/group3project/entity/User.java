package hvl.no.dat251.group3project.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class User {

	@Id
	@GeneratedValue
	private String id;
	private String name;
	private boolean dead;

	public User(String id, String name, boolean dead) {
		this.id = id;
		this.name = name;
		this.dead = dead;
	}

	public User(String name, boolean dead) {
		this.name = name;
		this.dead = dead;
	}

}
