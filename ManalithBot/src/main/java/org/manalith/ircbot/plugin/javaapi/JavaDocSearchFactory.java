/*
 * Created on 2005. 8. 13
 */
package org.manalith.ircbot.plugin.javaapi;

import java.util.HashMap;
import java.util.Map;

public class JavaDocSearchFactory {
	private static Map<String, JavaDocSearch> searchers = new HashMap<>();

	private JavaDocSearchFactory() {
	}

	public static JavaDocSearch getSearch(String source) {
		if (!searchers.containsKey(source)) {
			searchers.put(source, new JavaDocSearch(source));
		}
		return searchers.get(source);
	}
}
