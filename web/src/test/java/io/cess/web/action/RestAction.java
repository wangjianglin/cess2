//package lin.web.action;
//
//import com.opensymphony.xwork2.ActionSupport;
//
//public class RestAction extends ActionSupport {
//
//	public RestAction(){
//		System.out.println("reset action instance!");
//	}
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	public String execute() throws Exception {
//		return SUCCESS;
//	}
//	
//	public String show() {
//		System.out.println("id:"+id);
//		return "show";
//	}
//	
//	public String list() {
//		return "list";
//	}
//	
//	public String index() {
//		return SUCCESS;
//	}
//    // GET /orders/new
////    public String editNew() {
////        //model = new Order();
////        return "editNew";
////    }
////
////    // GET /orders/1/deleteConfirm
////    public String deleteConfirm() {
////        return "deleteConfirm";
////    }
////
////    // DELETE /orders/1
////    public String destroy() {
////        //ordersService.remove(id);
////        //addActionMessage("Order removed successfully");
////        return "success";
////    }
//
//    // POST /orders
////    public HttpHeaders create() {
////        //ordersService.save(model);
////        //addActionMessage("New order created successfully");
////        return new DefaultHttpHeaders("success")
////            .setLocationId("");
////    }
////
////    // PUT /orders/1
////    public String update() {
////        //ordersService.save(model);
////        //addActionMessage("Order updated successfully");
////        return "success";
////    }
////
////    public void validate() {
////        //if (model.getClientName() == null || model.getClientName().length() ==0) {
////        //    addFieldError("clientName", "The client name is empty");
////        //}
////    }
////
//    public void setId(String id) {
//        if (id != null) {
//           // this.model = ordersService.get(id);
//        }
//        this.id = id;
//    }
//    private String id;
//}
