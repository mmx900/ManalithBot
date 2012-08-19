package org.manalith.ircbot.plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.command.CommandParser;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.common.stereotype.BotCommand.BotEvent;
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

public class EventDispatcher extends ListenerAdapter<ManalithBot> {
	private final Logger logger = Logger.getLogger(getClass());
	private PluginManager pluginManager;

	public EventDispatcher(PluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}

	@Override
	public void onConnect(ConnectEvent<ManalithBot> event) throws Exception {
		logger.trace("CONNECT");

		// pluginManager.onConnect();
	}

	@Override
	public void onDisconnect(DisconnectEvent<ManalithBot> event)
			throws Exception {
		logger.trace("DISCONNECT");

		// pluginManager.onDisconnect();
	}

	@Override
	public void onServerResponse(ServerResponseEvent<ManalithBot> event)
			throws Exception {
		logger.trace(String.format("SERVER_RESPONSE: %s / %s", event.getCode(),
				event.getResponse()));

		// pluginManager.onServerResponse(code, response);
	}

	@Override
	public void onUserList(UserListEvent<ManalithBot> event) throws Exception {
		// TODO Auto-generated method stub
		super.onUserList(event);
	}

	@Override
	public void onMessage(MessageEvent<ManalithBot> event) {
		ManalithBot bot = event.getBot();
		String channel = event.getChannel().getName();
		String sender = event.getUser().getNick();
		String login = event.getUser().getLogin();
		String hostname = event.getUser().getHostmask();
		String message = event.getMessage();

		logger.trace(String.format("MESSAGE : %s / %s / %s / %s / %s", channel,
				sender, login, hostname, message));

		org.manalith.ircbot.resources.MessageEvent msg = null;

		// 릴레이 메시지일 경우 로컬 메시지로 변환한다.
		// TODO 메시지 필터 구현
		if (sender.equals("♠") || sender.equals("♠_")) {
			sender = CommandParser.getSenderByRelayMessage(message);
			message = CommandParser.convertRelayToLocalMessage(message);
			org.pircbotx.hooks.events.MessageEvent<ManalithBot> newEvent = new org.pircbotx.hooks.events.MessageEvent<ManalithBot>(
					bot, event.getChannel(), event.getUser(), message);
			msg = new org.manalith.ircbot.resources.MessageEvent(newEvent);
		}

		if (StringUtils.isEmpty(message))
			return;

		if (msg == null) {
			msg = new org.manalith.ircbot.resources.MessageEvent(event);
		}

		// 어노테이션(@BotCommand) 기반 플러그인 실행
		for (Method method : pluginManager.getCommands().keySet()) {
			BotCommand commandMeta = method.getAnnotation(BotCommand.class);

			String[] segments = StringUtils
					.splitByWholeSeparator(message, null);

			if (!ArrayUtils.contains(commandMeta.listeners(),
					BotEvent.ON_MESSAGE))
				continue;

			if (!ArrayUtils.contains(commandMeta.value(), segments[0]))
				continue;

			IBotPlugin plugin = (IBotPlugin) pluginManager.getCommands().get(
					method);

			if (segments.length - 1 < commandMeta.minimumArguments()) {
				bot.sendLoggedMessage(
						channel,
						String.format("실행에 필요한 인자의 수는 최소 %d 개입니다.",
								commandMeta.minimumArguments()));

				msg.setExecuted(true);
			} else {
				try {
					String result = null;

					// TODO MethodUtils 사용
					if (method.getParameterTypes().length == 0) {
						result = (String) method.invoke(plugin);
					} else if (method.getParameterTypes().length == 1) {
						if (method.getParameterTypes()[0] == MessageEvent.class) {
							result = (String) method.invoke(plugin, msg);
						} else {
							result = (String) method.invoke(plugin,
									(Object) ArrayUtils.subarray(segments, 1,
											segments.length));
						}
					} else {
						result = (String) method.invoke(plugin, msg, ArrayUtils
								.subarray(segments, 1, segments.length));
					}

					if (StringUtils.isNotBlank(result)) {
						bot.sendLoggedMessage(channel, result);
					}

					msg.setExecuted(commandMeta.stopEvent());
				} catch (IllegalArgumentException e) {
					logger.error(e);
					bot.sendLoggedMessage(channel,
							String.format("실행중 %s 오류가 발생했습니다.", e.getMessage()));
					msg.setExecuted(true);
				} catch (IllegalAccessException e) {
					logger.error(e);
					bot.sendLoggedMessage(channel,
							String.format("실행중 %s 오류가 발생했습니다.", e.getMessage()));
					msg.setExecuted(true);
				} catch (InvocationTargetException e) {
					logger.error(e);
					bot.sendLoggedMessage(channel,
							String.format("실행중 %s 오류가 발생했습니다.", e.getMessage()));
					msg.setExecuted(true);
				}
			}

			if (msg.isExecuted())
				return;
		}

		// 비 어노테이션 기반 플러그인 실행
		for (IBotPlugin plugin : pluginManager.getPlugins()) {
			plugin.onMessage(msg);

			if (msg.isExecuted())
				return;
		}
	}

	@Override
	public void onPrivateMessage(PrivateMessageEvent<ManalithBot> event)
			throws Exception {

		logger.trace(String.format("PRIVMSG : %s / %s / %s / %s", event
				.getUser().getNick(), event.getUser().getLogin(), event
				.getUser().getHostmask(), event.getMessage()));

		org.manalith.ircbot.resources.MessageEvent msg = new org.manalith.ircbot.resources.MessageEvent(
				event);
		for (IBotPlugin plugin : pluginManager.getPlugins()) {
			plugin.onPrivateMessage(msg);
			if (msg.isExecuted())
				break;
		}

	}

	@Override
	public void onAction(ActionEvent<ManalithBot> event) throws Exception {
		logger.trace(String.format("ACTION : %s / %s / %s / %s / %s", event
				.getChannel().getName(), event.getUser().getNick(), event
				.getUser().getLogin(), event.getUser().getHostmask(), event
				.getAction()));
		super.onAction(event);
	}

	@Override
	public void onNotice(NoticeEvent<ManalithBot> event) throws Exception {
		// TODO Auto-generated method stub
		super.onNotice(event);
	}

	@Override
	public void onJoin(JoinEvent<ManalithBot> event) throws Exception {
		logger.trace(String.format("JOIN : %s / %s / %s / %s", event
				.getChannel().getName(), event.getUser().getNick(), event
				.getUser().getLogin(), event.getUser().getHostmask()));

		for (IBotPlugin plugin : pluginManager.getPlugins())
			plugin.onJoin(event.getChannel().getName(), event.getUser()
					.getNick(), event.getUser().getLogin(), event.getUser()
					.getHostmask());
	}

	@Override
	public void onPart(PartEvent<ManalithBot> event) throws Exception {
		logger.trace(String.format("PART : %s / %s / %s / %s", event
				.getChannel().getName(), event.getUser().getNick(), event
				.getUser().getLogin(), event.getUser().getHostmask()));

		for (IBotPlugin plugin : pluginManager.getPlugins())
			plugin.onPart(event.getChannel().getName(), event.getUser()
					.getNick(), event.getUser().getLogin(), event.getUser()
					.getHostmask());
	}

	@Override
	public void onNickChange(NickChangeEvent<ManalithBot> event)
			throws Exception {
		logger.trace(String.format("NICK_CHANGE : %s / %s / %s / %s", event
				.getOldNick(), event.getUser().getLogin(), event.getUser()
				.getHostmask(), event.getNewNick()));

		// pluginManager.onNickChange(oldNick, login, hostname, newNick);
	}

	@Override
	public void onKick(KickEvent<ManalithBot> event) throws Exception {
		logger.trace(String.format("KICK : %s / %s / %s / %s / %s / %s", event
				.getChannel().getName(), event.getSource().getNick(), event
				.getSource().getLogin(), event.getSource().getHostmask(), event
				.getRecipient().getNick(), event.getReason()));

		// pluginManager.onKick(channel, kickerNick, kickerLogin,
		// kickerHostname, recipientNick, reason);
	}

	@Override
	public void onQuit(QuitEvent<ManalithBot> event) throws Exception {
		logger.trace(String.format("QUIT : %s / %s / %s / %s", event.getUser()
				.getNick(), event.getUser().getLogin(), event.getUser()
				.getHostmask(), event.getReason()));

		for (IBotPlugin plugin : pluginManager.getPlugins())
			plugin.onQuit(event.getUser().getNick(),
					event.getUser().getLogin(), event.getUser().getHostmask(),
					event.getReason());
	}

	@Override
	public void onTopic(TopicEvent<ManalithBot> event) throws Exception {
		logger.trace(String.format("TOPIC : %s / %s / %s / %s / %s", event
				.getChannel().getName(), event.getTopic(), event.getChannel()
				.getTopicSetter(), event.getTimestamp(), event.isChanged()));

		// pluginManager.onTopic(channel, topic, setBy, date, changed);
	}

	@Override
	public void onChannelInfo(ChannelInfoEvent<ManalithBot> event)
			throws Exception {
		// logger.trace(String.format("CHANNEL_INFO : %s / %s / %s", channel,
		// userCount, topic));

		// pluginManager.onTopic(channel, userCount, topic);
	}

	@Override
	public void onMode(ModeEvent<ManalithBot> event) throws Exception {
		// TODO Auto-generated method stub
		super.onMode(event);
	}

	@Override
	public void onUserMode(UserModeEvent<ManalithBot> event) throws Exception {
		// TODO Auto-generated method stub
		super.onUserMode(event);
	}

	@Override
	public void onOp(OpEvent<ManalithBot> event) throws Exception {
		// TODO Auto-generated method stub
		super.onOp(event);
	}

	@Override
	public void onVoice(VoiceEvent<ManalithBot> event) throws Exception {
		// TODO Auto-generated method stub
		super.onVoice(event);
	}

	@Override
	public void onSetChannelKey(SetChannelKeyEvent<ManalithBot> event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onSetChannelKey(event);
	}

	@Override
	public void onRemoveChannelKey(RemoveChannelKeyEvent<ManalithBot> event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveChannelKey(event);
	}

	@Override
	public void onSetChannelLimit(SetChannelLimitEvent<ManalithBot> event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onSetChannelLimit(event);
	}

	@Override
	public void onRemoveChannelLimit(RemoveChannelLimitEvent<ManalithBot> event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveChannelLimit(event);
	}

	@Override
	public void onSetChannelBan(SetChannelBanEvent<ManalithBot> event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onSetChannelBan(event);
	}

	@Override
	public void onRemoveChannelBan(RemoveChannelBanEvent<ManalithBot> event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveChannelBan(event);
	}

	@Override
	public void onSetTopicProtection(SetTopicProtectionEvent<ManalithBot> event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onSetTopicProtection(event);
	}

	@Override
	public void onRemoveTopicProtection(
			RemoveTopicProtectionEvent<ManalithBot> event) throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveTopicProtection(event);
	}

	@Override
	public void onSetNoExternalMessages(
			SetNoExternalMessagesEvent<ManalithBot> event) throws Exception {
		// TODO Auto-generated method stub
		super.onSetNoExternalMessages(event);
	}

	@Override
	public void onRemoveNoExternalMessages(
			RemoveNoExternalMessagesEvent<ManalithBot> event) throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveNoExternalMessages(event);
	}

	@Override
	public void onSetInviteOnly(SetInviteOnlyEvent<ManalithBot> event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onSetInviteOnly(event);
	}

	@Override
	public void onRemoveInviteOnly(RemoveInviteOnlyEvent<ManalithBot> event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveInviteOnly(event);
	}

	@Override
	public void onSetModerated(SetModeratedEvent<ManalithBot> event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onSetModerated(event);
	}

	@Override
	public void onRemoveModerated(RemoveModeratedEvent<ManalithBot> event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveModerated(event);
	}

	@Override
	public void onSetPrivate(SetPrivateEvent<ManalithBot> event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onSetPrivate(event);
	}

	@Override
	public void onRemovePrivate(RemovePrivateEvent<ManalithBot> event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onRemovePrivate(event);
	}

	@Override
	public void onSetSecret(SetSecretEvent<ManalithBot> event) throws Exception {
		// TODO Auto-generated method stub
		super.onSetSecret(event);
	}

	@Override
	public void onRemoveSecret(RemoveSecretEvent<ManalithBot> event)
			throws Exception {
		// TODO Auto-generated method stub
		super.onRemoveSecret(event);
	}

	@Override
	public void onInvite(InviteEvent<ManalithBot> event) throws Exception {
		// TODO Auto-generated method stub
		super.onInvite(event);
	}

	@Override
	public void onIncomingFileTransfer(
			IncomingFileTransferEvent<ManalithBot> event) throws Exception {
		// TODO Auto-generated method stub
		super.onIncomingFileTransfer(event);
	}

	@Override
	public void onFileTransferFinished(
			FileTransferFinishedEvent<ManalithBot> event) throws Exception {
		// TODO Auto-generated method stub
		super.onFileTransferFinished(event);
	}

	@Override
	public void onIncomingChatRequest(
			IncomingChatRequestEvent<ManalithBot> event) throws Exception {
		// TODO Auto-generated method stub
		super.onIncomingChatRequest(event);
	}
}
