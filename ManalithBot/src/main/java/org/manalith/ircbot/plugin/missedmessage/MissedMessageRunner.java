/*
 	org.manalith.ircbot.plugin.missedmessage/MissedMessageRunner.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2012  Seong-ho, Cho <darkcircle.0426@gmail.com>

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
package org.manalith.ircbot.plugin.missedmessage;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;

public class MissedMessageRunner {
	private String ResourcePath;
	private static String filename = "msglist.prop";

	public MissedMessageRunner() {
		setResourcePath("");
	}

	public MissedMessageRunner(String newResourcePath) {
		setResourcePath(newResourcePath);
	}

	private void setResourcePath(String newResourcePath) {
		File f = new File(newResourcePath);
		if (!f.exists())
			f.mkdirs();

		ResourcePath = newResourcePath;
	}

	private String getResourcePath() {
		return ResourcePath;
	}

	public String addMsg(String sender, String receiver, String msg) {
		StringBuilder result = new StringBuilder();

		boolean savedMsg = false;
		int msglen = msg.length();

		try {
			PropertiesConfiguration prop = new PropertiesConfiguration(
					getResourcePath() + MissedMessageRunner.filename);

			Iterator<String> userlist = prop.getKeys();
			if (userlist != null) {
				while (userlist.hasNext()) {
					String key = userlist.next();
					if (key.equals(receiver)) {
						if (prop.getString(receiver).length() != 0) {
							result.append(StringUtils.split(receiver, '.')[1]);
							String str = prop.getString(receiver);

							result.append("님의 남은 메시지 공간 ( ");
							int availableSpace = 3 - str.split("\\:\\:").length;
							if (availableSpace != 0) {
								if (msglen <= 150) {

									prop.setProperty(receiver, str + "::"
											+ "[남김:" + sender + "] " + msg);
									result.append(availableSpace - 1);
									result.append(" / 3 )");
								} else {
									result.delete(0, result.length());
									result.append("메시지 길이가 150자를 넘었습니다.");
								}
							} else {
								result.append(availableSpace);
								result.append(" / 3 ) : 더이상 메시지를 남길 수 없습니다");
							}
						} else // if key has no value
						{
							if (msglen <= 150) {
								prop.setProperty(receiver, "[남김:" + sender
										+ "] " + msg);
								result.append(StringUtils.split(receiver, '.')[1]);
								result.append("님의 남은 메시지 공간 ( 2 / 3 )");
							} else {
								result.delete(0, result.length());
								result.append("메시지 길이가 150자를 넘었습니다.");
							}
						}
						savedMsg = true;
						prop.save();
						break;
					}
				}

				if (!savedMsg) {
					result.append(receiver.split("\\.")[1]
							+ "님은 이 채널에 방문했거나 대화 후 나간 적이 없습니다.");
				}
			}
		} catch (ConfigurationException ce) {
			return ce.getMessage();
		}

		return result.toString();
	}

	public String[] getMsg(String newRecv) {
		String[] msgs = null;

		try {
			// init!
			PropertiesConfiguration prop = new PropertiesConfiguration(
					getResourcePath() + MissedMessageRunner.filename);

			if (isMatchedNickinList(newRecv)) {
				if (prop.getString(newRecv).length() != 0) {
					msgs = prop.getString(newRecv).split("\\:\\:");

					for (int i = 0; i < msgs.length; i++)
						msgs[i] = newRecv.split("\\.")[1] + ", " + msgs[i];

					prop.setProperty(newRecv, "");
					prop.save();
				}
			}
		} catch (ConfigurationException ce) {
			msgs = new String[1];
			msgs[0] = ce.getMessage();
		}

		return msgs;
	}

	public void addMsgSlot(String newRecv) {

		try {
			PropertiesConfiguration prop = new PropertiesConfiguration(
					getResourcePath() + MissedMessageRunner.filename);

			// if user not found, init msgslot
			prop.setProperty(newRecv, "");
			prop.save();

		} catch (ConfigurationException ce) {
			// ignore exception
		}
	}

	public boolean isMatchedNickinList(String newRecv) {
		boolean result = false;

		try {
			PropertiesConfiguration prop = new PropertiesConfiguration(
					getResourcePath() + MissedMessageRunner.filename);

			Iterator<String> userlist = prop.getKeys();
			if (userlist != null) {
				while (userlist.hasNext()) {
					String u = userlist.next();
					// if user found from userlist, just break this routine
					if (u.equals(newRecv)) {
						result = true;
						break;
					}
				}
			}
		} catch (ConfigurationException ce) {
			// Ignore.
		}

		return result;
	}

}
