package io.cess.demo.ws;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

@Component
//@WebService(serviceName = "helloService", endpointInterface = "com.j4t.demo.ws.IHelloService")
@WebService(serviceName = "helloService3", endpointInterface = "io.cess.demo.ws.IHelloService")
public class HelloService implements IHelloService {

	public String sayHello(String username) {
		return username+"! Welcome to CXF in Method[HelloService sayHello]";
	}

}
