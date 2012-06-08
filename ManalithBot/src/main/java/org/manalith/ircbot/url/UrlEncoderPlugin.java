package org.manalith.ircbot.url;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component
public class UrlEncoderPlugin extends AbstractBotPlugin {
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public String getName() {
		return "URL 변환 플러그인";
	}

	@Override
	public String getCommands() {
		return "!urlencode [url] 혹은 !urldecode [url]";
	}

	@Override
	public void onMessage(MessageEvent event) {
		String[] segments = StringUtils.split(event.getMessage(), " ");
		if (segments.length >= 2) {
			String cmd = segments[0];
			String txt = StringUtils.substring(event.getMessage(),
					"!urlencode ".length());

			try {
				if (cmd.equals("!urlencode")) {
					event.respond(URLEncoder.encode(txt, "UTF-8"));
				} else if (cmd.equals("!urldecode")) {
					event.respond(URLDecoder.decode(txt, "UTF-8"));
				}
			} catch (UnsupportedEncodingException e) {
				// impossible
				logger.error(e);
			}
		}
	}

}
