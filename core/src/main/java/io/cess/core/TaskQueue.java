//package lin.core;
//
//import java.util.Collection;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
//import lin.CessException;
//import lin.core.entity.Task;
//import lin.core.entity.TaskHistory;
//import lin.core.entity.TaskStatus;
//import lin.core.services.TaskQueueService;
////import lin.core.struts.autowiring.AutoWiring;
//
//public class TaskQueue {
//	private TaskQueueService service;
//
//	private static TaskQueue taskQueue;
//
//	static {
//		taskQueue = new TaskQueue();
////		AutoWiring.autoWiring(taskQueue);
//	}
//
//	private TaskQueue() {
//
//	}
//
//	/**
//	 * 任务执行以前调用
//	 *
//	 * @param r
//	 */
//	private static void before(Runnable r) {
//		Task task = taskQueue.service.getTaskByHashCode(r.hashCode());
//		task.setStatus(TaskStatus.EXECUTION);
//		task.setExecutionDate(new Date());
//		taskQueue.service.updateTask(task);
//	}
//
//	/***
//	 * 任务执行完成以后、 任务执行完成以后将其在Task的记录删除 将新记录存到TaskHistory里
//	 *
//	 * @param r
//	 */
//	private static void after(Runnable r) {
//		Task task = taskQueue.service.getTaskByHashCode(r.hashCode());
//		TaskHistory t = new TaskHistory();
//		t.setAddDate(task.getAddDate());
//		t.setCause("");
//		t.setCompleteDate(new Date());
//		t.setExecutionDate(task.getExecutionDate());
//		t.setHashCode(task.getHashCode());
//		t.setNumberOfTimes(1);
//		t.setOwner(task.getOwner());
//		t.setStatus(TaskStatus.COMPLETE);
//		t.setTaskObject(task.getTaskObject());
//		t.setType(task.getType());
//		taskQueue.service.deleteTask(task);
//		taskQueue.service.addTaskHistory(t);
//	}
//
//	/***
//	 * args1:corePoolSize 核心线程数 args2:maximumPoolSize 运行最大线程数
//	 * args3:keepAliveTime 返回线程保持活动的时间 args4:unit 运行时间单位 args5:workQueue
//	 * 线程池所使用的缓冲队列
//	 */
//	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 4,
//			3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3)) {
//
//		@Override
//		protected void beforeExecute(Thread t, Runnable r) {
//			before(r);
//			super.beforeExecute(t, r);
//		}
//
//		@Override
//		protected void afterExecute(Runnable r, Throwable t) {
//			after(r);
//			super.afterExecute(r, t);
//		}
//	};
//
//	/**
//	 * 新增任务
//	 *
//	 * @param task
//	 * @param owner
//	 * @param taskTppe
//	 * @return
//	 */
//	public static String addTask(Runnable task, String owner, String taskType) {
//		Task t = new Task();
//		t.setHashCode((long) task.hashCode());
//		t.setAddDate(new Date());
//		t.setOwner(owner);
//		t.setStatus(TaskStatus.PREPARE);
//		t.setTaskObject(null);
//		t.setType(taskType);
//		t = taskQueue.service.addTask(t);
//		executor.execute(task);
//		return t.getId().toString();
//	}
//
//	/***
//	 * 根据 taskId获得任务状态
//	 *
//	 * @param taskId
//	 * @return
//	 */
//	public static TaskStatus getTaskStatus(long taskId) {
//		TaskStatus taskStatus = taskQueue.service.getTaskStatus(taskId);
//		return taskStatus;
//	}
//
//	public static Task getTaskByHashCode(long hashCode) {
//		Task task = taskQueue.service.getTaskByHashCode(hashCode);
//		return task;
//	}
//
//	/***
//	 * 根据owner 获得任务状态
//	 *
//	 * @param owner
//	 * @return
//	 */
//	public static Collection<TaskStatus> getTaskStausesByOwner(String owner) {
//		Collection<TaskStatus> taskStatus = taskQueue.service
//				.getTaskStatusesByOwner(owner);
//		return taskStatus;
//	}
//
//	/**
//	 * 根据taskType 获得任务状态
//	 *
//	 * @param taskType
//	 * @return
//	 */
//	public static Collection<TaskStatus> getTaskStausesByType(String taskType) {
//		Collection<TaskStatus> taskStatus = taskQueue.service
//				.getTaskStatusesByOwner(taskType);
//		return taskStatus;
//	}
//
//	/***
//	 * 被拒绝的任务
//	 */
//	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//		// executor.getRejectedExecutionHandler().rejectedExecution(r,
//		// executor);
//	}
//
//	/**
//	 * 取消任务要在线程池里取消掉 可以取消的再删除掉 不可取消的抛异常
//	 *
//	 * @param taskId
//	 */
//	public static void cancelTask(long taskId) {
//		Task task = taskQueue.service.getTaskById(taskId);
//		// 判断在线程池里是否可以取消,可以取消的直接取消,转移到任务历史表,不可以取消的抛异常
//		if (executor.getQueue().remove(task)) {
//			TaskHistory t = new TaskHistory();
//			t.setAddDate(task.getAddDate());
//			t.setCause("");
//			t.setCompleteDate(new Date());
//			t.setExecutionDate(task.getExecutionDate());
//			t.setHashCode(task.getHashCode());
//			t.setNumberOfTimes(1);
//			t.setOwner(task.getOwner());
//			t.setStatus(TaskStatus.CANCEL);
//			t.setTaskObject(task.getTaskObject());
//			t.setType(task.getType());
//
//			taskQueue.service.deleteTask(task);
//			taskQueue.service.addTaskHistory(t);
//		} else {
//			throw new CessException(-0x1_0405, "任务不可以取消");
//		}
//	}
//
//	/***
//	 * 取消任务
//	 *
//	 * @param taskTpye
//	 */
//	public static void cancelTasksByType(String taskTpye) {
//		Collection<Task> tasks = taskQueue.service.getTaskByTaskTpye(taskTpye);
//		Iterator<Task> it = tasks.iterator();
//		while (it.hasNext()) {
//			Task task = it.next();
//			// 判断在线程池里是否可以取消,可以取消的直接取消,转移到任务历史表,不可以取消的抛异常
//			if (executor.getQueue().remove(task)) {
//				TaskHistory t = new TaskHistory();
//				t.setAddDate(task.getAddDate());
//				t.setCause("");
//				t.setCompleteDate(new Date());
//				t.setExecutionDate(task.getExecutionDate());
//				t.setHashCode(task.getHashCode());
//				t.setNumberOfTimes(1);
//				t.setOwner(task.getOwner());
//				t.setStatus(TaskStatus.CANCEL);
//				t.setTaskObject(task.getTaskObject());
//				t.setType(task.getType());
//				taskQueue.service.deleteTask(task);
//				taskQueue.service.addTaskHistory(t);
//			} else {
//				throw new CessException(-0x1_0405, "任务不可以取消");
//			}
//		}
//	}
//
//	/***
//	 * 取消任务
//	 *
//	 * @param owner
//	 */
//	public static void cancelTasksByOwner(String owner) {
//		Collection<Task> tasks = taskQueue.service.getTaskByOwner(owner);
//		Iterator<Task> it = tasks.iterator();
//		while (it.hasNext()) {
//			Task task = it.next();
//			// 判断在线程池里是否可以取消,可以取消的直接取消,转移到任务历史表,不可以取消的抛异常
//			if (executor.getQueue().remove(task)) {
//				TaskHistory t = new TaskHistory();
//				t.setAddDate(task.getAddDate());
//				t.setCause("");
//				t.setCompleteDate(new Date());
//				t.setExecutionDate(task.getExecutionDate());
//				t.setHashCode(task.getHashCode());
//				t.setNumberOfTimes(1);
//				t.setOwner(task.getOwner());
//				t.setStatus(TaskStatus.CANCEL);
//				t.setTaskObject(task.getTaskObject());
//				t.setType(task.getType());
//				taskQueue.service.deleteTask(task);
//				taskQueue.service.addTaskHistory(t);
//			} else {
//				throw new CessException(-0x1_0405, "任务不可以取消");
//			}
//		}
//	}
//
//	/***
//	 *
//	 * @param taskId
//	 */
//	public static void reStartTask(String taskId) {
//
//	}
//
//	/***
//	 *
//	 * @param taskType
//	 */
//	public static void reStartTaskByType(String taskType) {
//
//	}
//
//	/***
//	 *
//	 * @param owner
//	 */
//	public static void reStartTaskByOwner(String owner) {
//
//	}
//
//	public static ThreadPoolExecutor getExecutor() {
//		return executor;
//	}
//
//	public static void setExecutor(ThreadPoolExecutor executor) {
//		TaskQueue.executor = executor;
//	}
//
//	public TaskQueueService getService() {
//		return service;
//	}
//
//	public void setService(TaskQueueService service) {
//		this.service = service;
//	}
//
//}
