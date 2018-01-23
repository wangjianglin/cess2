package io.cess.core.jpa;

/**
 * 
 * @author 王江林
 * @date 2013-4-26 下午2:26:00
 *
 * @param <T>
 */
public class CommonQueryRemoveParams<T> {

	public Class<?> type;
	public Long id;
	
	public Object entity;
	
	/**
	 * 异常码
	 */
	private Long exceptionCode;
	/**
	 * 异常信息
	 */
	private String exceptionMessage;
	
	
	
	public CommonQueryRemoveParams(Class<?> type,Long id,
			Long exceptionCode, String exceptionMessage) {
		super();
		this.type = type;
		this.id = id;
		this.exceptionCode = exceptionCode;
		this.exceptionMessage = exceptionMessage;
	}
	
	public CommonQueryRemoveParams(Class<?> type,Long id) {
		super();
		this.type = type;
		this.id = id;
	}
	
	public CommonQueryRemoveParams(Object entity) {
		super();
		this.entity = entity;
	}
	
	public CommonQueryRemoveParams(Object entity,
			Long exceptionCode, String exceptionMessage) {
		super();
		this.entity = entity;
		this.exceptionCode = exceptionCode;
		this.exceptionMessage = exceptionMessage;
	}
	
	public Class<?> getType() {
		return type;
	}

	public Long getId() {
		return id;
	}
	public Object getEntity() {
		return entity;
	}
	public Long getExceptionCode() {
		return exceptionCode;
	}
	public String getExceptionMessage() {
		return exceptionMessage;
	}
}
