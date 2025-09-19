package com.ai.aws.rds.dynamodb.model;

public class DataModel {

	private String id;
	private String name;
	private String value;

	public DataModel(String id, String name, String value) {
		this.id = id;
		this.name = name;
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
