package org.manalith.ircbot.plugin.newCER;

import java.util.Properties;
import java.io.IOException;
import java.io.FileNotFoundException;

public class PropFileReadWriter extends FileReadWriter {
	public PropFileReadWriter () throws FileNotFoundException
	{
		super ();
	}
	
	public PropFileReadWriter ( String newFilename ) throws FileNotFoundException
	{
		super ( newFilename );
	}
	
	public Properties bringUpPropertyFromFile () throws IOException
	{
		this.allocateStreamReader();
		Properties result = new Properties();	
		result.load(this.getStreamReaderResource());
		
		return result;
	}
	
	public void pushUpPropertyToFile (Properties newProperties) throws IOException 
	{
		if ( !this.exists() )
			this.createFile();

		this.allocateStreamWriter();
		newProperties.store(this.getStreamWriterResource(), "");
	}
}
