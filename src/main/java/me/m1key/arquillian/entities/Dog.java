package me.m1key.arquillian.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dogs")
public class Dog {

	@Id
	@GeneratedValue
	@Column(name = "DOG_ID")
	@SuppressWarnings("unused")
	private Integer id;

	@Column(name = "NAME", unique = true, length = 128, nullable = false)
	private String name;

	protected Dog() {
	}

	public Dog(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
