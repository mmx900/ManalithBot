/*
 * Created on 2005. 8. 8
 */
package org.manalith.ircbot.plugin.javaapi;

/**
 * JavaDoc 안의 Class, Interface, Annotation 등을 하나의 항목을 나타내는 클래스.
 * @author setzer
 */
public class JavaDocObject {
	protected JavaDocObject(){
		
	}
	
	public static final int CLASS = 1;
	public static final int INTERFACE = 2;
	
	private String name;
	private int type;
	private String URL;
	
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * @param type The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}

	
	/**
	 * @return Returns the uRL.
	 */
	public String getURL() {
		return URL;
	}

	
	/**
	 * @param url The uRL to set.
	 */
	public void setURL(String url) {
		URL = url;
	}
}
