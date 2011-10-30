package org.manalith.ircbot.plugin.Calc;
import org.manalith.ircbot.plugin.Calc.Exceptions.EmptyTokenStreamException;
import org.manalith.ircbot.plugin.Calc.Exceptions.TokenAnalysisException;

public class CalcRunner {
	public static String run ( String expr )// throws EmptyTokenStreamException, TokenAnalysisException, Exception
	{
		String result = "";
		// Token analysis phase
		TokenArray tArray = new TokenArray();
		CalcTokenAnalyzer cta = new CalcTokenAnalyzer( expr );
		
		
		
		//*
		try 
		{
			tArray = cta.getTokenArray();	
		}
		catch ( EmptyTokenStreamException ets )
		{
			return "입력한 식이 없습니다.";
		}
		catch ( TokenAnalysisException e )
		{
			result += "Parse Error! : ";
			result += e.getMessage();
			return result;
		}
		/*
		catch ( NotImplementedException ie )
		{
			System.out.println("There is not implemented keyword.");
		}
		//*/
		
		
		
		//*
		try
		{
			ParseTreeUnit ptu = CalcParseTreeGenerator.generateParseTree( tArray );
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
			result = "Computation Error! : ";
			result += e.getMessage();
		}
		
		return result;
	}
}
