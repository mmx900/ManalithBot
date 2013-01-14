package org.manalith.ircbot.plugin.repl;

import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Connection.Method;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component
public class ReplPlugin extends AbstractBotPlugin {
	private Logger logger = Logger.getLogger(getClass());
	private final String[] commands = { "!scala", "!java", "!js",
			"!javascript", "!ruby", "!py", "!python", "!groovy" };
	private String serverAddress;

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	@Override
	public String getName() {
		return "REPL";
	}

	@Override
	public String getCommands() {
		return StringUtils.join(commands, "|");
	}

	@Override
	public String getHelp() {
		return getCommands() + " [실행할 스크립트]";
	}

	@Override
	public void onMessage(MessageEvent event) {
		String[] segs = event.getMessageSegments();

		if (ArrayUtils.contains(commands, segs[0])) {
			if (segs.length == 1 || StringUtils.isBlank(segs[1])) {
				event.respond(getHelp());
			} else {
				String type = segs[0];
				String script = segs[1];

				switch (type) {
				case "!scala":
					type = "scala";
					break;
				case "!java":
					type = "java";
					break;
				case "!js":
				case "!javascript":
					type = "javascript";
					break;
				case "!ruby":
					type = "ruby";
					break;
				case "!py":
				case "!python":
					type = "python";
					break;
				case "!groovy":
					type = "groovy";
					break;
				}

				try {
					String result = Jsoup.connect(serverAddress)
							.data("type", type).data("script", script)
							.method(Method.POST).execute().body();

					if (StringUtils.isBlank(result)) {
						result = "결과가 없습니다.";
					} else {
						result = result.replace("\n", "");
					}

					event.respond(result);
				} catch (HttpStatusException e) {
					logger.warn(e.getMessage(), e);
					event.respond(e.getMessage());
				} catch (IOException e) {
					logger.warn(e.getMessage(), e);
					event.respond(e.getMessage());
				}
			}
		}
	}

}
