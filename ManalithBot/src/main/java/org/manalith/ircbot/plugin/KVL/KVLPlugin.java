package org.manalith.ircbot.plugin.KVL;

import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class KVLPlugin extends AbstractBotPlugin {

	public KVLPlugin(ManalithBot bot) {
		super(bot);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "커널 최신버전";
	}

	@Override
	public String getNamespace() {
		// TODO Auto-generated method stub
		return "kernel";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "!kernel (latest[default]|all|help)";
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
				bot.sendLoggedMessage(channel, "Too many arguments!");
				return;
			}
			
			KVLRunner runner = new KVLRunner();
			
			if ( command.length >= 2 )
				bot.sendLoggedMessage(channel, runner.run( command[1] ));
			else
				bot.sendLoggedMessage(channel, runner.run( "" ));
		}
	}

}
