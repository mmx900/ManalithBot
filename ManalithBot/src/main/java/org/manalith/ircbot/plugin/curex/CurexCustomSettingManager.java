/*
 	org.manalith.ircbot.plugin.curex/CurexCustomSettingManager.java
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
package org.manalith.ircbot.plugin.curex;

import java.io.File;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.manalith.ircbot.plugin.curex.exceptions.EmptyTokenStreamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurexCustomSettingManager extends TokenAnalyzer {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private final String dataPath;
	private final String channel;
	private final String userNick;
	private final String currencyArgs;
	public static final String PROP_FILE = "customsetlist.prop";

	public CurexCustomSettingManager(String localPath, String channel,
			String userNick, String currencyArgs) throws ConfigurationException {
		dataPath = localPath;

		PropertiesConfiguration config;

		try {
			config = new PropertiesConfiguration(localPath + PROP_FILE);
		} catch (ConfigurationException e) {
			config = new PropertiesConfiguration();
			config.setFile(new File(localPath + PROP_FILE));
			config.save();
		}

		this.channel = channel;
		this.userNick = userNick;
		this.currencyArgs = currencyArgs;
	}

	public String addUserSetting() {
		String result = "";
		int chkCode;

		try {
			chkCode = validateToken(analysisTokenStream());
			if (chkCode != -1) {
				result = "알 수 없는 화폐 단위 : " + currencyArgs.split("\\,")[chkCode];
				result += ", ex) !cer USD, JPY, HKD (각 화폐 단위는 콤마로 구분합니다)";
				return result;
			}

			PropertiesConfiguration customsetlist = new PropertiesConfiguration(
					dataPath + PROP_FILE);

			if (!StringUtils.isEmpty(customsetlist.getString(userNick))) {
				result += "이미 설정이 등록되어 새로운 설정으로 대체합니다. ";
			}

			customsetlist.setProperty(channel.substring(1) + "." + userNick,
					currencyArgs);
			customsetlist.save();

			result += channel + "의 " + userNick + "님이 조회할 기본화폐 환율은 "
					+ currencyArgs + "입니다.";

		} catch (EmptyTokenStreamException e) {
			result = "지정한 화폐 단위가 없습니댜";
		} catch (ConfigurationException e) {
			logger.error(e.getMessage(), e);
			result = e.getMessage();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result = e.getMessage();
		}

		return result;
	}

	public String removeUserSetting() {
		String result = "";

		try {
			PropertiesConfiguration customsetlist = new PropertiesConfiguration(
					dataPath + PROP_FILE);

			Iterator<String> userlist = customsetlist.getKeys();
			if (userlist == null) {
				result = "설정을 등록한 사용자가 없습니다";
				return result;
			} else if (!StringUtils.isEmpty(customsetlist.getString(userNick))) {
				customsetlist.clearProperty(channel.substring(1) + "."
						+ userNick);
				customsetlist.save();
				result = channel + "의 " + userNick + "님에 대한 설정을 지웠습니다.";
			} else {
				result = channel + "의 " + userNick + "님은 설정을 등록하지 않았습니다.";
			}
		} catch (ConfigurationException e) {
			result = e.getMessage();
		}

		return result;
	}

	@Override
	public TokenType getTokenType(String TokenString) {
		TokenType result = TokenType.Unknown;

		Pattern cur_pattern = Pattern.compile("[A-Z]{3}");
		Matcher cur_matcher = cur_pattern.matcher(TokenString);

		if (cur_matcher.matches())
			result = TokenType.CurrencyUnit;

		return result;
	}

	@Override
	public TokenSubtype getTokenSubtype(String TokenString,
			TokenType CurrentType) {
		TokenSubtype result = TokenSubtype.Unknown;
		int hashCode = CurrentType.hashCode();

		if (hashCode == TokenType.CurrencyUnit.hashCode()) {
			try {
				result = TokenSubtype.valueOf("Currency" + TokenString);
			} catch (IllegalArgumentException e) {
				// ignore
			}
		}

		return result;
	}

	@Override
	public TokenArray analysisTokenStream() throws EmptyTokenStreamException {
		TokenArray result = new TokenArray();
		TokenType oTokenType = null;
		TokenSubtype oTokenSubtype = null;

		if (currencyArgs.length() == 0)
			throw new EmptyTokenStreamException();
		String[] options = currencyArgs.split("\\,");

		for (int i = 0; i < options.length; i++) {
			oTokenType = getTokenType(options[i]);
			oTokenSubtype = getTokenSubtype(options[i], oTokenType);

			TokenUnit u = new TokenUnit(options[i], oTokenType, oTokenSubtype);
			result.addElement(u);
		}

		return result;
	}

	private int validateToken(TokenArray tArray) {
		int result = -1; // no problem as default;
		int size = tArray.getSize();

		for (int i = 0; i < size; i++) {
			TokenUnit t = tArray.getElement(i);
			if (t.getTokenType() == TokenType.Unknown
					|| t.getTokenSubtype() == TokenSubtype.Unknown)
				return i; // i th element has problem
		}

		return result;
	}
}
