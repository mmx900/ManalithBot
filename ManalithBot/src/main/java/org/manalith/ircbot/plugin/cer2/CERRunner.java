/*
 	org.manalith.ircbot.plugin.cer2/CERRunner.java
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

import java.io.File;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.manalith.ircbot.common.PropertyManager;

public class CERRunner {

	private String[] arguments;
	private String dataPath;
	private String userNick;

	public CERRunner() {
		arguments = null;
		dataPath = "";
	}

	public CERRunner(String userNick, String[] arguments) {
		this.userNick = userNick;
		this.arguments = arguments;
		dataPath = "";
	}

	public CERRunner(String userNick, String dataPath, String[] arguments) {
		this.userNick = userNick;
		this.dataPath = dataPath;
		File path = new File(dataPath);

		if (!path.exists())
			path.mkdirs();

		this.arguments = arguments;
	}

	public String run() throws Exception {
		String result = "";

		CERTableUpdater updater = new CERTableUpdater(dataPath);
		updater.update();

		String[] cmd = null;
		CERInfoProvider info = null;

		if (ArrayUtils.isEmpty(arguments)) {
			String[] default_currency = null;

			PropertyManager prop = new PropertyManager(dataPath,
					"customsetlist.prop");
			prop.loadProperties();

			String[] userlist = prop.getKeyList();
			if (userlist == null) {
				default_currency = new String[4];
				default_currency[0] = "USD";
				default_currency[1] = "EUR";
				default_currency[2] = "JPY";
				default_currency[3] = "CNY";
			} else {
				int existidx = StringUtils.indexOfAny(userNick, userlist);
				if (existidx != -1) {
					default_currency = prop.getValue(userlist[existidx]).split(
							"\\,");
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
				cmd = CERMessageTokenAnalyzer.convertToCLICommandString(args);
				info = new CERInfoProvider(dataPath, cmd);

				if (i != 0)
					result += ", " + info.commandInterpreter();
				else
					result += info.commandInterpreter();
			}
		} else {
			cmd = CERMessageTokenAnalyzer.convertToCLICommandString(arguments);
			info = new CERInfoProvider(dataPath, cmd);

			result = info.commandInterpreter();
		}

		return result;
	}
}
