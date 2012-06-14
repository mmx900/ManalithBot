package org.manalith.ircbot.plugin.admin;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.plugin.IBotPlugin;
import org.manalith.ircbot.plugin.PluginManager;
import org.manalith.ircbot.resources.MessageEvent;

public class HelpPlugin extends AbstractBotPlugin {
	private final String[] helpCommands = new String[] { "!명령어", "!명령", "!도움",
			"!help", "!plugins" };
	private PluginManager pluginManager;

	public HelpPlugin(PluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}

	@Override
	public String getName() {
		return "도움말 플러그인";
	}

	@Override
	public String getCommands() {
		return StringUtils.join(helpCommands, ", ");
	}

	@Override
	public String getHelp() {
		return "플러그인들의 목록 및 도움말을 출력합니다.";
	}

	@Override
	public void onMessage(MessageEvent event) {
		if (ArrayUtils.contains(helpCommands, event.getMessage())) {
			event.respond(getPluginInfo());
		}
	}

	@Override
	public void onPrivateMessage(MessageEvent event) {
		if (ArrayUtils.contains(helpCommands, event.getMessage())) {
			event.respond(getPluginInfo());
		}
	}

	private String getPluginInfo() {
		StringBuilder sb = new StringBuilder();
		String name = "";
		int i = 0; // To make well-formed message
		for (IBotPlugin p : pluginManager.getPlugins()) {
			name = p.getName();
			if (name != null) {
				if (i != 0)
					sb.append(", "); // To make well-formed message
				else
					i++;

				sb.append(name);

				String commands = p.getCommands();
				if (StringUtils.isNotBlank(commands)) {
					sb.append("(" + commands + ")");
				}
			}
		}

		return sb.toString();
	}
}
