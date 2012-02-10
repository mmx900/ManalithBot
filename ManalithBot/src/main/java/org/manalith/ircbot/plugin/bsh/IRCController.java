package org.manalith.ircbot.plugin.bsh;

import org.manalith.ircbot.ManalithBot;

/*
 * Created on 2005. 7. 26
 */

public class IRCController {
	private final String currentUser;
	private final String currentChannel;
	private final ManalithBot bot;
	
	protected IRCController(String currentUserNick, String currentChannel, ManalithBot bot){
		this.currentUser = currentUserNick;
		this.currentChannel = currentChannel;
		this.bot = bot;
	}
	
	
}
