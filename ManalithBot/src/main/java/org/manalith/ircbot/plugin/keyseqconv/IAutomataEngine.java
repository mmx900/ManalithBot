/*
 	org.manalith.ircbot.plugin.keyseqconv/IAutomataEngine.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2012  Seong-ho, Cho <darkcircle.0426@gmail.com>

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
package org.manalith.ircbot.plugin.keyseqconv;

import org.manalith.ircbot.plugin.keyseqconv.exceptions.BackSlashesDoNotMatchException;
import org.manalith.ircbot.plugin.keyseqconv.exceptions.LayoutNotSpecifiedException;

public interface IAutomataEngine {
	public void setEnableParsingExceptionSyntax(boolean enable);

	public boolean isEnableParsingExceptionSyntax();

	public String parseKoreanStringToEngSpell(String korean)
			throws BackSlashesDoNotMatchException;

	public String parseKeySequenceToKorean(String keySequence)
			throws BackSlashesDoNotMatchException, LayoutNotSpecifiedException;
}
