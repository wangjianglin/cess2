package io.cess.core.web;

import java.io.InputStream;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;

public class LoggingListener implements javax.servlet.ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// 读取配置文件
		// ClassLoader cl = LogManager.class.getClassLoader();
		try (InputStream inputStream = this.getClass().getResourceAsStream(
				"/META-INF/lin/logging.properties")) {
			// if (cl != null) {
			// inputStream =
			// this.getClass().getResourceAsStream("log.properties");
			// } else {
			// inputStream = ClassLoader
			// .getSystemResourceAsStream("log.properties");
			// }
			java.util.logging.LogManager logManager = java.util.logging.LogManager
					.getLogManager();
			logManager.readConfiguration(inputStream);
		} catch (Throwable e) {
			Logger logger = Logger.getLogger(this.getClass().getName());
			logger.warning("/META-INF/lin/logging.properties load fault.");
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
