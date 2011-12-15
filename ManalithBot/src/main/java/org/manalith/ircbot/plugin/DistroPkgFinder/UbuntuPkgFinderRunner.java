package org.manalith.ircbot.plugin.DistroPkgFinder;

public class UbuntuPkgFinderRunner {
	
	private String keyword;
	public UbuntuPkgFinderRunner ( )
	{
		this.setKeyword("");
	}
	public UbuntuPkgFinderRunner ( String newKeyword )
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
		
		try
		{
			String latestPkgName = UbuntuPkgCurrentVer.currentUbuntuPkgVersion();
			String url = "http://packages.ubuntu.com/search?keywords=" + this.getKeyword() + "&searchon=names&suite=" + latestPkgName + "&section=all";
			StreamDownloader downloader = new StreamDownloader( url );
			String data = downloader.downloadDataStream();
			
			// DebianPkgTokenAnalyzer can be also used on the step of finding Ubuntu package.
			DebianPkgTokenAnalyzer analyzer = new DebianPkgTokenAnalyzer (data);
			
			TokenArray arr = analyzer.analysisTokenStream();
			
			DebianPkgInfoParser parser = new DebianPkgInfoParser(arr);
			result = parser.generatePkgTable().toString();
		}
		catch ( Exception e )
		{
			result = e.getMessage();
		}
		
		return result;
	}
}
