/*
 	org.manalith.ircbot.plugin.calc/ParseTreeUnit.java
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

import java.math.BigInteger;

import org.manalith.ircbot.plugin.calc.exceptions.InvalidOperatorUseException;
import org.manalith.ircbot.plugin.calc.exceptions.NotImplementedException;

public class ParseTreeUnit {
	protected TokenUnit node;
	protected ParseTreeUnit left;
	protected ParseTreeUnit right;

	public void setNode(TokenUnit node) {
		this.node = node;
	}

	public TokenUnit getNode() {
		if (node != null)
			return node;
		else
			return null;
	}

	public void setLeftLeapNode(TokenUnit newNode) {
		left = new ParseTreeUnit();
		left.setNode(newNode);
	}

	public TokenUnit getLeftLeapNode() {
		if (left != null)
			return left.getNode();
		else
			return null;
	}

	public void setRightLeapNode(TokenUnit newNode) {
		right = new ParseTreeUnit();
		right.setNode(newNode);
	}

	public TokenUnit getRightLeapNode() {
		if (right != null)
			return right.getNode();
		else
			return null;
	}

	public void addLeftSubtree(ParseTreeUnit newTree) {
		left = new ParseTreeUnit();
		left = newTree;
	}

	public ParseTreeUnit getLeftSubtree() {
		return left;
	}

	public void addRightSubtree(ParseTreeUnit newTree) {
		right = new ParseTreeUnit();
		right = newTree;
	}

	public ParseTreeUnit getRightSubtree() {
		return right;
	}

	public void removeLeftSubtree() {
		left = null;
	}

	public void removeRightSubtree() {
		right = null;
	}

	public void preorder() {
		System.out.println(getNode());
		if (left != null)
			left.preorder();
		if (right != null)
			right.preorder();
	}

	public BigInteger Factorial(BigInteger n) {
		if (n.compareTo(new BigInteger("1")) == 0)
			return n;
		else
			return n.multiply(Factorial(n.subtract(new BigInteger("1"))));
	}

	public String getResultType() {
		String leftStr = "";
		String rightStr = "";

		if (left != null)
			leftStr = left.getResultType();
		if (right != null)
			rightStr = right.getResultType();

		if (node.getTokenType() == TokenType.TriangleFunc
				|| node.getTokenSubtype() == TokenSubtype.Sqrt)
			return "FlPoint";
		else if (node.getTokenType() == TokenType.Integer
				|| node.getTokenType() == TokenType.FlPoint)
			return node.getTokenType().toString();
		else {
			if ((leftStr.equals("Integer") && rightStr.equals("Integer") || leftStr
					.equals("Integer") && rightStr.equals(""))
					|| leftStr.equals("") && rightStr.equals("Integer"))
				return "Integer";
			else if (leftStr.equals("") && rightStr.equals(""))
				return "";
			else
				return "FlPoint";
		}
	}

	public String getIntFpResult() throws NotImplementedException {
		String result = "";
		BigInteger leftVal = new BigInteger("0");
		BigInteger rightVal = new BigInteger("0");

		if (node.getTokenSubtype() == TokenSubtype.Decimal) {
			result = node.getTokenString();
		} else if (node.getTokenSubtype() == TokenSubtype.Binary) {
			String data = node.getTokenString();
			int len = data.length();

			int val = 0;
			// ignore last 'b'
			for (int i = 0; i < len - 1; i++) {
				val *= 2;
				val += data.charAt(i) - '0';
			}

			result = Integer.toString(val);

		} else if (node.getTokenSubtype() == TokenSubtype.Octal) {
			String data = node.getTokenString();
			int len = data.length();

			int val = 0;
			// ignore first '0'
			for (int i = 1; i < len; i++) {
				val *= 8;
				val += data.charAt(i) - '0';
			}

			result = Integer.toString(val);
		} else if (node.getTokenSubtype() == TokenSubtype.Hexadec) {
			String data = node.getTokenString();
			int len = data.length();

			int val = 0;
			for (int i = 2; i < len; i++) {
				val *= 16;

				if (data.charAt(i) >= '0' && data.charAt(i) <= '9')
					val += data.charAt(i) - '0';

				if (data.charAt(i) >= 'a' && data.charAt(i) <= 'f')
					val += (data.charAt(i) - 'a' + 10);

				if (data.charAt(i) >= 'A' && data.charAt(i) <= 'F')
					val += (data.charAt(i) - 'A' + 10);
			}

			result = Integer.toString(val);
		}

		if (node.getTokenType() == TokenType.Operatr) {
			if (left != null)
				leftVal = new BigInteger(left.getIntFpResult());
			if (right != null)
				rightVal = new BigInteger(right.getIntFpResult());

			int opval = node.getTokenSubtype().hashCode();

			if (opval == TokenSubtype.Plus.hashCode())
				result = leftVal.add(rightVal).toString();
			else if (opval == TokenSubtype.Minus.hashCode())
				result = leftVal.subtract(rightVal).toString();
			else if (opval == TokenSubtype.Times.hashCode())
				result = leftVal.multiply(rightVal).toString();
			else if (opval == TokenSubtype.Divide.hashCode()) {
				if (rightVal.equals(new BigInteger("0")))
					throw new ArithmeticException("/ by zero");
				if (leftVal.mod(rightVal).equals(new BigInteger("0")))
					result = leftVal.divide(rightVal).toString();
				else {
					double t0 = leftVal.doubleValue();
					double t1 = rightVal.doubleValue();
					result = Double.toString(t0 / t1);
				}
			} else if (opval == TokenSubtype.Modulus.hashCode()) {
				if (rightVal.equals(new BigInteger("0")))
					throw new ArithmeticException("/ by zero");
				result = leftVal.mod(rightVal).toString();
			} else if (opval == TokenSubtype.Power.hashCode())
				result = leftVal.pow(rightVal.intValue()).toString();
			else if (opval == TokenSubtype.Factorial.hashCode())
				result = Factorial(leftVal).toString();
		}

		if (node.getTokenType() == TokenType.BaseConvFunc) {
			if (right != null)
				rightVal = new BigInteger(right.getIntFpResult());

			int opval = node.getTokenSubtype().hashCode();

			if (opval == TokenSubtype.ToBin.hashCode()) {
				String val = "";
				while (rightVal.compareTo(new BigInteger("2")) >= 0) {
					val = rightVal.mod(new BigInteger("2")).toString() + val;
					rightVal = rightVal.divide(new BigInteger("2"));
				}

				val = rightVal.add(new BigInteger(val)).toString() + "b";

				result = val;
			} else if (opval == TokenSubtype.ToOct.hashCode()) {
				String val = "";

				while (rightVal.compareTo(new BigInteger("8")) >= 0) {
					val = rightVal.mod(new BigInteger("8")).toString() + val;
					rightVal = rightVal.divide(new BigInteger("8"));
				}

				val = "0" + rightVal.toString() + val;

				result = val;
			} else if (opval == TokenSubtype.ToDec.hashCode()) {
				result = rightVal.toString();
			} else if (opval == TokenSubtype.ToHex.hashCode()) {
				String val = "";
				BigInteger temp = new BigInteger("0");

				while (rightVal.compareTo(new BigInteger("16")) >= 0) {
					temp = rightVal.mod(new BigInteger("16"));
					if (temp.compareTo(new BigInteger("10")) < 0)
						val = temp.toString() + val;
					else
						val = Character.toString((char) (temp.subtract(
								new BigInteger("10")).intValue() - 'A'))
								+ val;

					rightVal = rightVal.divide(new BigInteger("16"));
				}

				if (rightVal.compareTo(new BigInteger("10")) < 0)
					val = "0x" + rightVal.toString() + val;
				else
					val = "0x"
							+ Character.toString((char) (rightVal.subtract(
									new BigInteger("10")).intValue() + 'A'))
							+ val;

				result = val;
			}
		}

		return result;
	}

	public String getFpResult() throws InvalidOperatorUseException {
		String result = "";

		double leftVal = 0.0;
		double rightVal = 0.0;

		if (node.getTokenSubtype() == TokenSubtype.Decimal) {
			String data = node.getTokenString();
			result = Double.toString(Integer.parseInt(data));
		} else if (node.getTokenSubtype() == TokenSubtype.Binary) {
			String data = node.getTokenString();
			int len = data.length();

			int val = 0;
			// ignore last 'b'
			for (int i = 0; i < len - 1; i++) {
				val *= 2;
				val += data.charAt(i) - '0';
			}

			result = Double.toString(val);
		} else if (node.getTokenSubtype() == TokenSubtype.Octal) {
			String data = node.getTokenString();
			int len = data.length();

			int val = 0;
			// ignore first '0'
			for (int i = 1; i < len; i++) {
				val *= 8;
				val += data.charAt(i) - '0';
			}

			result = Double.toString(val);
		} else if (node.getTokenSubtype() == TokenSubtype.Hexadec) {
			String data = node.getTokenString();
			int len = data.length();

			int val = 0;
			for (int i = 2; i < len; i++) {
				val *= 16;

				if (data.charAt(i) >= '0' && data.charAt(i) <= '9')
					val += data.charAt(i) - '0';

				if (data.charAt(i) >= 'a' && data.charAt(i) <= 'f')
					val += (data.charAt(i) - 'a' + 10);

				if (data.charAt(i) >= 'A' && data.charAt(i) <= 'F')
					val += (data.charAt(i) - 'A' + 10);
			}

			result = Double.toString(val);
		} else if (node.getTokenSubtype() == TokenSubtype.SpFltPoint) {
			String data = node.getTokenString();
			result = Double.toString(Float.parseFloat(data));
		} else if (node.getTokenSubtype() == TokenSubtype.DpFltPoint) {
			String data = node.getTokenString();
			result = Double.toString(Double.parseDouble(data));
		} else if (node.getTokenSubtype() == TokenSubtype.ExpFltPoint) {
			String data = node.getTokenString();
			result = Double.toString(Double.parseDouble(data));
		}

		if (node.getTokenType() == TokenType.Operatr) {
			if (left != null)
				leftVal = Double.parseDouble(left.getFpResult());
			if (right != null)
				rightVal = Double.parseDouble(right.getFpResult());

			int opval = node.getTokenSubtype().hashCode();

			if (opval == TokenSubtype.Plus.hashCode())
				result = Double.toString(leftVal + rightVal);
			else if (opval == TokenSubtype.Minus.hashCode())
				result = Double.toString(leftVal - rightVal);
			else if (opval == TokenSubtype.Times.hashCode())
				result = Double.toString(leftVal * rightVal);
			else if (opval == TokenSubtype.Divide.hashCode())
				result = Double.toString(leftVal / rightVal);
			else if (opval == TokenSubtype.Modulus.hashCode())
				throw new InvalidOperatorUseException();
			else if (opval == TokenSubtype.Power.hashCode())
				result = Double.toString(Math.pow(leftVal, rightVal));
			else if (opval == TokenSubtype.Factorial.hashCode()) {
				if (left.getNode().getTokenType() == TokenType.Integer) {
					result = Factorial(
							new BigInteger(left.getNode().getTokenString()
									.split(".")[0])).toString();
				} else {
					throw new InvalidOperatorUseException();
				}
			}
		} else if (node.getTokenType() == TokenType.TriangleFunc) {
			if (right != null) {
				rightVal = Double.parseDouble(right.getFpResult());
			}

			int funcval = node.getTokenSubtype().hashCode();

			if (funcval == TokenSubtype.Sine.hashCode())
				result = Double.toString(Math.sin(rightVal / 180.0 * Math.PI));
			else if (funcval == TokenSubtype.Cosine.hashCode())
				result = Double.toString(Math.cos(rightVal / 180.0 * Math.PI));
			else if (funcval == TokenSubtype.Tangent.hashCode())
				result = Double.toString(Math.tan(rightVal / 180.0 * Math.PI));
			else if (funcval == TokenSubtype.ArcSine.hashCode())
				result = Double.toString(Math.asin(rightVal / 180.0 * Math.PI));
			else if (funcval == TokenSubtype.ArcCosine.hashCode())
				result = Double.toString(Math.acos(rightVal / 180.0 * Math.PI));
			else if (funcval == TokenSubtype.ArcTangent.hashCode())
				result = Double.toString(Math.atan(rightVal / 180.0 * Math.PI));
		} else if (node.getTokenType() == TokenType.MathematFunc) {
			if (right != null) {
				rightVal = Double.parseDouble(right.getFpResult());
			}

			int funcval = node.getTokenSubtype().hashCode();

			if (funcval == TokenSubtype.Sqrt.hashCode()) {
				result = Double.toString(Math.sqrt(rightVal));
			}
		}

		else if (node.getTokenType() == TokenType.BaseConvFunc)
			throw new InvalidOperatorUseException();

		return result;
	}
}
