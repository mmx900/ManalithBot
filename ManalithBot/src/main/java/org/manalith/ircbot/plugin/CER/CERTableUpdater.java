package org.manalith.ircbot.plugin.CER;

import org.manalith.ircbot.plugin.CER.Exceptions.EmptyTokenStreamException;
import org.manalith.ircbot.plugin.CER.Exceptions.FileNotSpecifiedException;
import org.manalith.ircbot.plugin.CER.Exceptions.URLNotSpecifiedException;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.sql.SQLException;

public class CERTableUpdater {
	
	private Downloader d;
	private String LocalPath;
	private String LocalFilename;
	private String RemoteURL;
	
	// private HTMLTokenArray tArray;
	private CERTableTokenAnalyzer tokenAnalyzer;
	
	private SQLiteTableManager sqlman;
	
	public CERTableUpdater ()
	{
		this.setLocalFilename ( "org/manalith/ircbot/plugin/CER/data/get.html" );
		this.setRemoteURL( "" );
		
		d = null;
		
		// tArray = null;
		tokenAnalyzer = null;
		
		sqlman = null;
	}
	public CERTableUpdater ( String newRemoteURL )
	{
		this.setLocalPath( "" );
		this.setLocalFilename ( "get.html" );
		this.setRemoteURL( newRemoteURL );
		
		d = null;
		
		// tArray = null;
		tokenAnalyzer = null;
		
		sqlman = null;
	}
	public CERTableUpdater ( String newLocalPath, String newRemoteURL )
	{
		this.setLocalPath ( newLocalPath );
		this.setLocalFilename ( "get.html" );
		this.setRemoteURL( newRemoteURL );
		
		d = null;
		
		// tArray = null;
		tokenAnalyzer = null;
		
		sqlman = null;
	}
	
	private void setLocalPath ( String newLocalPath )
	{
		this.LocalPath = newLocalPath;
	}
	private String getLocalPath ( )
	{
		return this.LocalPath;
	}
	private void setLocalFilename ( String newLocalFilename )
	{
		this.LocalFilename = newLocalFilename;
	}
	private String getLocalFilename ( ) throws FileNotSpecifiedException
	{
		if ( this.LocalFilename.length() == 0 )
			throw new FileNotSpecifiedException();
		return this.LocalFilename;
	}
	
	public void setRemoteURL ( String newRemoteURL )
	{
		this.RemoteURL = newRemoteURL;
	}
	public String getRemoteURL ( ) throws URLNotSpecifiedException
	{
		if ( this.RemoteURL.length() == 0 )
			throw new URLNotSpecifiedException();
		
		return this.RemoteURL;
	}
	
	private void initDownloader ( ) throws URLNotSpecifiedException,FileNotSpecifiedException
	{
		d = new Downloader ();
		d.setUrl ( this.getRemoteURL() );
		d.setLocalFilename( this.getLocalPath() + this.getLocalFilename() );
	}
	private void initTokenAnalyzer ( ) throws FileNotSpecifiedException, FileNotFoundException, IOException
	{
		StreamFileReader fr = new StreamFileReader ( this.getLocalPath() + this.getLocalFilename() );
		String data = fr.bringUpStreamFromFile ();
		
		/// separate tokenstream into each token unit.
		// tArray = new HTMLTokenArray();
		tokenAnalyzer = new CERTableTokenAnalyzer();
	
		tokenAnalyzer.setTokenStringData( data );
	}
	private void initSQLiteTable ( ) throws SQLException, ClassNotFoundException
	{
		sqlman = new SQLiteTableManager( this.getLocalPath(), "currency.db" );
	}
	
	public void update() throws URLNotSpecifiedException, FileNotSpecifiedException, FileNotFoundException, 
									IOException, EmptyTokenStreamException, SQLException, ClassNotFoundException
	{
		this.initDownloader();
		d.downloadDataFile ();
		
		this.initTokenAnalyzer();
		this.initSQLiteTable();		
		sqlman.insertDataToTable(tokenAnalyzer.analysisTokenStream());
		sqlman.close();
	}
}
