package org.manalith.ircbot.plugin.javaapi;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class JavaDocSources {

	public static final Map<String, String> sources = new HashMap<>();

	// TODO : 시작시 properties 파일에서 소스 목록을 가져오게끔 변경할 것
	static {
		sources.put("hibernate", "http://www.hibernate.org/hib_docs/v3/api/");
		sources.put("tomcat",
				"http://tomcat.apache.org/tomcat-5.5-doc/servletapi/");
		sources.put("servlet",
				"http://tomcat.apache.org/tomcat-5.5-doc/servletapi/");
		sources.put("jsp", "http://tomcat.apache.org/tomcat-5.5-doc/jspapi/");
		sources.put("tidy", "http://jtidy.sourceforge.net/apidocs/");
		sources.put("j2se", "http://docs.oracle.com/javase/7/docs/api/");
		sources.put("7", "http://docs.oracle.com/javase/7/docs/api/");
		sources.put("6", "http://docs.oracle.com/javase/6/docs/api/");
		sources.put("1.5", "http://docs.oracle.com/javase/1.5.0/docs/api/");
		sources.put("1.5.0", "http://docs.oracle.com/javase/1.5.0/docs/api/");
		sources.put("5.0", "http://docs.oracle.com/javase/1.5.0/docs/api/");
		sources.put("1.4", "http://docs.oracle.com/javase/1.4.2/docs/api/");
		sources.put("1.4.2", "http://docs.oracle.com/javase/1.4.2/docs/api/");
		sources.put("1.3", "http://docs.oracle.com/javase/1.3.1/docs/api/");
		sources.put("1.3.1", "http://docs.oracle.com/javase/1.3.1/docs/api/");
	}

	private JavaDocSources() {
	}

	public static boolean hasSource(String sourceName) {
		return sources.containsKey(sourceName);
	}

	public static Map<String, String> getSources() {
		return sources;
	}

	public static String getSourceURL(String sourceName) {
		return sources.get(sourceName);
	}

	public static String getSourceNamesByString() {
		return StringUtils.join(sources.keySet(), ", ");
	}
}
