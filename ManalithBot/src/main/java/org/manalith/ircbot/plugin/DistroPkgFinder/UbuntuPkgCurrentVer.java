package org.manalith.ircbot.plugin.DistroPkgFinder;

import java.io.IOException;

import org.manalith.ircbot.plugin.DistroPkgFinder.Exceptions.EmptyTokenStreamException;

public class UbuntuPkgCurrentVer {
	public static String currentUbuntuPkgVersion ( ) throws IOException, EmptyTokenStreamException
	{

		String result = "";
		String url = "http://packages.ubuntu.com/";
		
		StreamDownloader downloader = new StreamDownloader( url );
		String data = downloader.downloadDataStream();
		UbuntuPkgCurrentVerTokenAnalyzer analyzer = new UbuntuPkgCurrentVerTokenAnalyzer (data);
		TokenArray arr = analyzer.analysisTokenStream();
		UbuntuPkgCurrentVerParser parser = new UbuntuPkgCurrentVerParser ( arr );

		result = parser.extractCurrentVersion();
		
		return result;
	}
}
