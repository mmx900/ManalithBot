package org.manalith.ircbot.plugin;

import org.manalith.ircbot.ManalithBot;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.InviteEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.NickChangeEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.events.QuitEvent;
import org.pircbotx.hooks.events.ServerResponseEvent;
import org.pircbotx.hooks.events.TopicEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EventLogger extends ListenerAdapter<ManalithBot> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void onConnect(ConnectEvent<ManalithBot> event) throws Exception {
		logger.info("CONNECT");
	}

	@Override
	public void onDisconnect(DisconnectEvent<ManalithBot> event)
			throws Exception {
		logger.info("DISCONNECT");
	}

	@Override
	public void onServerResponse(ServerResponseEvent<ManalithBot> event)
			throws Exception {
		logger.info("SERVER_RESPONSE: {} / {}", event.getCode(),
				event.getParsedResponse());
	}

	@Override
	public void onMessage(MessageEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sender = event.getUser().getNick();
		String login = event.getUser().getLogin();
		String hostname = event.getUser().getHostmask();
		String message = event.getMessage();

		logger.info("MESSAGE : {} / {} / {} / {} / {}", channel, sender, login,
				hostname, message);
	}

	@Override
	public void onPrivateMessage(PrivateMessageEvent<ManalithBot> event)
			throws Exception {
		logger.info("PRIVMSG : {} / {} / {} / {}", event.getUser().getNick(),
				event.getUser().getLogin(), event.getUser().getHostmask(),
				event.getMessage());
	}

	@Override
	public void onAction(ActionEvent<ManalithBot> event) throws Exception {
		logger.info("ACTION : {} / {} / {} / {} / {}", event.getChannel()
				.getName(), event.getUser().getNick(), event.getUser()
				.getLogin(), event.getUser().getHostmask(), event.getAction());
	}

	@Override
	public void onJoin(JoinEvent<ManalithBot> event) throws Exception {
		logger.info("JOIN : {} / {} / {} / {}", event.getChannel().getName(),
				event.getUser().getNick(), event.getUser().getLogin(), event
						.getUser().getHostmask());
	}

	@Override
	public void onPart(PartEvent<ManalithBot> event) throws Exception {
		logger.info("PART : {} / {} / {} / {}", event.getChannel().getName(),
				event.getUser().getNick(), event.getUser().getLogin(), event
						.getUser().getHostmask());
	}

	@Override
	public void onNickChange(NickChangeEvent<ManalithBot> event)
			throws Exception {
		logger.info("NICK_CHANGE : {} / {} / {} / {}", event.getOldNick(),
				event.getUser().getLogin(), event.getUser().getHostmask(),
				event.getNewNick());
	}

	@Override
	public void onKick(KickEvent<ManalithBot> event) throws Exception {
		logger.info("KICK : {} / {} / {} / {} / {} / {}", event.getChannel()
				.getName(), event.getUser().getNick(), event.getUser()
				.getLogin(), event.getUser().getHostmask(), event
				.getRecipient().getNick(), event.getReason());
	}

	@Override
	public void onQuit(QuitEvent<ManalithBot> event) throws Exception {
		logger.info("QUIT : {} / {} / {} / {}", event.getUser().getNick(),
				event.getUser().getLogin(), event.getUser().getHostmask(),
				event.getReason());

	}

	@Override
	public void onTopic(TopicEvent<ManalithBot> event) throws Exception {
		logger.info("TOPIC : {} / {} / {} / {} / {}", event.getChannel()
				.getName(), event.getTopic(), event.getChannel()
				.getTopicSetter(), event.getTimestamp(), event.isChanged());
	}

	@Override
	public void onInvite(InviteEvent<ManalithBot> event) throws Exception {
		logger.info("INVITE : {} / {}", event.getChannel(), event.getUser());
	}
}
