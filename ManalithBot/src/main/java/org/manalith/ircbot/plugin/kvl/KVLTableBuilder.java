//
// KVLTableBuilder.java
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.

package org.manalith.ircbot.plugin.kvl;

import java.io.IOException;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class KVLTableBuilder {

	private String url;

	public KVLTableBuilder() {
		this.setURL("");
	}

	public KVLTableBuilder ( String newURL )
	{
		this.setURL ( newURL );
	}
	private void setURL ( String newURL )
	{
		this.url = newURL;
	}
	private String getURL ( )
	{
		return this.url;
	}

	public KVLTable generateKernelVersionTable() {
		KVLTable result = new KVLTable();

		String newTag = "";
		String newVerElement = "";
		
		try
		{
			Iterator<Element> e = Jsoup.connect(this.getURL()).get().select("table.kver>tbody>tr").iterator();
			 
			//*
			while ( e.hasNext() )
			{
				Elements tds = e.next().select("td");
				newTag = tds.get(0).text().replaceAll("\\:", "");
				newVerElement = tds.get(1).text();
				
				result.addVersionInfo(newTag, newVerElement);
			}
			//*/
		}
		catch ( IOException e )
		{
			System.out.println(e.getMessage());
		}
		
		return result;
	}

}
