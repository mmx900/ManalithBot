/*
 	org.manalith.ircbot.plugin.curex/HSQLDBTableManager.java
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class HSQLDBTableManager {
	private Logger logger = Logger.getLogger(getClass());

	private Connection conn;
	private Statement stat;

	private String path;
	private String filename;

	public HSQLDBTableManager(String newFilename) throws SQLException,
			ClassNotFoundException {
		setPath("");
		setFilename(newFilename);
		initJDBCDriverHSQLDB();
	}

	public HSQLDBTableManager(String newPath, String newFilename)
			throws SQLException, ClassNotFoundException {
		setPath(newPath);
		setFilename(newFilename);
		initJDBCDriverHSQLDB();
	}

	private void setPath(String newPath) {
		path = newPath;
	}

	private String getPath() {
		return path;
	}

	private void setFilename(String newFilename) {
		filename = newFilename;
	}

	private String getFilename() {
		return filename;
	}

	private void initJDBCDriverHSQLDB() throws SQLException,
			ClassNotFoundException {
		Class.forName("org.hsqldb.jdbc.JDBCDriver");

		conn = DriverManager.getConnection("jdbc:hsqldb:" + getPath()
				+ getFilename(), "sa", "");
		stat = conn.createStatement();
	}

	private void initCurrencyRateTable() throws SQLException {
		stat.executeUpdate("drop table if exists CurrencyRate");
		// stat.executeUpdate("vacuum");

		String fields = "";
		fields += "country_name VARCHAR(32) NOT NULL ,";
		fields += "currency_name VARCHAR(5) NOT NULL ,";
		fields += "currency_unit INT NOT NULL ,";
		fields += "central_rate REAL NOT NULL ,";
		fields += "cash_buy REAL NOT NULL ,";
		fields += "cash_cell REAL NOT NULL ,";
		fields += "remittance_send REAL NOT NULL ,";
		fields += "remittance_recv REAL NOT NULL ,";
		fields += "exchan_comm_rate REAL NOT NULL ,";
		fields += "dollar_exc_rate REAL NOT NULL";

		stat.executeUpdate("create table CurrencyRate(" + fields + ");");
	}

	public void insertDataToTable() throws SQLException {
		initCurrencyRateTable();

		PreparedStatement stmt = conn
				.prepareStatement("insert into CurrencyRate values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		String url = "http://info.finance.naver.com/marketindex/exchangeList.nhn";
		double t = 0.0;
		try {
			Elements trs = Jsoup.connect(url).get().select("div.tbl_area")
					.get(0).select("table.tbl_exchange").get(0).select("tbody")
					.get(0).select("tr");
			int size = trs.size();
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < 8; j++) {
					Elements tds = trs.get(i).select("td");
					if (j == 0) {
						String data = tds.get(j).select("a").get(0).text();
						int third_field_value = data.contains("100") ? 100 : 1;

						int k = 0;
						String[] split = data.split("\\s");
						String country_name = "";
						while (!Pattern.compile("[A-Z]{3}").matcher(split[k])
								.matches()) {
							country_name += " " + split[k++];
						}
						country_name = country_name.trim();
						stmt.setString(1, country_name);
						stmt.setString(2, split[k]);
						stmt.setInt(3, third_field_value);
					} else if (j == 4) {
						if (Double.toString(
								Double.parseDouble(tds.get(j).text()
										.replaceAll("\\,", ""))).equals("0.0")) {
							j += 2;
							for (int k = 0; k < 3; k++) {
								stmt.setDouble(7 + k, 0.0);
							}
						} else {
							for (int k = 0; k < 3; k++) {
								stmt.setDouble(
										7 + k,
										Double.parseDouble(tds.get(j++).text()
												.replaceAll("\\,", "")));
							}
							j--; // trick
						}
					} else {
						if (i == 0 && j == 1)
							t = Double.parseDouble(tds.get(j).text()
									.replaceAll("\\,", ""));
						stmt.setDouble(
								3 + j,
								Double.parseDouble(tds.get(j).text()
										.replaceAll("\\,", "")));
					}
				}

				stmt.addBatch();
				conn.setAutoCommit(false);
				stmt.executeBatch();
				conn.setAutoCommit(true);
				stmt.clearParameters();
			}

			stmt.setString(1, "한국");
			stmt.setString(2, "KRW");
			stmt.setInt(3, 1);
			for (int k = 4; k <= 6; k++) {
				stmt.setDouble(k, 1.0);
			}
			for (int k = 7; k <= 9; k++) {
				stmt.setDouble(k, 0.0);
			}
			stmt.setDouble(10, 1.0 / t);

			stmt.addBatch();
			conn.setAutoCommit(false);
			stmt.executeBatch();
			conn.setAutoCommit(true);
			stmt.clearParameters();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public String[] selectDataFromTable(String field, String CurrencyName)
			throws SQLException {
		String[] result = null;

		String sqlcmd = "select " + field
				+ " from CurrencyRate where currency_name=\'" + CurrencyName
				+ "\';";

		ResultSet rs = stat.executeQuery(sqlcmd);

		while (rs.next()) {
			if (field.equals("*")) {
				int j = 0;
				result = new String[9];
				result[j++] = rs.getString(1);
				result[j++] = Integer.toString(rs.getInt(3));
				for (int i = 4; i <= 10; i++)
					result[j++] = Double.toString(rs.getDouble(i));

			} else {
				String[] separatedfield = field.split("\\,");

				result = new String[separatedfield.length];
				for (int i = 0; i < separatedfield.length; i++) {
					if (separatedfield[i].contains("name")) {
						result[i] = rs.getString(i + 1);
					} else if (separatedfield[i].equals("currency_unit")) {
						result[i] = Integer.toString(rs.getInt(i + 1));
					} else {
						result[i] = Double.toString(rs.getDouble(i + 1));
					}
				}
			}
		}

		return result;
	}

	public void close() throws SQLException {
		stat.executeUpdate("SHUTDOWN");
		conn.close();
	}
}
