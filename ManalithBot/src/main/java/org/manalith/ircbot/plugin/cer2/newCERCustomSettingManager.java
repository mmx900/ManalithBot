package org.manalith.ircbot.plugin.cer2;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.manalith.ircbot.plugin.cer2.exceptions.EmptyTokenStreamException;

public class newCERCustomSettingManager extends TokenAnalyzer {
	
	private String LocalPath;
	private String channel;
	private String userNick;
	private String CurrencyArgString;
	
	public newCERCustomSettingManager ( String newLocalPath )
	{
		this.setLocalPath ( newLocalPath ); 
		this.setChannel("");
		this.setUserNick("");
		this.setCurrencyArgString("");
	}
	
	public newCERCustomSettingManager ( String newLocalPath, String newChannel, String newUserNick )
	{
		this.setLocalPath ( newLocalPath );
		this.setChannel(newChannel);
		this.setUserNick(newUserNick);
		this.setCurrencyArgString("");
	}
	
	public newCERCustomSettingManager ( String newLocalPath, String newChannel, String newUserNick , String newArgs )
	{
		this.setLocalPath( newLocalPath );
		this.setChannel(newChannel);
		this.setUserNick(newUserNick);
		this.setCurrencyArgString(newArgs);
	}
	
	public void setChannel ( String newChannel )
	{
		this.channel = newChannel;
	}
	public String getChannel ( )
	{
		return this.channel;
	}
	
	public void setUserNick ( String newUserNick )
	{
		this.userNick = newUserNick;
	}
	public String getUserNick ()
	{
		return this.userNick;
	}
	
	public void setCurrencyArgString ( String newArgs )
	{
		this.CurrencyArgString = newArgs; 
	}
	public String getCurrencyArgString ( )
	{
		return this.CurrencyArgString;
	}
	
	public void setLocalPath ( String newLocalPath )
	{
		this.LocalPath = newLocalPath;
		
		File f = new File ( newLocalPath );
		if ( !f.exists() ) 	f.mkdirs();
	}
	public String getLocalPath ( )
	{
		return this.LocalPath;
	}
	
	public String addUserSetting ( )
	{
		String result = "";
		int chkCode;

		try
		{
			chkCode = this.validateToken(this.analysisTokenStream());
			if ( chkCode != -1 )
			{
				result = "알 수 없는 화폐 단위 : " + this.getCurrencyArgString().split("\\,")[chkCode];
				result += ", ex) !cer USD, JPY, HKD (각 화폐 단위는 콤마로 구분합니다)";
				return result;
			}
			
			PropertyManager customsetlist = new PropertyManager ( this.getLocalPath(), "customsetlist.prop");
			try
			{
				customsetlist.loadProperties();
			}
			catch ( IOException ii )
			{
				customsetlist.setProp(new Properties());
				customsetlist.storeProperties();
			}
			
			String [] userlist = customsetlist.getKeyList();
			if ( userlist == null )
			{
				;
			}
			else if ( this.indexOfContained( userlist, this.getUserNick() ) != -1 )
			{
				result += "이미 설정이 등록되어 새로운 설정으로 대체합니다. ";
			}
			
			customsetlist.setValue(this.getChannel().substring(1) + "." + this.getUserNick(), this.getCurrencyArgString());
			customsetlist.storeProperties();
			
			result += this.getChannel() + "의 " + this.getUserNick() + "님이 조회할 기본화폐 환율은 " + this.getCurrencyArgString() + "입니다.";
			
			
		}
		catch ( EmptyTokenStreamException e )
		{
			result = "지정한 화폐 단위가 없습니댜";
		}
		catch ( IOException ioe )
		{
			result = ioe.getMessage();
		}
		
		return result;
	}
	
	public String removeUserSetting ( )
	{
		String result = "";
		
		try
		{
			PropertyManager customsetlist = new PropertyManager ( this.getLocalPath(), "customsetlist.prop");
			try	
			{
				customsetlist.loadProperties();
			}
			catch ( IOException ii )
			{
				customsetlist.setProp(new Properties());
				customsetlist.storeProperties();
			}
		
			String [] userlist = customsetlist.getKeyList();
			if ( userlist == null )
			{
				result = "설정을 등록한 사용자가 없습니다";
				return result;
			}
			else if ( this.indexOfContained( userlist, this.getUserNick() ) != -1 )
			{ 
				customsetlist.removeKeyValue( this.getChannel().substring(1) + "." + this.getUserNick() );
				customsetlist.storeProperties();
				result = this.getChannel() + "의 " + this.getUserNick() + "님에 대한 설정을 지웠습니다.";
			}
			else
			{
				result = this.getChannel() + "의 " + this.getUserNick() + "님은 설정을 등록하지 않았습니다.";
			}
		}
		catch ( IOException e )
		{
			result = e.getMessage();
		}
		
		return result;
	}
		
	@Override
	public TokenType getTokenType(String TokenString) {
		TokenType result = TokenType.Unknown;
		
		Pattern cur_pattern = Pattern.compile("[A-Z]{3}");
		Matcher cur_matcher = cur_pattern.matcher(TokenString);
		
		if ( cur_matcher.matches() )
			result = TokenType.CurrencyUnit;
		
		return result;
	}

	@Override
	public TokenSubtype getTokenSubtype(String TokenString,
			TokenType CurrentType) {
		TokenSubtype result = TokenSubtype.Unknown;
		int hashCode = CurrentType.hashCode();
		
		if ( hashCode == TokenType.CurrencyUnit.hashCode() )
		{
			try
			{
				result = TokenSubtype.valueOf("Currency" + TokenString);
			}
			catch ( IllegalArgumentException e )
			{
				;
			}
		}
		
		return result;
	}

	@Override
	public TokenArray analysisTokenStream() throws EmptyTokenStreamException {
		TokenArray result = new TokenArray();
		TokenType oTokenType = null;
		TokenSubtype oTokenSubtype = null;
		
		if ( this.getCurrencyArgString().length() == 0 )
			throw new EmptyTokenStreamException();
		String [] options = this.getCurrencyArgString().split("\\,");
		
		for ( int i = 0 ; i < options.length ; i++ )
		{
			oTokenType = this.getTokenType(options[i]);
			oTokenSubtype = this.getTokenSubtype(options[i], oTokenType);
			
			TokenUnit u = new TokenUnit ( options[i] , oTokenType, oTokenSubtype );
			result.addElement(u);
		}
		
		return result;
	}
	
	private int validateToken ( TokenArray tArray )
	{
		int result = -1; // no problem as default;
		int size = tArray.getSize();
		
		for ( int i = 0 ; i < size ; i++ )
		{
			TokenUnit t = tArray.getElement(i);
			if ( t.getTokenType() == TokenType.Unknown || t.getTokenSubtype() == TokenSubtype.Unknown )
				return i; // i th element has problem
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
