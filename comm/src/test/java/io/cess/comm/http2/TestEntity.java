package io.cess.comm.http2;

import java.util.Date;
import java.util.List;


public class TestEntity {

	private Long id;
	private List<Long> ids;
	
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

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
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
