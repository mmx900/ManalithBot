package org.manalith.ircbot.plugin.sample;

import org.jibble.pircbot.User;
import org.manalith.ircbot.BotMain;
import org.manalith.ircbot.plugin.IBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class SamplePlugin implements IBotPlugin {

	public String getName() {
		return "Sample Plugin";
	}

	public String getNamespace() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getHelp() {
		return "!친반묘";
	}

	public void onJoin(String channel, String sender, String login,
			String hostname) {
		// TODO Auto-generated method stub

	}

	public void onMessage(MessageEvent event) {
		String message = event.getMessage();
		String channel = event.getChannel();

		if (message.equals("!친반묘")) {
			User[] users = BotMain.BOT.getUsers(channel);
			boolean isMyo = false;
			for (User u : users) {
				if (u.getNick().equals("myojok")) {
					isMyo = true;
					break;
				}
			}
			BotMain.BOT.sendLoggedMessage(channel, isMyo ? "(두리번) ... 친묘!"
					: "(두리번) +_+/ 멸묘!");
			event.setExecuted(true);
		}
	}

	public void onPart(String channel, String sender, String login,
			String hostname) {
		// TODO Auto-generated method stub
		
	}

	public void onQuit(String sourceNick, String sourceLogin,
			String sourceHostname, String reason) {
		// TODO Auto-generated method stub
		
	}

}
