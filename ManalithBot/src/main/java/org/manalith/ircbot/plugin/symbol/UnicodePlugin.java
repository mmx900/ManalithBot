package org.manalith.ircbot.plugin.symbol;

import org.manalith.ircbot.annotation.Description;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.springframework.stereotype.Component;

@Component
public class UnicodePlugin extends SimplePlugin {

	@Override
	public String getName() {
		return "유니코드 변환";
	}

	@BotCommand({ "u", "유니코드" })
	public String unicode(@Description("변환할 문자 혹은 코드") String text) {
		try {
			if (text.startsWith("\\u") || text.startsWith("U+")) {
				char ch = (char) Integer.parseInt(text.substring(2), 16);
				return ch + " - " + Character.getName(ch);
			} else {
				char[] chars = text.toCharArray();
				StringBuilder sb = new StringBuilder();
				for (char ch : chars) {
					sb.append("U+").append(Integer.toHexString(ch | 0x10000).substring(1));
				}
				if (chars.length == 1) {
					sb.append(" - ").append(Character.getName(chars[0]));
				}
				return sb.toString();
			}
		} catch (NumberFormatException e) {
			return "잘못된 유니코드 문자열입니다.";
		}
	}
}
