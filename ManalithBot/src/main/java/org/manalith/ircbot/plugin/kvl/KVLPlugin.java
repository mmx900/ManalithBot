package org.manalith.ircbot.plugin.kvl;

import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class KVLPlugin extends AbstractBotPlugin {

	public KVLPlugin(ManalithBot bot) {
		super(bot);
	}

	public String getName() {
		return "커널 최신버전";
	}

	public String getNamespace() {
		return "kernel";
	}

	@Override
	public String getHelp() {
		return "!kernel (latest[default]|all|help)";
	}

	@Override
	public void onMessage(MessageEvent event) {
		String msg = event.getMessage();
		String channel = event.getChannel();

		String[] command = msg.split("\\s");

		if (command[0].equals("!kernel")) {
			if (command.length >= 3) {
				bot.sendLoggedMessage(channel, "Too many arguments!");
				event.setExecuted(true);
				return;
			}

			KVLRunner runner = new KVLRunner();

			if (command.length >= 2)
				bot.sendLoggedMessage(channel, runner.run(command[1]));
			else
				bot.sendLoggedMessage(channel, runner.run(""));
			event.setExecuted(true);
		}
	}

}
