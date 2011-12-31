package org.manalith.ircbot.plugin.distropkgfinder;

import org.manalith.ircbot.plugin.distropkgfinder.exceptions.EmptyTokenStreamException;
public class DebianPkgInfoParser {
	private TokenArray array;
	
	public DebianPkgInfoParser ( )
	{
		this.array = null;
	}
	
	public DebianPkgInfoParser ( TokenArray newArray )
	{
		this.array = newArray;
	}
	
	public PkgTable generatePkgTable ( ) throws EmptyTokenStreamException
	{
		PkgTable result = new PkgTable();
		
		if ( array == null )
			throw new EmptyTokenStreamException();
		
		int cnt = array.getSize();
		if ( cnt == 0 )
			throw new EmptyTokenStreamException();
		
		int skipDivCount = 0;
		int i = 0;
		
		String [] tagnattr = null;
		String [] keyval = null;
		String value = "";
		
		String str = "";
		
		String GroupName = "";
		String PkgName = "";
		String Version = "";
		String Description = "";
		
		while ( i < cnt )
		{
			if ( array.getElement(i).getTokenSubtype() == TokenSubtype.DivOpen)
			{
				tagnattr = array.getElement(i++).getTokenString().split("\\s");
				keyval = tagnattr[1].split("\\=");
				value = keyval[1].substring(1,keyval[1].length() - 2);
				
				if ( value.equals("note") )
				{
					skipDivCount++;
				}
				while ( array.getElement(i).getTokenSubtype() != TokenSubtype.DivClose)
				{
					if ( array.getElement(i).getTokenSubtype() == TokenSubtype.POpen )
					{
						i++;
						while ( array.getElement(i).getTokenSubtype() != TokenSubtype.PClose )
						{
							if ( array.getElement(i).getTokenSubtype() == TokenSubtype.POpen )
							{
								TokenUnit unit = array.getElement(i);
								tagnattr = unit.getTokenString().split("\\s");
								keyval = tagnattr[1].split("\\=");
								value = keyval[1].substring(1,keyval[1].length() - 2);
								
								// just finish with no result
								if ( value.equals("psearchnoresult") ) return result; 
							}
							else
							{
								i++;
							}
						}
					}
					else if ( array.getElement(i).getTokenSubtype() == TokenSubtype.DivOpen)
					{
						while ( array.getElement(i).getTokenSubtype() != TokenSubtype.DivClose )
						{
							i++;
						}
					}
					else if ( array.getElement(i).getTokenSubtype() == TokenSubtype.H2Open )
					{
						i++;
						while ( array.getElement(i).getTokenSubtype() != TokenSubtype.H2Close )
						{
							if ( array.getElement(i).getTokenSubtype() == TokenSubtype.TextString )
							{
								GroupName = array.getElement(i).getTokenString();
							}
							i++;
						}
					}
					else if ( array.getElement(i).getTokenSubtype() == TokenSubtype.H3Open )
					{
						i++;
						str = "";
						while ( array.getElement(i).getTokenSubtype() != TokenSubtype.H3Close )
						{
							if ( array.getElement(i).getTokenSubtype() == TokenSubtype.TextString )
							{
								PkgName = (array.getElement(i++).getTokenString().split("\\s"))[1]; 
							}
						}
					}
					else if ( array.getElement(i).getTokenSubtype() == TokenSubtype.UlOpen )
					{
						i++;
						while ( array.getElement(i).getTokenSubtype() != TokenSubtype.UlClose )
						{
							if ( array.getElement(i++).getTokenSubtype() == TokenSubtype.LiOpen )
							{
								str = "";
								while ( array.getElement(i).getTokenSubtype() != TokenSubtype.Br )
								{
									if ( array.getElement(i).getTokenSubtype() == TokenSubtype.TextString )
									{
										str += array.getElement(i++).getTokenString();
									}
									else
									{
										i++;
									}
								}
								Description = (str.split("\\:"))[1].trim();

								str = "";
								while ( array.getElement(i).getTokenSubtype() != TokenSubtype.LiClose )
								{
									if ( array.getElement(i).getTokenSubtype() == TokenSubtype.TextString )
									{
										str += array.getElement(i++).getTokenString();
									}
									else
									{
										i++;
									}
								}
								Version = (str.split("\\:"))[0];
								i++;
							}
						}
						
						PkgUnit newPkgUnit = new PkgUnit (PkgName, Version, Description);
						result.addElement(GroupName, newPkgUnit);
						
					}
					i++;
				}
				if ( array.getElement(i).getTokenSubtype() == TokenSubtype.DivClose )
				{
					if (skipDivCount != 0)
					{
						skipDivCount--;
						i++;
						continue;
					}
					else break;
				}
		 	}
		}
		
		return result;
	}
}
