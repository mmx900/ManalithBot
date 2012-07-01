package org.manalith.ircbot.plugin.admin;

import java.util.ArrayList;

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
		return "도움말";
	}

	@Override
	public String getCommands() {
		return StringUtils.join(helpCommands, ", ");
	}

	@Override
	public String getHelp() {
		return "설  명: 플러그인들의 목록 및 각 플러그인에 대한 도움말을 출력합니다, 사용법: !명령어|!명령|!도움|!help|!plugins [명령] (명령이름 생략 가능)";
	}

	@Override
	public void onMessage(MessageEvent event) {
		String[] msgs = event.getMessage().split(" ");
		if (ArrayUtils.contains(helpCommands, msgs[0])) {
			String[] arr;
			if (msgs.length == 1 || msgs.length == 2) {
				arr = (msgs.length == 1) ? getPluginInfo("")
						: getPluginInfo(msgs[1]);
				for (String s : arr)
					event.respond(s);
			} else {
				event.respond("너무 많은 값이 있습니다.");
			}
		}
	}

	@Override
	public void onPrivateMessage(MessageEvent event) {
		if (ArrayUtils.contains(helpCommands, event.getMessage())) {
			String[] arr;
			String[] msgs = event.getMessage().split(" ");
			if (msgs.length == 1 || msgs.length == 2) {
				arr = (msgs.length == 1) ? getPluginInfo("")
						: getPluginInfo(msgs[1]);
				for (String s : arr)
					event.respond(s);
			} else {
				event.respond("너무 많은 값이 있습니다.");
			}
		}
	}

	private String[] getPluginInfo(String arg) {
		ArrayList<String> arr = new ArrayList<String>();
		String[] result = null;
		StringBuilder sb = new StringBuilder();
		String name = "";
		// To make well-formed message
		int i = 0;

		if (arg.equals("")) {
			for (IBotPlugin p : pluginManager.getPlugins()) {
				name = p.getName();
				if (name != null) {

					if (sb.length() > 250) {
						sb.append(",");
						arr.add(sb.toString());
						sb.delete(0, sb.length());
						i = 0;
					}

					if (i != 0)
						// To make well-formed message
						sb.append(", ");
					else
						i++;

					sb.append(name);

					String commands = p.getCommands();
					if (StringUtils.isNotBlank(commands))
						sb.append("(" + commands + ")");

				}
			}
			arr.add(sb.toString());
			result = new String[arr.size()];
			arr.toArray(result);
		} else {
			for (IBotPlugin p : pluginManager.getPlugins()) {
				if (p.getCommands() != null) {
					if (p.getCommands().contains(arg)) {
						result = new String[1];
						result[0] = p.getHelp();
						break;
					}
				}
			}

			if (result == null) {
				result = new String[1];
				result[0] = "그런 명령어가 존재하지 않습니다: " + arg;
			}
		}

		return result;
	}
}
