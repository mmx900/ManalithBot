/*
 	org.manalith.ircbot.plugin.curex/TokenUnit.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2011  Seong-ho, Cho <darkcircle.0426@gmail.com>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.manalith.ircbot.plugin.curex;

public class TokenUnit {

	private TokenType tokenType;
	private TokenSubtype tokenSubtype;
	private String tokenString;

	public TokenUnit(String tokenString, TokenType tokenType,
			TokenSubtype tokenSubtype) {
		this.tokenString = tokenString;
		this.tokenType = tokenType;
		this.tokenSubtype = tokenSubtype;
	}

	public String getTokenString() {
		return this.tokenString;
	}

	public TokenType getTokenType() {
		return this.tokenType;
	}

	public TokenSubtype getTokenSubtype() {
		return this.tokenSubtype;
	}

	public String toString() {
		return this.tokenString + " : " + tokenType.toString() + "("
				+ tokenSubtype.toString() + ")";
	}

}
