package org.manalith.ircbot.plugin.CER;

import java.util.Properties;
import java.util.Set;
import java.io.IOException;
import java.io.FileNotFoundException;

public class PropertyManager {
	
	private String filename;
	private Properties prop;
	
	public PropertyManager ( )
	{
		this.setFilename ( "" );
		this.setProp ( null );
	}
	public PropertyManager ( String newFilename )
	{
		this.setFilename ( newFilename );
		this.setProp ( null );
	}
	
	public void setFilename ( String newFilename )
	{
		this.filename = newFilename;
	}
	public String getFilename ( )
	{
		return this.filename;
	}
	
	public void initProp ( )
	{
		this.setProp ( new Properties() );
	}
	public void setProp ( Properties newProperty )
	{
		this.prop = newProperty;
	}
	public Properties getProp ( )
	{
		return this.prop;
	}
	
	public void loadProperties ( ) throws IOException
	{
		PropFileReadWriter fr = new PropFileReadWriter ( this.getFilename() );
		this.setProp(fr.bringUpPropertyFromFile());
		
		if ( this.getProp() == null )
			this.initProp();
	}
	public void storeProperties ( ) throws FileNotFoundException, IOException
	{
		PropFileReadWriter fw = new PropFileReadWriter ( this.getFilename() );
		fw.pushUpPropertyToFile(this.getProp());
	}
	
	public String getValue ( String key )
	{
		return (String)this.getProp().get(key);
	}
	public void setValue ( String key, String value )
	{
		this.getProp().setProperty(key, value);
	}
	
	public String[] getKeyList ()
	{
		Set<String> ss = this.getProp().stringPropertyNames();
		String [] result = new String[ss.size()];
		result = (String [])ss.toArray();
		
		return result;
	}
}
