package org.manalith.ircbot.plugin.url;

import org.manalith.ircbot.annotation.NotNull;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.manalith.ircbot.util.UrlUtils;
import org.springframework.stereotype.Component;

@Component
public class UrlEncoderPlugin extends SimplePlugin {

	@Override
	public String getName() {
		return "URL 변환";
	}

	@Override
	public String getDescription() {
		return "URL을 유니코드값 문자열로 인코딩하거나 반대로 디코딩 해줍니다.";
	}

	@BotCommand
	public String urlEncode(@NotNull String txt) {
		return UrlUtils.encode(txt);
	}

	@BotCommand
	public String urlDecode(@NotNull String txt) {
		return UrlUtils.decode(txt);
	}
}
