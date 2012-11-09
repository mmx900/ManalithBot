package org.manalith.ircbot.plugin;

import org.apache.log4j.Logger;
import org.manalith.ircbot.ManalithBot;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.NickChangeEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.events.QuitEvent;
import org.pircbotx.hooks.events.ServerResponseEvent;
import org.pircbotx.hooks.events.TopicEvent;
import org.springframework.stereotype.Component;

@Component
public class EventLogger extends ListenerAdapter<ManalithBot> {
	private final Logger logger = Logger.getLogger(getClass());

	@Override
	public void onConnect(ConnectEvent<ManalithBot> event) throws Exception {
		logger.trace("CONNECT");
	}

	@Override
	public void onDisconnect(DisconnectEvent<ManalithBot> event)
			throws Exception {
		logger.trace("DISCONNECT");
	}

	@Override
	public void onServerResponse(ServerResponseEvent<ManalithBot> event)
			throws Exception {
		logger.trace(String.format("SERVER_RESPONSE: %s / %s", event.getCode(),
				event.getResponse()));
	}

	@Override
	public void onMessage(MessageEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sender = event.getUser().getNick();
		String login = event.getUser().getLogin();
		String hostname = event.getUser().getHostmask();
		String message = event.getMessage();

		logger.trace(String.format("MESSAGE : %s / %s / %s / %s / %s", channel,
				sender, login, hostname, message));
	}

	@Override
	public void onPrivateMessage(PrivateMessageEvent<ManalithBot> event)
			throws Exception {
		logger.trace(String.format("PRIVMSG : %s / %s / %s / %s", event
				.getUser().getNick(), event.getUser().getLogin(), event
				.getUser().getHostmask(), event.getMessage()));
	}

	@Override
	public void onAction(ActionEvent<ManalithBot> event) throws Exception {
		logger.trace(String.format("ACTION : %s / %s / %s / %s / %s", event
				.getChannel().getName(), event.getUser().getNick(), event
				.getUser().getLogin(), event.getUser().getHostmask(), event
				.getAction()));
	}

	@Override
	public void onJoin(JoinEvent<ManalithBot> event) throws Exception {
		logger.trace(String.format("JOIN : %s / %s / %s / %s", event
				.getChannel().getName(), event.getUser().getNick(), event
				.getUser().getLogin(), event.getUser().getHostmask()));
	}

	@Override
	public void onPart(PartEvent<ManalithBot> event) throws Exception {
		logger.trace(String.format("PART : %s / %s / %s / %s", event
				.getChannel().getName(), event.getUser().getNick(), event
				.getUser().getLogin(), event.getUser().getHostmask()));
	}

	@Override
	public void onNickChange(NickChangeEvent<ManalithBot> event)
			throws Exception {
		logger.trace(String.format("NICK_CHANGE : %s / %s / %s / %s", event
				.getOldNick(), event.getUser().getLogin(), event.getUser()
				.getHostmask(), event.getNewNick()));
	}

	@Override
	public void onKick(KickEvent<ManalithBot> event) throws Exception {
		logger.trace(String.format("KICK : %s / %s / %s / %s / %s / %s", event
				.getChannel().getName(), event.getSource().getNick(), event
				.getSource().getLogin(), event.getSource().getHostmask(), event
				.getRecipient().getNick(), event.getReason()));
	}

	@Override
	public void onQuit(QuitEvent<ManalithBot> event) throws Exception {
		logger.trace(String.format("QUIT : %s / %s / %s / %s", event.getUser()
				.getNick(), event.getUser().getLogin(), event.getUser()
				.getHostmask(), event.getReason()));

	}

	@Override
	public void onTopic(TopicEvent<ManalithBot> event) throws Exception {
		logger.trace(String.format("TOPIC : %s / %s / %s / %s / %s", event
				.getChannel().getName(), event.getTopic(), event.getChannel()
				.getTopicSetter(), event.getTimestamp(), event.isChanged()));
	}
}
