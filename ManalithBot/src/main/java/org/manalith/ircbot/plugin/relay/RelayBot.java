package org.manalith.ircbot.plugin.relay;

import org.jibble.pircbot.PircBot;
import org.manalith.ircbot.BotMain;

public class RelayBot extends PircBot {

	public RelayBot(String botName) {
		setName(botName);
	}
	
	@Override
	protected void onMessage(String channel,
			String sender,
			String login,
			String hostname,
			String message){
		if(RelayPlugin.isRelaying()){
			/**
			 * 릴레이봇에 메시지가 들어오면 메인 봇으로 전송한다.
			 */
			String targetChannel = "#setzer";
			
			if(channel.equals("#gnome")){
				targetChannel = "#gnome";
			}else if(channel.equals("#setzer")){
				targetChannel = "#setzer";
			}
			
			BotMain.BOT.sendLoggedMessage(targetChannel, "<" + sender + "> " + message, false);
		}
	}
	
}
