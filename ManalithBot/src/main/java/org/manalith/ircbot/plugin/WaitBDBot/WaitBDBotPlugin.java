package org.manalith.ircbot.plugin.WaitBDBot;

import org.jibble.pircbot.User;
import org.manalith.ircbot.BotMain;
import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class WaitBDBotPlugin extends AbstractBotPlugin {

	public WaitBDBotPlugin(ManalithBot bot) {
		super(bot);
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		// TODO Auto-generated method stub
		return "뒷북바라기";
	}

	public String getNamespace() {
		// TODO Auto-generated method stub
		return "뒷북바라기";
	}
	
	public void onMessage(MessageEvent event)
	{
		String channel = event.getChannel(); 
		User[] users = BotMain.BOT.getUsers(channel);
		
		for(User u : users){
			if(u.getNick().equals("뒷북요정")){
				bot.sendLoggedMessage(channel, "후다닥 =3");
				System.exit(0);
			}
		}
	}
	
	public void onJoin ( String channel, String sender, String login, String hostname )
	{
		if ( sender.equals("뒷북요정") )
		{
			bot.sendLoggedMessage(channel, "후다닥 =3");
			System.exit(0);
		}
	}
}
