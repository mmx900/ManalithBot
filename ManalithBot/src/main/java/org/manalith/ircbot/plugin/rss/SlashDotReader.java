/*
 * Created on 2005. 8. 8
 */
package org.manalith.ircbot.plugin.rss;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class SlashDotReader implements RssReaderInterface{
	private Logger logger = Logger.getLogger(getClass());
	private static final String SLASHDOT_RSS_URL="http://slashdot.org/rss/index.rss";
	
	public String read(){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		StringBuffer sb = new StringBuffer();
		
		try {
			DocumentBuilder domParser = factory.newDocumentBuilder();
			Document document = domParser.parse(SLASHDOT_RSS_URL);
			
			NodeList items = document.getElementsByTagName("item");
			sb.append("[");
			sb.append(items.item(0).getChildNodes().item(1).getChildNodes().item(0).getNodeValue());
			sb.append("]");
			sb.append(" - ");
			
			String dcDate = items.item(0).getChildNodes().item(9).getChildNodes().item(0).getNodeValue();

			SimpleDateFormat dcDateParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.US);
			SimpleDateFormat dcDateFormatter = new SimpleDateFormat("yyyy'년' MM'월' dd'일' a HH:mm:ss",Locale.KOREA);

			sb.append(dcDateFormatter.format(dcDateParser.parse(dcDate)));

			sb.append(" - ");
			sb.append(items.item(0).getChildNodes().item(3).getChildNodes().item(0).getNodeValue());
			
		} catch (ParserConfigurationException e) {
			logger.error(e);
		} catch (SAXException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} catch (ParseException e) {
			logger.error(e);
		}
		
		return sb.toString();
	}
}
