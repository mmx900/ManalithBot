/*
 	org.manalith.ircbot.plugin.curex/CurexTableUpdater.java
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

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.manalith.ircbot.plugin.curex.exceptions.FileDoesntSpecifiedException;

public class CurexTableUpdater {

	private String LocalPath;
	private String propFileName;
	private HSQLDBTableManager sqlman;

	private DateTimeRound local;
	private DateTimeRound remote;

	private SimpleDateFormat sdf;

	public CurexTableUpdater() {
		this("");
	}

	public CurexTableUpdater(String newLocalPath) {
		this.setLocalPath(newLocalPath);
		propFileName = "LatestUpdatedDatetime.prop";
		sqlman = null;
		local = null;
		remote = null;
		sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREAN);
	}

	private void setLocalPath(String newLocalPath) {
		this.LocalPath = newLocalPath;
	}

	private String getLocalPath() {
		return this.LocalPath;
	}

	private void initSQLiteTable() throws SQLException, ClassNotFoundException {
		if (this.getLocalPath().equals("")) {
			sqlman = new HSQLDBTableManager("currency.db");
		} else {
			sqlman = new HSQLDBTableManager(this.getLocalPath(), "currency.db");
		}
	}

	public boolean checkLocalLastRoundExpired()
			throws FileDoesntSpecifiedException, ConfigurationException,
			ParseException, IOException {
		RemoteLocalDatetimeChecker check = new RemoteLocalDatetimeChecker(
				this.getLocalPath(), propFileName);
		local = check.checkLatestUpdatedLocalDateandTime();
		remote = check.checkLatestNoticeDateandTime();

		return (remote.compareTo(local) > 0);
	}

	public void updateLastRound() throws ConfigurationException {
		// set properties using the value.
		PropertiesConfiguration pm = new PropertiesConfiguration(
				this.getLocalPath() + propFileName);

		String date = sdf.format(remote.getCalendar().getTime());
		pm.setProperty("date", date);

		String round = Integer.toString(remote.getRoundVal());
		while (round.length() < 3)
			round = "0" + round;
		pm.setProperty("round", round);

		// commit into prop file.
		pm.save();
	}

	public void updateDataTable() throws ClassNotFoundException, SQLException {
		this.initSQLiteTable();
		sqlman.insertDataToTable();
		sqlman.close();
	}

	public void update() throws IOException, SQLException,
			ClassNotFoundException, FileDoesntSpecifiedException,
			ParseException, ConfigurationException {
		boolean data = this.checkLocalLastRoundExpired();
		if (data) {
			this.updateLastRound();
			this.updateDataTable();
		}
	}
}
