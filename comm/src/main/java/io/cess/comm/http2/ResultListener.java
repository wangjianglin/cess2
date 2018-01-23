package io.cess.comm.http2;


import java.util.List;

/**
 * 
 * @author 王江林
 * @date 2013-7-16 上午11:28:01
 *
 */
public interface ResultListener {
	 public void result(Object obj, List<Error> warning);
	 public void fault(Error error);
}


