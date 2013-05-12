/*
 	org.manalith.ircbot.plugin.curex/CurexRunner.java
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

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class CurexRunner {

	private String[] arguments;
	private String dataPath;
	private String userNick;

	public CurexRunner() {
		arguments = null;
		dataPath = "";
	}

	public CurexRunner(String userNick, String[] arguments) {
		this.userNick = userNick;
		this.arguments = arguments;
		dataPath = "";
	}

	public CurexRunner(String userNick, String dataPath, String[] arguments) {
		this.userNick = userNick;
		this.dataPath = dataPath;
		File path = new File(dataPath);

		if (!path.exists())
			path.mkdirs();

		this.arguments = arguments;
	}

	public String run() throws Exception {
		String result = "";

		CurexTableUpdater updater = new CurexTableUpdater(dataPath);
		updater.update();

		String[] cmd = null;
		CurexInfoProvider info = null;

		if (ArrayUtils.isEmpty(arguments)) {
			String[] default_currency = null;

			PropertiesConfiguration prop = new PropertiesConfiguration(dataPath
					+ "customsetlist.prop");

			Iterator<String> userlist = prop.getKeys();
			if (userlist == null) {
				default_currency = new String[4];
				default_currency[0] = "USD";
				default_currency[1] = "EUR";
				default_currency[2] = "JPY";
				default_currency[3] = "CNY";
			} else {
				String value = prop.getString(userNick);

				if (!StringUtils.isEmpty(value)) {
					default_currency = value.split("\\,");
				} else {
					default_currency = new String[4];
					default_currency[0] = "USD";
					default_currency[1] = "EUR";
					default_currency[2] = "JPY";
					default_currency[3] = "CNY";
				}
			}

			for (int i = 0; i < default_currency.length; i++) {

				String[] args = new String[1];
				args[0] = default_currency[i];
				cmd = CurexMessageTokenAnalyzer.convertToCLICommandString(args);
				info = new CurexInfoProvider(dataPath, cmd);

				if (i != 0)
					result += ", " + info.commandInterpreter();
				else
					result += info.commandInterpreter();
			}
		} else {
			cmd = CurexMessageTokenAnalyzer
					.convertToCLICommandString(arguments);
			info = new CurexInfoProvider(dataPath, cmd);

			result = info.commandInterpreter();
		}

		return result;
	}
}
