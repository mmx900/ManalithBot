package org.manalith.ircbot.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser {
	/**
	 * 메시지를 받아서 봇의 이름과 구분자가 포함되었는지 확인하고 있으면 이름과 구분자를 제거하여 반환한다.
	 * 봇에게 말을 하는 것인지 알아내기 위한 메서드다.
	 * @param botNick
	 * @param message
	 * @return 봇의 이름이 포함되었으면 이름과 구분자, 첫 스페이스를 제거한 메시지(순수 커맨드)를 반환, 없으면 null을 반환
	 */
	public static String checkMessageAndRemoveNick(String botNick, String message){
			//봇의 이름 및 delimiter 부분을 삭제
			//대상 : "봇이름,", "봇이름 ,", "봇이름", "봇이름 //", "봇이름 "
			//\s : whitespace
			String regex = "^" + Pattern.quote(botNick) + "\\s*[,/:]*\\s+";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(message);
			
			if(m.lookingAt())
				return m.replaceFirst("");
			else
				return null;

	}
	
	/**
	 * 다른 봇에 의해 다른 서버에서 릴레이된 메시지를 로컬 메시지로 변환한다.
	 * @param message
	 * @return
	 */
	public static String convertRelayToLocalMessage(String message){
		// "<setzer> 안녕" 식의 메시지를 받아 "안녕"만 반환

		String regex = "<[^>]*>\\s+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(message);
		
		if(m.lookingAt())
			return m.replaceFirst("");
		else
			return message;
	}
	
	/**
	 * 다른 봇에 의해 다른 서버에서 릴레이된 메시지를 받아 보낸 사람을 반환한다.
	 * TODO convertRelayToLocalMessage와 통합할 것
	 * @param message
	 * @return
	 */
	public static String getSenderByRelayMessage(String message){
		// "<setzer> 어쩌고" 식의 메시지를 받아 "setzer"를 반환

		String regex = "<([^>]*)>\\s+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(message);
		boolean matchFound = m.find();
		
		if(matchFound)
			return m.group(1);
		else
			return null;
	}
}
