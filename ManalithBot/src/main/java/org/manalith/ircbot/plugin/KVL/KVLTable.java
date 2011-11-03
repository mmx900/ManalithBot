//
// KVLTable.java
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.

package org.manalith.ircbot.plugin.KVL;
import java.util.ArrayList;

public class KVLTable {
	private class KVLRecord
	{
		private String tag;
		private ArrayList<String> versionlist;
		/*
		public KVLRecord ()
		{
			this.setTag( "" );
			this.versionlist = new ArrayList<String>();
		}
		*/
		public KVLRecord ( String newTag )
		{
			this.setTag( newTag );
			this.versionlist = new ArrayList<String>();
		}
		
		public void setTag ( String newTag )
		{
			this.tag = newTag;
		}
		public String getTag ( )
		{
			return this.tag;
		}
		
		public void addVersionElement ( String newVerElement )
		{
			this.versionlist.add( newVerElement );
		}
		
		public String getLatestVersion ( )
		{
			return this.versionlist.get(0);
		}
		
		public String toString ()
		{
			String result = "";
			result += this.getTag() + " : ";
			int arraysize = this.versionlist.size();
			for ( int i = 0 ; i < arraysize ; i++ )
			{
				result += this.versionlist.get(i);
				if ( i != arraysize - 1 )
					result += ", ";
			}
			return result;
		}
	}
	
	ArrayList<KVLRecord> array;
	
	public KVLTable ( )
	{
		array = new ArrayList<KVLRecord>();
	}
	
	public void addVersionInfo ( String newTag, String newVerElement )
	{
		int arraysize = this.array.size();
		boolean isSaved = false;
		for ( int i = 0; i < arraysize; i++ )
		{
			if ( array.get(i).getTag().equals(newTag) )
			{
				isSaved = true;
				array.get(i).addVersionElement(newVerElement);
			}
		}
		
		if ( !isSaved )
		{
			KVLRecord newRecord = new KVLRecord(newTag);
			newRecord.addVersionElement(newVerElement);
			
			array.add(newRecord);
		}		
	}
	
	public String getLatestVersions ( )
	{
		String result = "";
		
		int arraysize = this.array.size();
		
		for ( int i = 0 ; i < arraysize ; i++ )
		{
			result += this.array.get(i).getTag() + " : " + this.array.get(i).getLatestVersion();
			if ( i != arraysize - 1 )
				result += ", ";
		}
		
		return result;
	}
	
	public String getAllVersionInfo ( )
	{
		String result = "";
		int arraysize = this.array.size();
		
		for ( int i = 0 ; i < arraysize ; i++ )
		{
			result += this.array.get(i).toString();
			if ( i != arraysize - 1 )
			{
				result += ", ";
			}
		}
		
		return result;
	}
	
	public String toString()
	{
		return this.getLatestVersions();
	}
}
