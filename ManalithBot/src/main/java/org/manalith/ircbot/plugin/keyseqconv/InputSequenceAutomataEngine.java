/*
 org.manalith.ircbot.plugin.keyseqconv/InputSequenceAutomataEngine.java
 ManalithBot - An open source IRC bot based on the PircBot Framework.
 Copyright (C) 2012 Seong-ho, Cho <darkcircle.0426@gmail.com>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.manalith.ircbot.plugin.keyseqconv;

public abstract class InputSequenceAutomataEngine implements IAutomataEngine {

	public InputSequenceAutomataEngine() {
	}

	protected abstract boolean isISingleConsonant(String tICon);

	protected abstract boolean isIDoubleConsonant(String tICon);

	protected abstract boolean isVowel(String tVow);

	protected abstract boolean isFConsonant(String tFCon);

	protected abstract int getSingleCharVal(String keySequence);

	protected abstract String getSingleChar(int charVal);

}
