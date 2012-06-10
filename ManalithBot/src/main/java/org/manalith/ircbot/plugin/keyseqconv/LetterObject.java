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

import org.manalith.ircbot.plugin.keyseqconv.symboltable.DubeolSymbol;
import org.manalith.ircbot.plugin.keyseqconv.symboltable.SebeolFinalSymbol;
import org.manalith.ircbot.plugin.keyseqconv.symboltable.Sebeol390Symbol;
import org.manalith.ircbot.plugin.keyseqconv.symboltable.SebeolNoSftSymbol;

public class LetterObject {

	private KeyboardLayout layout;

	private DubeolSymbol.DubeolIConsonant initd;
	private DubeolSymbol.DubeolVowel vowd;
	private DubeolSymbol.DubeolFConsonant find;
	private SebeolFinalSymbol.SebeolIConsonant initsf;
	private SebeolFinalSymbol.SebeolVowel vowsf;
	private SebeolFinalSymbol.SebeolFConsonant finsf;
	private Sebeol390Symbol.SebeolIConsonant inits3;
	private Sebeol390Symbol.SebeolVowel vows3;
	private Sebeol390Symbol.SebeolFConsonant fins3;
	private SebeolNoSftSymbol.SebeolIConsonant initsn;
	private SebeolNoSftSymbol.SebeolVowel vowsn;
	private SebeolNoSftSymbol.SebeolFConsonant finsn;

	private boolean assignedFConstantFirst;

	public LetterObject() throws IllegalArgumentException {
		this(KeyboardLayout.Null);
	}

	public LetterObject(KeyboardLayout newLayout)
			throws IllegalArgumentException {
		this.setLayout(newLayout);
		this.initLetter();
	}

	public void setLayout(KeyboardLayout newLayout) {
		this.layout = newLayout;
	}

	public KeyboardLayout getLayout() {
		return this.layout;
	}

	public void initLetter() throws IllegalArgumentException {
		if (this.getLayout() == KeyboardLayout.Null)
			throw new IllegalArgumentException("Layout doesn't specified");
		else if (this.getLayout() == KeyboardLayout.Dubeol) {
			initd = DubeolSymbol.DubeolIConsonant.nul;
			vowd = DubeolSymbol.DubeolVowel.nul;
			find = DubeolSymbol.DubeolFConsonant.nul;
		} else if(this.getLayout() == KeyboardLayout.SebeolFinal) {
			initsf = SebeolFinalSymbol.SebeolIConsonant.nul;
			vowsf = SebeolFinalSymbol.SebeolVowel.nul;
			finsf = SebeolFinalSymbol.SebeolFConsonant.nul;
		} else if(this.getLayout() == KeyboardLayout.Sebeol390) {
			inits3 = Sebeol390Symbol.SebeolIConsonant.nul;
			vows3 = Sebeol390Symbol.SebeolVowel.nul;
			fins3 = Sebeol390Symbol.SebeolFConsonant.nul;
		} else if(this.getLayout() == KeyboardLayout.SebeolNoSft) {
			initsn = SebeolNoSftSymbol.SebeolIConsonant.nul;
			vowsn = SebeolNoSftSymbol.SebeolVowel.nul;
			finsn = SebeolNoSftSymbol.SebeolFConsonant.nul;
		}
		assignedFConstantFirst = false;
	}

	public boolean isAssignedFConstantFirst() {
		return this.assignedFConstantFirst;
	}

	public void setIConsonant(String keyVal) {
		if (this.getLayout() == KeyboardLayout.Dubeol)
			initd = DubeolSymbol.DubeolIConsonant.valueOf(keyVal);
		else if (this.getLayout() == KeyboardLayout.SebeolFinal)
			initsf = SebeolFinalSymbol.SebeolIConsonant.valueOf(keyVal);
		else if (this.getLayout() == KeyboardLayout.Sebeol390)
			inits3 = Sebeol390Symbol.SebeolIConsonant.valueOf(keyVal);
		else if (this.getLayout() == KeyboardLayout.SebeolNoSft)
			initsn = SebeolNoSftSymbol.SebeolIConsonant.valueOf(keyVal);
	}

	public void setVowel(String keyVal) {
		if (this.getLayout() == KeyboardLayout.Dubeol)
			vowd = DubeolSymbol.DubeolVowel.valueOf(keyVal);
		else if (this.getLayout() == KeyboardLayout.SebeolFinal)
			vowsf = SebeolFinalSymbol.SebeolVowel.valueOf(keyVal);
		else if (this.getLayout() == KeyboardLayout.Sebeol390)
			vows3 = Sebeol390Symbol.SebeolVowel.valueOf(keyVal);
		else if (this.getLayout() == KeyboardLayout.SebeolNoSft)
			vowsn = SebeolNoSftSymbol.SebeolVowel.valueOf(keyVal);
	}

	public void setFConsonant(String keyVal) {
		if (this.getLayout() == KeyboardLayout.Dubeol) {
			if (initd.value() == 99 && vowd.value() == 99)
				this.assignedFConstantFirst = true;
			find = DubeolSymbol.DubeolFConsonant.valueOf(keyVal);
		} else if (this.getLayout() == KeyboardLayout.SebeolFinal) {
			if (initsf.value() == 99 && vowsf.value() == 99)
				this.assignedFConstantFirst = true;
			finsf = SebeolFinalSymbol.SebeolFConsonant.valueOf(keyVal);
		} else if (this.getLayout() == KeyboardLayout.Sebeol390) {
			if (inits3.value() == 99 && vows3.value() == 99)
				this.assignedFConstantFirst = true;
			fins3 = Sebeol390Symbol.SebeolFConsonant.valueOf(keyVal);
		} else if (this.getLayout() == KeyboardLayout.SebeolNoSft) {
			if (initsn.value() == 99 && vowsn.value() == 99)
				this.assignedFConstantFirst = true;
			finsn = SebeolNoSftSymbol.SebeolFConsonant.valueOf(keyVal);
		}
	}

	public String getIConsonantKeySymbol() {
		String result = "nul";
		if (this.getLayout() == KeyboardLayout.Dubeol)
			result = initd.toString();
		else if (this.getLayout() == KeyboardLayout.SebeolFinal)
			result = initsf.toString();
		else if (this.getLayout() == KeyboardLayout.Sebeol390)
			result = inits3.toString();
		else if (this.getLayout() == KeyboardLayout.SebeolNoSft)
			result = initsn.toString();
		return result;
	}

	public String getVowelKeySymbol() {
		String result = "nul";

		if (this.getLayout() == KeyboardLayout.Dubeol)
			result = vowd.toString();
		else if (this.getLayout() == KeyboardLayout.SebeolFinal)
			result = vowsf.toString();
		else if (this.getLayout() == KeyboardLayout.Sebeol390)
			result = vows3.toString();
		else if (this.getLayout() == KeyboardLayout.SebeolNoSft)
			result = vowsn.toString();
		return result;
	}

	public String getFConsonantKeySymbol() {
		String result = "nul";

		if (this.getLayout() == KeyboardLayout.Dubeol)
			result = find.toString();
		else if (this.getLayout() == KeyboardLayout.SebeolFinal)
			result = finsf.toString();
		else if (this.getLayout() == KeyboardLayout.Sebeol390)
			result = fins3.toString();
		else if (this.getLayout() == KeyboardLayout.SebeolNoSft)
			result = finsn.toString();
		return result;
	}

	public int getIConsonantValue() {
		int result = 99;
		
		if (this.getLayout() == KeyboardLayout.Dubeol)
			result = initd.value();
		else if (this.getLayout() == KeyboardLayout.SebeolFinal)
			result = initsf.value();
		else if (this.getLayout() == KeyboardLayout.Sebeol390)
			result = inits3.value();
		else if (this.getLayout() == KeyboardLayout.SebeolNoSft)
			result = initsn.value();
		return result;
	}

	public int getVowelValue() {
		int result = 99;

		if (this.getLayout() == KeyboardLayout.Dubeol)
			result = vowd.value();
		else if (this.getLayout() == KeyboardLayout.SebeolFinal)
			result = vowsf.value();
		else if (this.getLayout() == KeyboardLayout.Sebeol390)
			result = vows3.value();
		else if (this.getLayout() == KeyboardLayout.SebeolNoSft)
			result = vowsn.value();
		return result;
	}

	public int getFConsonantValue() {
		int result = 0;

		if (this.getLayout() == KeyboardLayout.Dubeol)
			result = find.value();
		else if (this.getLayout() == KeyboardLayout.SebeolFinal)
			result = finsf.value();
		else if (this.getLayout() == KeyboardLayout.Sebeol390)
			result = fins3.value();
		else if (this.getLayout() == KeyboardLayout.SebeolNoSft)
			result = finsn.value();
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

		if (this.getLayout() == KeyboardLayout.Dubeol)
			result = (initd != DubeolSymbol.DubeolIConsonant.nul && vowd != DubeolSymbol.DubeolVowel.nul);
		else if (this.getLayout() == KeyboardLayout.SebeolFinal)
			result = (initsf != SebeolFinalSymbol.SebeolIConsonant.nul && vowsf != SebeolFinalSymbol.SebeolVowel.nul);
		else if (this.getLayout() == KeyboardLayout.Sebeol390)
			result = (inits3 != Sebeol390Symbol.SebeolIConsonant.nul && vows3 != Sebeol390Symbol.SebeolVowel.nul);
		else if (this.getLayout() == KeyboardLayout.SebeolNoSft)
			result = (initsn != SebeolNoSftSymbol.SebeolIConsonant.nul && vowsn != SebeolNoSftSymbol.SebeolVowel.nul);
		return result;
	}
}
