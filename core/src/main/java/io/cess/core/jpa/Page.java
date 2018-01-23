package io.cess.core.jpa;

import java.util.Collection;
/**
 * 
 * @author lin
 * @date 2011-7-15
 *
 */
public class Page<T> {

	private Integer pageNo; //起始页
	private Integer pageSize;//每一页大小
	private Collection<T> list;
	private Integer total;
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Collection<T> getList() {
		return list;
	}
	
	public void setList(Collection<T> list) {
		this.list = list;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
}
