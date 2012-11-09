package org.manalith.ircbot.util;

import org.springframework.context.ApplicationContext;

public class AppContextUtils {
	private static ApplicationContext applicationContext;

	public static void setApplicationContext(
			ApplicationContext applicationContext) {
		AppContextUtils.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
