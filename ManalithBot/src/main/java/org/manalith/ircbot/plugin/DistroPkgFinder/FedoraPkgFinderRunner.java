package org.manalith.ircbot.plugin.DistroPkgFinder;

public class FedoraPkgFinderRunner {
	
	private String keyword;
	
	public FedoraPkgFinderRunner ( )
	{
		this.setKeyword("");
	}
	public FedoraPkgFinderRunner ( String newKeyword )
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
	
	public String run ()
	{
		String result = "";
		
		if ( this.getKeyword().equals("") )
		{
			result = "Keyword is not specified";
			return result;
		}
		
		try
		{
			String url = "http://rpmfind.net/linux/rpm2html/search.php?query=" + this.getKeyword() + "&submit=Search";
			StreamDownloader downloader = new StreamDownloader ( url );
			String data = downloader.downloadDataStream();
			
			FedoraRpmFindTokenAnalyzer analyzer = new FedoraRpmFindTokenAnalyzer ( data );
			TokenArray arr = analyzer.analysisTokenStream();
			
			FedoraRpmFindInfoParser parser = new FedoraRpmFindInfoParser ( arr );
			PkgTable tbl = parser.generatePkgTable();
			
			/* 
			 * this is deprecated because this logic is tooooo complicated. -_-
			 * and it causes too many useless load. It's just overkill :P 
			 
			String url = "https://admin.fedoraproject.org/pkgdb/acls/list/?searchwords=" + this.getKeyword();
			StreamDownloader downloader = new StreamDownloader( url );
			String data = downloader.downloadDataStream();
			
			FedoraPkgTokenAnalyzer analyzer = new FedoraPkgTokenAnalyzer (data);
			TokenArray arr = analyzer.analysisTokenStream();
			
			FedoraPkgInfoParser parser = new FedoraPkgInfoParser ( arr );
			PkgTable tbl = parser.generatePkgTable( null );
			
			url += "*";
			downloader = new StreamDownloader( url );
			data = downloader.downloadDataStream();
			
			analyzer = new FedoraPkgTokenAnalyzer ( data );
			arr = analyzer.analysisTokenStream();
			
			parser = new FedoraPkgInfoParser ( arr );
			tbl = parser.generatePkgTable( tbl );
			
			//*/
			result = tbl.toString();
		}
		catch ( Exception e )
		{
			result = e.getMessage();
		}
	
		return result;
	}
}
