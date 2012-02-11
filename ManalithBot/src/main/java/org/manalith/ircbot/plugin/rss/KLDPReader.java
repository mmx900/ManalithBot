/*
 * Created on 2005. 8. 8
 */
package org.manalith.ircbot.plugin.rss;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class KLDPReader implements RssReaderInterface{
	private Logger logger = Logger.getLogger(getClass());
	private static final String KLDP_BBS_RSS_URL = "http://bbs.kldp.org/rss.php";
	
	public String read(){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		StringBuffer sb = new StringBuffer();
		
		try {
			DocumentBuilder domParser = factory.newDocumentBuilder();
			Document document = domParser.parse(KLDP_BBS_RSS_URL);
			
			NodeList items = document.getElementsByTagName("item");
			sb.append("[");
			sb.append(items.item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeValue());
			sb.append("]");
			sb.append(" - ");
			sb.append(items.item(0).getChildNodes().item(3).getChildNodes().item(0).getNodeValue());
			sb.append(" - ");
			String description = items.item(0).getChildNodes().item(5).getChildNodes().item(0).getNodeValue();
			if(description.length() > 100) description = description.substring(0, 100);
			sb.append(description);
			
		} catch (ParserConfigurationException e) {
			logger.error(e);
		} catch (SAXException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		
		return sb.toString();
	}

}
