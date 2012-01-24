//
// CERInfoProvider.java
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.

package org.manalith.ircbot.plugin.cer2;

import org.manalith.ircbot.common.PropertyManager;
import org.manalith.ircbot.plugin.cer2.exceptions.EmptyTokenStreamException;
import org.manalith.ircbot.plugin.cer2.exceptions.InvalidArgumentException;

import java.io.IOException;
import java.sql.SQLException;

public class CERInfoProvider {

	private String command;
	private String path;

	public CERInfoProvider() {
		this.setCommand("");
	}

	public CERInfoProvider(String newCommand) {
		this.setCommand(newCommand);
		this.setPath("");
	}

	public CERInfoProvider(String newPath, String newCommand) {
		this.setCommand(newCommand);
		this.setPath(newPath);
	}

	public void setCommand(String newCommand) {
		this.command = newCommand;
	}

	public String getCommand() {
		return this.command;
	}

	public void setPath(String newPath) {
		this.path = newPath;
	}

	public String getPath() {
		return this.path;
	}

	public String commandInterpreter() throws EmptyTokenStreamException,
			InvalidArgumentException, IOException, SQLException,
			ClassNotFoundException {
		String result = "";

		CommandTokenAnalyzer cta = new CommandTokenAnalyzer(this.getCommand());
		TokenArray ta = cta.analysisTokenStream();

		int size = this.getCommand().split("\\s").length;
		TokenType[] TokenTypeSequence = new TokenType[size];
		TokenSubtype[] TokenSubTSequence = new TokenSubtype[size];

		for (int i = 0; i < size; i++) {
			TokenTypeSequence[i] = ta.getElement(i).getTokenType();
			TokenSubTSequence[i] = ta.getElement(i).getTokenSubtype();
		}

		// interpret command.
		int i = 0;

		if (TokenSubTSequence[i] == TokenSubtype.CommandHelp)
			result = "Help!";
		else if (TokenSubTSequence[i] == TokenSubtype.CommandLastRound)
			result = this.showLatestRound();
		else if (TokenSubTSequence[i] == TokenSubtype.CommandShow) {
			i++;
			String currencyName = TokenSubTSequence[i++].toString()
					.substring(8);
			StringBuilder fieldName = new StringBuilder();
			fieldName.append("country_name,");

			if (TokenSubTSequence[i] == TokenSubtype.FAAll)
				fieldName.append("*");
			else if (TokenSubTSequence[i] == TokenSubtype.Unknown
					|| TokenSubTSequence[i] == TokenSubtype.FACentralRate)
				fieldName.append("currency_unit,central_rate");
			else if (TokenSubTSequence[i] == TokenSubtype.FABuyCash)
				fieldName.append("currency_unit,cash_buy");
			else if (TokenSubTSequence[i] == TokenSubtype.FACellCash)
				fieldName.append("currency_unit,cash_cell");
			else if (TokenSubTSequence[i] == TokenSubtype.FARecvRemit)
				fieldName.append("currency_unit,remittance_recv");
			else if (TokenSubTSequence[i] == TokenSubtype.FASendRemit)
				fieldName.append("currency_unit,remittance_send");
			else if (TokenSubTSequence[i] == TokenSubtype.FAECRate)
				fieldName.append("currency_unit,exchan_comm_rate");
			else if (TokenSubTSequence[i] == TokenSubtype.FADollarExcRate)
				fieldName.append("currency_unit,dollar_exc_rate");
			else
				throw new InvalidArgumentException(
						"Unknown field abbreviation [ "
								+ ta.getElement(i).getTokenString() + " ] ");

			result = this.showCurrencyRate(currencyName, fieldName.toString());
		} else if (TokenSubTSequence[i] == TokenSubtype.CommandConvert) {
			String value = "";
			i++;
			if (TokenSubTSequence[i] == TokenSubtype.AmountNatural)
				value = ta.getElement(i++).getTokenString() + ".0";
			else
				value = ta.getElement(i++).getTokenString();

			String cname1 = TokenSubTSequence[i++].toString().substring(8);
			String cname2 = TokenSubTSequence[i++].toString().substring(8);

			result = this.convert(value, cname1, cname2);
		} else {
			i++;

			String currency_name = TokenSubTSequence[i++].toString().substring(
					8);
			String value = "";

			if (TokenSubTSequence[i] == TokenSubtype.AmountNatural)
				value = ta.getElement(i).getTokenString() + ".0";
			else
				value = ta.getElement(i).getTokenString();

			if (TokenSubTSequence[0] == TokenSubtype.CommandBuyCash)
				result = this.buyCash(currency_name, value);
			else if (TokenSubTSequence[0] == TokenSubtype.CommandCellCash)
				result = this.cellCash(currency_name, value);
			else if (TokenSubTSequence[0] == TokenSubtype.CommandSendRemit)
				result = this.sendRemittance(currency_name, value);
			else if (TokenSubTSequence[0] == TokenSubtype.CommandRecvRemit)
				result = this.recvRemittance(currency_name, value);
		}

		return result;
	}

	public static String getIRCHelpMessagePart1() {
		String result = "!curex ( help | lastround ) | !cer ( buycash | cellcash | sendremit | recvremit ) [Amount] [Currency_Unit] | !cer ( show | \"\" ) [Currency_Unit] ";
		result += "[FieldAbbr] | !cer ( conv | \"\" ) ( [Amount] : KRW to USD | [Amount] [Currency_Unit(1)] [Current_Unit(2)] | [Currency_Unit(1)] [Current_Unit(2)] : | [Amount] [Currency_Unit(2)] )";

		return result;
	}

	public static String getIRCHelpMessagePart2() {
		return "help: 도움말, lastround: 최종갱신회차, buycash: 현찰매수, cellcash: 현찰매도, sendremit: 송금보냄, recvremit: 송금받음, show: 환율보이기, conv: 환율계산";
	}

	public static String getIRCHelpMessagePart3() {
		String result = "[Currency_Unit] : USD:U.S, EUR:Europe, JPY:Japan, CNY:China, HKD:HongKong, TWD:Taiwan, GBP:Great Britain, CAD:Canada, CHF:Switzerland, SEK:Sweden, ";
		result += "AUD:Australia, NZD:NewZealand, ISL:Israel, DKK:Denmark, NOK:Norway, SAR:Saudi Arabia, KWD:Kuweit, BHD:Bahrain, AED:United of Arab Emirates, ";
		result += "JOD:Jordan, EGP:Egypt, THB:Thailand, SGD:Singapore, MYR:Malaysia, IDR:Indonesia, BND:Brunei, INR:India, PKR:Pakistan,";

		return result;
	}

	public static String getIRCHelpMessagePart4() {
		String result = "BDT:Bangladesh, PHP:Philippine, MXN:Mexico, BRL:Brazil, VND:Vietnam, ZAR:Republic of South Africa, RUB:Russia, HUF:Hungary, PLN:Poland ";
		result += "[FieldAbbr] (show 명령에만 해당) 모두보기, 매매기준, 현찰매수, 현찰매도, 송금보냄, 송금받음, 환수수료, 대미환산 ";
		result += "[Amount] 금액 [Currency_Unit] 단일화폐단위 [Current_Unit(1)] 변환할화폐단위 [Current_Unit(2)] 대상화폐단위";

		return result;
	}

	/*
	 * This method is for executing on the CLI environment. private void
	 * showHelpMessage ( ) { // this method must be deprecated for executing
	 * from ircbot. System.out.println ( "사용법 : ./run.sh --command [옵션]\n");
	 * System.out.println ( "\t--help : 이 메시지를 보여줍니다."); System.out.println (
	 * "\t--latestround : 최종 갱신 일자, 시간, 회차를 보여줍니다."); System.out.println (
	 * "\t--forceupdate : 강제로 정보 테이블을 갱신합니다."); System.out.println (
	 * "\t--show [Currency_unit] ([Field]) : 지정한 화폐단위에 대한 환율 항목을 보여줍니다.");
	 * System.out.println (
	 * "\t--convert [Amount] [Currency_unit] : 지정 금액을 원화로 취급하며 [Currency_Unit] 으로 환산하여 보여줍니다."
	 * ); System.out.println (
	 * "\t--convert [Amount] [Curr_unit1] [Curr_unit2] : 지정 금액을 [Curr_unit1] 단위로 취급하며 [Curr_Unit2] 로 환산하여 보여줍니다."
	 * ); System.out.println (
	 * "\t--convert [Curr_unit1] [Curr_unit2] : 금액 1을 [Curr_unit1] 단위로 취급하며 [Curr_unit2] 로 환산하여 보여줍니다."
	 * ); System.out.println (
	 * "\t--buycash [Currency_unit] [Amount] : 환전 거래시 [Currency_name]단위의 [Amount] 금액으로 바꾸기 위해 필요한 원화금액을 보여줍니다."
	 * ); System.out.println (
	 * "\t--cellcash [Currency_unit] [Amount] : 환전 거래시 [Currency_name]단위의 [Amount] 금액을 받을 수 있는 원화금액을 보여줍니다."
	 * ); System.out.println (
	 * "\t--sendremit [Currency_unit] [Amount] : 송금 보낼 때 [Currency_name]단위의 [Amount] 금액을 보내기 위해 필요한 원화금액을 보여줍니다."
	 * ); System.out.println (
	 * "\t--recvremit [Currency_unit] [Amount] : 송금 받을 때 [Currency_name]단위의 [Amount] 금액에 대해 받을 수 있는 원화금액을 보여줍니다.\n"
	 * );
	 * 
	 * System.out.println ( "\t [Currency_name]"); System.out.println (
	 * "\t USD : U.S Dollar, EUR : Europe United EURO, JPY : Japan Yen, CNY : China Yuan"
	 * ); System.out.println (
	 * "\t HKD : HongKong HongKongDollar, TWD : Taiwan NewTaiwanDollar, GBP : Great Britain Pound"
	 * ); System.out.println (
	 * "\t CAD : Canada CanadianDollar, CHF : Switzerland SwissFranc, SEK : Sweden SwedishKrona"
	 * ); System.out.println (
	 * "\t AUD : Australia AustralianDollar, NZD : NewZealand NewZealandDollar, ISL : Israel NewShekel"
	 * ); System.out.println (
	 * "\t DKK : Denmark DanishKrone, NOK : Norway NorwegianKrone, SAR : Saudi Arabia SaudiRial"
	 * ); System.out.println (
	 * "\t KWD : Kuweit Dinar, BHD : Bahrain Dinar, AED : United of Arab Emirates EmiratiDirham"
	 * ); System.out.println (
	 * "\t JOD : Jordan Dinar, EGP : Egypt EgyptianPound, THB : Thailand Baht, SGD : Singapore SingaporeDollar"
	 * ); System.out.println (
	 * "\t MYR : Malaysia Ringgit, IDR : Indonesia Rupiah, BND : Brunei BruneiDollar, INR : India Rupee"
	 * ); System.out.println (
	 * "\t PKR : Pakistan PakistaniRupee, BDT : Bangladesh Taka, PHP : Philippine Peso, MXN : Mexico MexicanPero"
	 * ); System.out.println (
	 * "\t BRL : Brazil Real, VND : Vietnam VietnameseDong, ZAR : Republic of South Africa Rand"
	 * ); System.out.println (
	 * "\t RUB : Russia Ruble, HUF : Hungary Forint, PLN : Poland Zloty\n");
	 * 
	 * System.out.println ( "\t [Field]"); System.out.println (
	 * "\t all : 모두 보여주기"); System.out.println (
	 * "\t cr : 매매 기준율 (기본값), cb : 현찰 매수 지불, cc : 현찰 매도 수익"); System.out.println
	 * ( "\t rs : 송금 보낼 때, rr : 송금 받을 때, ec : 환전 수수료율"); System.out.println (
	 * "\t de : 달러 환산율"); }
	 */
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
		result.append(month[Integer.parseInt(date[1]) - 1]);
		result.append(" ");
		result.append(date[2]);
		result.append(", ");
		result.append(date[0]);
		result.append("  ");
		result.append(datetime[1]);
		result.append(", ");
		result.append(Integer.parseInt(roundstr));
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

			if (Integer.parseInt(data[1]) == 100) {
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
						(Double.parseDouble(data[j]) * 100.0)));
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

			double currency_unit = Double.parseDouble(data[1]);
			double central_rate = Double.parseDouble(data[2]);
			double s_value = Double.parseDouble(value);

			resval = String.format("%.2f", (s_value / central_rate)
					* currency_unit);
			result = value + " KRW" + " => " + resval + " " + CurrencyUnit2;

		} else if (CurrencyUnit2.equals("KRW")) {
			String[] data = sqlman.selectDataFromTable(field, CurrencyUnit1);

			double currency_unit = Double.parseDouble(data[1]);
			double central_rate = Double.parseDouble(data[2]);
			double s_value = Double.parseDouble(value);

			resval = String.format("%.2f", (s_value / currency_unit)
					* central_rate);

		} else {
			String[] data = sqlman.selectDataFromTable(field, CurrencyUnit1);

			double currency_unit = Double.parseDouble(data[1]);
			double central_rate = Double.parseDouble(data[2]);
			double s_value = Double.parseDouble(value);

			resval = String.format("%.2f", (s_value / currency_unit)
					* central_rate);
			// result = value + " " + CurrencyUnit1 + " => ￦" + resval;

			data = sqlman.selectDataFromTable(field, CurrencyUnit2);
			sqlman.close();

			currency_unit = Double.parseDouble(data[1]);
			central_rate = Double.parseDouble(data[2]);
			s_value = Double.parseDouble(resval);

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

		double currency_unit = Double.parseDouble(data[1]);
		double central_rate = Double.parseDouble(data[2]);
		double s_value = Double.parseDouble(value);

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

		double currency_unit = Double.parseDouble(data[1]);
		double central_rate = Double.parseDouble(data[2]);
		double s_value = Double.parseDouble(value);

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

		double currency_unit = Double.parseDouble(data[1]);
		double central_rate = Double.parseDouble(data[2]);
		double s_value = Double.parseDouble(value);

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

		double currency_unit = Double.parseDouble(data[1]);
		double central_rate = Double.parseDouble(data[2]);
		double s_value = Double.parseDouble(value);

		String resval = String.format("%.2f", (s_value / currency_unit)
				* central_rate);
		result = value + " " + CurrencyUnit + " 을(를) 송금하기위한 필요 원화금액 : ￦"
				+ resval;

		return result;
	}

}
