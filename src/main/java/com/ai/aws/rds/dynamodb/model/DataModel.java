package com.ai.aws.rds.dynamodb.model;

import java.util.Objects;

public class DataModel {

	private String id;
	private String name;
	private String value;

	// Default constructor (required by some frameworks/libraries)
	public DataModel() {
	}

	// Parameterized constructor
	public DataModel(String id, String name, String value) {
		this.id = id;
		this.name = name;
		this.value = value;
	}

	// Getters
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	// Setters
	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	// toString
	@Override
	public String toString() {
		return "DataModel{id='" + id + "', name='" + name + "', value='" + value + "'}";
	}

	// equals and hashCode (good for comparison and collections)
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DataModel))
			return false;
		DataModel that = (DataModel) o;
		return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, value);
	}
}
