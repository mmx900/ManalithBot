package org.manalith.ircbot.plugin.encode;

import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

/**
 * EUC-KR을 UTF8로 인코딩하는 경우 발생할 수 있는 유니코드 대체 문자를 감지해 사용자에게 안내해준다.
 * 
 * @author setzer
 * 
 */
@Component
public class ReplacementCharacterDetectPlugin extends AbstractBotPlugin {

	@Override
	public String getName() {
		return "유니코드 대체 문자 감지 플러그인";
	}

	@Override
	public String getCommands() {
		return null;
	}

	@Override
	public void onMessage(MessageEvent event) {
		if (event.getMessage().startsWith("\uFFFD")) {
			event.getBot().sendLoggedMessage(
					event.getChannel().getName(),
					event.getUser().getNick()
							+ ", You need to configure your client to UTF-8.");
		}
	}

}
