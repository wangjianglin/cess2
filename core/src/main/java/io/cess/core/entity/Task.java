//package lin.core.entity;
//
//import java.sql.Blob;
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Inheritance;
//import javax.persistence.InheritanceType;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;
//
///**
// * 任务实体，记录未执行完成的任务状态
// *
// * @author 李水云 2013-6-8
// */
//@Entity(name = "task")
//@Inheritance(strategy = InheritanceType.JOINED)
//@NamedQueries(value = {
//		@NamedQuery(name = "findTaskByHashCode", query = "select a from lin.core.entity.Task a where a.hashCode = ?1"),
//		@NamedQuery(name = "findTaskByType", query = "select a from lin.core.entity.Task a where a.type = ?1"),
//		@NamedQuery(name = "findTaskByOwner", query = "select a from lin.core.entity.Task a where a.owner = ?1"),
//		@NamedQuery(name = "findTaskStausByOwner", query = "select a.status from lin.core.entity.Task a where a.owner = ?1"),
//		@NamedQuery(name = "findTaskStausByType", query = "select a.status from lin.core.entity.Task a where a.type = ?1")})
//public class Task {
//	/**
//	 * 主键id
//	 */
//	@Id
//	@GeneratedValue(strategy=GenerationType.AUTO)
//	private Long id;
//
//	/**
//	 * 记录任务对象的Hash Code，用于区分任务
//	 */
//	@Column(name = "task_hashCode")
//	private Long hashCode;
//
//	/**
//	 * 任务的状态
//	 */
//	@Column(name = "task_status")
//	@Enumerated(EnumType.STRING)
//	private TaskStatus status = TaskStatus.PREPARE;
//
//	/**
//	 * 任务启动时间
//	 */
//	@Column(name = "task_addDate")
//	private Date addDate;
//
//	/**
//	 * 任务开始执行时间
//	 */
//	@Column(name = "task_executionDate")
//	private Date executionDate;
//
//	/**
//	 * 任务类型，用于区分同一类任务
//	 */
//	@Column(name = "task_type")
//	private String type;
//
//	/**
//	 * 任务拥有者
//	 */
//	@Column(name = "task_owner")
//	private String owner;
//
//	/**
//	 * 表示任务第几次执行
//	 */
//	@Column(name = "task_numberOfTimes")
//	private int numberOfTimes;
//
//	/**
//	 * 任务对象，用于第二次执行用，执行的任务必须能序列号
//	 */
//	@Column(name = "task_taskObject")
//	private Blob taskObject;
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public Long getHashCode() {
//		return hashCode;
//	}
//
//	public void setHashCode(Long hashCode) {
//		this.hashCode = hashCode;
//	}
//
//	public TaskStatus getStatus() {
//		return status;
//	}
//
//	public void setStatus(TaskStatus status) {
//		this.status = status;
//	}
//
//	public Date getAddDate() {
//		return addDate;
//	}
//
//	public void setAddDate(Date addDate) {
//		this.addDate = addDate;
//	}
//
//	public Date getExecutionDate() {
//		return executionDate;
//	}
//
//	public void setExecutionDate(Date executionDate) {
//		this.executionDate = executionDate;
//	}
//
//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}
//
//	public String getOwner() {
//		return owner;
//	}
//
//	public void setOwner(String owner) {
//		this.owner = owner;
//	}
//
//	public int getNumberOfTimes() {
//		return numberOfTimes;
//	}
//
//	public void setNumberOfTimes(int numberOfTimes) {
//		this.numberOfTimes = numberOfTimes;
//	}
//
//	public Blob getTaskObject() {
//		return taskObject;
//	}
//
//	public void setTaskObject(Blob taskObject) {
//		this.taskObject = taskObject;
//	}
//}
