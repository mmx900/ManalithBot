package tv.myhome.darkcircle.Calc;
import tv.myhome.darkcircle.Calc.Exceptions.EmptyTokenStreamException;
import tv.myhome.darkcircle.Calc.Exceptions.TokenAnalysisException;

public class CalcRunner {
	public static void run ( String expr )
	{
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
			System.out.println(" === Parse Error! === ");
			System.out.println( e.getMessage() );
			System.exit(0);
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
				System.out.println(" => " +  ptu.getIntResult() );
			else
				System.out.println(" => " + ptu.getFpResult() );
		}
		catch ( Exception e )
		{
			System.out.println ( " === Computation Error! === ");
			System.out.println ( e.getMessage() );
			System.exit(0);
		}
	}
}
