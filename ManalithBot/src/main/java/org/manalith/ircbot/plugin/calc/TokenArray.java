/*
 	org.manalith.ircbot.plugin.calc/TokenArray.java
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
package org.manalith.ircbot.plugin.calc;

import java.util.ArrayList;

public class TokenArray {
	private ArrayList<TokenUnit> tokenArray;

	public TokenArray() {
		tokenArray = new ArrayList<>();
	}

	public TokenArray(TokenArray sourceObject) {
		tokenArray = new ArrayList<>();
		tokenArray.addAll(sourceObject.getArray());
	}

	public TokenArray(ArrayList<TokenUnit> sourceArray) {
		tokenArray = new ArrayList<>();
		tokenArray.addAll(sourceArray);
	}

	public void addToken(TokenType newTokenType, TokenSubtype newTokenSubtype,
			String newTokenStr) {
		TokenUnit newTokenUnit = new TokenUnit(newTokenType, newTokenSubtype,
				newTokenStr);
		tokenArray.add(newTokenUnit);
	}

	public void addToken(TokenUnit newTokenUnit) {
		tokenArray.add(newTokenUnit);
	}

	public TokenUnit getToken(int index) {
		return tokenArray.get(index);
	}

	public int getSize() {
		return tokenArray.size();
	}

	public ArrayList<TokenUnit> getArray() {
		return this.tokenArray;
	}

	@Override
	public String toString() {
		String result = "";

		for (TokenUnit unit : tokenArray) {
			result += unit.toString() + "\n";
		}

		return result;
	}
}
