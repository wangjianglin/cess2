package io.cess.demo.ws;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.cess.core.ws.UrlPrefix;

import org.springframework.stereotype.Component;

//@WebService
@Named("helloworld")
@Component
@Path("/heloworld2")
@UrlPrefix("hello")
// (endpointInterface="com.demo.HelloWorld",serviceName="HelloWorld")
public class HelloWorldImpl2 {

	@GET
//	@Produces(MediaType.TEXT_PLAIN)
	@Path("/say/{name}")
	public String say(String name) {
		return name + "，您好！";
	}
	

	
	@POST
//	@Produces(MediaType.TEXT_PLAIN)
	@Path("/sayHello/{name}")
	@Produces({ MediaType.APPLICATION_JSON})
	public String sayHello(String name,User user) {
		return user.getName() + "，您好！";
	}

	@GET
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getList/{id}")
	public List<User> getList(Long id) {
		List<User> list = new ArrayList<User>();

		Long sid = 1L;
		User user = new User(sid, "张三" + sid, 21);
		list.add(user);

		sid = 2L;
		user = new User(sid, "张三" + sid, 21);
		list.add(user);

		sid = 3L;
		user = new User(sid, "张三" + sid, 21);
		list.add(user);
		return list;
	}


	@GET
//	@Produces(MediaType.TEXT_PLAIN)
	@Path("/say1")
//	@Override
	public String say2() {
		return "ok.";
	}

//	@Override
	@GET
//	@Produces(MediaType.TEXT_PLAIN)
	@Path("/get")
	public User get() {
		long sid = 123;
		User user = new User(sid, "张三" + sid, 21);
		return user;
	}
}
