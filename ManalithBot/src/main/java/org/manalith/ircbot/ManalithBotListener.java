package org.manalith.ircbot;

import org.apache.log4j.Logger;
import org.manalith.ircbot.command.CommandParser;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.ChannelInfoEvent;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.FileTransferFinishedEvent;
import org.pircbotx.hooks.events.IncomingChatRequestEvent;
import org.pircbotx.hooks.events.IncomingFileTransferEvent;
import org.pircbotx.hooks.events.InviteEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.ModeEvent;
import org.pircbotx.hooks.events.NickChangeEvent;
import org.pircbotx.hooks.events.NoticeEvent;
import org.pircbotx.hooks.events.OpEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.events.QuitEvent;
import org.pircbotx.hooks.events.RemoveChannelBanEvent;
import org.pircbotx.hooks.events.RemoveChannelKeyEvent;
import org.pircbotx.hooks.events.RemoveChannelLimitEvent;
import org.pircbotx.hooks.events.RemoveInviteOnlyEvent;
import org.pircbotx.hooks.events.RemoveModeratedEvent;
import org.pircbotx.hooks.events.RemoveNoExternalMessagesEvent;
import org.pircbotx.hooks.events.RemovePrivateEvent;
import org.pircbotx.hooks.events.RemoveSecretEvent;
import org.pircbotx.hooks.events.RemoveTopicProtectionEvent;
import org.pircbotx.hooks.events.ServerResponseEvent;
import org.pircbotx.hooks.events.SetChannelBanEvent;
import org.pircbotx.hooks.events.SetChannelKeyEvent;
import org.pircbotx.hooks.events.SetChannelLimitEvent;
import org.pircbotx.hooks.events.SetInviteOnlyEvent;
import org.pircbotx.hooks.events.SetModeratedEvent;
import org.pircbotx.hooks.events.SetNoExternalMessagesEvent;
import org.pircbotx.hooks.events.SetPrivateEvent;
import org.pircbotx.hooks.events.SetSecretEvent;
import org.pircbotx.hooks.events.SetTopicProtectionEvent;
import org.pircbotx.hooks.events.TopicEvent;
import org.pircbotx.hooks.events.UserListEvent;
import org.pircbotx.hooks.events.UserModeEvent;
import org.pircbotx.hooks.events.VoiceEvent;

public class ManalithBotListener extends ListenerAdapter {
	private Logger logger = Logger.getLogger(getClass());
	
	private ManalithBot bot;

	public ManalithBotListener(ManalithBot bot) {
		this.bot = bot;
	}

	@Override
	public void onConnect(ConnectEvent event) throws Exception {
		logger.trace("CONNECT");

		// pluginManager.onConnect();
	}

	@Override
	public void onDisconnect(DisconnectEvent event) throws Exception {
		logger.trace("DISCONNECT");

		// pluginManager.onDisconnect();
	}

	@Override
	public void onServerResponse(ServerResponseEvent event) throws Exception {
		logger.trace(String.format("SERVER_RESPONSE: %s / %s", event.getCode(),
				event.getResponse()));

		// pluginManager.onServerResponse(code, response);
	}

	@Override
	public void onUserList(UserListEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onUserList(event);
	}

	@Override
	public void onMessage(MessageEvent event) {
		String channel = event.getChannel().getName();
		String sender = event.getUser().getNick();
		String login = event.getUser().getLogin();
		String hostname = event.getUser().getHostmask();
		String message = event.getMessage();

		logger.trace(String.format("MESSAGE : %s / %s / %s / %s / %s", channel,
				sender, login, hostname, message));

		// 릴레이 메시지일 경우 로컬 메시지로 변환한다.
		// TODO 메시지 필터 구현
		if (sender.equals("♠") || sender.equals("♠_")) {
			sender = CommandParser.getSenderByRelayMessage(message);
			message = CommandParser.convertRelayToLocalMessage(message);
		}

		if (message.equals("!도움") || message.equals("!help")
				|| message.equals("!plugins")) {
			bot.sendMessage(channel, bot.getPluginManager().getPluginInfo());
		} else if (message.equals("!quit")) {
			if (bot.isOwner(sender)) {
				bot.quitServer();
				System.exit(-1);
			}
		} else {
			bot.getPluginManager().onMessage(channel, sender, login, hostname, message);
		}
	}

	@Override
	public void onPrivateMessage(PrivateMessageEvent event) throws Exception {
		String message = event.getMessage();
		String sender = event.getUser().getNick();

		logger.trace(String.format("PRIVMSG : %s / %s / %s / %s", event
				.getUser().getNick(), event.getUser().getLogin(), event
				.getUser().getHostmask(), event.getMessage()));

		if (message.equals("!도움") || message.equals("!help")
				|| message.equals("!plugins")) {
			bot.sendMessage(sender, bot.getPluginManager().getPluginInfo());
		} else {
			bot.getPluginManager().onPrivateMessage(event.getUser().getNick(), event
					.getUser().getLogin(), event.getUser().getHostmask(), event
					.getMessage());
		}
	}

	@Override
	public void onAction(ActionEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onAction(event);
	}

	@Override
	public void onNotice(NoticeEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onNotice(event);
	}

	@Override
	public void onJoin(JoinEvent event) throws Exception {
		logger.trace(String.format("JOIN : %s / %s / %s / %s", event
				.getChannel().getName(), event.getUser().getNick(), event
				.getUser().getLogin(), event.getUser().getHostmask()));

		bot.getPluginManager().onJoin(event.getChannel().getName(), event.getUser()
				.getNick(), event.getUser().getLogin(), event.getUser()
				.getHostmask());
	}

	@Override
	public void onPart(PartEvent event) throws Exception {
		logger.trace(String.format("PART : %s / %s / %s / %s", event
				.getChannel().getName(), event.getUser().getNick(), event
				.getUser().getLogin(), event.getUser().getHostmask()));

		bot.getPluginManager().onPart(event.getChannel().getName(), event.getUser()
				.getNick(), event.getUser().getLogin(), event.getUser()
				.getHostmask());
	}

	@Override
	public void onNickChange(NickChangeEvent event) throws Exception {
		logger.trace(String.format("NICK_CHANGE : %s / %s / %s / %s", event
				.getOldNick(), event.getUser().getLogin(), event.getUser()
				.getHostmask(), event.getNewNick()));

		// pluginManager.onNickChange(oldNick, login, hostname, newNick);
	}

	@Override
	public void onKick(KickEvent event) throws Exception {
		logger.trace(String.format("KICK : %s / %s / %s / %s / %s / %s", event
				.getChannel().getName(), event.getSource().getNick(), event
				.getSource().getLogin(), event.getSource().getHostmask(), event
				.getRecipient().getNick(), event.getReason()));

		// pluginManager.onKick(channel, kickerNick, kickerLogin,
		// kickerHostname, recipientNick, reason);
	}

	@Override
	public void onQuit(QuitEvent event) throws Exception {
		logger.trace(String.format("QUIT : %s / %s / %s / %s", event.getUser()
				.getNick(), event.getUser().getLogin(), event.getUser()
				.getHostmask(), event.getReason()));

		bot.getPluginManager().onQuit(event.getUser().getNick(), event.getUser()
				.getLogin(), event.getUser().getHostmask(), event.getReason());
	}

	@Override
	public void onTopic(TopicEvent event) throws Exception {
		logger.trace(String.format("TOPIC : %s / %s / %s / %s / %s", event
				.getChannel().getName(), event.getTopic(), event.getChannel()
				.getTopicSetter(), event.getTimestamp(), event.isChanged()));

		// pluginManager.onTopic(channel, topic, setBy, date, changed);
	}

	@Override
	public void onChannelInfo(ChannelInfoEvent event) throws Exception {
//		logger.trace(String.format("CHANNEL_INFO : %s / %s / %s", channel,
//				userCount, topic));

		// pluginManager.onTopic(channel, userCount, topic);
	}

	@Override
	public void onMode(ModeEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onMode(event);
	}

	@Override
	public void onUserMode(UserModeEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onUserMode(event);
	}

	@Override
	public void onOp(OpEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onOp(event);
	}

	@Override
	public void onVoice(VoiceEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onVoice(event);
	}

	@Override
	public void onSetChannelKey(SetChannelKeyEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onSetChannelKey(event);
	}

	@Override
	public void onRemoveChannelKey(RemoveChannelKeyEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveChannelKey(event);
	}

	@Override
	public void onSetChannelLimit(SetChannelLimitEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onSetChannelLimit(event);
	}

	@Override
	public void onRemoveChannelLimit(RemoveChannelLimitEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveChannelLimit(event);
	}

	@Override
	public void onSetChannelBan(SetChannelBanEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onSetChannelBan(event);
	}

	@Override
	public void onRemoveChannelBan(RemoveChannelBanEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveChannelBan(event);
	}

	@Override
	public void onSetTopicProtection(SetTopicProtectionEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onSetTopicProtection(event);
	}

	@Override
	public void onRemoveTopicProtection(RemoveTopicProtectionEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveTopicProtection(event);
	}

	@Override
	public void onSetNoExternalMessages(SetNoExternalMessagesEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onSetNoExternalMessages(event);
	}

	@Override
	public void onRemoveNoExternalMessages(RemoveNoExternalMessagesEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveNoExternalMessages(event);
	}

	@Override
	public void onSetInviteOnly(SetInviteOnlyEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onSetInviteOnly(event);
	}

	@Override
	public void onRemoveInviteOnly(RemoveInviteOnlyEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveInviteOnly(event);
	}

	@Override
	public void onSetModerated(SetModeratedEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onSetModerated(event);
	}

	@Override
	public void onRemoveModerated(RemoveModeratedEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveModerated(event);
	}

	@Override
	public void onSetPrivate(SetPrivateEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onSetPrivate(event);
	}

	@Override
	public void onRemovePrivate(RemovePrivateEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onRemovePrivate(event);
	}

	@Override
	public void onSetSecret(SetSecretEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onSetSecret(event);
	}

	@Override
	public void onRemoveSecret(RemoveSecretEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveSecret(event);
	}

	@Override
	public void onInvite(InviteEvent event) throws Exception {
		// TODO Auto-generated method stub
		super.onInvite(event);
	}

	@Override
	public void onIncomingFileTransfer(IncomingFileTransferEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onIncomingFileTransfer(event);
	}

	@Override
	public void onFileTransferFinished(FileTransferFinishedEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onFileTransferFinished(event);
	}

	@Override
	public void onIncomingChatRequest(IncomingChatRequestEvent event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onIncomingChatRequest(event);
	}
}
