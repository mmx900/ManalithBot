//
// StreamFileReader.java
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.
//

package org.manalith.ircbot.plugin.CER;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.CharBuffer;



public class StreamFileReader extends FileReadWriter {
	
	public StreamFileReader () throws FileNotFoundException 
	{
		super ();
	}
	
	public StreamFileReader (String newFilename) throws FileNotFoundException
	{
		super (newFilename);
	}
	
	public String bringUpStreamFromFile () throws IOException
	{
		char [] buf = new char[4096];
		int len = 0;
		
		this.allocateStreamReader();
		
		while ( true )
		{
			len = this.getStreamReaderResource().read(buf, 0, 4096);
			this.setFileStream((this.getFileStream() + CharBuffer.wrap(buf,0,len).toString()));
			if ( len != 4096 ) break;
				
			buf = new char[4096];		
		}
		
		return this.getFileStream();
	}
}
