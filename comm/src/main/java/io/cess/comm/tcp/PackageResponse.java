package io.cess.comm.tcp;

import io.cess.util.thread.AutoResetEvent;


public class PackageResponse {
	private AutoResetEvent set = new AutoResetEvent();

	PackageResponse() {
	}

	private volatile ErrorPackage error;
	private volatile ResponsePackage pack;

	void response(ResponsePackage pack) {
		this.pack = pack;
		set.set();
	}

	void setError(ErrorPackage error){
		this.error = error;
		set.set();
	}

	public ResponsePackage getResponse(){
		waitForEnd();
		return this.pack;
	}

	// / <summary>
	// /
	// / </summary>
	// / <param name="timeout">超时，以毫秒为单位，默认120秒</param>
	// / <returns></returns>
	public PackageResponse waitForEnd() {
		return waitForEnd(120000);
	}

	public PackageResponse waitForEnd(int timeout) {
		set.waitOne();
		return this;
	}
}
