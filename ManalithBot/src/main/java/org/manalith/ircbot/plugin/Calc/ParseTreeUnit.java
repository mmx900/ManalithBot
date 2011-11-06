package org.manalith.ircbot.plugin.Calc;
// ParseTreeUnit.java 
//
// This class can return a result of computation using user input expression.
//
// This program can be distributed under the terms of GNU GPL v3 or later.
// darkcircle.0426@gmail.com

import org.manalith.ircbot.plugin.Calc.Exceptions.InvalidOperatorUseException;
import org.manalith.ircbot.plugin.Calc.Exceptions.NotImplementedException;

public class ParseTreeUnit {
	protected TokenUnit node;
	protected ParseTreeUnit left;
	protected ParseTreeUnit right;
	
	public ParseTreeUnit ( )
	{
		node = null;
		left = null;
		right = null;
	}
	public ParseTreeUnit ( TokenUnit newNode )
	{
		setNode ( newNode );
		left = null;
		right = null;
	}
	
	public void setNode ( TokenUnit newNode )
	{
		this.node = newNode;
	}
	public TokenUnit getNode ( )
	{
		if ( this.node != null )
			return this.node;
		else
			return null;
	}
	
	public void setLeftLeapNode ( TokenUnit newNode )
	{
		left = new ParseTreeUnit();
		left.setNode(newNode);
	}
	public TokenUnit getLeftLeapNode ( )
	{
		if ( left != null )
			return left.getNode();
		else 
			return null;
	}
	public void setRightLeapNode ( TokenUnit newNode )
	{
		right = new ParseTreeUnit();
		right.setNode(newNode);
	}
	public TokenUnit getRightLeapNode ( )
	{
		if ( right != null )
			return right.getNode();
		else
			return null;
	}

	public void addLeftSubtree ( ParseTreeUnit newTree )
	{
		left = new ParseTreeUnit();
		left = newTree;
	}
	public ParseTreeUnit getLeftSubtree ( )
	{
		return left;
	}
	public void addRightSubtree ( ParseTreeUnit newTree )
	{
		right = new ParseTreeUnit();
		right = newTree;
	}
	public ParseTreeUnit getRightSubtree ( )
	{
		return right;
	}
	public void removeLeftSubtree ( )
	{
		this.left = null;
	}
	public void removeRightSubtree ( )
	{
		this.right = null;
	}

	public void preorder()
	{
		System.out.println( this.getNode() );
		if ( left != null )
			left.preorder();
		if ( right != null)
			right.preorder();
	}
	
	public int Factorial ( int n )
	{
		if ( n == 1 ) return n ;
		else return n * Factorial ( n - 1 );
	}
	
	public String getResultType()
	{
		String leftStr = "";
		String rightStr = "";
		
		if ( left != null )
			leftStr = left.getResultType();
		if ( right != null )
			rightStr = right.getResultType();
		
		if ( node.getTokenType() == TokenType.TriangleFunc 
				|| node.getTokenSubtype() == TokenSubtype.Sqrt )
			return "FlPoint";
		else if ( node.getTokenType() == TokenType.Integer ||
				node.getTokenType() == TokenType.FlPoint )
			return node.getTokenType().toString();
		else
		{
			if ( ( leftStr.equals("Integer") && rightStr.equals("Integer") ||
				 leftStr.equals("Integer") && rightStr.equals("") ) ||
					leftStr.equals("") && rightStr.equals("Integer") )
				return "Integer";
			else if ( leftStr.equals("") && rightStr.equals("") )
				return "";
			else 
				return "FlPoint";
		}
	}
	
	public String getIntResult() throws NotImplementedException
	{
		String result = "";
		int leftVal = 0;
		int rightVal = 0;
		
		if ( node.getTokenSubtype() == TokenSubtype.Decimal )
		{
			result = node.getTokenString();
		}
		else if ( node.getTokenSubtype() == TokenSubtype.Binary )
		{
			String data = node.getTokenString();
			int len = data.length();
			
			int val = 0;
			// ignore last 'b'
			for ( int i = 0 ; i < len - 1 ; i++ )
			{
				val *= 2;
				val += (int)(data.charAt(i) - '0');				
			}
			
			result = Integer.toString(val);

		}
		else if ( node.getTokenSubtype() == TokenSubtype.Octal )
		{
			String data = node.getTokenString();
			int len = data.length();
			
			int val = 0;
			// ignore first '0'
			for ( int i = 1 ; i < len ; i++ )
			{				
				val *= 8;
				val += (int)(data.charAt(i) - '0');
			}
			
			result = Integer.toString(val);
		}
		else if ( node.getTokenSubtype() == TokenSubtype.Hexadec )
		{
			String data = node.getTokenString();
			int len = data.length();
			
			int val = 0;
			for ( int i = 2 ; i < len ; i++ )
			{
				val *= 16;
				
				if ( data.charAt(i) >= '0' && data.charAt(i) <= '9' )
					val += (int)(data.charAt(i) - '0');

				if ( data.charAt(i) >= 'a' && data.charAt(i) <= 'f' )
					val += ((int)(data.charAt(i) - 'a') + 10);
				
				if ( data.charAt(i) >= 'A' && data.charAt(i) <= 'F' )
					val += ((int)(data.charAt(i) - 'A') + 10);
			}
			
			result = Integer.toString(val);
		}
		
		if ( node.getTokenType() == TokenType.Operatr )
		{
			if ( left != null )
				leftVal = Integer.parseInt(left.getIntResult());
			if ( right != null )
				rightVal = Integer.parseInt(right.getIntResult());
			
			int opval = node.getTokenSubtype().hashCode();
			
			if ( opval == TokenSubtype.Plus.hashCode() )
				result = Integer.toString(leftVal + rightVal);
			else if ( opval == TokenSubtype.Minus.hashCode() )
				result = Integer.toString(leftVal - rightVal);
			else if ( opval == TokenSubtype.Times.hashCode() )
				result = Integer.toString(leftVal * rightVal);
			else if ( opval == TokenSubtype.Divide.hashCode() )
			{
				if ( rightVal == 0 )
					throw new ArithmeticException("/ by zero");
				result = Integer.toString(leftVal / rightVal);
			}
			else if ( opval == TokenSubtype.Modulus.hashCode() )
			{
				if ( rightVal == 0 )
					throw new ArithmeticException("/ by zero");
				result = Integer.toString(leftVal % rightVal);
			}
			else if ( opval == TokenSubtype.Power.hashCode() )
				result = Integer.toString((int)Math.pow((double)leftVal, (double)rightVal));
			else if ( opval == TokenSubtype.Factorial.hashCode() )
				result = Integer.toString(this.Factorial(leftVal));
		}
		
		if ( node.getTokenType() == TokenType.BaseConvFunc )
		{
			if ( right != null )
				rightVal = Integer.parseInt(right.getIntResult());
		
			int opval = node.getTokenSubtype().hashCode();
			
			if ( opval == TokenSubtype.ToBin.hashCode() )
			{
				String val = "";
				while ( rightVal >= 2 )
				{
					val = Integer.toString(rightVal % 2) + val;
					rightVal /= 2;
				}
				
				val = Integer.toString(rightVal) + val + "b";
				
				result = val;
			}
			else if ( opval == TokenSubtype.ToOct.hashCode() )
			{
				String val = "";
				
				while ( rightVal >= 8 )
				{
					val = Integer.toString(rightVal % 8) + val;
					rightVal /= 8;
				}
				
				val = "0" + Integer.toString(rightVal) + val;
				
				result = val;
			}
			else if ( opval == TokenSubtype.ToDec.hashCode() )
			{
				result = Integer.toString(rightVal);
			}
			else if ( opval == TokenSubtype.ToHex.hashCode() )
			{
				String val = "";
				int temp = 0;
				
				while ( rightVal >= 16 )
				{
					temp = rightVal % 16;
					if ( temp < 10 )
						val = Integer.toString(temp) + val;
					else
						val = Character.toString((char)((temp - 10) + 'A')) + val;
					
					rightVal /= 16;
				}
				
				if ( rightVal < 10 )
					val = "0x" + Integer.toString(rightVal) + val;
				else
					val = "0x" + Character.toString((char)((rightVal - 10) + 'A')) + val;
				
				result = val;
			}
		}
		
		return result;
	}
	
	public String getFpResult() throws InvalidOperatorUseException
	{
		String result = "";
		
		double leftVal = 0.0;
		double rightVal = 0.0;
		
		if ( node.getTokenSubtype() == TokenSubtype.Decimal )
		{
			String data = node.getTokenString();
			result = Double.toString((double)Integer.parseInt(data));
		}
		else if ( node.getTokenSubtype() == TokenSubtype.Binary )
		{
			String data = node.getTokenString();
			int len = data.length();
			
			int val = 0;
			// ignore last 'b'
			for ( int i = 0 ; i < len - 1 ; i++ )
			{
				val *= 2;
				val += (int)(data.charAt(i) - '0');				
			}

			result = Double.toString((double)val);
		}
		else if ( node.getTokenSubtype() == TokenSubtype.Octal )
		{
			String data = node.getTokenString();
			int len = data.length();
			
			int val = 0;
			// ignore first '0'
			for ( int i = 1 ; i < len ; i++ )
			{				
				val *= 8;
				val += (int)(data.charAt(i) - '0');
			}
			
			result = Double.toString((double)val);
		}
		else if ( node.getTokenSubtype() == TokenSubtype.Hexadec )
		{
			String data = node.getTokenString();
			int len = data.length();
			
			int val = 0;
			for ( int i = 2 ; i < len ; i++ )
			{
				val *= 16;
				
				if ( data.charAt(i) >= '0' && data.charAt(i) <= '9' )
					val += (int)(data.charAt(i) - '0');

				if ( data.charAt(i) >= 'a' && data.charAt(i) <= 'f' )
					val += ((int)(data.charAt(i) - 'a') + 10);
				
				if ( data.charAt(i) >= 'A' && data.charAt(i) <= 'F' )
					val += ((int)(data.charAt(i) - 'A') + 10);
			}
			
			result = Double.toString((double)val);
		}
		else if ( node.getTokenSubtype() == TokenSubtype.SpFltPoint )
		{
			String data = node.getTokenString();
			result = Double.toString((double)Float.parseFloat(data));
		}
		else if ( node.getTokenSubtype() == TokenSubtype.DpFltPoint )
		{
			String data = node.getTokenString();
			result = Double.toString(Double.parseDouble(data));
		}
		else if ( node.getTokenSubtype() == TokenSubtype.ExpFltPoint )
		{
			String data = node.getTokenString();
			result = Double.toString(Double.parseDouble(data));
		}
		
		if ( node.getTokenType() == TokenType.Operatr )
		{
			if ( left != null )
				leftVal = Double.parseDouble(left.getFpResult());
			if ( right != null )
				rightVal = Double.parseDouble(right.getFpResult());
			
			int opval = node.getTokenSubtype().hashCode();
			
			if ( opval == TokenSubtype.Plus.hashCode() )
				result = Double.toString(leftVal + rightVal);
			else if ( opval == TokenSubtype.Minus.hashCode() )
				result = Double.toString(leftVal - rightVal);
			else if ( opval == TokenSubtype.Times.hashCode() )
				result = Double.toString(leftVal * rightVal);
			else if ( opval == TokenSubtype.Divide.hashCode() )
				result = Double.toString(leftVal / rightVal);
			else if ( opval == TokenSubtype.Modulus.hashCode() )
				throw new InvalidOperatorUseException();
			else if ( opval == TokenSubtype.Power.hashCode() )
				result = Double.toString(Math.pow((double)leftVal, (double)rightVal));
			else if ( opval == TokenSubtype.Factorial.hashCode() )
			{
				if ( left.getNode().getTokenType() == TokenType.Integer )
				{
					result = Double.toString(Double.parseDouble(Integer.toString(this.Factorial(Integer.parseInt(left.getNode().getTokenString())))));
				}
				else
				{
					throw new InvalidOperatorUseException();
				}
			}
		}
		else if ( node.getTokenType() == TokenType.TriangleFunc )
		{
			if ( right != null )
			{
				rightVal = Double.parseDouble(right.getFpResult());
			}
			
			int funcval = node.getTokenSubtype().hashCode();
			
			if ( funcval == TokenSubtype.Sine.hashCode() )
				result = Double.toString(Math.sin(rightVal / 180.0 * Math.PI));
			else if ( funcval == TokenSubtype.Cosine.hashCode() )
				result = Double.toString(Math.cos(rightVal / 180.0 * Math.PI));
			else if ( funcval == TokenSubtype.Tangent.hashCode() )
				result = Double.toString(Math.tan(rightVal / 180.0 * Math.PI));
			else if ( funcval == TokenSubtype.ArcSine.hashCode() )
				result = Double.toString(Math.asin(rightVal / 180.0 * Math.PI));
			else if ( funcval == TokenSubtype.ArcCosine.hashCode() )
				result = Double.toString(Math.acos(rightVal / 180.0 * Math.PI));
			else if ( funcval == TokenSubtype.ArcTangent.hashCode() )
				result = Double.toString(Math.atan(rightVal / 180.0 * Math.PI));
		}
		else if ( node.getTokenType() == TokenType.MathematFunc )
		{
			if ( right != null )
			{
				rightVal = Double.parseDouble(right.getFpResult());
			}
			
			int funcval = node.getTokenSubtype().hashCode();
			
			if ( funcval == TokenSubtype.Sqrt.hashCode() )
			{
				result = Double.toString(Math.sqrt(rightVal));
			}
		}
		
		else if ( node.getTokenType() == TokenType.BaseConvFunc )
			throw new InvalidOperatorUseException();
		
		return result;
	}
}
