/*
 	org.manalith.ircbot.plugin.curex/TokenAnalyzer.java
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

import org.manalith.ircbot.plugin.curex.exceptions.EmptyTokenStreamException;

public abstract class TokenAnalyzer {
	public abstract TokenType getTokenType(String TokenString);

	public abstract TokenSubtype getTokenSubtype(String TokenString,
			TokenType CurrentType);

	public abstract TokenArray analysisTokenStream()
			throws EmptyTokenStreamException;
}
