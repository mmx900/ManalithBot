package org.manalith.ircbot.plugin.CER;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileReadWriter {
	private File f;
	private String filename;
	private String filestream;
	
	private FileInputStream fis;
	private InputStreamReader isr;
	private FileOutputStream fos;
	private OutputStreamWriter osw;
	
	public FileReadWriter () throws FileNotFoundException
	{
		f = null;
		filename = "";
		filestream = "";
		
		fis = null;
		isr = null;
		fos = null;
		osw = null;
		
		this.openFile();
	}
	public FileReadWriter (String newFilename)
	{
		this.setFilename(newFilename);
		f = null;
		filestream = "";
		
		fis = null;
		isr = null;
		fos = null;
		osw = null;
		
		this.openFile();
	}
	
	public void setFilename (String newFilename)
	{
		this.filename = newFilename;
	}
	public String getFilename ()
	{
		return this.filename;
	}
	
	public void setFileStream (String newFileStream)
	{
		this.filestream = newFileStream;
	}
	public String getFileStream ()
	{
		return this.filestream;
	}
	
	protected File allocateFileObject ()
	{
		return new File(this.filename);
	}
	protected boolean exists ()
	{
		return f.exists();
	}
	protected void openFile ()
	{
		f = this.allocateFileObject();
	}
	protected void createFile () throws FileNotFoundException, IOException
	{
		f.createNewFile();
		this.allocateStreamWriter();
	}
	protected void allocateStreamReader() throws FileNotFoundException
	{
		fis = new FileInputStream(f);
		isr = new InputStreamReader(fis);
	}
	protected void allocateStreamWriter() throws FileNotFoundException
	{
		fos = new FileOutputStream(f);
		osw = new OutputStreamWriter(fos);
	}
	
	public InputStreamReader getStreamReaderResource ()
	{
		return this.isr;
	}
	public OutputStreamWriter getStreamWriterResource ()
	{
		return this.osw;
	}
}
