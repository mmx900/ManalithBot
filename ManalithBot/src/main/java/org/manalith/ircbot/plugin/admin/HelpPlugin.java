package org.manalith.ircbot.plugin.admin;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.manalith.ircbot.plugin.Plugin;
import org.manalith.ircbot.plugin.PluginManager;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class HelpPlugin extends SimplePlugin {

	private static final String[] HELP_COMMANDS = new String[] { "!명령어", "!명령",
			"!도움", "!도움말", "!help", "!plugins" };
	private PluginManager pluginManager;

	public HelpPlugin(PluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}

	@Override
	public String getName() {
		return "도움말";
	}

	@Override
	public String getCommands() {
		return StringUtils.join(HELP_COMMANDS, ", ");
	}

	@Override
	public String getHelp() {
		return "설  명: 플러그인들의 목록 및 각 플러그인에 대한 도움말을 출력합니다. 사용법: !명령어|!명령|!도움|!help|!plugins [명령] (명령이름 생략 가능)";
	}

	@Override
	public void onMessage(MessageEvent event) {
		String result = parseMessage(event.getMessage());
		if (result != null)
			event.respond(result);
	}

	@Override
	public void onPrivateMessage(MessageEvent event) {
		String result = parseMessage(event.getMessage());
		if (result != null)
			event.respond(result);
	}

	private String parseMessage(String message) {
		String[] msgs = StringUtils.split(message);
		if (ArrayUtils.contains(HELP_COMMANDS, msgs[0])) {
			if (msgs.length == 1) {
				return getPluginInfo();
			} else if (msgs.length == 2) {
				return getPluginInfo(msgs[1]);
			} else {
				return "너무 많은 값이 있습니다.";
			}
		} else {
			return null;
		}
	}

	private String getPluginInfo() {
		ArrayList<String> plugins = new ArrayList<String>();

		for (Plugin p : pluginManager.getPlugins()) {
			String name = p.getName();
			String commands = p.getCommands();

			if (StringUtils.isNotBlank(commands)) {
				plugins.add(String.format("%s(%s)", name, commands));
			} else {
				plugins.add(name);
			}
		}

		return StringUtils.join(plugins, ", ");
	}

	private String getPluginInfo(String command) {
		for (Plugin p : pluginManager.getPlugins())
			if (StringUtils.contains(p.getCommands(), command))
				return p.getHelp();

		return String.format("그런 명령어가 존재하지 않습니다: %s", command);
	}
}
