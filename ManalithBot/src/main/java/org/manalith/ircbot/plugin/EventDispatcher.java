package org.manalith.ircbot.plugin;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.manalith.ircbot.ManalithBot;
import org.manalith.ircbot.command.CommandParser;
import org.manalith.ircbot.common.stereotype.BotCommand.BotEvent;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.ChannelInfoEvent;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.IncomingChatRequestEvent;
import org.pircbotx.hooks.events.IncomingFileTransferEvent;
import org.pircbotx.hooks.events.InviteEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.ModeEvent;
import org.pircbotx.hooks.events.NickAlreadyInUseEvent;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventDispatcher extends ListenerAdapter<ManalithBot> {
	public static final String COMMAND_PREFIX = "!";
	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private PluginManager plugins;

	public void dispatchMessageEvent(
			org.manalith.ircbot.resources.MessageEvent event) {

		// 공백으로 구성된 메시지는 처리하지 않는다.
		if (StringUtils.isBlank(event.getMessage())) {
			return;
		}

		String[] segments = event.getMessageSegments();
		String cmd = segments[0];
		String[] params = ArrayUtils.subarray(segments, 1, segments.length);

		// 어노테이션(@BotCommand) 기반 플러그인 실행
		if (cmd.startsWith(COMMAND_PREFIX)) {
			for (Command command : plugins.getCommands().keySet()) {
				if (!command.matches(cmd, BotEvent.ON_MESSAGE))
					continue;

				try {
					event.respond(command.execute(event, params));
				} catch (IllegalArgumentException | IllegalAccessException
						| InvocationTargetException e) {
					logger.warn(e.getMessage(), e);

					event.respond(String.format("실행중 %s 오류가 발생했습니다.",
							e.getMessage()));
				}

				if (event.isExecuted())
					return;
			}
		}

		// 비 어노테이션 기반 플러그인 실행
		for (Plugin plugin : plugins.getPlugins()) {
			plugin.onMessage(event);

			if (event.isExecuted())
				return;
		}
	}

	@Override
	public void onConnect(ConnectEvent<ManalithBot> event) throws Exception {
		// pluginManager.onConnect();
	}

	@Override
	public void onDisconnect(DisconnectEvent<ManalithBot> event)
			throws Exception {
		// pluginManager.onDisconnect();
	}

	@Override
	public void onNickAlreadyInUse(NickAlreadyInUseEvent<ManalithBot> event)
			throws Exception {
		logger.error("닉네임이 이미 사용중입니다.");
	}

	@Override
	public void onServerResponse(ServerResponseEvent<ManalithBot> event)
			throws Exception {
		// pluginManager.onServerResponse(code, response);
	}

	@Override
	public void onUserList(UserListEvent<ManalithBot> event) throws Exception {
		super.onUserList(event);
	}

	@Override
	public void onMessage(MessageEvent<ManalithBot> event) {
		String sender = event.getUser().getNick();
		String message = event.getMessage();

		// 릴레이 메시지일 경우 로컬 메시지로 변환한다.
		// TODO 메시지 필터 구현
		if (sender.equals("♠") || sender.equals("♠_")) {
			sender = CommandParser.getSenderByRelayMessage(message);
			message = CommandParser.convertRelayToLocalMessage(message);
		}

		if (StringUtils.isEmpty(message))
			return;

		org.manalith.ircbot.resources.MessageEvent msg = new org.manalith.ircbot.resources.MessageEvent(
				event);
		msg.setMessage(message);

		dispatchMessageEvent(msg);
	}

	@Override
	public void onPrivateMessage(PrivateMessageEvent<ManalithBot> event)
			throws Exception {

		org.manalith.ircbot.resources.MessageEvent msg = new org.manalith.ircbot.resources.MessageEvent(
				event);
		for (Plugin plugin : plugins.getPlugins()) {
			plugin.onPrivateMessage(msg);
			if (msg.isExecuted())
				break;
		}

	}

	@Override
	public void onAction(ActionEvent<ManalithBot> event) throws Exception {
		super.onAction(event);
	}

	@Override
	public void onNotice(NoticeEvent<ManalithBot> event) throws Exception {
		super.onNotice(event);
	}

	@Override
	public void onJoin(JoinEvent<ManalithBot> event) throws Exception {
		for (Plugin plugin : plugins.getPlugins())
			plugin.onJoin(event.getChannel().getName(), event.getUser()
					.getNick(), event.getUser().getLogin(), event.getUser()
					.getHostmask());
	}

	@Override
	public void onPart(PartEvent<ManalithBot> event) throws Exception {
		for (Plugin plugin : plugins.getPlugins())
			plugin.onPart(event.getChannel().getName(), event.getUser()
					.getNick(), event.getUser().getLogin(), event.getUser()
					.getHostmask());
	}

	@Override
	public void onNickChange(NickChangeEvent<ManalithBot> event)
			throws Exception {
		// pluginManager.onNickChange(oldNick, login, hostname, newNick);
	}

	@Override
	public void onKick(KickEvent<ManalithBot> event) throws Exception {
		// pluginManager.onKick(channel, kickerNick, kickerLogin,
		// kickerHostname, recipientNick, reason);
	}

	@Override
	public void onQuit(QuitEvent<ManalithBot> event) throws Exception {
		for (Plugin plugin : plugins.getPlugins())
			plugin.onQuit(event.getUser().getNick(),
					event.getUser().getLogin(), event.getUser().getHostmask(),
					event.getReason());
	}

	@Override
	public void onTopic(TopicEvent<ManalithBot> event) throws Exception {
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
		super.onMode(event);
	}

	@Override
	public void onUserMode(UserModeEvent<ManalithBot> event) throws Exception {
		super.onUserMode(event);
	}

	@Override
	public void onOp(OpEvent<ManalithBot> event) throws Exception {
		super.onOp(event);
	}

	@Override
	public void onVoice(VoiceEvent<ManalithBot> event) throws Exception {
		super.onVoice(event);
	}

	@Override
	public void onSetChannelKey(SetChannelKeyEvent<ManalithBot> event)
			throws Exception {
		super.onSetChannelKey(event);
	}

	@Override
	public void onRemoveChannelKey(RemoveChannelKeyEvent<ManalithBot> event)
			throws Exception {
		super.onRemoveChannelKey(event);
	}

	@Override
	public void onSetChannelLimit(SetChannelLimitEvent<ManalithBot> event)
			throws Exception {
		super.onSetChannelLimit(event);
	}

	@Override
	public void onRemoveChannelLimit(RemoveChannelLimitEvent<ManalithBot> event)
			throws Exception {
		super.onRemoveChannelLimit(event);
	}

	@Override
	public void onSetChannelBan(SetChannelBanEvent<ManalithBot> event)
			throws Exception {
		super.onSetChannelBan(event);
	}

	@Override
	public void onRemoveChannelBan(RemoveChannelBanEvent<ManalithBot> event)
			throws Exception {
		super.onRemoveChannelBan(event);
	}

	@Override
	public void onSetTopicProtection(SetTopicProtectionEvent<ManalithBot> event)
			throws Exception {
		super.onSetTopicProtection(event);
	}

	@Override
	public void onRemoveTopicProtection(
			RemoveTopicProtectionEvent<ManalithBot> event) throws Exception {
		super.onRemoveTopicProtection(event);
	}

	@Override
	public void onSetNoExternalMessages(
			SetNoExternalMessagesEvent<ManalithBot> event) throws Exception {
		super.onSetNoExternalMessages(event);
	}

	@Override
	public void onRemoveNoExternalMessages(
			RemoveNoExternalMessagesEvent<ManalithBot> event) throws Exception {
		super.onRemoveNoExternalMessages(event);
	}

	@Override
	public void onSetInviteOnly(SetInviteOnlyEvent<ManalithBot> event)
			throws Exception {
		super.onSetInviteOnly(event);
	}

	@Override
	public void onRemoveInviteOnly(RemoveInviteOnlyEvent<ManalithBot> event)
			throws Exception {
		super.onRemoveInviteOnly(event);
	}

	@Override
	public void onSetModerated(SetModeratedEvent<ManalithBot> event)
			throws Exception {
		super.onSetModerated(event);
	}

	@Override
	public void onRemoveModerated(RemoveModeratedEvent<ManalithBot> event)
			throws Exception {
		super.onRemoveModerated(event);
	}

	@Override
	public void onSetPrivate(SetPrivateEvent<ManalithBot> event)
			throws Exception {
		super.onSetPrivate(event);
	}

	@Override
	public void onRemovePrivate(RemovePrivateEvent<ManalithBot> event)
			throws Exception {
		super.onRemovePrivate(event);
	}

	@Override
	public void onSetSecret(SetSecretEvent<ManalithBot> event) throws Exception {
		super.onSetSecret(event);
	}

	@Override
	public void onRemoveSecret(RemoveSecretEvent<ManalithBot> event)
			throws Exception {
		super.onRemoveSecret(event);
	}

	@Override
	public void onInvite(InviteEvent<ManalithBot> event) throws Exception {
		ManalithBot bot = event.getBot();
		if (bot.getManalithBotConfiguration().isAutoAcceptInvite()) {
			bot.sendIRC().joinChannel(event.getChannel());
		}
	}

	@Override
	public void onIncomingFileTransfer(
			IncomingFileTransferEvent<ManalithBot> event) throws Exception {
		super.onIncomingFileTransfer(event);
	}

	@Override
	public void onIncomingChatRequest(
			IncomingChatRequestEvent<ManalithBot> event) throws Exception {
		super.onIncomingChatRequest(event);
	}
}
