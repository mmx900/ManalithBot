/*
 	org.manalith.ircbot/ManalithBotEvent.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2012  Seong-ho, Cho <darkcircle.0426@gmail.com>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

// NOTE If you want to modify, please refer http://site.pircbotx.googlecode.com/hg-history/1.6/apidocs/org/pircbotx/hooks/ListenerAdapter.html

package org.manalith.ircbot;

import org.apache.log4j.Logger;

import org.manalith.ircbot.command.CommandParser;
import org.manalith.ircbot.plugin.PluginManager;
//import org.pircbotx.Channel;
//import org.pircbotx.DccChat;
//import org.pircbotx.DccFileTransfer;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.ListenerAdapter;

import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.ChannelInfoEvent;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
//import org.pircbotx.hooks.events.FileTransferFinishedEvent;
//import org.pircbotx.hooks.events.IncomingChatRequestEvent;
//import org.pircbotx.hooks.events.IncomingFileTransferEvent;
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

public class ManalithBotEvent extends ListenerAdapter<ManalithBot> implements
		Listener<ManalithBot> {
	private Logger logger;
	private String[] owners;
	private PluginManager pluginManager;

	public ManalithBotEvent(String[] newOwners, Logger newLogger,
			PluginManager newPluginManager) {
		this.setLogger(newLogger);
		this.setOwnerList(newOwners);
		this.setPluginManater(newPluginManager);
	}

	public void setLogger(Logger newLogger) {
		this.logger = newLogger;
	}

	public void setOwnerList(String[] newOwners) {
		this.owners = new String[newOwners.length];
		System.arraycopy(newOwners, 0, this.owners, 0, newOwners.length);
	}

	public void setPluginManater(PluginManager newPluginManager) {
		this.pluginManager = newPluginManager;
	}

	private boolean isOwner(String nick) {
		// TODO nick(String) 비교가 아닌 User 비교로
		for (String s : owners) {
			if (s.equals(nick))
				return true;
		}
		return false;
	}

	public void onConnect(ConnectEvent<ManalithBot> event) {
		logger.trace("CONNECT");

		// pluginManager.onConnect();
	}

	@Override
	public void onDisconnect(DisconnectEvent<ManalithBot> event) {
		logger.trace("DISCONNECT");

		// pluginManager.onDisconnect();
	}

	@Override
	public void onServerResponse(ServerResponseEvent<ManalithBot> event) {
		logger.trace(String.format("SERVER_RESPONSE: %s / %s",
				Integer.toString(event.getCode()), event.getResponse()));

		// pluginManager.onServerResponse(code, response);
	}

	@Override
	public void onUserList(UserListEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		User[] users = new User[event.getChannel().getUsers().size()];
		event.getChannel().getUsers().toArray(users);
		
		logger.trace(String.format("USER_LIST : %s / %s", channel, users[0].getNick() + " and " + (users.length - 1) + " more."));
	}

	// *
	@Override
	public void onMessage(MessageEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sender = event.getUser().getNick();
		String login = event.getUser().getLogin();
		String hostname = event.getBot().getServer();
		String message = event.getMessage();

		logger.trace(String.format("MESSAGE : %s / %s / %s / %s / %s", channel,
				sender, login, hostname, message));

		// 릴레이 메시지일 경우 로컬 메시지로 변환한다.
		// TODO 메시지 필터 구현
		if (sender.equals("♣오씨네") || sender.equals("♣오씨네가건물")) {
			sender = CommandParser.getSenderByRelayMessage(message);
			message = CommandParser.convertRelayToLocalMessage(message);
		}

		if (message.equals("!도움") || message.equals("!help")
				|| message.equals("!plugins")) {
			event.respond(pluginManager.getPluginInfo()); // is for the pircbotx
			// sendMessage(channel, pluginManager.getPluginInfo()); 
			// is for the pircbot
		} else if (message.equals("!quit")) {
			if (isOwner(sender)) {
				event.getBot().quitServer(); // is for the pircbotx
				// quitServer(); // is for the pircbot
				System.exit(-1);
			}
		} else {

			pluginManager.onMessage(event);
		}
	}

	// */
	@Override
	public void onPrivateMessage(PrivateMessageEvent<ManalithBot> event) {

		String sender = event.getUser().getNick();
		String login = event.getUser().getLogin();
		String hostname = event.getUser().getServer();
		String message = event.getMessage();

		logger.trace(String.format("PRIVMSG : %s / %s / %s / %s", sender,
				login, hostname, message));
		if (message.equals("!도움") || message.equals("!help")
				|| message.equals("!plugins")) {

			event.respond(pluginManager.getPluginInfo()); // is for the pircbotx
			// sendMessage(sender, pluginManager.getPluginInfo()); // is for the pircbot
		} else {
			pluginManager.onPrivateMessage(event);
		}
	}

	@Override
	public void onAction(ActionEvent<ManalithBot> event) {
		String sender = event.getUser().getNick();
		String login = event.getUser().getLogin();
		String hostname = event.getUser().getServer();
		String target = event.getChannel().getName();
		String action = event.getAction();

		logger.trace(String.format("NOTICE : %s / %s / %s / %s / %s", sender,
				login, hostname, target, action));

		pluginManager.onAction(event);
	}

	@Override
	public void onNotice(NoticeEvent<ManalithBot> event) {
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();
		String target;
		if ( event.getChannel() != null )   
			target = event.getChannel().getName();
		else
			target = "";
		String notice = event.getNotice();

		logger.trace(String.format("NOTICE : %s / %s / %s / %s / %s",
				sourceNick, sourceLogin, sourceHostname, target, notice));
	}

	@Override
	public void onJoin(JoinEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sender = event.getUser().getNick();
		String login = event.getUser().getLogin();
		String hostname = event.getUser().getServer();

		logger.trace(String.format("JOIN : %s / %s / %s / %s", channel, sender,
				login, hostname));

		pluginManager.onJoin(event);
	}

	@Override
	public void onPart(PartEvent<ManalithBot> event) {

		String channel = event.getChannel().getName();
		String sender = event.getUser().getNick();
		String login = event.getUser().getLogin();
		String hostname = event.getUser().getServer();

		logger.trace(String.format("PART : %s / %s / %s / %s", channel, sender,
				login, hostname));

		pluginManager.onPart(event);
	}

	@Override
	public void onNickChange(NickChangeEvent<ManalithBot> event) {

		String oldNick = event.getOldNick();
		String login = event.getUser().getLogin();
		String hostname = event.getUser().getServer();
		String newNick = event.getNewNick();

		logger.trace(String.format("NICK_CHANGE : %s / %s / %s / %s", oldNick,
				login, hostname, newNick));

		// pluginManager.onNickChange(oldNick, login, hostname, newNick);
	}

	@Override
	public void onKick(KickEvent<ManalithBot> event) {

		String channel = event.getChannel().getName();
		String kickerNick = event.getSource().getNick();
		String kickerLogin = event.getSource().getLogin();
		String kickerHostname = event.getSource().getServer();
		String recipientNick = event.getRecipient().getNick();
		String reason = event.getReason();

		logger.trace(String.format("KICK : %s / %s / %s / %s / %s / %s",
				channel, kickerNick, kickerLogin, kickerHostname,
				recipientNick, reason));

		// pluginManager.onKick(channel, kickerNick, kickerLogin,
		// kickerHostname, recipientNick, reason);
	}

	@Override
	public void onQuit(QuitEvent<ManalithBot> event) {
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();
		String reason = event.getReason();

		logger.trace(String.format("QUIT : %s / %s / %s / %s", sourceNick,
				sourceLogin, sourceHostname, reason));
		
		pluginManager.onQuit(event);
	}

	@Override
	public void onTopic(TopicEvent<ManalithBot> event) {

		String channel = event.getChannel().getName();
		String topic = event.getTopic();
		String setBy = event.getUser().getNick();
		long date = event.getDate();
		boolean changed = event.isChanged();

		logger.trace(String.format("TOPIC : %s / %s / %s / %s / %s", channel,
				topic, setBy, date, changed));

		// pluginManager.onTopic(channel, topic, setBy, date, changed);
	}

	@Override
	public void onChannelInfo(ChannelInfoEvent<ManalithBot> event) {
		// TODO: Under construction
		/*
		 * String channel = event. int userCount = String topic =
		 * 
		 * logger.trace(String.format("CHANNEL_INFO : %s / %s / %s", channel,
		 * userCount, topic));
		 */
		// pluginManager.onTopic(channel, userCount, topic);
	}

	public void onMode(ModeEvent<ManalithBot> event) {

		String channel = event.getChannel().getName();
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();
		String mode = event.getMode();

		logger.trace(String.format("MODE : %s / %s / %s / %s / %s", channel,
				sourceNick, sourceLogin, sourceHostname, mode));
	}

	@Override
	public void onUserMode(UserModeEvent<ManalithBot> event) {
		String targetNick = event.getTarget().getNick();
		String sourceNick = event.getSource().getNick();
		String sourceLogin = event.getSource().getLogin();
		String sourceHostname = event.getSource().getServer();
		String mode = event.getMode();

		logger.trace(String.format("USERMODE : %s / %s / %s / %s / %s",
				targetNick, sourceNick, sourceLogin, sourceHostname, mode));
	}

	@Override
	public void onOp(OpEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sourceNick = event.getSource().getNick();
		String sourceLogin = event.getSource().getLogin();
		String sourceHostname = event.getSource().getServer();
		String recipient = event.getRecipient().getNick();

		// This variable is added for the pircbotx
		boolean isOp = event.isOp();

		logger.trace(String.format("OP : %s / %s / %s / %s / %s / %s", channel,
				sourceNick, sourceLogin, sourceHostname, recipient, isOp));
	}

	// @Override
	// public void onDeop(String channel, String sourceNick, String sourceLogin,
	// String sourceHostname, String recipient) {
	//
	// } // deprecated in the pircbotx. do not delete for ready to recover

	@Override
	public void onVoice(VoiceEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sourceNick = event.getSource().getNick();
		String sourceLogin = event.getSource().getLogin();
		String sourceHostname = event.getSource().getServer();
		String recipient = event.getRecipient().getNick();

		// This variable is added for the pircbotx
		boolean hasVoice = event.hasVoice();

		logger.trace(String.format("VOICE : %s / %s / %s / %s / %s / %s",
				channel, sourceNick, sourceLogin, sourceHostname, recipient,
				hasVoice));
	}

	// @Override
	// public void onDeVoice(String channel, String sourceNick,
	// String sourceLogin, String sourceHostname, String recipient) {
	//
	// } // deprecated in the pircbotx do not delete for ready to recover

	@Override
	public void onSetChannelKey(SetChannelKeyEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();
		String key = event.getKey();

		logger.trace(String.format("SET_CHAN_KEY : %s / %s / %s / %s / %s",
				channel, sourceNick, sourceLogin, sourceHostname, key));
	}

	public void onRemoveChannelKey(RemoveChannelKeyEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();
		String key = event.getKey();

		logger.trace(String.format("REM_CHAN_KEY : %s / %s / %s / %s / %s",
				channel, sourceNick, sourceLogin, sourceHostname, key));

	}

	public void onSetChannelLimit(SetChannelLimitEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();
		int limit = event.getLimit();

		logger.trace(String.format("SET_CHAN_LIM : %s / %s / %s / %s / %s",
				channel, sourceNick, sourceLogin, sourceHostname, limit));
	}

	@Override
	public void onRemoveChannelLimit(RemoveChannelLimitEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();

		logger.trace(String.format("REM_CHAN_LIM : %s / %s / %s / %s", channel,
				sourceNick, sourceLogin, sourceHostname));
	}

	@Override
	public void onSetChannelBan(SetChannelBanEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();
		String hostmask = event.getHostmask();

		logger.trace(String.format("SET_CHAN_BAN : %s / %s / %s / %s / %s",
				channel, sourceNick, sourceLogin, sourceHostname, hostmask));
	}

	@Override
	public void onRemoveChannelBan(RemoveChannelBanEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();
		String hostmask = event.getHostmask();

		logger.trace(String.format("REM_CHAN_BAN : %s / %s / %s / %s / %s",
				channel, sourceNick, sourceLogin, sourceHostname, hostmask));
	}

	@Override
	public void onSetTopicProtection(SetTopicProtectionEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();

		logger.trace(String.format("SET_TOPIC_PROT : %s / %s / %s / %s",
				channel, sourceNick, sourceLogin, sourceHostname));
	}

	@Override
	public void onRemoveTopicProtection(
			RemoveTopicProtectionEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();

		logger.trace(String.format("REM_TOPIC_PROT : %s / %s / %s / %s",
				channel, sourceNick, sourceLogin, sourceHostname));
	}

	@Override
	public void onSetNoExternalMessages(
			SetNoExternalMessagesEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();

		logger.trace(String.format("SET_NO_EXT_MSG : %s / %s / %s / %s",
				channel, sourceNick, sourceLogin, sourceHostname));
	}

	@Override
	public void onRemoveNoExternalMessages(
			RemoveNoExternalMessagesEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();

		logger.trace(String.format("SET_NO_EXT_MSG : %s / %s / %s / %s",
				channel, sourceNick, sourceLogin, sourceHostname));
	}

	@Override
	public void onSetInviteOnly(SetInviteOnlyEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();

		logger.trace(String.format("SET_INV_ONLY : %s / %s / %s / %s", channel,
				sourceNick, sourceLogin, sourceHostname));
	}

	@Override
	public void onRemoveInviteOnly(RemoveInviteOnlyEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();

		logger.trace(String.format("REM_INV_ONLY : %s / %s / %s / %s", channel,
				sourceNick, sourceLogin, sourceHostname));
	}

	@Override
	public void onSetModerated(SetModeratedEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();

		logger.trace(String.format("SET_MODERATED : %s / %s / %s / %s",
				channel, sourceNick, sourceLogin, sourceHostname));
	}

	@Override
	public void onRemoveModerated(RemoveModeratedEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();

		logger.trace(String.format("SET_MODERATED : %s / %s / %s / %s",
				channel, sourceNick, sourceLogin, sourceHostname));
	}

	@Override
	public void onSetPrivate(SetPrivateEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();

		logger.trace(String.format("SET_PRIV : %s / %s / %s / %s", channel,
				sourceNick, sourceLogin, sourceHostname));
	}

	@Override
	public void onRemovePrivate(RemovePrivateEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();

		logger.trace(String.format("REM_PRIV : %s / %s / %s / %s", channel,
				sourceNick, sourceLogin, sourceHostname));
	}

	@Override
	public void onSetSecret(SetSecretEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();

		logger.trace(String.format("SET_SECRET : %s / %s / %s / %s", channel,
				sourceNick, sourceLogin, sourceHostname));
	}

	@Override
	public void onRemoveSecret(RemoveSecretEvent<ManalithBot> event) {
		String channel = event.getChannel().getName();
		String sourceNick = event.getUser().getNick();
		String sourceLogin = event.getUser().getLogin();
		String sourceHostname = event.getUser().getServer();

		logger.trace(String.format("SET_SECRET : %s / %s / %s / %s", channel,
				sourceNick, sourceLogin, sourceHostname));
	}

	@Override
	public void onInvite(InviteEvent<ManalithBot> event) {
		// String targetNick; // is deprecated in the pircbotx
		String sourceNick = event.getUser();
		// String sourceLogin; // is deprecated in the pircbotx
		// String sourceHostname; // is deprecated in the pircbotx
		String channel = event.getChannel();

		logger.trace(String.format("INVITE : %s / %s", sourceNick, channel));
	}

	// @Override
	// public void onIncomingFileTransfer(IncomingFileTransferEvent<ManalithBot>
	// event) {
	// DccFileTransfer transfer = event.getTransfer();
	// } // do not remove for recovery

	// @Override
	// public void onFileTransferFinished(FileTransferFinishedEvent<ManalithBot>
	// event) {
	// DccFileTransfer transfer = event.getTransfer();
	// Exception e = event.getException();
	// } // do not remove for recovery

	// @Override
	// public void onIncomingChatRequest(IncomingChatRequestEvent<ManalithBot>
	// event) {
	// DccChat chat = event.getChat();
	// } // do not remove for recovery

}
