package io.cess.comm.http2;

import java.util.List;

public class Page<T> {
//	 Page * page = [[Page alloc] init];
//	    page.pageSize = json[@"pageSize"].asInt;
//	    page.pageNo = json[@"currentpage"].asInt;
//	    page.total = json[@"rowCount"].asInt;
//	    page.datas = [json[@"resultList"] asObjectArray:^JsonModel *(Json * json) {
//	        return [[Requirement alloc] initWithJson:json];
//	    }];
	private int pageSize;
	private int currentPage;
	private int rowCount;
	private List<T> resultList;
	private List<T> vo_list;
	private int pages;
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public List<T> getResultList() {
		return resultList;
	}
	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public List<T> getVo_list() {
		return vo_list;
	}
	public void setVo_list(List<T> vo_list) {
		this.vo_list = vo_list;
	}
}
