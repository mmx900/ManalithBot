/*
 * Created on 2005. 8. 8
 */
package org.manalith.ircbot.plugin.javaapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaDocSearch implements SearchInterface {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private final String baseUrl;
	private static final String JAVADOC_ALLCLASS_FRAME_URL = "allclasses-frame.html";
	private List<JavaDocObject> docObjects = null;

	/**
	 * 
	 * @param baseUrl
	 *            JavaDoc의 기본 URL (예 : http://java.sun.com/j2se/1.5.0/docs/api/)
	 */
	public JavaDocSearch(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	private Document getDocument() {
		try {
			return Jsoup.connect(baseUrl + JAVADOC_ALLCLASS_FRAME_URL).get();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	private List<JavaDocObject> parseDocObjects(Document doc) {
		Elements items = doc.select("a");

		List<JavaDocObject> objs = new ArrayList<>();

		for (int i = 0; i < items.size(); i++) {
			Element element = items.get(i);
			JavaDocObject o = new JavaDocObject();

			// A 태그 안에 클래스 이름이 있을 경우
			String nodeValue = element.text();
			if (StringUtils.isEmpty(nodeValue)) {
				// A 태그 안의 I 태그 안에 인터페이스 이름이 있을 경우
				o.setName(element.select("i").get(0).text());
				o.setType(JavaDocObject.INTERFACE);
			} else {
				o.setName(nodeValue);
				o.setType(JavaDocObject.CLASS);
			}

			o.setURL(element.attr("href"));

			objs.add(o);
		}

		return objs;
	}

	/**
	 * 검색하여 일치하는 1개의 클래스를 가져 온다.
	 * 
	 * @param className
	 *            검색할 클래스의 이름
	 * @return 클래스 이름 + 클래스 api 링크
	 */
	@Override
	public String search(String className) {
		if (docObjects == null)
			docObjects = parseDocObjects(getDocument());

		StringBuffer sb = new StringBuffer();

		for (JavaDocObject o : docObjects) {
			if (o.getName().equalsIgnoreCase(className)) {
				sb.append("[");
				sb.append(o.getName());
				sb.append("]");
				sb.append(" - ");
				sb.append(baseUrl);
				sb.append(o.getURL());

				break;
			}
		}

		return sb.toString();
	}

	/**
	 * 검색하여 해당 패턴에 해당하는 클래스명을 가진 모든 클래스를 가져온다.
	 * 
	 * @param regex
	 *            검색할 클래스의 이름(정규표현식)
	 * @param classes
	 *            검색 결과를 추가할 리스트, null일 경우 새로이 생성하여 리턴한다.
	 * @return 리스트(클래스 이름 + 클래스 api 링크)
	 */
	@Override
	public List<String> searchAll(String regex, List<String> classes) {
		if (classes == null)
			classes = new ArrayList<>();

		if (docObjects == null)
			docObjects = parseDocObjects(getDocument());

		for (JavaDocObject o : docObjects) {
			if (o.getName().matches(regex)) {
				StringBuffer sb = new StringBuffer();
				sb.append("[");
				sb.append(o.getName());
				sb.append("]");
				sb.append(" - ");
				sb.append(baseUrl);
				sb.append(o.getURL());
				classes.add(sb.toString());
			}
		}

		return classes;
	}
}