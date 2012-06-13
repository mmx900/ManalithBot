package org.manalith.ircbot.remote;

import org.manalith.ircbot.ManalithBot;
import org.springframework.stereotype.Component;

@Component("remoteService")
public class RemoteServiceImpl implements RemoteService {

	@Override
	public void sendMessage(String target, String message) {
		ManalithBot.getInstance().sendLoggedMessage(target, message);
	}

}
