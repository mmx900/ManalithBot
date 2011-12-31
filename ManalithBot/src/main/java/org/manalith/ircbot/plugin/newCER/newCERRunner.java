package org.manalith.ircbot.plugin.newCER;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class newCERRunner {

	private String args;
	private String dataPath;
	private String usernick;
	
	public newCERRunner ( )
	{
		this.setArgs ( "" );
		this.setDataPath( "" );
	}
	public newCERRunner ( String newUserNick, String newArgs )
	{
		this.setUserNick(newUserNick);
		this.setArgs( newArgs );
		this.setDataPath( "" );
	}
	public newCERRunner ( String newUserNick, String newDataPath, String newArgs )
	{
		this.setUserNick(newUserNick);
		this.setDataPath(newDataPath);
		File path = new File(this.getDataPath());
		
		if ( !path.exists() )
			path.mkdirs();
		
		this.setArgs(newArgs);
	}
	
	public void setArgs ( String newArgs )
	{
		this.args = newArgs;
	}
	private String getArgs ( )
	{
		return this.args;
	}
	
	public void setDataPath ( String newDataPath )
	{
		this.dataPath = newDataPath;
	}
	private String getDataPath ( )
	{
		return this.dataPath;
	}
	
	private void setUserNick ( String newUserNick )
	{
		this.usernick = newUserNick;
	}
	private String getUserNick ( )
	{
		return this.usernick;
	}
	
	public String run() throws Exception
	{
		String result = "";

		CERTableUpdater updater = new CERTableUpdater(this.getDataPath());
		updater.update();
			
		String cmd = "";
		CERInfoProvider info = null;
		
		if ( this.getArgs().equals("") )
		{
			String [] default_currency = null;
			
			PropertyManager prop = new PropertyManager ( this.getDataPath() , "customsetlist.prop" );
			try
			{
				prop.loadProperties();
			}
			catch ( IOException e )
			{
				prop.setProp(new Properties());
				prop.storeProperties();
			}
			
			String [] userlist = prop.getKeyList();
			if ( userlist == null )
			{
				default_currency = new String[4];
				default_currency[0] = "USD";
				default_currency[1] = "EUR";
				default_currency[2] = "JPY";
				default_currency[3] = "CNY";
			}
			else
			{
				int existidx = this.indexOfContained(userlist, this.getUserNick());
				if ( existidx != -1 )
				{
					default_currency = prop.getValue(userlist[existidx]).split("\\,");
				}
				else
				{
					default_currency = new String[4];
					default_currency[0] = "USD";
					default_currency[1] = "EUR";
					default_currency[3] = "JPY";
					default_currency[4] = "CNY";
				}
			}
			
			
			for ( int i = 0 ; i < default_currency.length ; i++ )
			{
				cmd = CERMessageTokenAnalyzer.convertToCLICommandString(default_currency[i]);
				info = new CERInfoProvider(this.getDataPath(), cmd);
				
				if ( i != 0 )
					result += ", " + info.commandInterpreter();
				else
					result += info.commandInterpreter();
			}
		}
		else
		{
			cmd = CERMessageTokenAnalyzer.convertToCLICommandString(this.getArgs());
			info = new CERInfoProvider(this.getDataPath(), cmd);
			
			result = info.commandInterpreter();
		}
		
		return result;
	}
	
	private int indexOfContained ( String [] strarray , String value )  
	{
		int result = -1;
		int length = strarray.length;
		
		for ( int i = 0 ; i < length ; i++ )
		{
			if ( strarray[i].contains(value) )
			{
				result = i;
				break;
			}
		}
		
		return result;
	}
}
