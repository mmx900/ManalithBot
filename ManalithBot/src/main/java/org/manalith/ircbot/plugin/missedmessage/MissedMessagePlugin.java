package org.manalith.ircbot.plugin.missedmessage;

import org.jibble.pircbot.User;
import org.manalith.ircbot.BotMain;
import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.MessageEvent;

public class MissedMessagePlugin extends AbstractBotPlugin {

	public MissedMessagePlugin(ManalithBot bot) {
		super(bot);
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNamespace() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onMessage(MessageEvent event) {
		// TODO <- is lie =3
		String channel = event.getChannel();
		String sender = event.getSender();
		String message = event.getMessage();
		User[] users = BotMain.BOT.getUsers(channel);
		
		String [] cmdnmsg = message.split("\\s");
		
		
		if ( cmdnmsg[0].equals("!msg") )
		{
			if ( cmdnmsg.length == 1 )
				bot.sendLoggedMessage(channel, this.getHelp());
			else if ( cmdnmsg.length == 2 )
			{
				String recv = cmdnmsg[1];
				
				for (User u : users) {
					if (u.getNick().equals(recv)) // check someone who has a matched nick
					{
						bot.sendLoggedMessage(channel, sender + ", ...(물끄럼)...");
						return;
					}
				}
				
				bot.sendLoggedMessage(channel, "남길 메시지가 없습니다");
			}
			else if ( cmdnmsg.length >= 3 )
			{
				String recv = cmdnmsg[1];
				
				for (User u : users) {
					if (u.getNick().equals(recv)) // check someone who has a matched nick
					{
						bot.sendLoggedMessage(channel, sender + ", ...(물끄럼)...");
						return;
					}
				}

				StringBuilder msg = new StringBuilder();
				
				for ( int i = 2 ; i < cmdnmsg.length ; i++ )
				{
					if ( i != 2 ) msg.append(" ");
					msg.append(cmdnmsg[i]);
				}
				
				MissedMessageRunner runner = new MissedMessageRunner ( this.getResourcePath() );
				bot.sendLoggedMessage(channel, runner.addMsg( sender, channel.substring(1) + "." + recv , msg.toString() )); // exclude # in the channel name.
			}
		}
		
		return;
	}
	
	public void onJoin (String channel, String sender, String login,
			String hostname) {
		// TODO Auto-generated method stub
		MissedMessageRunner runner = new MissedMessageRunner ( this.getResourcePath() );
		
		if ( !runner.isMatchedNickinList( channel.substring(1) + "." + sender ) )
			runner.addMsgSlot( channel.substring(1) + "." + sender );
		else
		{
			String [] msgs = runner.getMsg( channel.substring(1) + "." + sender );
		
			if ( msgs != null )
				for ( int i = 0 ; i < msgs.length ; i++ )
				{
					bot.sendLoggedMessage(channel, msgs[i] );
				}
		}
	}
	
	public void onPart(String channel, String sender, String login,
			String hostname) {
		// TODO Auto-generated method stub
		MissedMessageRunner runner = new MissedMessageRunner ( this.getResourcePath() );
		
		if ( !runner.isMatchedNickinList( channel.substring(1) + "." + sender ) )
			runner.addMsgSlot( channel.substring(1) + "." + sender );
	}	
}
