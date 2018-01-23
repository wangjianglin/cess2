package io.cess.core.spring;

import java.util.Date;


public class TestEntity {

	private Long id;
	
	private String name;
	
	private String value;
	
	private Date date;

	private TestEntity data;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public TestEntity getData() {
		return data;
	}

	public void setData(TestEntity data) {
		this.data = data;
	}
	
}
