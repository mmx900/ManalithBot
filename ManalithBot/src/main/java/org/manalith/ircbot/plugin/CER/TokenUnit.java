package org.manalith.ircbot.plugin.CER;


public class TokenUnit {
	
	private TokenType tokentype;
	private TokenSubtype tokensubtype;
	private String tokenstring; 
	
	public TokenUnit ()
	{
		this.setTokenString("");
		this.setTokenType(TokenType.Unknown);
		this.setTokenSubtype(TokenSubtype.Unknown);
	}
	public TokenUnit (String newTokenString)
	{
		this.setTokenString(newTokenString);
		this.setTokenType(TokenType.Unknown);
		this.setTokenSubtype(TokenSubtype.Unknown);
	}	
	public TokenUnit (String newTokenString, TokenType newTokenType, TokenSubtype newTokenSubtype)
	{
		this.setTokenString(newTokenString);
		this.setTokenType(newTokenType);
		this.setTokenSubtype(newTokenSubtype);
	}
	
	public void setTokenString (String newTokenString)
	{
		this.tokenstring = newTokenString;
	}
	public String getTokenString ()
	{
		return this.tokenstring;
	}
	
	public void setTokenType (TokenType newTokenType)
	{
		this.tokentype = newTokenType;
	}
	public TokenType getTokenType ()
	{
		return this.tokentype;
	}
	
	public void setTokenSubtype (TokenSubtype newTokenSubtype)
	{
		this.tokensubtype = newTokenSubtype;
	}
	public TokenSubtype getTokenSubtype ()
	{
		return this.tokensubtype;
	}
	
	public String toString()
	{
		return this.tokenstring + " : " + tokentype.toString() + "(" + tokensubtype.toString() + ")"; 
	}

}
