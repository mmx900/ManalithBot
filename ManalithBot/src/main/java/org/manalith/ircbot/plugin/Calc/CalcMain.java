package tv.myhome.darkcircle.Calc;

//import java.util.Scanner;

public class CalcMain {
	public static void main(String [] args)
	{
		// Scanner scan = new Scanner(System.in);
		
		//System.out.print("Input the expression to compute : ");
		
		String expr;// = scan.next();
		// expr = "((3+cos(12*5))+(cos(12*5)+sin(15*2)))*2";
		// expr = "tobin(0xB8) + 2";
		// expr = "36/(4-4)";
		//expr = "3!!";
		expr = "(1+2+3!)/3+sqrt(4)";
		CalcRunner.run(expr);
		//*/
		
	}
}
