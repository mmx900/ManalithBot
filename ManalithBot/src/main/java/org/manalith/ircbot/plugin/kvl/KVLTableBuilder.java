//
// KVLTableBuilder.java
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.

package org.manalith.ircbot.plugin.kvl;

import java.io.IOException;
import java.util.Iterator;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import org.jsoup.select.Elements;

public class KVLTableBuilder {

	// private TokenArray array;
	private String url;

	public KVLTableBuilder() {
		// this.setArray(null);
		this.setURL("");
	}
/*
	public KVLTableBuilder(TokenArray newArray) {
		this.setArray(newArray);
	}
*/
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
/*
	public void setArray(TokenArray newArray) {
		this.array = newArray;
	}

	public TokenArray getArray() {
		return this.array;
	}
*/
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
		
		/*
		int i = 0;
		int columncount = 0;
		int arraysize = this.getArray().getSize();

		
		TokenUnit uTokenUnit = null;

		while (i < arraysize) {
			if (this.getArray().getElement(i++).getTokenSubtype() == TokenSubtype.TableOpen) {
				while (this.getArray().getElement(i).getTokenSubtype() != TokenSubtype.TableClose) {
					if (this.getArray().getElement(i++).getTokenSubtype() == TokenSubtype.TROpen) {
						while (this.getArray().getElement(i).getTokenSubtype() != TokenSubtype.TRClose) {
							uTokenUnit = this.getArray().getElement(i++);
							if (uTokenUnit.getTokenSubtype() == TokenSubtype.TDOpen) {

								columncount++; // columncount = 1;
								while (uTokenUnit.getTokenSubtype() != TokenSubtype.TDClose) {
									if (columncount == 1
											&& uTokenUnit.getTokenSubtype() == TokenSubtype.TextString) {
										newTag = uTokenUnit.getTokenString().substring(	0,
														uTokenUnit
																.getTokenString()
																.length() - 1);
										uTokenUnit = this.getArray()
												.getElement(i++);

									} else if (columncount == 2
											&& uTokenUnit.getTokenSubtype() == TokenSubtype.TextString) {
										newVersion = uTokenUnit
												.getTokenString();
										result.addVersionInfo(newTag,
												newVersion);
										uTokenUnit = this.getArray()
												.getElement(i++);

									} else {
										uTokenUnit = this.getArray()
												.getElement(i++);
									}

								}

							}
						}
						columncount = 0;
					}
				}
			}
		}
		*/
		
		return result;
	}

}
