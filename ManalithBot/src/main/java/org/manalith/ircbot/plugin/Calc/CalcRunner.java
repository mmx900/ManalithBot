//
// CalcRunner.java
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.

package org.manalith.ircbot.plugin.Calc;
import org.manalith.ircbot.plugin.Calc.Exceptions.EmptyTokenStreamException;
import org.manalith.ircbot.plugin.Calc.Exceptions.TokenAnalysisException;

public class CalcRunner {
	public static String run ( String expr )
	{
		
		String result = "";
		// Token analysis phase
		TokenArray tArray = new TokenArray();
		CalcTokenAnalyzer cta = new CalcTokenAnalyzer( expr );
		
		try 
		{
			tArray = cta.getTokenArray();
			/*
			int asize = tArray.getSize();
			for ( int i = 0 ; i < asize ; i++ )
				System.out.println(tArray.getToken(i));
			//*/	
		}
		catch ( EmptyTokenStreamException ets )
		{
			System.out.println("input expression is empty string.");
		}
		catch ( TokenAnalysisException e )
		{
			result = " === Parse Error! === " + e.getMessage();
			return result;
		}
		/*
		catch ( NotImplementedException ie )
		{
			System.out.println("There is a keyword which is not implemented.");
		}
		*/
		
		ParseTreeUnit ptu = null;
		
		try
		{
			// Parse tree generation phase
			ptu = CalcParseTreeGenerator.generateParseTree( tArray );
			// ptu.preorder();
			// System.out.println("Result type : " + ptu.getResultType());
			
			// Computation phase
			if ( ptu.getResultType().equals("Integer") )
				result = " => " +  ptu.getIntResult();
			else
				result = " => " + ptu.getFpResult();
		}
		catch ( Exception e )
		{
			result = "Computation Error! : " + e.getMessage();
			return result;
		}
		
		return result;
	}
}
