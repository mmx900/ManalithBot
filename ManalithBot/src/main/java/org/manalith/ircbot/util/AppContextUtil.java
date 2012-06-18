package org.manalith.ircbot.util;

import org.springframework.context.ApplicationContext;

public class AppContextUtil {
	private static ApplicationContext applicationContext;

	public static void setApplicationContext(
			ApplicationContext applicationContext) {
		AppContextUtil.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
