package io.cess.demo.ws;

import javax.jws.WebService;

@WebService
public interface IHelloService {
	public String sayHello(String username);
}
