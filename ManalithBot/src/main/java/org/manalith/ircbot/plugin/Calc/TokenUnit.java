package org.manalith.ircbot.plugin.Calc;
// TokenUnit.java 
//
// TokenUnit can have "a Token string", token type information and token subtype information.
//
// This program can be distributed under the terms of GNU GPL v3 or later.
// darkcircle.0426@gmail.com

public class TokenUnit {
	private TokenType Type;
	private TokenSubtype Subtype;
	private String TokenStr;
	
	public TokenUnit ( )
	{
		setTokenType ( TokenType.Unknown );
		setTokenSubtype ( TokenSubtype.Unknown );
		setTokenString ( "" );
	}
	public TokenUnit ( String newTokenStr )
	{
		setTokenType ( TokenType.Unknown );
		setTokenSubtype ( TokenSubtype.Unknown );
		setTokenString ( newTokenStr );
	}
	public TokenUnit ( TokenType newTokenType, TokenSubtype newTokenSubtype, String newTokenStr )
	{
		setTokenType ( newTokenType );
		setTokenSubtype ( newTokenSubtype );
		setTokenString ( newTokenStr );
	}
	
	public void setTokenType ( TokenType newTokenType )
	{
		this.Type = newTokenType;
	}
	public void setTokenSubtype ( TokenSubtype newTokenSubtype )
	{
		this.Subtype = newTokenSubtype;
	}
	public void setTokenString ( String newTokenStr )
	{
		this.TokenStr = newTokenStr;
	}
	
	public TokenType getTokenType ( )
	{
		return this.Type;
	}
	public TokenSubtype getTokenSubtype ( )
	{
		return this.Subtype;
	}
	public String getTokenString ( )
	{
		return this.TokenStr;
	}
	
	public String toString ( )
	{
		return this.TokenStr + " : " + this.Type.toString() + "(" + this.Subtype.toString() +")";
	}
}