package org.manalith.ircbot.plugin.curex;

import java.util.ArrayList;

import org.manalith.ircbot.plugin.curex.TokenObject.TokenType;
import org.manalith.ircbot.plugin.curex.exceptions.IllegalArgumentException;
import org.manalith.ircbot.plugin.curex.exceptions.MissingArgumentException;

public class CommandParser {

	private String[] opt;
	private ArrayList<TokenObject> intArr;

	public CommandParser(String[] options) {
		opt = new String[options.length];
		for (int i = 0; i < options.length; i++)
			opt[i] = options[i];

		intArr = new ArrayList<TokenObject>();
	}

	public ArrayList<TokenObject> parse() throws Exception {

		ArrayList<TokenObject> result = new ArrayList<TokenObject>();

		for (String str : opt)
			intArr.add(new TokenObject(str));

		switch (intArr.size()) { // number of arguments
		case 0:
			result.add(new TokenObject("show"));
			break;
		case 1:
			switch (intArr.get(0).getTokenType().value()) {
			case 0: // Command
				switch (intArr.get(0).getTokenSubtype().value()) {
				case 0:
				case 1:
				case 3:
				case 6: // show, help, unitlist, flush
					result.add(intArr.get(0));
					break;
				case 2:
				case 4:
				case 5:// calc, add, remove
					throw new MissingArgumentException(1);
				case 10: // UnDefined
					throw new IllegalArgumentException(1);
				}
				break;
			case 1: // Unit
				result.add(new TokenObject("show"));
				result.add(intArr.get(0));
				break;
			case 2: // Number
				result.add(new TokenObject("calc"));
				result.add(intArr.get(0));
				result.add(new TokenObject("USD"));
				result.add(new TokenObject("KRW"));
				break;
			case 3: // Unknown
				throw new IllegalArgumentException(1);
			}
			break;
		case 2:
			switch (intArr.get(0).getTokenType().value()) {
			case 0: // Command
				switch (intArr.get(0).getTokenSubtype().value()) {
				case 0: // show
					if (!intArr.get(1).getTokenType().equals(TokenType.Unit))
						throw new IllegalArgumentException(2);
					else {
						result.add(intArr.get(0));
						result.add(intArr.get(1));
					}
					break;
				case 1:
				case 3:
				case 6:
				case 10: // help, unitlist, flush, Undefined
					throw new IllegalArgumentException(2);
				case 2: // calc
					if (intArr.get(1).getTokenType().equals(TokenType.Unit)) {
						result.add(intArr.get(0));
						result.add(new TokenObject("1.0"));
						result.add(intArr.get(1));
						result.add(new TokenObject("KRW"));
					} else if (intArr.get(1).getTokenType()
							.equals(TokenType.Number)) {
						result.add(intArr.get(0));
						result.add(intArr.get(1));
						result.add(new TokenObject("USD"));
						result.add(new TokenObject("KRW"));
					} else
						throw new IllegalArgumentException(2);
				case 4:
				case 5: // add, remove
					if (!intArr.get(1).getTokenType().equals(TokenType.Unit))
						throw new IllegalArgumentException(2);
					else {
						result.add(intArr.get(0));
						result.add(intArr.get(1));
					}
					break;
				}
				break;
			case 1: // Unit
				if (!intArr.get(1).getTokenType().equals(TokenType.Unit))
					throw new IllegalArgumentException(2);
				else {
					result.add(new TokenObject("calc"));
					result.add(new TokenObject("1.0"));
					result.add(intArr.get(0));
					result.add(intArr.get(1));
				}
				break;
			case 2: // Number
				if (!intArr.get(1).getTokenType().equals(TokenType.Unit))
					throw new IllegalArgumentException(2);
				else {
					result.add(new TokenObject("calc"));
					result.add(intArr.get(0));
					result.add(intArr.get(1));
					result.add(new TokenObject("KRW"));
				}
				break;
			case 3:
				throw new IllegalArgumentException(2);
			}
			break;
		case 3:
			switch (intArr.get(0).getTokenType().value()) {
			case 0: // command
				switch (intArr.get(0).getTokenSubtype().value()) {
				case 0:
				case 1:
				case 3:
				case 6:
				case 10:
					// show, help, unitlist, flush, unknown
					throw new IllegalArgumentException(1);
				case 2: // calc
					if (!intArr.get(1).getTokenType().equals(TokenType.Number)
							&& !intArr.get(1).getTokenType()
									.equals(TokenType.Unit))
						throw new IllegalArgumentException(2);
					if (!intArr.get(2).getTokenType().equals(TokenType.Unit))
						throw new IllegalArgumentException(3);

					result.add(intArr.get(0));

					if (intArr.get(1).getTokenType().equals(TokenType.Number)) {
						result.add(intArr.get(1));
						result.add(intArr.get(2));
						result.add(new TokenObject("KRW"));
					} else if (intArr.get(1).getTokenType()
							.equals(TokenType.Unit)) {
						result.add(new TokenObject("1.0"));
						result.add(intArr.get(1));
						result.add(intArr.get(2));
					}
					break;
				case 4:
				case 5: // add, remove
					if (!intArr.get(1).getTokenType().equals(TokenType.Unit))
						throw new IllegalArgumentException(2);
					if (!intArr.get(2).getTokenType().equals(TokenType.Unit))
						throw new IllegalArgumentException(3);

					result.add(intArr.get(0));
					result.add(intArr.get(1));
					result.add(intArr.get(2));
					break;
				}
				break;
			case 1:
			case 3: // unit, undefined
				throw new IllegalArgumentException(3);
			case 2: // number
				if (!intArr.get(1).getTokenType().equals(TokenType.Unit))
					throw new IllegalArgumentException(2);
				if (!intArr.get(2).getTokenType().equals(TokenType.Unit))
					throw new IllegalArgumentException(3);

				result.add(new TokenObject("calc"));
				result.add(intArr.get(0));
				result.add(intArr.get(1));
				result.add(intArr.get(2));
				break;
			}
			break;
		}

		return result;
	}

}
