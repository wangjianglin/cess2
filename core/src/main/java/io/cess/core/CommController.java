package io.cess.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.cess.core.spring.JsonBody;

@Controller
@RequestMapping("/core/comm")
//@RequestMapping()
public class CommController{// implements Controller {

	@RequestMapping("/test")
	@JsonBody
	public String test(@RequestParam(value="data",required=false) String data){
		System.out.println("data:"+data);
		return data;
	}
	
	@RequestMapping("/null")
	@JsonBody
	public void testNull(){
	}

	
	@JsonBody
	@RequestMapping("/sessionId")
	public String sessionId(HttpServletRequest request,HttpServletResponse response){
		System.out.println("session id:"+request.getSession().getId());
		return request.getSession().getId();
	}
}
