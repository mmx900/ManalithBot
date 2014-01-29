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

import org.manalith.ircbot.plugin.calc.TokenUnit.TokenSubtype;
import org.manalith.ircbot.plugin.calc.TokenUnit.TokenType;
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
		String data = "";
		String valstr = "";
		int len = 0;

		BigInteger leftVal = new BigInteger("0");
		BigInteger rightVal = new BigInteger("0");

		BigInteger valint = new BigInteger("0");

		switch (node.getTokenType().value()) {
		case 1:
			switch (node.getTokenSubtype().value()) {
			case 3: // decimal
				result = node.getTokenString();
				break;
			case 1: // binary
				data = node.getTokenString();
				len = data.length();

				// ignore last 'b'
				for (int i = 0; i < len - 1; i++) {
					valint.multiply(new BigInteger("2"));
					valint.add(new BigInteger(
							Integer.toString(data.charAt(i) - '0')));
				}

				result = valint.toString();
				break;
			case 2: // octal
				data = node.getTokenString();
				len = data.length();

				// ignore first '0'
				for (int i = 1; i < len; i++) {
					valint.multiply(new BigInteger("8"));
					valint.add(new BigInteger(
							Integer.toString(data.charAt(i) - '0')));
				}

				result = valint.toString();
				break;
			case 4: // hex
				data = node.getTokenString();
				len = data.length();

				for (int i = 2; i < len; i++) {
					valint.multiply(new BigInteger("16"));

					if (data.charAt(i) >= '0' && data.charAt(i) <= '9')
						valint.add(new BigInteger(Integer.toString(data
								.charAt(i) - '0')));
					else if (data.charAt(i) >= 'a' && data.charAt(i) <= 'f')
						valint.add(new BigInteger(Integer.toString(data
								.charAt(i) - 'a' + 10)));
					else if (data.charAt(i) >= 'A' && data.charAt(i) <= 'F')
						valint.add(new BigInteger(Integer.toString(data
								.charAt(i) - 'A' + 10)));
				}

				result = valint.toString();
				break;
			}
		case 3: // operator
			if (left != null)
				leftVal = new BigInteger(left.getIntFpResult());
			if (right != null)
				rightVal = new BigInteger(right.getIntFpResult());

			switch (node.getTokenSubtype().value()) {
			case 8: // plus
				result = leftVal.add(rightVal).toString();
				break;
			case 9: // minus
				result = leftVal.subtract(rightVal).toString();
				break;
			case 10: // multiply
				result = leftVal.multiply(rightVal).toString();
				break;
			case 11: // divide
				if (rightVal.equals(new BigInteger("0")))
					throw new ArithmeticException("/ by zero");
				if (leftVal.mod(rightVal).equals(new BigInteger("0")))
					result = leftVal.divide(rightVal).toString();
				else {
					double t0 = leftVal.doubleValue();
					double t1 = rightVal.doubleValue();
					result = Double.toString(t0 / t1);
				}
				break;
			case 12: // modulus
				if (rightVal.equals(new BigInteger("0")))
					throw new ArithmeticException("/ by zero");
				result = leftVal.mod(rightVal).toString();
				break;
			case 13: // power
				result = leftVal.pow(rightVal.intValue()).toString();
				break;
			case 14: // factorial
				result = Factorial(leftVal).toString();
				break;
			}
			break;
		case 6: // base conversion function
			if (right != null)
				rightVal = new BigInteger(right.getIntFpResult());

			switch (node.getTokenSubtype().value()) {
			case 23: // bin
				while (rightVal.compareTo(new BigInteger("2")) >= 0) {
					valstr = rightVal.mod(new BigInteger("2")).toString()
							+ valstr;
					rightVal = rightVal.divide(new BigInteger("2"));
				}

				valstr = rightVal.add(new BigInteger(valstr)).toString() + "b";

				result = valstr;
				break;
			case 24: // octal
				while (rightVal.compareTo(new BigInteger("8")) >= 0) {
					valstr = rightVal.mod(new BigInteger("8")).toString()
							+ valstr;
					rightVal = rightVal.divide(new BigInteger("8"));
				}

				valstr = "0" + rightVal.toString() + valstr;

				result = valstr;
				break;
			case 25: // decimal
				result = rightVal.toString();
				break;
			case 26: // hex
				BigInteger temp = new BigInteger("0");

				while (rightVal.compareTo(new BigInteger("16")) >= 0) {
					temp = rightVal.mod(new BigInteger("16"));
					if (temp.compareTo(new BigInteger("10")) < 0)
						valstr = temp.toString() + valstr;
					else
						valstr = Character.toString((char) (temp.subtract(
								new BigInteger("10")).intValue() + 'A'))
								+ valstr;

					rightVal = rightVal.divide(new BigInteger("16"));
				}

				if (rightVal.compareTo(new BigInteger("10")) < 0)
					valstr = "0x" + rightVal.toString() + valstr;
				else
					valstr = "0x"
							+ Character.toString((char) (rightVal.subtract(
									new BigInteger("10")).intValue() + 'A'))
							+ valstr;

				result = valstr;
				break;
			}
		}

		return result;
	}

	public String getFpResult() throws InvalidOperatorUseException {
		String result = "";
		String data = "";
		int len = 0;
		BigInteger val = new BigInteger("0");

		double leftVal = 0.0;
		double rightVal = 0.0;

		switch (node.getTokenType().value()) {
		case 2: // floating-point
			switch (node.getTokenSubtype().value()) {
			case 3: // decimal
				data = node.getTokenString();
				result = Double.toString(Integer.parseInt(data));
				break;
			case 1: // binary
				data = node.getTokenString();
				len = data.length();

				// ignore last 'b'
				for (int i = 0; i < len - 1; i++) {
					val.multiply(new BigInteger("2"));
					val.add(new BigInteger(Integer.toString(data.charAt(i) - '0')));
				}

				result = Double.toString(val.doubleValue());
				break;
			case 2: // octal
				data = node.getTokenString();
				len = data.length();

				// ignore first '0'
				for (int i = 1; i < len; i++) {
					val.multiply(new BigInteger("8"));
					val.add(new BigInteger(Integer.toString(data.charAt(i) - '0')));
				}

				result = Double.toString(val.doubleValue());
				break;
			case 4: // hex
				data = node.getTokenString();
				len = data.length();

				for (int i = 2; i < len; i++) {
					val.multiply(new BigInteger("16"));

					if (data.charAt(i) >= '0' && data.charAt(i) <= '9')
						val.add(new BigInteger(Integer.toString(data.charAt(i) - '0')));

					if (data.charAt(i) >= 'a' && data.charAt(i) <= 'f')
						val.add(new BigInteger(Integer.toString(data.charAt(i) - 'a' + 10)));

					if (data.charAt(i) >= 'A' && data.charAt(i) <= 'F')
						val.add(new BigInteger(Integer.toString(data.charAt(i) - 'A' + 10)));
				}

				result = Double.toString(val.doubleValue());
				break;
			case 5: // single-precision floating point
				data = node.getTokenString();
				result = Double.toString(Float.parseFloat(data));
				break;
			case 6: // double-precision floating point
				data = node.getTokenString();
				result = Double.toString(Double.parseDouble(data));
				break;
			case 7: // exponential floating point
				data = node.getTokenString();
				result = Double.toString(Double.parseDouble(data));
				break;
			}
			break;
		case 3: // operator
			if (left != null)
				leftVal = Double.parseDouble(left.getFpResult());
			if (right != null)
				rightVal = Double.parseDouble(right.getFpResult());

			switch (node.getTokenSubtype().value()) {
			case 8: // plus
				result = Double.toString(leftVal + rightVal);
				break;
			case 9: // minus
				result = Double.toString(leftVal - rightVal);
				break;
			case 10: // multiply
				result = Double.toString(leftVal * rightVal);
				break;
			case 11: // divide
				result = Double.toString(leftVal / rightVal);
				break;
			case 12: // modulus
				throw new InvalidOperatorUseException();
			case 13: // power
				result = Double.toString(Math.pow(leftVal, rightVal));
				break;
			case 14: // factorial
				if (left.getNode().getTokenType().equals(TokenType.Integer))
					result = Factorial(
							new BigInteger(left.getNode().getTokenString()
									.split(".")[0])).toString();
				else
					throw new InvalidOperatorUseException();
			}
			break;
		case 5: // triangular function
			if (right != null)
				rightVal = Double.parseDouble(right.getFpResult());

			switch (node.getTokenSubtype().value()) {
			case 17: // sine
				result = Double.toString(Math.sin(rightVal / 180.0 * Math.PI));
				break;
			case 18: // cosine
				result = Double.toString(Math.cos(rightVal / 180.0 * Math.PI));
				break;
			case 19: // tangent
				result = Double.toString(Math.tan(rightVal / 180.0 * Math.PI));
				break;
			case 20: // arcsine
				result = Double.toString(Math.asin(rightVal / 180.0 * Math.PI));
				break;
			case 21: // arccosine
				result = Double.toString(Math.acos(rightVal / 180.0 * Math.PI));
				break;
			case 22: // arctangent
				result = Double.toString(Math.atan(rightVal / 180.0 * Math.PI));
				break;
			}
			break;
		case 6: // base conversion function
			throw new InvalidOperatorUseException();
		case 7: // mathematical function
			if (right != null)
				rightVal = Double.parseDouble(right.getFpResult());

			if (node.getTokenSubtype().equals(TokenSubtype.Sqrt))
				result = Double.toString(Math.sqrt(rightVal));
			break;
		}

		return result;
	}
}

