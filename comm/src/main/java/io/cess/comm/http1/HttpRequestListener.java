package io.cess.comm.http1;


import java.util.List;

/**
 * 监听所有http通信
 * @author 王江林
 * @date 2013-7-16 上午11:33:44
 *
 */
public interface HttpRequestListener {

	 /// <summary>
    /// 开始请求
    /// </summary>
    //public static event System.Action<HttpCommunicateType, object> HttpRequest;
	public void HttpRequest(HttpCommunicateType type, Object obj);
    /// <summary>
    /// 请求完成
    /// </summary>
    //public static event System.Action<HttpCommunicateType, object, object, IList<Error>> HttpRequestComplete;
	public void HttpRequestComplete(HttpCommunicateType type, Object obj1, Object obj2, List<Error> warning);

    /// <summary>
    /// 请求错误
    /// </summary>
    //public static event System.Action<HttpCommunicateType, object, Error> HttpRequestFault;
	public void HttpRequestFault(HttpCommunicateType type, Object obj, Error error);
}
