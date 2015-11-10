/*
 	org.manalith.ircbot.command/CommandParser.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2005, 2011  Ki-Beom, Kim

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

package org.manalith.ircbot.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser {
	/**
	 * 메시지를 받아서 봇의 이름과 구분자가 포함되었는지 확인하고 있으면 이름과 구분자를 제거하여 반환한다. 봇에게 말을 하는 것인지
	 * 알아내기 위한 메서드다.
	 * 
	 * @param botNick
	 * @param message
	 * @return 봇의 이름이 포함되었으면 이름과 구분자, 첫 스페이스를 제거한 메시지(순수 커맨드)를 반환, 없으면 null을 반환
	 */
	public static String checkMessageAndRemoveNick(String botNick,
			String message) {
		// 봇의 이름 및 delimiter 부분을 삭제
		// 대상 : "봇이름,", "봇이름 ,", "봇이름", "봇이름 //", "봇이름 "
		// \s : whitespace
		String regex = "^" + Pattern.quote(botNick) + "\\s*[,/:]*\\s+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(message);

		if (m.lookingAt())
			return m.replaceFirst("");
		else
			return null;
	}

	/**
	 * 다른 봇에 의해 다른 서버에서 릴레이된 메시지를 로컬 메시지로 변환한다.
	 * 
	 * @param message
	 * @return
	 */
	public static String convertRelayToLocalMessage(String message) {
		// "<setzer> 안녕" 식의 메시지를 받아 "안녕"만 반환

		String regex0 = "<[^>]*>\\s+";
		String regex1 = "\\[[^\\]]*\\]\\s+";

		Pattern p0 = Pattern.compile(regex0);
		Pattern p1 = Pattern.compile(regex1);

		Matcher m0 = p0.matcher(message);
		Matcher m1 = p1.matcher(message);

		if (m0.lookingAt())
			return m0.replaceFirst("");
		else if (m1.lookingAt())
			return m1.replaceFirst("");
		else
			return message;
	}

	/**
	 * 다른 봇에 의해 다른 서버에서 릴레이된 메시지를 받아 보낸 사람을 반환한다. TODO
	 * convertRelayToLocalMessage와 통합할 것
	 * 
	 * @param message
	 * @return
	 */
	public static String getSenderByRelayMessage(String message) {
		// "<setzer> 어쩌고" 식의 메시지를 받아 "setzer"를 반환

		String regex0 = "<([^>]*)>\\s+";
		String regex1 = "\\[[^\\]]*\\]\\s+";

		Pattern p0 = Pattern.compile(regex0);
		Pattern p1 = Pattern.compile(regex1);

		Matcher m0 = p0.matcher(message);
		Matcher m1 = p1.matcher(message);

		boolean matchFound0 = m0.find();
		boolean matchFound1 = m1.find();

		if (matchFound0)
			return m0.group(1);
		else if (matchFound1)
			return m1.group(1);
		else
			return null;
	}
}
