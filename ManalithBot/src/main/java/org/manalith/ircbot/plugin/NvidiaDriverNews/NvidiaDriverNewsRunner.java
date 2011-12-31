package org.manalith.ircbot.plugin.NvidiaDriverNews;

// import java.net.URL; // this is for the first way
import java.io.IOException;

import org.jsoup.Jsoup;

// need to get org.jdom:jdom:1.1 from maven-repo
/*
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.JDOMException;
*/ // is for the first way.

import org.jsoup.select.Elements;

public class NvidiaDriverNewsRunner {
	public NvidiaDriverNewsRunner ( )
	{
		;
	}
	
	public String run()
	{
		String result = "";
		
		/*
		String ProductSeriesType = "3"; // Geforce
		String ProductSeries = ""; // reserved to get from xml
		// String ddlDownloadType = "3"; // Driver : useless
		String ddlOperatingSystem = "11"; // Linux:ia32 , 12 => amd64
		String hidPageLanguage = "kr"; // hidden page language value = "kr"
		String ddlLanguage = "8"; // Korean
		
		
		try
		{
			URL url = new URL("http", "www.nvidia.com", "/Download/API/lookupValueSearch.aspx?TypeID=2&ParentID=1");
			SAXBuilder simpleAPIforXMLBuilder = new SAXBuilder(); 
			ProductSeries = ((Element)(simpleAPIforXMLBuilder.build(url)).getRootElement().getChild("LookupValues").getChildren("LookupValue").get(0)).getChild("Value").getValue(); 
		}
		catch ( IOException ioe )
		{
			result = ioe.getMessage();
		}
		catch ( JDOMException jde )
		{
			result = jde.getMessage();
		}

		String baseurl = "http://www.nvidia.com/Download/processDriver.aspx";
		baseurl += "?psid=" + ProductSeries;
		baseurl += "&pfid=" + ProductSeriesType;
		baseurl += "&rpf=1";
		//baseurl += "&dtid=" + ddlDownloadType; 
		baseurl += "&osid=" + ddlOperatingSystem;
		baseurl += "&lid=" + ddlLanguage;
		baseurl += "&lang=" + hidPageLanguage;
		
		// http://www.nvidia.com/Download/processDriver.aspx?psid=76&pfid=3&rpf=1&osid=11&lid=8&lang=kr
			
		try
		{
			String [] urlsplit = (new StreamDownloader ( baseurl )).downloadDataStream().split("\\/");
			String [] versplit = urlsplit[urlsplit.length-1].split("\\-");
			result = versplit[0] + "-" + versplit[2] + "-" + versplit[3];
			
			// amd64
			ddlOperatingSystem = "12";
			
			baseurl = "http://www.nvidia.co.kr/Download/processDriver.aspx";
			baseurl += "?psid=" + ProductSeries;
			baseurl += "&pfid=" + ProductSeriesType;
			baseurl += "&rpf=1";
			// baseurl += "&dtid=" + ddlDownloadType; 
			baseurl += "&osid=" + ddlOperatingSystem;
			baseurl += "&lid=" + ddlLanguage;
			baseurl += "&lang=kr" + hidPageLanguage;
			
			urlsplit = (new StreamDownloader ( baseurl )).downloadDataStream().split("\\/");
			versplit = urlsplit[urlsplit.length-1].split("\\-");
			
			result += ", " + versplit[0] + "-" + versplit[2] + "-" + versplit[3];			
		}
		catch ( IOException ioe )
		{
			result = ioe.getMessage();
		}
		*/ // The First method is the way to get from the official nvidia download site.
		
		try
		{
			Elements e = Jsoup.connect("http://www.nvnews.net/vbulletin/showthread.php?t=122606").get().select("div#post_message_1836667").get(0).select("a");
			result = "Current long-lived branch release: " + e.get(0).text();
			result += ", Current official release: " + e.get(1).text();
			result += ", Current beta release: " + e.get(2).text();
			
		}
		catch ( IOException ioe )
		{
			result = ioe.getMessage();
		}
		
		return result;
	}
}
