package io.cess.demo.ws;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

@Component
@WebService(serviceName = "worldService", endpointInterface = "io.cess.demo.ws.IWorldService")
public class WorldService implements IWorldService {

	public String sayWorld(String username) {
		return username+"! Welcome to CXF in Method[WorldService sayWorld]";
	}

}
