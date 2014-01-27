package org.manalith.ircbot.plugin.curex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.manalith.ircbot.plugin.curex.TokenObject.TokenType;
import org.manalith.ircbot.plugin.curex.exceptions.AppIDMissingException;
import org.manalith.ircbot.plugin.curex.exceptions.IllegalArgumentException;
import org.manalith.ircbot.plugin.curex.exceptions.UnknownCurrencyException;

public class CurexRunner {

	private String path_;
	private String app_id;
	private String[] irco; // irc option

	private CommandParser parser;
	private OpenExchangeRate oer;
	private ArrayList<TokenObject> tarray;

	public CurexRunner(String path, String appid, String[] options)
			throws Exception {

		// allocate app_id
		if (StringUtils.isEmpty(appid))
			throw new AppIDMissingException();
		path_ = path;
		app_id = appid;

		// prepare to parse options
		irco = new String[options.length];
		for (int i = 0; i < options.length; i++)
			irco[i] = options[i];

		parser = new CommandParser(irco);
		tarray = parser.parse();
	}

	public String run() throws Exception, IOException {
		String result = "";
		oer = new OpenExchangeRate(path_, app_id);

		if (!tarray.get(0).getTokenType().equals(TokenType.Command))
			throw new IllegalArgumentException(-1);

		switch (tarray.size()) {
		case 1:
			switch (tarray.get(0).getTokenSubtype().value()) {
			case 0: // show
				result += getShowResult("USD");
				result += ", " + getShowResult("EUR");
				result += ", " + getShowResult("JPY");
				result += ", " + getShowResult("CNY");
				result += ", " + getShowResult("BTC");
				break;
			case 1: // help
				result += "[자세한 도움말] " + getHelpResult();
				break;
			case 3: // unitlist
				result += "[통화 기호 목록] ";
				result += getUnitListResult();
				break;
			}
			break;
		case 2:
			switch (tarray.get(0).getTokenSubtype().value()) {
			case 0: // show
				if (!isValidCurrency(tarray.get(1).getTokenString()))
					throw new UnknownCurrencyException(tarray.get(1)
							.getTokenString());
				result += getShowResult(tarray.get(1).getTokenString());
			}
			break;
		case 4:
			switch (tarray.get(0).getTokenSubtype().value()) {
			case 2: // calc
				if (!isValidCurrency(tarray.get(2).getTokenString()))
					throw new UnknownCurrencyException(tarray.get(2)
							.getTokenString());
				if (!isValidCurrency(tarray.get(3).getTokenString()))
					throw new UnknownCurrencyException(tarray.get(3)
							.getTokenString());
				result = getCalcResult(tarray.get(1).getTokenString(), tarray
						.get(2).getTokenString(), tarray.get(3)
						.getTokenString());
				break;
			}
		}

		return result;
	}

	// targetUnit is only KRW
	public String getShowResult(String sourceUnit) {
		String country_name = oer.getCountryName(sourceUnit);
		double show_result = oer.calc(1, sourceUnit, "KRW");

		return String.format(Locale.getDefault(), "%s(%s)에 대한 거래기준액: ￦%.4f",
				country_name, sourceUnit, show_result);
	}

	public String getHelpResult() {
		return "(준비 안됨. 빠른 시일 내에 준비하겠습니다.)";
	}

	public String getCalcResult(String val, String sourceUnit, String targetUnit) {
		int v0;
		double v1;
		double result;

		v0 = NumberUtils.toInt(val);
		if (v0 != 0)
			result = oer.calc(v0, sourceUnit, targetUnit);
		else {
			v1 = NumberUtils.toDouble(val);
			result = oer.calc(v1, sourceUnit, targetUnit);
		}

		return String.format(Locale.getDefault(), val + " " + sourceUnit
				+ " => %.4f " + targetUnit, result);
	}

	public String getUnitListResult() {
		String result = "";

		ArrayList<String> ul = oer.getCurrencyList();
		for (String s : ul)
			result += s + ", ";

		return result.substring(0, result.length() - 2);
	}

	public boolean isValidCurrency(String unit) {
		return oer.isValidCurrency(unit);
	}
}