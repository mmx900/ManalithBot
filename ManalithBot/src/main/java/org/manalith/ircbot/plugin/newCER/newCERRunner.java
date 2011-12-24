package org.manalith.ircbot.plugin.newCER;

import java.io.File;

public class newCERRunner {

	private String args;
	private String dataPath;
	
	public newCERRunner ( )
	{
		this.setArgs ( "" );
	}
	public newCERRunner ( String newArgs )
	{
		this.setArgs( newArgs );
	}
	public newCERRunner ( String newDataPath, String newArgs )
	{
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
	
	public String run() throws Exception
	{
		String result = "";
				

		CERTableUpdater updater = new CERTableUpdater(this.getDataPath());
		updater.update();
			
		String cmd = CERMessageTokenAnalyzer.convertToCLICommandString(this.getArgs());
		CERInfoProvider info = new CERInfoProvider(this.getDataPath(), cmd);
			
		result = info.commandInterpreter();		
		
		return result;
	}
}
