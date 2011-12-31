package org.manalith.ircbot.plugin.distropkgfinder;

public class PkgUnit {
	
	private String pkgname;
	private String version;
	private String desc;
	
	public PkgUnit () 
	{
		this.setPkgName("");
		this.setVersion("");
		this.setDescription("");
	}
	
	public PkgUnit ( String newPkgName, String newVersion, String newDesc )
	{
		this.setPkgName(newPkgName);
		this.setVersion(newVersion);
		this.setDescription(newDesc);
	}
	
	public void setPkgName ( String newPkgName )
	{
		this.pkgname = newPkgName;
	}
	public String getPkgName ( )
	{
		return this.pkgname;
	}
	public void setVersion ( String newVersion )
	{
		this.version = newVersion;
	}
	public String getVersion ( )
	{
		return this.version;
	}
	
	public void setDescription ( String newDesc )
	{
		this.desc = newDesc;
	}
	public String getDescription ( )
	{
		return this.desc;
	}
	
	public String toString ()
	{
		return this.getPkgName() + "-" + this.getVersion() + " : " + this.getDescription();
	}
}
