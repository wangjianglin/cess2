//package lin.web.action;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.struts2.convention.annotation.Result;
//import org.apache.struts2.convention.annotation.Results;
//
//import lin.core.jpa.Page;
//import ExtData;
//
//
//@SuppressWarnings("unchecked")
//@Results(value={
//		@Result(name="read",type="cloud",params={"root","page"}),
//		@Result(name="create",type="cloud",params={"root","data"}),
//		@Result(name="update",type="cloud",params={"root","data"}),
//		@Result(name="destroy",type="cloud",params={"root","data"})
//})
//public class ExtAction {
//
//	private static List<ExtData> list = new ArrayList<ExtData>();
//	private ExtData data;
//	@SuppressWarnings("rawtypes")
//	private static Page page = new Page();
//	static{
//		for(long n=1;n<11;n++){
//			ExtData data = new ExtData();
//			data.setId(n);
//			data.setName("name-" + n);
//			data.setDesc("desc-" + n);
//			list.add(data);
//		}
//		page.setList(list);
//		page.setPageNo(2);
//		page.setPageSize(30);
//		page.setTotal(300);
//	}
//	
//
//	private Integer pageNo;
//	private Integer pageSize;
//	
//	public String read(){
//		System.out.println("page no:"+pageNo+"\tpage size:"+pageSize);
//		return "read";
//	}
//	
//	public String create(){
//		data.setDesc("create-"+data.getDesc());
//		ExtData d = new ExtData();
//		d.setName("create-"+data.getDesc());
//		list.add(d);
//		return "create";
//	}
//	
//	public String update(){
//		data.setDesc("update-"+data.getDesc());
//		return "update";
//	}
//	
//	public String destroy(){
//		data.setDesc("destroy-"+data.getDesc());
//		return "destroy";
//	}
//
//	public List<ExtData> getList() {
//		return list;
//	}
//
//	public void setList(List<ExtData> list) {
//		ExtAction.list = list;
//	}
//
//	public ExtData getData() {
//		return data;
//	}
//
//	public void setData(ExtData data) {
//		this.data = data;
//	}
//
//	@SuppressWarnings("rawtypes")
//	public Page getPage() {
//		return page;
//	}
//
//	public void setPage(@SuppressWarnings("rawtypes") Page page) {
//		ExtAction.page = page;
//	}
//
//	public Integer getPageNo() {
//		return pageNo;
//	}
//
//	public void setPageNo(Integer pageNo) {
//		this.pageNo = pageNo;
//	}
//
//	public Integer getPageSize() {
//		return pageSize;
//	}
//
//	public void setPageSize(Integer pageSize) {
//		this.pageSize = pageSize;
//	}
//}
