package org.manalith.ircbot.plugin.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component
public class RegexPlugin extends AbstractBotPlugin {

	@Override
	public String getName() {
		return "정규표현식 플러그인";
	}

	@Override
	public String getCommands() {
		return "!regex";
	}

	@Override
	public String getHelp() {
		return "사용법 : !regex [정규표현식] [테스트 문자열]";
	}

	@Override
	public void onMessage(MessageEvent event) {
		String[] segments = event.getMessageSegments();

		if (StringUtils.startsWith(segments[0], "!regex")) {
			if (segments.length < 3) {
				event.respond(getHelp());
			} else {
				String regex = segments[1];
				String fixture = StringUtils.join(segments, " ", 2,
						segments.length);
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(fixture);

				if (matcher.matches()) {
					if (matcher.groupCount() > 0) {
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < matcher.groupCount(); i++) {
							sb.append(" " + i + ":" + matcher.group(i));
						}
						event.respond(sb.toString());
					} else {
						event.respond(matcher.toString());
					}
				} else {
					event.respond("매치된 문자열이 없습니다.");
				}
			}
		}
	}

}
