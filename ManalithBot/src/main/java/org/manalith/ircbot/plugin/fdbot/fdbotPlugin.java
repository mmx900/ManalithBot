package org.manalith.ircbot.plugin.fdbot;

import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class fdbotPlugin extends AbstractBotPlugin {

	public fdbotPlugin(ManalithBot bot) {
		super(bot);
	}

	public String getName() {
		return "앞북요정";
	}

	public String getNamespace() {
		return "앞북요정";
	}

	@Override
	public void onMessage(MessageEvent event) {
		String msg = event.getMessage();
		String channel = event.getChannel();

		String[] command = msg.split("\\s");

		int firstidx = 0;
		if (command[0].charAt(0) == '<'
				&& command[0].charAt(command[0].length() - 1) == '>')
			firstidx = 1;

		String[] ans = { "(빈둥~)", "(먼산)", "(뭉기적)", "(꿈틀)", "(뿡=3)", "(>>ㅓ억~~)",
				"(쩝쩝쩝-ㅠ-)", "(...)" };

		if (command[firstidx].contains("앞북요정")) {
			boolean hit = false;
			int i = 1;
			while (i < command.length) {
				if (command[i].contains("빡")
						|| command[i].contains("퍽")
						|| command[i].contains("푹")
						|| command[i].contains("뽝")
						|| ((command[i].contains("빠")
								|| command[i].contains("뽜")
								|| command[i].contains("파") || command[i]
									.contains("퐈")) && (command[i]
								.contains("악") || (command[i].contains("앍"))))) {
					hit = true;
					break;
				}
				i++;
			}
			if (hit)
				bot.sendLoggedMessage(channel, "뒷북요정 산책갔다능! ㅠㅠ");
			else {
				int idx = ((int) (Math.random() * 1000.0) % ans.length);
				bot.sendLoggedMessage(channel, ans[idx]);
			}
		}
	}

}
