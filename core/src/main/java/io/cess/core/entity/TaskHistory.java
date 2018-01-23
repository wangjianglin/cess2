//package lin.core.entity;
//
//import java.util.Date;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//
///**
// * 记录已经执行完成的任务状态，包括正常结束 和为执行完成的，继承时才用一个实体一个表的方式
// *
// * @author 李水云
// * 2013-6-8
// *
// */
//@Entity(name = "task_history")
//public class TaskHistory extends Task{
//	/**
//	 * 任务执行完成时间
//	 */
//	@Column(name = "task_history_completeDate")
//	private Date completeDate;
//
//	/**
//	 * 任务执行原因
//	 */
//	@Column(name = "task_history_cause")
//	private String cause;
//
//	public Date getCompleteDate() {
//		return completeDate;
//	}
//
//	public void setCompleteDate(Date completeDate) {
//		this.completeDate = completeDate;
//	}
//
//	public String getCause() {
//		return cause;
//	}
//
//	public void setCause(String cause) {
//		this.cause = cause;
//	}
//}
