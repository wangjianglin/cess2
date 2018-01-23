package io.cess.demo.ws;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

//@WebService
//@Named("helloworld")
@Component
@Path("/heloworld")
// (endpointInterface="com.demo.HelloWorld",serviceName="HelloWorld")
public class HelloWorldImpl implements HelloWorld {
	public String say(String name) {
		return name + "，您好！";
	}

	public String sayHello(String name,User user) {
		return user.getName() + "，您好！";
	}

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

	@Override
	public String say2() {
		return "ok.";
	}

	@Override
	public User get() {
		long sid = 123;
		User user = new User(sid, "张三" + sid, 21);
		return user;
	}
}
