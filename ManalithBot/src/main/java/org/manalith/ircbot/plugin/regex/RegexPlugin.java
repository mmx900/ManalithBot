package org.manalith.ircbot.plugin.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.manalith.ircbot.annotation.Description;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.springframework.stereotype.Component;

@Component
public class RegexPlugin extends SimplePlugin {

	@Override
	public String getName() {
		return "정규표현식";
	}

	@BotCommand
	public String regex(@Description("정규표현식") String regex,
			@Description("테스트 문자열") String fixture) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(fixture);

		if (matcher.matches()) {
			if (matcher.groupCount() > 0) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < matcher.groupCount(); i++) {
					sb.append(" " + i + ":" + matcher.group(i));
				}
				return sb.toString();
			} else {
				return matcher.toString();
			}
		} else {
			return "매치된 문자열이 없습니다.";
		}
	}
}
