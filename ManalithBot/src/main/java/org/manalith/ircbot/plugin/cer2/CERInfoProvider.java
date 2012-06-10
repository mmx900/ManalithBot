/*
 	org.manalith.ircbot.plugin.cer2/CERInfoProvider.java
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
package org.manalith.ircbot.plugin.cer2;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.math.NumberUtils;
import org.manalith.ircbot.common.PropertyManager;
import org.manalith.ircbot.plugin.cer2.exceptions.EmptyTokenStreamException;
import org.manalith.ircbot.plugin.cer2.exceptions.InvalidArgumentException;

public class CERInfoProvider {

	private String[] args;
	private String path;

	private Options optionList;
	private CommandLineParser argParser;

	/**
	 * 3 types of Constructors
	 */
	public CERInfoProvider() {
		this.setArgs(null);
		this.setPath("");
		this.initArgumentParser();
	}

	public CERInfoProvider(String[] newArgs) {
		this.setArgs(newArgs);
		this.setPath("");
		this.initArgumentParser();
	}

	public CERInfoProvider(String newPath, String[] newArgs) {
		this.setArgs(newArgs);
		this.setPath(newPath);
		this.initArgumentParser();
	}

	/**
	 * set array into args
	 * 
	 * @param newArgs
	 */
	public void setArgs(String[] newArgs) {
		this.args = newArgs;
	}

	/**
	 * return argument as array of string
	 * 
	 * @return string[] args
	 */
	private String[] getArgs() {
		return this.args;
	}

	/**
	 * set this plugin's data path
	 * 
	 * @param newPath
	 */
	public void setPath(String newPath) {
		this.path = newPath;
	}

	/**
	 * return this plugin's data path as a string
	 * 
	 * @return String path.
	 */
	private String getPath() {
		return this.path;
	}

	/**
	 * Initiate org.apache.commons.cli based argument parser
	 */
	private void initArgumentParser() {
		optionList = new Options();

		Option help = new Option("help", "show help messages");
		Option lastround = new Option("lastround",
				"show latest updated date and time");
		Option currencyunit = new Option("unitlist",
				"show the list of a currency unit");

		Option show = new Option("show",
				"show the value on the currency exchance table.");
		show.setArgs(2);

		Option conv = new Option("conv",
				"compute the value for the selected currency unit.");
		conv.setArgs(3);

		Option buycash = new Option("buycash",
				"show the price when we buy the cache.");
		buycash.setArgs(2);

		Option cellcash = new Option("cellcash",
				"show the price when we cell the cacle.");
		cellcash.setArgs(2);

		Option recvremit = new Option("recvremit",
				"show the price when we receive the remittance.");
		recvremit.setArgs(2);

		Option sendremit = new Option("sendremit",
				"show the price when we send the remittance");
		sendremit.setArgs(2);

		optionList.addOption(help);
		optionList.addOption(lastround);
		optionList.addOption(currencyunit);
		optionList.addOption(show);
		optionList.addOption(conv);
		optionList.addOption(buycash);
		optionList.addOption(cellcash);
		optionList.addOption(recvremit);
		optionList.addOption(sendremit);

		argParser = new GnuParser();
	}

	/**
	 * Token Lexer
	 * 
	 * @param TokenString
	 * @return TokenType
	 */
	public TokenType getTokenType(String TokenString) {
		TokenType result = TokenType.Unknown;

		Pattern cmd_pattern = Pattern.compile("\\-[a-z]+");
		Pattern currency_pattern = Pattern.compile("[A-Z]{3}");
		Pattern fieldabbr_pattern = Pattern.compile("[a-z]{2,3}");
		Pattern amount_pattern = Pattern
				.compile("[0-9]{1,3}((\\,)?[0-9]{3})*(.[0-9]{1,2})?");

		Matcher cmd_match = cmd_pattern.matcher(TokenString);
		Matcher currency_match = currency_pattern.matcher(TokenString);
		Matcher fieldabbr_match = fieldabbr_pattern.matcher(TokenString);
		Matcher amount_match = amount_pattern.matcher(TokenString);

		if (cmd_match.matches()) {
			result = TokenType.Command;
		} else if (currency_match.matches()) {
			result = TokenType.CurrencyUnit;
		} else if (fieldabbr_match.matches()) {
			result = TokenType.FieldAbbr;
		} else if (amount_match.matches()) {
			result = TokenType.Amount;
		}

		return result;
	}

	/**
	 * Token Lexer
	 * 
	 * @param TokenString
	 * @param CurrentType
	 * @return TokenSubtype
	 */
	public TokenSubtype getTokenSubtype(String TokenString,
			TokenType CurrentType) {
		TokenSubtype result = TokenSubtype.Unknown;

		if (CurrentType == TokenType.Command) {
			String cmd = TokenString.substring(1, TokenString.length());
			if (cmd.equals("help")) {
				result = TokenSubtype.CommandHelp;
			} else if (cmd.equals("show")) {
				result = TokenSubtype.CommandShow;
			} else if (cmd.equals("lastround")) {
				result = TokenSubtype.CommandLastRound;
			} else if (cmd.equals("unitlist")) {
				result = TokenSubtype.CommandUnitList;
			} else if (cmd.equals("conv")) {
				result = TokenSubtype.CommandConvert;
			} else if (cmd.equals("buycash")) {
				result = TokenSubtype.CommandBuyCash;
			} else if (cmd.equals("cellcash")) {
				result = TokenSubtype.CommandCellCash;
			} else if (cmd.equals("sendremit")) {
				result = TokenSubtype.CommandSendRemit;
			} else if (cmd.equals("recvremit")) {
				result = TokenSubtype.CommandRecvRemit;
			} else {
				result = TokenSubtype.Unknown;
			}
		} else if (CurrentType == TokenType.CurrencyUnit) {
			try {
				result = TokenSubtype.valueOf("Currency" + TokenString);
			} catch (IllegalArgumentException e) {
				result = TokenSubtype.Unknown;
			}
		} else if (CurrentType == TokenType.FieldAbbr) {
			if (TokenString.equals("all")) {
				result = TokenSubtype.FAAll;
			} else if (TokenString.equals("cr")) {
				result = TokenSubtype.FACentralRate;
			} else if (TokenString.equals("cb")) {
				result = TokenSubtype.FABuyCash;
			} else if (TokenString.equals("cc")) {
				result = TokenSubtype.FACellCash;
			} else if (TokenString.equals("rs")) {
				result = TokenSubtype.FASendRemit;
			} else if (TokenString.equals("rr")) {
				result = TokenSubtype.FARecvRemit;
			} else if (TokenString.equals("de")) {
				result = TokenSubtype.FADollarExcRate;
			} else {
				result = TokenSubtype.Unknown;
			}
		} else if (CurrentType == TokenType.Amount) {
			Pattern natural_pattern = Pattern
					.compile("[0-9]{1,3}((\\,)?[0-9]{3})*");
			Pattern fp_pattern = Pattern
					.compile("[0-9]{1,3}((\\,)?[0-9]{3})*\\.[0-9]{1,2}");

			Matcher natural_match = natural_pattern.matcher(TokenString);
			Matcher fp_match = fp_pattern.matcher(TokenString);

			if (natural_match.matches())
				result = TokenSubtype.AmountNatural;
			else if (fp_match.matches())
				result = TokenSubtype.AmountFp;
		}

		return result;
	}

	/**
	 * Argument re-arranger
	 * 
	 * @return arranged argument array
	 * @throws InvalidArgumentException
	 */
	public String[] rearrangeArgs() throws InvalidArgumentException {
		String[] result = null;

		TokenSubtype ts = this.getTokenSubtype(this.getArgs()[0],
				TokenType.Command);

		if (ts == TokenSubtype.CommandHelp
				|| ts == TokenSubtype.CommandLastRound
				|| ts == TokenSubtype.CommandUnitList) {
			result = new String[1];
			result[0] = this.getArgs()[0];
		} else if (ts == TokenSubtype.CommandBuyCash
				|| ts == TokenSubtype.CommandCellCash
				|| ts == TokenSubtype.CommandRecvRemit
				|| ts == TokenSubtype.CommandSendRemit) {
			result = new String[3];
			result[0] = this.getArgs()[0];

			switch (this.getArgs().length) {
			case 1:
				result[1] = "1";
				result[2] = "USD";
				break;
			case 2:
				if (this.getTokenSubtype(this.getArgs()[1],
						TokenType.CurrencyUnit) != TokenSubtype.Unknown) {
					result[1] = "1";
					result[2] = this.getArgs()[1];
				} else if (this.getTokenType(this.getArgs()[1]) == TokenType.Amount) {
					result[1] = this.getArgs()[1];
					result[2] = "USD";
				} else
					throw new InvalidArgumentException(this.getArgs()[1]);
				break;
			case 3:
				// first arg.
				if (this.getTokenSubtype(this.getArgs()[1], TokenType.Amount) != TokenSubtype.Unknown)
					result[1] = this.getArgs()[1];
				else
					throw new InvalidArgumentException(this.getArgs()[1]);

				// second arg
				if (this.getTokenSubtype(this.getArgs()[2],
						TokenType.CurrencyUnit) != TokenSubtype.Unknown)
					result[2] = this.getArgs()[2];
				else
					throw new InvalidArgumentException(this.getArgs()[2]);

				break;
			default:
				throw new InvalidArgumentException("불 필요한 인자");
			}
		} else if (ts == TokenSubtype.CommandShow) {
			switch (this.getArgs().length) {
			case 1:
				result = new String[3];
				result[0] = this.getArgs()[0];
				result[1] = "USD";
				result[2] = "cr";
				break;
			case 2:
				if (this.getTokenSubtype(this.getArgs()[1],
						TokenType.CurrencyUnit) != TokenSubtype.Unknown) {
					result = new String[3];
					System.arraycopy(this.getArgs(), 0, result, 0,
							this.getArgs().length);
					result[2] = "cr";
				} else {
					throw new InvalidArgumentException(this.getArgs()[1]);
				}
				break;
			case 3:
				if (this.getTokenSubtype(this.getArgs()[1],
						TokenType.CurrencyUnit) != TokenSubtype.Unknown
						&& this.getTokenSubtype(this.getArgs()[2],
								TokenType.FieldAbbr) != TokenSubtype.Unknown) {
					result = new String[this.getArgs().length];
					System.arraycopy(this.getArgs(), 0, result, 0,
							this.getArgs().length);
				} else
					throw new InvalidArgumentException(this.getArgs()[1]
							+ " and " + this.getArgs()[2]);
				break;
			default:
				throw new InvalidArgumentException("불 필요한 인자");
			}

		} else if (ts == TokenSubtype.CommandConvert) {
			switch (this.getArgs().length) {
			case 1:
				throw new InvalidArgumentException("필요한 옵션 빠짐");
			case 2:
				if (this.getTokenSubtype(this.getArgs()[1], TokenType.Amount) != TokenSubtype.Unknown) {
					result = new String[4];
					System.arraycopy(this.getArgs(), 0, result, 0, 2);
					result[2] = "USD";
					result[3] = "KRW";
				} else
					throw new InvalidArgumentException(this.getArgs()[1]);

				break;
			case 3:
				if (this.getTokenSubtype(this.getArgs()[1],
						TokenType.CurrencyUnit) != TokenSubtype.Unknown
						&& this.getTokenSubtype(this.getArgs()[2],
								TokenType.CurrencyUnit) != TokenSubtype.Unknown) {
					result = new String[4];
					result[0] = this.getArgs()[0];
					result[1] = "1";
					System.arraycopy(this.getArgs(), 1, result, 2, 2);
				} else if (this.getTokenSubtype(this.getArgs()[1],
						TokenType.Amount) != TokenSubtype.Unknown
						&& this.getTokenSubtype(this.getArgs()[2],
								TokenType.CurrencyUnit) != TokenSubtype.Unknown) {
					result = new String[4];
					System.arraycopy(this.getArgs(), 0, result, 0,
							this.getArgs().length);
					result[3] = "KRW";
				} else
					throw new InvalidArgumentException(this.getArgs()[1]
							+ " and " + this.getArgs()[2]);
				break;
			case 4:
				if (this.getTokenSubtype(this.getArgs()[1], TokenType.Amount) != TokenSubtype.Unknown
						&& this.getTokenSubtype(this.getArgs()[2],
								TokenType.CurrencyUnit) != TokenSubtype.Unknown
						&& this.getTokenSubtype(this.getArgs()[3],
								TokenType.CurrencyUnit) != TokenSubtype.Unknown) {
					result = new String[4];
					System.arraycopy(this.getArgs(), 0, result, 0,
							this.getArgs().length);
				} else
					throw new InvalidArgumentException(this.getArgs()[1] + ", "
							+ this.getArgs()[2] + " and " + this.getArgs()[3]);
				break;
			default:
				throw new InvalidArgumentException("불 필요한 인자");
			}
		} else {
			switch (this.getArgs().length) {
			case 1:
				if (this.getTokenSubtype(this.getArgs()[0],
						TokenType.CurrencyUnit) != TokenSubtype.Unknown) {
					result = new String[3];
					result[0] = "-show";
					result[1] = this.getArgs()[0];
					result[2] = "cr";
				} else if (this.getTokenSubtype(this.getArgs()[0],
						TokenType.Amount) != TokenSubtype.Unknown) {
					result = new String[4];
					result[0] = "-conv";
					result[1] = this.getArgs()[0];
					result[2] = "USD";
					result[3] = "KRW";
				} else {
					throw new InvalidArgumentException(this.getArgs()[0]);
				}
				break;
			case 2:
				if (this.getTokenSubtype(this.getArgs()[0],
						TokenType.CurrencyUnit) != TokenSubtype.Unknown
						&& this.getTokenSubtype(this.getArgs()[1],
								TokenType.FieldAbbr) != TokenSubtype.Unknown) {
					result = new String[3];
					result[0] = "-show";
					System.arraycopy(this.getArgs(), 0, result, 1, 2);
				} else if (this.getTokenSubtype(this.getArgs()[0],
						TokenType.CurrencyUnit) != TokenSubtype.Unknown
						&& this.getTokenSubtype(this.getArgs()[1],
								TokenType.CurrencyUnit) != TokenSubtype.Unknown) {
					result = new String[4];
					result[0] = "-conv";
					result[1] = "1";
					System.arraycopy(this.getArgs(), 0, result, 2, 2);
				} else if (this.getTokenSubtype(this.getArgs()[0],
						TokenType.Amount) != TokenSubtype.Unknown
						&& this.getTokenSubtype(this.getArgs()[1],
								TokenType.CurrencyUnit) != TokenSubtype.Unknown) {
					result = new String[4];
					result[0] = "-conv";
					System.arraycopy(this.getArgs(), 0, result, 1, 2);
					result[3] = "KRW";
				} else {
					throw new InvalidArgumentException(this.getArgs()[0]
							+ " and " + this.getArgs()[1]);
				}
				break;
			case 3:
				if (this.getTokenSubtype(this.getArgs()[0], TokenType.Amount) != TokenSubtype.Unknown
						&& this.getTokenSubtype(this.getArgs()[1],
								TokenType.CurrencyUnit) != TokenSubtype.Unknown
						&& this.getTokenSubtype(this.getArgs()[2],
								TokenType.CurrencyUnit) != TokenSubtype.Unknown) {
					result = new String[4];
					result[0] = "-conv";
					System.arraycopy(this.getArgs(), 0, result, 1, 3);
				}
				break;
			default:
				throw new InvalidArgumentException("불 필요한 인자");

			}

		}

		return result;
	}

	/**
	 * Analyze arguments, process for the request, and return result.
	 * 
	 * @return result message as string
	 * @throws EmptyTokenStreamException
	 * @throws InvalidArgumentException
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public String commandInterpreter() throws EmptyTokenStreamException,
			InvalidArgumentException, IOException, SQLException,
			ParseException, ClassNotFoundException {
		String result = "";
		TokenSubtype st;

		// Argument preprocessor
		String[] aargs = this.rearrangeArgs();
		// Parse!
		CommandLine cl = argParser.parse(optionList, aargs);

		if (cl.hasOption("help"))
			result = "Help!";
		else if (cl.hasOption("lastround"))
			result = this.showLatestRound();
		else if (cl.hasOption("unitlist"))
			result = "unitlist";
		else if (cl.hasOption("show")) {
			String[] args = cl.getOptionValues(aargs[0].substring(1));

			String currencyUnit;
			if (this.getTokenSubtype(args[0], TokenType.CurrencyUnit) != TokenSubtype.Unknown)
				currencyUnit = args[0];
			else
				throw new InvalidArgumentException("통화 단위 아님");

			StringBuilder fieldName = new StringBuilder();
			fieldName.append("country_name,");

			if (this.getTokenSubtype(args[1], TokenType.FieldAbbr) == TokenSubtype.FAAll) {
				fieldName.delete(0, fieldName.length());
				fieldName.append("*");
			} else if (this.getTokenSubtype(args[1], TokenType.FieldAbbr) == TokenSubtype.FACentralRate)
				fieldName.append("currency_unit,central_rate");
			else if (this.getTokenSubtype(args[1], TokenType.FieldAbbr) == TokenSubtype.FABuyCash)
				fieldName.append("currency_unit,cash_buy");
			else if (this.getTokenSubtype(args[1], TokenType.FieldAbbr) == TokenSubtype.FACellCash)
				fieldName.append("currency_unit,cash_cell");
			else if (this.getTokenSubtype(args[1], TokenType.FieldAbbr) == TokenSubtype.FARecvRemit)
				fieldName.append("currency_unit,remittance_recv");
			else if (this.getTokenSubtype(args[1], TokenType.FieldAbbr) == TokenSubtype.FASendRemit)
				fieldName.append("currency_unit,remittance_send");
			else if (this.getTokenSubtype(args[1], TokenType.FieldAbbr) == TokenSubtype.FAECRate)
				fieldName.append("currency_unit,exchan_comm_rate");
			else if (this.getTokenSubtype(args[1], TokenType.FieldAbbr) == TokenSubtype.FADollarExcRate)
				fieldName.append("currency_unit,dollar_exc_rate");
			else
				throw new InvalidArgumentException("알 수 없는 필드 [ " + args[1]
						+ " ] ");

			result = this.showCurrencyRate(currencyUnit, fieldName.toString());
		} else if (cl.hasOption("conv")) {
			String[] args = cl.getOptionValues(aargs[0].substring(1));
			String value;
			st = this.getTokenSubtype(args[0], TokenType.Amount);

			// argument validation
			if (st != TokenSubtype.Unknown) {
				if (st == TokenSubtype.AmountNatural)
					value = args[0] + ".0";
				else
					value = args[0];
			} else
				throw new InvalidArgumentException("금액 아님");
			st = this.getTokenSubtype(args[1], TokenType.CurrencyUnit);
			if (st == TokenSubtype.Unknown)
				throw new InvalidArgumentException("화폐 단위 아님");
			st = this.getTokenSubtype(args[2], TokenType.CurrencyUnit);
			if (st == TokenSubtype.Unknown)
				throw new InvalidArgumentException("화폐 단위 아님");

			result = this.convert(value, args[1], args[2]);
		} else if (cl.hasOption("buycash") || cl.hasOption("cellcash")
				|| cl.hasOption("recvremit") || cl.hasOption("sendremit")) {
			String[] args = cl.getOptionValues(aargs[0].substring(1));
			String currencyUnit;
			String value;

			st = this.getTokenSubtype(args[0], TokenType.Amount);
			if (st != TokenSubtype.Unknown) {
				if (st == TokenSubtype.AmountNatural)
					value = args[0] + ".0";
				else
					value = args[0];
			} else
				throw new InvalidArgumentException("금액 아님");
			st = this.getTokenSubtype(args[1], TokenType.CurrencyUnit);
			if (st == TokenSubtype.Unknown)
				throw new InvalidArgumentException("화폐 단위 아님");

			currencyUnit = args[1];

			if (cl.hasOption("buycash"))
				result = this.buyCash(currencyUnit, value);
			else if (cl.hasOption("cellcash"))
				result = this.cellCash(currencyUnit, value);
			else if (cl.hasOption("recvremit"))
				result = this.recvRemittance(currencyUnit, value);
			else if (cl.hasOption("sendremit"))
				result = this.sendRemittance(currencyUnit, value);
		}

		return result;
	}

	public static String getUnitListPart1() {
		String result = "[Currency_Unit] : USD:U.S, EUR:Europe, JPY:Japan, CNY:China, HKD:HongKong, TWD:Taiwan, GBP:Great Britain, CAD:Canada, CHF:Swiss, SEK:Sweden, ";
		result += "AUD:Australia, NZD:NewZealand, ISL:Israel, DKK:Denmark, NOK:Norway, SAR:Saudi Arabia, KWD:Kuweit, BHD:Bahrain, AED:UAE, JOD:Jordan, ";
		return result;
	}

	public static String getUnitListPart2() {
		String result = "EGP:Egypt, THB:Thailand, SGD:Singapore, MYR:Malaysia, IDR:Indonesia, BND:Brunei, INR:India, PKR:Pakistan, BDT:Bangladesh, PHP:Philippine, MXN:Mexico, ";
		result += " BRL:Brazil, VND:Vietnam, ZAR:Republic of South Africa, RUB:Russia, HUF:Hungary, PLN:Poland";
		return result;
	}

	public static String getIRCHelpMessagePart1() {
		String result = "!(환율|curex) ( help | lastround | unitlist ) | !(환율|curex) ( buycash | cellcash | sendremit | recvremit ) [Amount] [Currency_Unit] | !(환율|curex) ( show | \"\" ) [Currency_Unit] [FieldAbbr]";
		result += " | !(환율|curex) ( conv | \"\" ) ( [Amount] : USD to KRW | [Amount] [Currency_Unit(1)] [Current_Unit(2)] | [Currency_Unit(1)] [Current_Unit(2)] : | [Amount] [Currency_Unit(2)] )";
		return result;
	}

	public static String getIRCHelpMessagePart2() {
		String result = "help: 도움말, lastround: 최종갱신회차, buycash: 현찰매수, cellcash: 현찰매도, sendremit: 송금보냄, recvremit: 송금받음, show: 환율보이기, conv: 환율계산, unitlist: 화폐단위";
		result += "[FieldAbbr] (show 명령에만 해당) 모두보기, 매매기준, 현찰매수, 현찰매도, 송금보냄, 송금받음, 환수수료, 대미환산 ";
		result += "[Amount] 금액 [Currency_Unit] 화폐단위 [Current_Unit(1)] 대상 [Current_Unit(2)] 목적";

		return result;
	}

	private String showLatestRound() throws IOException {
		StringBuilder result = new StringBuilder();
		String[] month = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
				"Aug", "Sep", "Oct", "Nov", "Dec" };

		PropertyManager pm = new PropertyManager(this.getPath(),
				"LatestUpdatedDatetime.prop");
		pm.loadProperties();

		String dt = pm.getValue("date");
		String roundstr = pm.getValue("round");

		String[] datetime = dt.split("\\s");
		String[] date = datetime[0].split("\\.");

		result.append("Last Updated : ");
		result.append(month[NumberUtils.toInt(date[1]) - 1]);
		result.append(" ");
		result.append(date[2]);
		result.append(", ");
		result.append(date[0]);
		result.append("  ");
		result.append(datetime[1]);
		result.append(", ");
		result.append(NumberUtils.toInt(roundstr));
		int len = roundstr.length();
		char ch = roundstr.charAt(len - 1);
		switch (ch) {
		case '1':
			result.append("st round.");
			break;
		case '2':
			result.append("nd round.");
			break;
		case '3':
			result.append("rd round.");
			break;
		default:
			result.append("th round.");

		}
		return result.toString();
	}

	private String showCurrencyRate(String CurrencyUnit, String FieldId)
			throws SQLException, ClassNotFoundException {
		StringBuilder result = new StringBuilder();

		HSQLDBTableManager sqlManager = new HSQLDBTableManager(this.getPath(),
				"currency.db");
		String[] data = sqlManager.selectDataFromTable(FieldId.toString(),
				CurrencyUnit.toString());
		if (FieldId.equals("*"))
			FieldId = "country_name,currency_unit,central_rate,cash_buy,cash_cell,remittance_send,remittance_recv,exchan_comm_rate,dollar_exc_rate";
		String[] separatedfield = FieldId.split("\\,");

		for (int j = 2; j < separatedfield.length; j++) {
			if (j == 2) {
				result.append(data[0] + "(");
			} else {
				result.append(", ");
				result.append(data[0]);
				result.append("(");
			}

			if (NumberUtils.toInt(data[1]) == 100) {
				result.append(data[1]);
				result.append(" ");
			}

			result.append(CurrencyUnit);
			result.append(")에 대한 ");

			if (separatedfield[j].equals("exchan_comm_rate")) {
				if (data[j].equals("0.0")) {
					result.append("환전수수료율을 가져올 수 없습니다. (존재하지 않음)");
				} else {
					result.append("환전수수료율 : " + data[j] + "%");
				}
			} else if (separatedfield[j].equals("dollar_exc_rate")) {
				result.append("대미환산율 : ");
				result.append(String.format("%.3f",
						(NumberUtils.toDouble(data[j]) * 100.0)));
				result.append("%");
			} else {
				if (separatedfield[j].equals("central_rate"))
					result.append("거래기준액");
				else if (separatedfield[j].equals("cash_buy"))
					result.append("현금매수액");
				else if (separatedfield[j].equals("cash_cell"))
					result.append("현금매도액");
				else if (separatedfield[j].equals("remittance_send"))
					result.append("송금수신액");
				else if (separatedfield[j].equals("remittance_recv"))
					result.append("송금전송액");

				if (data[j].equals("0.0")) {
					result.append(" : (거래불가) ");
				} else {
					result.append(" : ￦");
					result.append(data[j]);
				}
			}

		}
		return result.toString();
	}

	private String convert(String value, String CurrencyUnit1,
			String CurrencyUnit2) throws SQLException, ClassNotFoundException {
		String result = "";

		if (CurrencyUnit1.equals(CurrencyUnit2)) {
			result = value + CurrencyUnit1 + " => " + value + " "
					+ CurrencyUnit2;
			return result;
		}

		HSQLDBTableManager sqlman = new HSQLDBTableManager(this.getPath(),
				"currency.db");
		String field = "country_name,currency_unit,central_rate";
		String resval = "";

		if (CurrencyUnit1.equals("KRW")) {
			String[] data = sqlman.selectDataFromTable(field, CurrencyUnit2);
			sqlman.close();

			double currency_unit = NumberUtils.toDouble(data[1]);
			double central_rate = NumberUtils.toDouble(data[2]);
			double s_value = NumberUtils.toDouble(value);

			resval = String.format("%.2f", (s_value / central_rate)
					* currency_unit);
			result = value + " KRW" + " => " + resval + " " + CurrencyUnit2;

		} else if (CurrencyUnit2.equals("KRW")) {
			String[] data = sqlman.selectDataFromTable(field, CurrencyUnit1);

			double currency_unit = NumberUtils.toDouble(data[1]);
			double central_rate = NumberUtils.toDouble(data[2]);
			double s_value = NumberUtils.toDouble(value);

			resval = String.format("%.2f", (s_value / currency_unit)
					* central_rate);

		} else {
			String[] data = sqlman.selectDataFromTable(field, CurrencyUnit1);

			double currency_unit = NumberUtils.toDouble(data[1]);
			double central_rate = NumberUtils.toDouble(data[2]);
			double s_value = NumberUtils.toDouble(value);

			resval = String.format("%.2f", (s_value / currency_unit)
					* central_rate);
			// result = value + " " + CurrencyUnit1 + " => ￦" + resval;

			data = sqlman.selectDataFromTable(field, CurrencyUnit2);
			sqlman.close();

			currency_unit = NumberUtils.toDouble(data[1]);
			central_rate = NumberUtils.toDouble(data[2]);
			s_value = NumberUtils.toDouble(resval);

			resval = String.format("%.2f", (s_value / central_rate)
					* currency_unit);
			// result = "￦" + value + " => " + resval + " " + CurrencyUnit;
		}

		result = value + " " + CurrencyUnit1 + " => " + resval + " "
				+ CurrencyUnit2;
		return result;
	}

	private String buyCash(String CurrencyUnit, String value)
			throws SQLException, ClassNotFoundException {
		String result = "";
		HSQLDBTableManager sqlman = new HSQLDBTableManager(this.getPath(),
				"currency.db");
		String field = "country_name,currency_unit,cash_buy";
		String[] data = sqlman.selectDataFromTable(field, CurrencyUnit);
		sqlman.close();

		double currency_unit = NumberUtils.toDouble(data[1]);
		double central_rate = NumberUtils.toDouble(data[2]);
		double s_value = NumberUtils.toDouble(value);

		String resval = String.format("%.2f", (s_value / currency_unit)
				* central_rate);
		result = value + " " + CurrencyUnit + " 을(를) 받기위한 필요 원화금액 : ￦" + resval;

		return result;
	}

	private String cellCash(String CurrencyUnit, String value)
			throws SQLException, ClassNotFoundException {
		String result = "";
		HSQLDBTableManager sqlman = new HSQLDBTableManager(this.getPath(),
				"currency.db");
		String field = "country_name,currency_unit,cash_cell";
		String[] data = sqlman.selectDataFromTable(field, CurrencyUnit);
		sqlman.close();

		double currency_unit = NumberUtils.toDouble(data[1]);
		double central_rate = NumberUtils.toDouble(data[2]);
		double s_value = NumberUtils.toDouble(value);

		String resval = String.format("%.2f", (s_value / currency_unit)
				* central_rate);
		result = value + " " + CurrencyUnit + " (으)로 받을 수 있는 원화금액 : ￦" + resval;

		return result;
	}

	private String recvRemittance(String CurrencyUnit, String value)
			throws SQLException, ClassNotFoundException {
		String result = "";
		HSQLDBTableManager sqlman = new HSQLDBTableManager(this.getPath(),
				"currency.db");
		String field = "country_name,currency_unit,remittance_recv";
		String[] data = sqlman.selectDataFromTable(field, CurrencyUnit);
		sqlman.close();

		double currency_unit = NumberUtils.toDouble(data[1]);
		double central_rate = NumberUtils.toDouble(data[2]);
		double s_value = NumberUtils.toDouble(value);

		String resval = String.format("%.2f", (s_value / currency_unit)
				* central_rate);
		result = value + " " + CurrencyUnit + " (으)로 송금받을 수 있는 원화금액 : ￦"
				+ resval;

		return result;
	}

	private String sendRemittance(String CurrencyUnit, String value)
			throws SQLException, ClassNotFoundException {
		String result = "";
		HSQLDBTableManager sqlman = new HSQLDBTableManager(this.getPath(),
				"currency.db");
		String field = "country_name,currency_unit,remittance_send";
		String[] data = sqlman.selectDataFromTable(field, CurrencyUnit);
		sqlman.close();

		double currency_unit = NumberUtils.toDouble(data[1]);
		double central_rate = NumberUtils.toDouble(data[2]);
		double s_value = NumberUtils.toDouble(value);

		String resval = String.format("%.2f", (s_value / currency_unit)
				* central_rate);
		result = value + " " + CurrencyUnit + " 을(를) 송금하기위한 필요 원화금액 : ￦"
				+ resval;

		return result;
	}

}
