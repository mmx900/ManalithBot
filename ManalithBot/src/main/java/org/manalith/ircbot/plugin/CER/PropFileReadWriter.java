//
// PropFileReadWriter.java
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.

package org.manalith.ircbot.plugin.CER;

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
