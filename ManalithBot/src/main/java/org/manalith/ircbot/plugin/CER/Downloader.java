package org.manalith.ircbot.plugin.CER;

import java.net.URL;
import java.net.URLConnection;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

public class Downloader {	
	private String Url;
	private String LocalFilename;
	private URLConnection urlConn = null;
	
	public Downloader ()
	{
		Url = "";
		LocalFilename = "";
	}
	public Downloader (String newUrl, String newLocalFilename)
	{
		this.setUrl(newUrl);
		this.setLocalFilename(newLocalFilename);
	}
	
	public void setUrl (String newUrl)
	{
		this.Url = newUrl;
	}
	public String getUrl ()
	{
		return this.Url;
	}
	
	public void setLocalFilename (String newLocalFilename)
	{
		this.LocalFilename = newLocalFilename;
	}
	public String getLocalFilename ()
	{
		return this.LocalFilename;
	}
	
	private void setUrlConnection ()
	{
		String protocol = "";
		String host = "";
		String filename = "";
		
		int len = Url.length();
		int i = 0; 
		
		while ( Url.charAt(i) != ':' )
		{
			protocol = protocol + Character.toString(Url.charAt(i));
			i++;
		}
		
		i += 3; // ://
		
		while ( Url.charAt(i) != '/' )
		{
			host = host + Character.toString(Url.charAt(i));
			i++;
		}
		
		while ( i < len )
		{
			filename = filename + Character.toString(Url.charAt(i));
			i++;
		}
		
		try
		{
			URL url = new URL(protocol,host,filename);
			urlConn = url.openConnection();
		}
		catch ( Exception e )
		{
			System.out.println(e.getMessage());
		}
	}
	
	public void downloadDataFile ()
	{
		this.setUrlConnection();
		
		InputStreamReader isr = null;
		File f = null;
		OutputStreamWriter osw = null;
		
		try
		{
			isr = new InputStreamReader(urlConn.getInputStream(),"EUC-KR");
			f = new File(this.getLocalFilename());
			osw = new OutputStreamWriter(new FileOutputStream(f),"UTF8");
		}
		catch ( Exception e )
		{
			System.out.println(e.getMessage());
		}
		
		char[] buf = new char[1024];
		int len = 0;
		
		try
		{
			while( ( len = isr.read(buf, 0, 1024) ) != -1)
			{
				osw.write(buf, 0, len);
			}
			
			isr.close();
			osw.close();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}
