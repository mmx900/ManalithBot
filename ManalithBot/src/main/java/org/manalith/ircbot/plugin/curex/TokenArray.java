/*
 	org.manalith.ircbot.plugin.curex/TokenArray.java
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

import java.util.ArrayList;

public class TokenArray {

	private ArrayList<TokenUnit> array;

	public TokenArray() {
		array = new ArrayList<TokenUnit>();
	}

	public int getSize() {
		return array.size();
	}

	public void addElement(TokenUnit newTokenUnit) {
		array.add(newTokenUnit);
	}

	public void addElement(String tokenString, TokenType newTokenType,
			TokenSubtype newTokenSubtype) {
		TokenUnit newTokenUnit = new TokenUnit(tokenString, newTokenType,
				newTokenSubtype);
		array.add(newTokenUnit);
	}

	public void removeElement(int index) {
		array.remove(index);
	}

	public TokenUnit getElement(int index) {
		return array.get(index);
	}

	public String toString() {
		String result = "";

		int size = this.array.size();

		for (int i = 0; i < size; i++) {
			result += (this.getElement(i).toString() + "\n");
		}

		return result;
	}

}
