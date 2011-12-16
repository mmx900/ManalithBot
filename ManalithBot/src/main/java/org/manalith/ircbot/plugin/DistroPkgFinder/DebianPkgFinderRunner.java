package org.manalith.ircbot.plugin.DistroPkgFinder;

public class DebianPkgFinderRunner {
	private String keyword;
	
	public DebianPkgFinderRunner ( )
	{
		this.setKeyword("");
	}
	public DebianPkgFinderRunner ( String newKeyword )
	{
		this.setKeyword(newKeyword);
	}
	
	public void setKeyword ( String newKeyword )
	{
		this.keyword = newKeyword;
	}
	public String getKeyword ( )
	{
		return this.keyword;
	}
	
	public String run ( ) 
	{
		String result = "";
		if ( this.getKeyword().equals("") )
		{
			result = "Keyword is not specified";
			return result;
		}
		
		String url = "http://packages.debian.org/search?keywords=" + this.getKeyword() + "&searchon=names&suite=stable&section=all";
		
		try
		{
			StreamDownloader downloader = new StreamDownloader( url );
			String data = downloader.downloadDataStream();
			DebianPkgTokenAnalyzer analyzer = new DebianPkgTokenAnalyzer (data);
			TokenArray arr = analyzer.analysisTokenStream();
			DebianPkgInfoParser parser = new DebianPkgInfoParser ( arr );
			result = parser.generatePkgTable().toString();
		}
		catch (Exception e)
		{
			result = e.getMessage();
		}
		
		return result;
	}
}
