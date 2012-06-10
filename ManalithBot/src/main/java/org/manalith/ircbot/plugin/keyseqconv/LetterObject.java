/*
 	org.manalith.ircbot.plugin.keyseqconv/LetterObject.java
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

import org.manalith.ircbot.plugin.keyseqconv.exceptions.LayoutNotSpecifiedException;

public class LetterObject {

	public static enum LayoutFlag {
		Null, Dubeol, Sebeol
	}

	private LayoutFlag layout;

	private DubeolSymbol.DubeolIConsonant initd;
	private DubeolSymbol.DubeolVowel vowd;
	private DubeolSymbol.DubeolFConsonant find;
	private SebeolSymbol.SebeolIConsonant inits;
	private SebeolSymbol.SebeolVowel vows;
	private SebeolSymbol.SebeolFConsonant fins;

	private boolean assignedFConstantFirst;

	public LetterObject() throws LayoutNotSpecifiedException {
		this(LetterObject.LayoutFlag.Null);
	}

	public LetterObject(LetterObject.LayoutFlag newLayout)
			throws LayoutNotSpecifiedException {
		this.setLayout(newLayout);
		this.initLetter();
	}

	public void setLayout(LetterObject.LayoutFlag newLayout) {
		this.layout = newLayout;
	}

	public LetterObject.LayoutFlag getLayout() {
		return this.layout;
	}

	public void initLetter() throws LayoutNotSpecifiedException {
		if (this.getLayout() == LayoutFlag.Null)
			throw new LayoutNotSpecifiedException();
		else if (this.getLayout() == LayoutFlag.Dubeol) {
			initd = DubeolSymbol.DubeolIConsonant.nul;
			vowd = DubeolSymbol.DubeolVowel.nul;
			find = DubeolSymbol.DubeolFConsonant.nul;
		} else if (this.getLayout() == LayoutFlag.Sebeol) {
			inits = SebeolSymbol.SebeolIConsonant.nul;
			vows = SebeolSymbol.SebeolVowel.nul;
			fins = SebeolSymbol.SebeolFConsonant.nul;
		}

		assignedFConstantFirst = false;
	}

	public boolean isAssignedFConstantFirst() {
		return this.assignedFConstantFirst;
	}

	public void setIConsonant(String keyVal) {
		if (this.getLayout() == LayoutFlag.Dubeol)
			initd = DubeolSymbol.DubeolIConsonant.valueOf(keyVal);
		else if (this.getLayout() == LayoutFlag.Sebeol)
			inits = SebeolSymbol.SebeolIConsonant.valueOf(keyVal);
	}

	public void setVowel(String keyVal) {
		if (this.getLayout() == LayoutFlag.Dubeol)
			vowd = DubeolSymbol.DubeolVowel.valueOf(keyVal);
		else if (this.getLayout() == LayoutFlag.Sebeol)
			vows = SebeolSymbol.SebeolVowel.valueOf(keyVal);
	}

	public void setFConsonant(String keyVal) {
		if (this.getLayout() == LayoutFlag.Dubeol) {
			if (initd.value() == 99 && vowd.value() == 99)
				this.assignedFConstantFirst = true;
			find = DubeolSymbol.DubeolFConsonant.valueOf(keyVal);
		} else if (this.getLayout() == LayoutFlag.Sebeol) {
			if (inits.value() == 99 && vows.value() == 99)
				this.assignedFConstantFirst = true;
			fins = SebeolSymbol.SebeolFConsonant.valueOf(keyVal);
		}
	}

	public String getIConsonantKeySymbol() {
		String result = "nul";
		if (this.getLayout() == LayoutFlag.Dubeol)
			result = initd.toString();
		else if (this.getLayout() == LayoutFlag.Sebeol)
			result = inits.toString();

		return result;
	}

	public String getVowelKeySymbol() {
		String result = "nul";

		if (this.getLayout() == LayoutFlag.Dubeol)
			result = vowd.toString();
		else if (this.getLayout() == LayoutFlag.Sebeol)
			result = vows.toString();

		return result;
	}

	public String getFConsonantKeySymbol() {
		String result = "nul";

		if (this.getLayout() == LayoutFlag.Dubeol)
			result = find.toString();
		else if (this.getLayout() == LayoutFlag.Sebeol)
			result = fins.toString();

		return result;
	}

	public int getIConsonantValue() {
		int result = 99;
		if (this.getLayout() == LayoutFlag.Dubeol)
			result = initd.value();
		else if (this.getLayout() == LayoutFlag.Sebeol)
			result = inits.value();

		return result;
	}

	public int getVowelValue() {
		int result = 99;

		if (this.getLayout() == LayoutFlag.Dubeol)
			result = vowd.value();
		else if (this.getLayout() == LayoutFlag.Sebeol)
			result = vows.value();

		return result;
	}

	public int getFConsonantValue() {
		int result = 0;

		if (this.getLayout() == LayoutFlag.Dubeol)
			result = find.value();
		else if (this.getLayout() == LayoutFlag.Sebeol)
			result = fins.value();

		return result;
	}

	public String getLetter() {
		String result = "";
		char[] ch = new char[1];

		if (this.isCompleteSyllable()) {

			ch[0] = (char) ((this.getIConsonantValue() * 21 * 28
					+ this.getVowelValue() * 28 + this.getFConsonantValue()) + 0xAC00);

			result = new String(ch);
		}

		return result;
	}

	public boolean isCompleteSyllable() {
		boolean result = false;

		if (this.getLayout() == LayoutFlag.Dubeol)
			result = (initd != DubeolSymbol.DubeolIConsonant.nul && vowd != DubeolSymbol.DubeolVowel.nul);
		else if (this.getLayout() == LayoutFlag.Sebeol)
			result = (inits != SebeolSymbol.SebeolIConsonant.nul && vows != SebeolSymbol.SebeolVowel.nul);

		return result;
	}
}
