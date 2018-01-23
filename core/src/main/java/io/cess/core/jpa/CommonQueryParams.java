package io.cess.core.jpa;

/**
 * 
 * @author 王江林
 * @date 203-3-25
 * 
 */
public class CommonQueryParams<T> {
	/**
	 * 异常码
	 */
	private Long exceptionCode;
	/**
	 * 异常信息
	 */
	private String exceptionMessage;
	/**
	 * 查询语句
	 */
	private String query;
	/**
	 * 参数名称
	 */
	private String[] names;
	/**
	 * 参数值
	 */
	private Object[] params;
	
	public CommonQueryParams(String query) {
		super();
		this.query = query;
	}
	
	public CommonQueryParams(String query,Long exceptionCode, String exceptionMessage) {
		super();
		this.exceptionCode = exceptionCode;
		this.exceptionMessage = exceptionMessage;
		this.query = query;
	}
	
	public CommonQueryParams(String query,
			String[] names, Object ... params) {
		super();
		this.query = query;
		this.names = names;
		this.params = params;
	}
	
	public CommonQueryParams(String query, Long exceptionCode, String exceptionMessage,
			String[] names, Object ... params) {
		super();
		this.exceptionCode = exceptionCode;
		this.exceptionMessage = exceptionMessage;
		this.query = query;
		this.names = names;
		this.params = params;
	}
	
	public CommonQueryParams(String query,
			Object ... params) {
		super();
		this.query = query;
		this.params = params;
	}
	
	public CommonQueryParams(String query, Long exceptionCode, String exceptionMessage,
			Object ... params) {
		super();
		this.exceptionCode = exceptionCode;
		this.exceptionMessage = exceptionMessage;
		this.query = query;
		this.params = params;
	}

	public Long getExceptionCode() {
		return exceptionCode;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public String getQuery() {
		return query;
	}

	public String[] getNames() {
		return names;
	}

	public Object[] getParams() {
		return params;
	}
}
