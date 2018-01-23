package io.cess.comm.tcp;

@FunctionalInterface
public interface Response {
	void response(ResponsePackage pack);
}
