package org.manalith.ircbot.plugin.KVL;

import org.manalith.ircbot.BotMain;
import org.manalith.ircbot.plugin.IBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class KVLPlugin implements IBotPlugin {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "커널최신버전";
	}

	@Override
	public String getNamespace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "!kernel (latest[default]|all|help)";
	}

	@Override
	public void onJoin(String channel, String sender, String login,
			String hostname) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessage(MessageEvent event) {
		// TODO Auto-generated method stub
		String msg = event.getMessage();
		String channel = event.getChannel();
		
		String [] command = msg.split("\\s");
		
		if ( command[0].equals("!kernel") )
		{
			if ( command.length >= 3 )
			{
				BotMain.BOT.sendLoggedMessage(channel, "Too many arguments!");
				return;
			}
			
			KVLRunner runner = new KVLRunner();
			
			if ( command.length >= 2 )
				BotMain.BOT.sendLoggedMessage(channel, runner.run( command[1] ));
			else
				BotMain.BOT.sendLoggedMessage(channel, runner.run( "" ));
		}
	}

	@Override
	public void onPart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onQuit() {
		// TODO Auto-generated method stub

	}

}
