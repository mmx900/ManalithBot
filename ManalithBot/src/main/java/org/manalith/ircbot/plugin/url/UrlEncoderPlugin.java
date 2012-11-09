package org.manalith.ircbot.plugin.url;

import org.apache.commons.lang3.StringUtils;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.manalith.ircbot.util.UrlUtils;
import org.springframework.stereotype.Component;

@Component
public class UrlEncoderPlugin extends AbstractBotPlugin {

	@Override
	public String getName() {
		return "URL 변환";
	}

	@Override
	public String getCommands() {
		return "!urlencode|!urldecode";
	}

	public String getHelp() {
		return "설  명: URL을 유니코드값 문자열로 인코딩하거나 반대로 디코딩 해줍니다, 사용법: !urlencode|!urldecode [URL]";
	}

	@Override
	public void onMessage(MessageEvent event) {
		String[] segments = event.getMessageSegments();
		if (segments.length >= 2) {
			String cmd = segments[0];
			String txt = StringUtils.substring(event.getMessage(),
					"!urlencode ".length());

			if (cmd.equals("!urlencode")) {
				UrlUtils.encode(txt);
			} else if (cmd.equals("!urldecode")) {
				UrlUtils.decode(txt);
			}
		} else if (segments.length == 1) {
			String cmd = segments[0];
			if (cmd.equals("!urlencode") || cmd.equals("!urldecode"))
				event.respond("!urlencode [url] 혹은 !urldecode [url]");
		}
	}
}
