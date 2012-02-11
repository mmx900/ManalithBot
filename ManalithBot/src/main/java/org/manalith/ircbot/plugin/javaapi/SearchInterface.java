/*
 * Created on 2005. 8. 8
 */
package org.manalith.ircbot.plugin.javaapi;

import java.util.List;


public interface SearchInterface {
	//TODO search와 searchAll은 혼동의 여지가 있으니 클래스명을 바꾸도록 하자.
	public String search(String className);
	public List<String> searchAll(String className, List<String> classes);
}
