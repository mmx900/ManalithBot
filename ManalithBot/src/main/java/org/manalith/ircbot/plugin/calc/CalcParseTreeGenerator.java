/*
 	org.manalith.ircbot.plugin.calc/CalcParseTreeGenerator.java
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

import org.manalith.ircbot.plugin.calc.TokenUnit.TokenSubtype;
import org.manalith.ircbot.plugin.calc.TokenUnit.TokenType;
import org.manalith.ircbot.plugin.calc.exceptions.InvalidSequenceTokenException;
import org.manalith.ircbot.plugin.calc.exceptions.ParenthesesMismatchException;

public class CalcParseTreeGenerator {

	public static ParseTreeUnit generateParseTree(TokenArray sArray)
			throws ParenthesesMismatchException, InvalidSequenceTokenException {
		int size = sArray.getSize();

		ParseTreeUnit pstu_root = new ParseTreeUnit();

		int i = 0;

		TokenUnit arg0 = null;
		TokenUnit op = null;
		TokenUnit arg1 = null;

		int lparen_cnt = 0;
		int rparen_cnt = 0;

		// Check whether Pairs of parentheses are match or not.
		for (; i < size; i++) {
			if (sArray.getToken(i).getTokenSubtype()
					.equals(TokenSubtype.Left_Parenthesis))
				lparen_cnt++;
			if (sArray.getToken(i).getTokenSubtype()
					.equals(TokenSubtype.Righ_Parenthesis))
				rparen_cnt++;
		}

		if (lparen_cnt != rparen_cnt)
			throw new ParenthesesMismatchException();

		i = 0;

		while (i < size) {
			if (i == 0) {
				arg0 = sArray.getToken(i++);

				if (arg0.getTokenSubtype().equals(TokenSubtype.Minus)) {
					TokenUnit zero = new TokenUnit(TokenType.Integer,
							TokenSubtype.Decimal, "0");

					pstu_root.setNode(arg0);
					pstu_root.setLeftLeapNode(zero);

					TokenArray subArray = new TokenArray();

					do {
						subArray.addToken(sArray.getToken(i++));
					} while (i < size);

					pstu_root.addRightSubtree(generateParseTree(subArray));
				} else if (arg0.getTokenSubtype().equals(
						TokenSubtype.Left_Parenthesis)) {
					TokenArray subArray1 = new TokenArray();

					int skipPairofParentheses = 0;

					while (true) {
						TokenUnit temp = new TokenUnit();
						temp = sArray.getToken(i);

						if (temp.getTokenSubtype().equals(
								TokenSubtype.Left_Parenthesis))
							skipPairofParentheses++;
						if (temp.getTokenSubtype().equals(
								TokenSubtype.Righ_Parenthesis))
							skipPairofParentheses--;

						if (skipPairofParentheses == -1) {
							i++;
							break;
						}

						subArray1.addToken(temp);
						i++;
					}

					if (i == size || i == size - 1) {
						if (subArray1.getSize() == 1) {
							pstu_root.setNode(subArray1.getToken(0));
						} else {
							pstu_root = generateParseTree(subArray1);
						}
					} else {
						op = sArray.getToken(i++);

						if (!op.getTokenType().equals(TokenType.Operatr))
							throw new InvalidSequenceTokenException(
									"Missing Operator");

						if (i == size)
							throw new InvalidSequenceTokenException(
									"Missing Operand.");

						arg1 = sArray.getToken(i++);

						pstu_root.setNode(op);
						pstu_root.addLeftSubtree(generateParseTree(subArray1));

						if (arg1.getTokenType().equals(TokenType.Integer)
								|| arg1.getTokenType()
										.equals(TokenType.FlPoint))
							pstu_root.setRightLeapNode(arg1);

						else if (arg1.getTokenType().equals(TokenType.Parents)
								&& arg1.getTokenSubtype().equals(
										TokenSubtype.Left_Parenthesis)) {
							TokenArray subArray2 = new TokenArray();
							skipPairofParentheses = 0;

							while (true) {
								TokenUnit temp = new TokenUnit();
								temp = sArray.getToken(i);

								if (temp.getTokenSubtype().equals(
										TokenSubtype.Left_Parenthesis))
									skipPairofParentheses++;
								if (temp.getTokenSubtype().equals(
										TokenSubtype.Righ_Parenthesis))
									skipPairofParentheses--;

								if (skipPairofParentheses == -1) {
									i++;
									break;
								}

								subArray2.addToken(temp);
								i++;
							}

							if (subArray2.getSize() == 1) {
								pstu_root.setRightLeapNode(subArray2
										.getToken(0));
							} else {
								pstu_root
										.addRightSubtree(generateParseTree(subArray2));
							}
						}
					}
				} else if (arg0.getTokenType().equals(TokenType.Integer)
						|| arg0.getTokenType().equals(TokenType.FlPoint)) {
					if (size == 1) {
						pstu_root.setNode(arg0);
						break;
					}

					op = sArray.getToken(i++);
					if (!op.getTokenType().equals(TokenType.Operatr))
						throw new InvalidSequenceTokenException(
								"Missing operator.");

					if (i == size
							&& !op.getTokenSubtype().equals(
									TokenSubtype.Factorial))
						throw new InvalidSequenceTokenException(
								"Missing operand.");

					if (op.getTokenSubtype().equals(TokenSubtype.Factorial)) {
						TokenUnit zero = new TokenUnit(TokenType.Integer,
								TokenSubtype.Decimal, "0");
						arg1 = zero;
					} else {
						arg1 = sArray.getToken(i++);
					}

					pstu_root.setNode(op);
					pstu_root.setLeftLeapNode(arg0);

					if (arg1.getTokenType().equals(TokenType.Integer)
							|| arg1.getTokenType().equals(TokenType.FlPoint))
						pstu_root.setRightLeapNode(arg1);
					else if (arg1.getTokenType().equals(TokenType.Parents)
							&& arg1.getTokenSubtype().equals(
									TokenSubtype.Left_Parenthesis)) {
						TokenArray subArray2 = new TokenArray();
						int skipPairofParentheses = 0;

						while (true) {
							TokenUnit temp = new TokenUnit();
							temp = sArray.getToken(i);

							if (temp.getTokenSubtype().equals(
									TokenSubtype.Left_Parenthesis))
								skipPairofParentheses++;
							if (temp.getTokenSubtype().equals(
									TokenSubtype.Righ_Parenthesis))
								skipPairofParentheses--;

							if (skipPairofParentheses == -1) {
								i++;
								break;
							}

							subArray2.addToken(temp);
							i++;
						}

						if (subArray2.getSize() == 1) {
							pstu_root.setRightLeapNode(subArray2.getToken(0));
						} else {
							pstu_root
									.addRightSubtree(generateParseTree(subArray2));
						}
					}
					// arg1.getTokenType().equals(TokenType.TriangleFunc)
					else if (arg1.getTokenType().equals(TokenType.TriangleFunc)
							|| arg1.getTokenType().equals(
									TokenType.MathematFunc)) {
						TokenUnit zero = new TokenUnit(TokenType.Integer,
								TokenSubtype.Decimal, "0");
						ParseTreeUnit newSubtree2 = new ParseTreeUnit();

						newSubtree2.setNode(arg1); // triangle function
						newSubtree2.setLeftLeapNode(zero);

						arg1 = sArray.getToken(i++);

						if (!arg1.getTokenType().equals(TokenType.Parents)) {
							throw new InvalidSequenceTokenException(
									"Left parenthesis is expected.");
						}

						TokenArray subArray2 = new TokenArray();
						int skipPairofParentheses = 0;

						while (true) {
							TokenUnit temp = new TokenUnit();
							temp = sArray.getToken(i);

							if (temp.getTokenSubtype().equals(
									TokenSubtype.Left_Parenthesis))
								skipPairofParentheses++;
							if (temp.getTokenSubtype().equals(
									TokenSubtype.Righ_Parenthesis))
								skipPairofParentheses--;

							if (skipPairofParentheses == -1) {
								i++;
								break;
							}

							subArray2.addToken(temp);
							i++;
						}

						if (subArray2.getSize() == 1) {
							newSubtree2.setRightLeapNode(subArray2.getToken(0));
						} else {
							newSubtree2
									.addRightSubtree(generateParseTree(subArray2));
						}

						pstu_root.addRightSubtree(newSubtree2);
					} else if (arg1.getTokenType().equals(
							TokenType.BaseConvFunc)) {
						throw new InvalidSequenceTokenException(
								"Base conversion function can be used for whole expression gets integer value.");
					} else {
						throw new InvalidSequenceTokenException(
								"Missing operand.");
					}
				} else if ((arg0.getTokenType().equals(TokenType.TriangleFunc) || arg0
						.getTokenType().equals(TokenType.BaseConvFunc))
						|| arg0.getTokenType().equals(TokenType.MathematFunc)) {
					pstu_root.setNode(arg0);
					TokenUnit zero = new TokenUnit(TokenType.Integer,
							TokenSubtype.Decimal, "0");
					pstu_root.setLeftLeapNode(zero);
					arg1 = sArray.getToken(i++);

					TokenArray subArray2 = new TokenArray();
					int skipPairofParentheses = 0;

					while (true) {
						TokenUnit temp = new TokenUnit();
						temp = sArray.getToken(i);

						if (temp.getTokenSubtype().equals(
								TokenSubtype.Left_Parenthesis))
							skipPairofParentheses++;
						if (temp.getTokenSubtype().equals(
								TokenSubtype.Righ_Parenthesis))
							skipPairofParentheses--;

						if (skipPairofParentheses == -1) {
							i++;
							break;
						}

						subArray2.addToken(temp);
						i++;
					}

					if (subArray2.getSize() == 1) {
						pstu_root.setRightLeapNode(subArray2.getToken(0));
					} else {
						pstu_root.addRightSubtree(generateParseTree(subArray2));
					}
				} else {
					throw new InvalidSequenceTokenException("Missing operand.");
				}
			}

			// if ( i != 0 )
			else {

				op = sArray.getToken(i++); // something operator
				if (!op.getTokenType().equals(TokenType.Operatr))
					throw new InvalidSequenceTokenException("Missing operator.");

				if (i == size
						&& !op.getTokenSubtype().equals(TokenSubtype.Factorial))
					throw new InvalidSequenceTokenException("Missing operand.");

				if (op.getTokenSubtype().equals(TokenSubtype.Factorial))
					arg1 = sArray.getToken(i++); // can be integer, floating
													// point, left parenthesis,
													// or function
				else {
					TokenUnit zero = new TokenUnit(TokenType.Integer,
							TokenSubtype.Decimal, "0");
					arg1 = zero;
				}

				if (pstu_root.getNode().getTokenType()
						.equals(TokenType.BaseConvFunc)) {
					throw new InvalidSequenceTokenException(
							"more tokens cannot be here.");
				}

				// The most priority
				if (pstu_root.getNode().getTokenSubtype()
						.equals(TokenSubtype.Factorial)) {
					ParseTreeUnit newSubtree = new ParseTreeUnit();

					newSubtree.setNode(op);
					newSubtree.addLeftSubtree(pstu_root);

					if (arg1.getTokenType().equals(TokenType.Integer)
							|| arg1.getTokenType().equals(TokenType.FlPoint)) {
						newSubtree.setRightLeapNode(arg1);
						pstu_root = newSubtree;
					} else if (arg1.getTokenType().equals(TokenType.Parents)
							&& arg1.getTokenSubtype().equals(
									TokenSubtype.Left_Parenthesis)) {
						TokenArray subArray2 = new TokenArray();
						int skipPairofParentheses = 0;

						while (true) {
							TokenUnit temp = new TokenUnit();
							temp = sArray.getToken(i);

							if (temp.getTokenSubtype().equals(
									TokenSubtype.Left_Parenthesis))
								skipPairofParentheses++;
							if (temp.getTokenSubtype().equals(
									TokenSubtype.Righ_Parenthesis))
								skipPairofParentheses--;

							if (skipPairofParentheses == -1) {
								i++;
								break;
							}

							subArray2.addToken(temp);
							i++;
						}

						if (subArray2.getSize() == 1) {
							newSubtree.setRightLeapNode(subArray2.getToken(0));
						} else {
							newSubtree
									.addRightSubtree(generateParseTree(subArray2));
						}

						pstu_root = newSubtree;
					}
				} else if (((pstu_root.getNode().getTokenSubtype() == TokenSubtype.Power || pstu_root
						.getNode().getTokenSubtype() == TokenSubtype.Modulus) || (pstu_root
						.getNode().getTokenSubtype() == TokenSubtype.Times || pstu_root
						.getNode().getTokenSubtype() == TokenSubtype.Divide))
						|| (pstu_root.getNode().getTokenType()
								.equals(TokenType.TriangleFunc) || pstu_root
								.getNode().getTokenType()
								.equals(TokenType.MathematFunc))) {
					ParseTreeUnit newSubtree = new ParseTreeUnit();

					if (op.getTokenSubtype() == TokenSubtype.Power
							|| op.getTokenSubtype() == TokenSubtype.Factorial) {
						newSubtree.setNode(op);
						newSubtree.addLeftSubtree(pstu_root.getRightSubtree());

						if (arg1.getTokenType().equals(TokenType.Integer)
								|| arg1.getTokenType()
										.equals(TokenType.FlPoint)) {
							newSubtree.setRightLeapNode(arg1);
							pstu_root.addRightSubtree(newSubtree);
						} else if (arg1.getTokenType()
								.equals(TokenType.Parents)
								&& arg1.getTokenSubtype().equals(
										TokenSubtype.Left_Parenthesis)) {
							TokenArray subArray2 = new TokenArray();
							int skipPairofParentheses = 0;

							while (true) {
								TokenUnit temp = new TokenUnit();
								temp = sArray.getToken(i);

								if (temp.getTokenSubtype().equals(
										TokenSubtype.Left_Parenthesis))
									skipPairofParentheses++;
								if (temp.getTokenSubtype().equals(
										TokenSubtype.Righ_Parenthesis))
									skipPairofParentheses--;

								if (skipPairofParentheses == -1) {
									i++;
									break;
								}

								subArray2.addToken(temp);
								i++;
							}

							if (subArray2.getSize() == 1) {
								newSubtree.setRightLeapNode(subArray2
										.getToken(0));
							} else {
								newSubtree
										.addRightSubtree(generateParseTree(subArray2));
							}
						} else if ((arg1.getTokenType().equals(
								TokenType.TriangleFunc) || arg1.getTokenType()
								.equals(TokenType.BaseConvFunc))
								|| arg1.getTokenType().equals(
										TokenType.MathematFunc)) {
							TokenUnit zero = new TokenUnit(TokenType.Integer,
									TokenSubtype.Decimal, "0");
							ParseTreeUnit newSubtree2 = new ParseTreeUnit();

							newSubtree2.setNode(arg1); // triangle function
							newSubtree2.setLeftLeapNode(zero);

							arg1 = sArray.getToken(i++);

							if (!arg1.getTokenType().equals(TokenType.Parents)) {
								throw new InvalidSequenceTokenException(
										"Left parenthesis is expected.");
							}

							TokenArray subArray2 = new TokenArray();
							int skipPairofParentheses = 0;

							while (true) {
								TokenUnit temp = new TokenUnit();
								temp = sArray.getToken(i);

								if (temp.getTokenSubtype().equals(
										TokenSubtype.Left_Parenthesis))
									skipPairofParentheses++;
								if (temp.getTokenSubtype().equals(
										TokenSubtype.Righ_Parenthesis))
									skipPairofParentheses--;

								if (skipPairofParentheses == -1) {
									i++;
									break;
								}

								subArray2.addToken(temp);
								i++;
							}

							if (subArray2.getSize() == 1) {
								newSubtree2.setRightLeapNode(subArray2
										.getToken(0));
							} else {
								newSubtree2
										.addRightSubtree(generateParseTree(subArray2));
							}

							newSubtree.addRightSubtree(newSubtree2);
						} else {
							throw new InvalidSequenceTokenException(
									"Missing operand.");
						}
					} else {
						newSubtree.setNode(op);
						newSubtree.addLeftSubtree(pstu_root);

						if (arg1.getTokenType().equals(TokenType.Integer)
								|| arg1.getTokenType()
										.equals(TokenType.FlPoint)) {
							newSubtree.setRightLeapNode(arg1);
							pstu_root = newSubtree;
						} else if (arg1.getTokenType()
								.equals(TokenType.Parents)
								|| arg1.getTokenSubtype().equals(
										TokenSubtype.Left_Parenthesis)) {
							TokenArray subArray2 = new TokenArray();
							int skipPairofParentheses = 0;

							while (true) {
								TokenUnit temp = new TokenUnit();
								temp = sArray.getToken(i);

								if (temp.getTokenSubtype().equals(
										TokenSubtype.Left_Parenthesis))
									skipPairofParentheses++;
								if (temp.getTokenSubtype().equals(
										TokenSubtype.Righ_Parenthesis))
									skipPairofParentheses--;

								if (skipPairofParentheses == -1) {
									i++;
									break;
								}

								subArray2.addToken(temp);
								i++;
							}

							if (subArray2.getSize() == 1) {
								newSubtree.setRightLeapNode(subArray2
										.getToken(0));
							} else {
								newSubtree
										.addRightSubtree(generateParseTree(subArray2));
							}

							pstu_root = newSubtree;
						} else if (arg1.getTokenType().equals(
								TokenType.TriangleFunc)
								|| arg1.getTokenType().equals(
										TokenType.MathematFunc)) {
							TokenUnit zero = new TokenUnit(TokenType.Integer,
									TokenSubtype.Decimal, "0");
							ParseTreeUnit newSubtree2 = new ParseTreeUnit();

							newSubtree2.setNode(arg1); // triangle function
							newSubtree2.setLeftLeapNode(zero);

							arg1 = sArray.getToken(i++);

							if (!arg1.getTokenType().equals(TokenType.Parents)) {
								throw new InvalidSequenceTokenException(
										"Left parenthesis is expected.");
							}

							TokenArray subArray2 = new TokenArray();
							int skipPairofParentheses = 0;

							while (true) {
								TokenUnit temp = new TokenUnit();
								temp = sArray.getToken(i);

								if (temp.getTokenSubtype().equals(
										TokenSubtype.Left_Parenthesis))
									skipPairofParentheses++;
								if (temp.getTokenSubtype().equals(
										TokenSubtype.Righ_Parenthesis))
									skipPairofParentheses--;

								if (skipPairofParentheses == -1) {
									i++;
									break;
								}

								subArray2.addToken(temp);
								i++;
							}

							if (subArray2.getSize() == 1) {
								newSubtree2.setRightLeapNode(subArray2
										.getToken(0));
							} else {
								newSubtree2
										.addRightSubtree(generateParseTree(subArray2));
							}

							newSubtree.addRightSubtree(newSubtree2);
							pstu_root = newSubtree;
						} else if (arg1.getTokenType().equals(
								TokenType.BaseConvFunc))
							throw new InvalidSequenceTokenException(
									"Base conversion function can be used for whole expression gets integer value.");
						else {
							throw new InvalidSequenceTokenException(
									"Missing operand.");
						}
					}
				}

				// The least priority
				else if (pstu_root.getNode().getTokenSubtype()
						.equals(TokenSubtype.Plus)
						|| pstu_root.getNode().getTokenSubtype()
								.equals(TokenSubtype.Minus)) {
					ParseTreeUnit newSubtree = new ParseTreeUnit();

					if ((op.getTokenSubtype().equals(TokenSubtype.Power) || op
							.getTokenSubtype().equals(TokenSubtype.Modulus))
							|| (op.getTokenSubtype().equals(TokenSubtype.Times) || op
									.getTokenSubtype().equals(
											TokenSubtype.Divide))
							|| op.getTokenSubtype().equals(
									TokenSubtype.Factorial)) {
						newSubtree.setNode(op);
						newSubtree.addLeftSubtree(pstu_root.getRightSubtree());

						if (arg1.getTokenType().equals(TokenType.Integer)
								|| arg1.getTokenType()
										.equals(TokenType.FlPoint)) {
							newSubtree.setRightLeapNode(arg1);
							pstu_root.addRightSubtree(newSubtree);
						} else if (arg1.getTokenType()
								.equals(TokenType.Parents)
								|| arg1.getTokenSubtype().equals(
										TokenSubtype.Left_Parenthesis)) {
							TokenArray subArray2 = new TokenArray();
							int skipPairofParentheses = 0;

							while (true) {
								TokenUnit temp = new TokenUnit();
								temp = sArray.getToken(i);

								if (temp.getTokenSubtype().equals(
										TokenSubtype.Left_Parenthesis))
									skipPairofParentheses++;
								if (temp.getTokenSubtype().equals(
										TokenSubtype.Righ_Parenthesis))
									skipPairofParentheses--;

								if (skipPairofParentheses == -1) {
									i++;
									break;
								}

								subArray2.addToken(temp);
								i++;
							}

							if (subArray2.getSize() == 1) {
								newSubtree.setRightLeapNode(subArray2
										.getToken(0));
							} else {
								newSubtree
										.addRightSubtree(generateParseTree(subArray2));
							}
						} else if (arg1.getTokenType().equals(
								TokenType.TriangleFunc)
								|| arg1.getTokenType().equals(
										TokenType.MathematFunc)) {
							TokenUnit zero = new TokenUnit(TokenType.Integer,
									TokenSubtype.Decimal, "0");
							ParseTreeUnit newSubtree2 = new ParseTreeUnit();

							newSubtree2.setNode(arg1); // triangle function
							newSubtree2.setLeftLeapNode(zero);

							arg1 = sArray.getToken(i++);

							if (!arg1.getTokenType().equals(TokenType.Parents)) {
								throw new InvalidSequenceTokenException(
										"Left parenthesis is expected.");
							}

							TokenArray subArray2 = new TokenArray();
							int skipPairofParentheses = 0;

							while (true) {
								TokenUnit temp = new TokenUnit();
								temp = sArray.getToken(i);

								if (temp.getTokenSubtype().equals(
										TokenSubtype.Left_Parenthesis))
									skipPairofParentheses++;
								if (temp.getTokenSubtype().equals(
										TokenSubtype.Righ_Parenthesis))
									skipPairofParentheses--;

								if (skipPairofParentheses == -1) {
									i++;
									break;
								}

								subArray2.addToken(temp);
								i++;
							}

							if (subArray2.getSize() == 1) {
								newSubtree2.setRightLeapNode(subArray2
										.getToken(0));
							} else {
								newSubtree2
										.addRightSubtree(generateParseTree(subArray2));
							}

							newSubtree.addRightSubtree(newSubtree2);
						} else if (arg1.getTokenType().equals(
								TokenType.BaseConvFunc)) {
							throw new InvalidSequenceTokenException(
									"Base conversion function can be used for whole expression gets integer value.");
						} else {
							throw new InvalidSequenceTokenException(
									"Missing operand.");
						}
					} else {
						newSubtree.setNode(op);
						newSubtree.addLeftSubtree(pstu_root);

						if (arg1.getTokenType().equals(TokenType.Integer)
								|| arg1.getTokenType()
										.equals(TokenType.FlPoint)) {
							newSubtree.setRightLeapNode(arg1);
							pstu_root = newSubtree;
						} else if (arg1.getTokenType()
								.equals(TokenType.Parents)
								|| arg1.getTokenSubtype().equals(
										TokenSubtype.Left_Parenthesis)) {
							TokenArray subArray2 = new TokenArray();
							int skipPairofParentheses = 0;

							while (true) {
								TokenUnit temp = new TokenUnit();
								temp = sArray.getToken(i);

								if (temp.getTokenSubtype().equals(
										TokenSubtype.Left_Parenthesis))
									skipPairofParentheses++;
								if (temp.getTokenSubtype().equals(
										TokenSubtype.Righ_Parenthesis))
									skipPairofParentheses--;

								if (skipPairofParentheses == -1) {
									i++;
									break;
								}

								subArray2.addToken(temp);
								i++;
							}

							if (subArray2.getSize() == 1) {
								newSubtree.setRightLeapNode(subArray2
										.getToken(0));
							} else {
								newSubtree
										.addRightSubtree(generateParseTree(subArray2));
							}
							pstu_root = newSubtree;
						} else if (arg1.getTokenType().equals(
								TokenType.TriangleFunc)
								|| arg1.getTokenType().equals(
										TokenType.MathematFunc)) {
							TokenUnit zero = new TokenUnit(TokenType.Integer,
									TokenSubtype.Decimal, "0");
							ParseTreeUnit newSubtree2 = new ParseTreeUnit();

							newSubtree2.setNode(arg1); // triangle function
							newSubtree2.setLeftLeapNode(zero);

							arg1 = sArray.getToken(i++);

							if (!arg1.getTokenType().equals(TokenType.Parents)) {
								throw new InvalidSequenceTokenException(
										"Left parenthesis is expected.");
							}

							TokenArray subArray2 = new TokenArray();
							int skipPairofParentheses = 0;

							while (true) {
								TokenUnit temp = new TokenUnit();
								temp = sArray.getToken(i);

								if (temp.getTokenSubtype().equals(
										TokenSubtype.Left_Parenthesis))
									skipPairofParentheses++;
								if (temp.getTokenSubtype().equals(
										TokenSubtype.Righ_Parenthesis))
									skipPairofParentheses--;

								if (skipPairofParentheses == -1) {
									i++;
									break;
								}

								subArray2.addToken(temp);
								i++;
							}

							if (subArray2.getSize() == 1) {
								newSubtree2.setRightLeapNode(subArray2
										.getToken(0));
							} else {
								newSubtree2
										.addRightSubtree(generateParseTree(subArray2));
							}

							newSubtree.addRightSubtree(newSubtree2);
							pstu_root = newSubtree;
						} else if (arg1.getTokenType().equals(
								TokenType.BaseConvFunc)) {
							throw new InvalidSequenceTokenException(
									"Base conversion function can be used for whole expression gets integer value.");
						} else {
							throw new InvalidSequenceTokenException(
									"Missing operand.");
						}
					}
				}
			}
		}

		return pstu_root;
	}
}
