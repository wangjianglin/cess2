package io.cess.util.json;

/**
 * 
 * @author 王江林
 * @date 2013-7-16 下午8:48:36
 *
 */
public class JSONException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JSONException() {
		super();
	}

	public JSONException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public JSONException(String message, Throwable cause) {
		super(message, cause);
	}

	public JSONException(String message) {
		super(message);
	}

	public JSONException(Throwable cause) {
		super(cause);
	}

}
