/*
 	org.manalith.ircbot.plugin.calc/CalcRunner.java
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

import org.manalith.ircbot.plugin.calc.exceptions.EmptyTokenStreamException;
import org.manalith.ircbot.plugin.calc.exceptions.TokenAnalysisException;

public class CalcRunner {
	public static String run(String expr) {

		String result = "";
		// Token analysis phase
		TokenArray tArray = new TokenArray();
		CalcTokenAnalyzer cta = new CalcTokenAnalyzer(expr);

		try {
			tArray = cta.getTokenArray();
			/*
			 * int asize = tArray.getSize(); for ( int i = 0 ; i < asize ; i++ )
			 * System.out.println(tArray.getToken(i)); //
			 */
		} catch (EmptyTokenStreamException ets) {
			System.out.println("input expression is empty string.");
		} catch (TokenAnalysisException e) {
			result = " === Parse Error! === " + e.getMessage();
			return result;
		}
		/*
		 * catch ( NotImplementedException ie ) {
		 * System.out.println("There is a keyword which is not implemented."); }
		 */

		ParseTreeUnit ptu = null;

		try {
			// Parse tree generation phase
			ptu = CalcParseTreeGenerator.generateParseTree(tArray);
			// ptu.preorder();
			// System.out.println("Result type : " + ptu.getResultType());

			// Computation phase
			if (ptu.getResultType().equals("Integer"))
				result = " => " + ptu.getIntResult();
			else
				result = " => " + ptu.getFpResult();
		} catch (Exception e) {
			result = "Computation Error! : " + e.getMessage();
			return result;
		}

		return result;
	}
}
