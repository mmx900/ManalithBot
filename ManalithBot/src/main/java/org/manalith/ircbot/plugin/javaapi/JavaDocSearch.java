/*
 * Created on 2005. 8. 8
 */
package org.manalith.ircbot.plugin.javaapi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;


public class JavaDocSearch implements SearchInterface{
	private Logger logger = Logger.getLogger(getClass());
	private final String JAVADOC_BASE_URL;
	private static final String  JAVADOC_ALLCLASS_FRAME_URL = "allclasses-frame.html";
	private List<JavaDocObject> docObjects = null;
	
	/**
	 * 
	 * @param baseUrl JavaDoc의 기본 URL (예 : http://java.sun.com/j2se/1.5.0/docs/api/)
	 */
	public JavaDocSearch(String baseUrl){
		JAVADOC_BASE_URL = baseUrl;
	}
	
	private Document getDocument(){
		HttpClient client = new DefaultHttpClient();
		HttpGet method = null;
		InputStream inputStream = null;
		Document doc = null;
		
		try{
			method = new HttpGet(JAVADOC_BASE_URL + JAVADOC_ALLCLASS_FRAME_URL);
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 8000);

			HttpResponse response= client.execute(method);
			//client.executeMethod(method);
			//inputStream = method.getResponseBodyAsStream();
			
			Tidy tidy = new Tidy();
			tidy.setXHTML(true);
			doc = tidy.parseDOM(inputStream, null);
//		}catch (HttpException e) {
//			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} finally {
			client.getConnectionManager().shutdown();
		}
		
		
		return doc;
	}
	
	private List<JavaDocObject> parseDocObjects(Document doc){
		NodeList items = doc.getElementsByTagName("a");
		List<JavaDocObject> objs = new ArrayList<JavaDocObject>();
		
		for(int i=0; i < items.getLength(); i++){
			JavaDocObject o = new JavaDocObject();
			
			//A 태그 안에 클래스 이름이 있을 경우
			String nodeValue = items.item(i).getChildNodes().item(0).getNodeValue();
			if(StringUtils.isEmpty(nodeValue)){
				//A 태그 안의 I 태그 안에 인터페이스 이름이 있을 경우
				o.setName(items.item(i).getChildNodes().item(0).getChildNodes().item(0).getNodeValue());
				o.setType(JavaDocObject.INTERFACE);
			}else{
				o.setName(nodeValue);
				o.setType(JavaDocObject.CLASS);
			}
			
			NamedNodeMap nnm = items.item(i).getAttributes();
			for(int y=0; y < nnm.getLength(); y++){
				if(nnm.item(y).getNodeName().equals("href")){
					o.setURL(nnm.item(y).getNodeValue());
					break;
				}
			}
			objs.add(o);
		}
		
		return objs;
	}
	
	/**
	 * 검색하여 일치하는 1개의 클래스를 가져 온다.
	 * @param className 검색할 클래스의 이름
	 * @return 클래스 이름 + 클래스 api 링크
	 */
	public String search(String className){
		if(docObjects == null)
			docObjects = parseDocObjects(getDocument());
		
		StringBuffer sb = new StringBuffer();
		
		for(JavaDocObject o : docObjects){
			if(o.getName().equalsIgnoreCase(className)){
				sb.append("[");
				sb.append(o.getName());
				sb.append("]");
				sb.append(" - ");
				sb.append(JAVADOC_BASE_URL);
				sb.append(o.getURL());
				
				break;
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * 검색하여 해당 패턴에 해당하는 클래스명을 가진 모든 클래스를 가져온다.
	 * @param regex 검색할 클래스의 이름(정규표현식)
	 * @param classes 검색 결과를 추가할 리스트, null일 경우 새로이 생성하여 리턴한다.
	 * @return 리스트(클래스 이름 + 클래스 api 링크)
	 */
	public List<String> searchAll(String regex, List<String> classes){
		if(classes == null) classes = new ArrayList<String>();
		
		if(docObjects == null)
			docObjects = parseDocObjects(getDocument());
		
		for(JavaDocObject o : docObjects){
			if(o.getName().matches(regex)){
				StringBuffer sb = new StringBuffer();
				sb.append("[");
				sb.append(o.getName());
				sb.append("]");
				sb.append(" - ");
				sb.append(JAVADOC_BASE_URL);
				sb.append(o.getURL());
				classes.add(sb.toString());
			}
		}
		
		return classes;
	}
}