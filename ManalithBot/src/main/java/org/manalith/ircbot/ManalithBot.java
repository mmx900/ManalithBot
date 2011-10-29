package org.manalith.ircbot;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.jibble.pircbot.DccChat;
import org.jibble.pircbot.DccFileTransfer;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.manalith.ircbot.command.CommandParser;
import org.manalith.ircbot.plugin.IBotPlugin;
import org.manalith.ircbot.plugin.PluginManager;
//import org.manalith.ircbot.plugin.relay.RelayPlugin;

public class ManalithBot extends PircBot {
	private Logger logger = Logger.getLogger(getClass());
	
	//TODO 닉 도용 가능성이 있으니 로그인 기능을 만들 것
	//private static final String[] owners = {"setzer"};
	private static final String[] owners = {};
	private PluginManager pluginManager = new PluginManager();
	
	protected ManalithBot(String name) throws NickAlreadyInUseException, IOException, IrcException{
		this.setName(name);
	}
	
	public void addPlugin(IBotPlugin plugin){
		pluginManager.add(plugin);
	}
	
	/**
	 * sendMessage(target, message)가 final 메서드이므로 로깅을 위해 이 메시지를 사용한다.
	 * @param target
	 * @param message
	 */
	public void sendLoggedMessage(String target, String message, boolean redirectToRelayBot){
		logger.trace(String.format("MESSAGE(LOCAL) : %s / %s", target, message));
		
		sendMessage(target, message);
		
//		if(redirectToRelayBot && RelayPlugin.RELAY_BOT != null)
//			RelayPlugin.RELAY_BOT.sendMessage(target, message);
	}
	
	public void sendLoggedMessage(String target, String message){
		sendLoggedMessage(target, message, true);
	}
	
//	private void sendMessage(Message m){
//		//너무 긴 문자는 자른다.
//		if(m.getMessage().length() > 180) m.setMessage(m.getMessage().substring(0, 179));
//		sendMessage(m.getChannel(), m.getSender() + ", " + m.getMessage());
//	}
	
	//TODO nick(String) 비교가 아닌 User 비교로
	private boolean isOwner(String nick){
		boolean result = false;
		for(String s : owners){
			if(s.equals(nick)){
				result = true;
				break;
			}
		}
		return result;
	}
	
	@Override
    protected void onConnect() {
    	logger.trace("CONNECT");
    	
    	//pluginManager.onConnect();
    }
	
	@Override
    protected void onDisconnect() {
    	logger.trace("DISCONNECT");
    	
    	//pluginManager.onDisconnect();
    }
	
	@Override
    protected void onServerResponse(int code, String response) {
		logger.trace(String.format("SERVER_RESPONSE: %s / %s", code, response));
		
		//pluginManager.onServerResponse(code, response);
	}
	
	@Override
    protected void onUserList(String channel, User[] users) {
    	
    }
	
	@Override
	protected void onMessage(String channel,
			String sender,
			String login,
			String hostname,
			String message){
		logger.trace(String.format("MESSAGE : %s / %s / %s / %s / %s", channel, sender, login, hostname, message));
		
		//릴레이 메시지일 경우 로컬 메시지로 변환한다.
		if(sender.equals("♣오씨네") || sender.equals("♣오씨네가건물")){
			sender = CommandParser.getSenderByRelayMessage(message);
			message = CommandParser.convertRelayToLocalMessage(message);
		}
		
		if(message.equals("!도움") || message.equals("!help")){
			sendMessage(channel, "!도움, !help, 배워, !plugins");
		}else if(message.equals("!plugins")){
			sendMessage(channel, pluginManager.getPluginInfo());
		}else if(message.equals("!quit")){
			if(isOwner(sender)){
				quitServer();
				System.exit(-1);
			}
		}else{
			pluginManager.onMessage(channel, sender, login, hostname, message);
		}
	}
	
	@Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
		
	}
	
	@Override
    protected void onAction(String sender, String login, String hostname, String target, String action) {
		
	}
	
	@Override
    protected void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
		
	}
	
	@Override
	protected void onJoin(String channel, String sender, String login, String hostname) {
		logger.trace(String.format("JOIN : %s / %s / %s / %s", channel, sender, login, hostname));
		
		pluginManager.onJoin(channel, sender, login, hostname);
	}
	
	@Override
	protected void onPart(String channel, String sender, String login, String hostname) {
		logger.trace(String.format("PART : %s / %s / %s / %s", channel, sender, login, hostname));

		//pluginManager.onPart(channel, sender, login, hostname);
	}
	
	@Override
	protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
		logger.trace(String.format("NICK_CHANGE : %s / %s / %s / %s", oldNick, login, hostname, newNick));

		//pluginManager.onNickChange(oldNick, login, hostname, newNick);
	}
	
	@Override
	protected void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
		logger.trace(String.format("KICK : %s / %s / %s / %s / %s / %s", channel, kickerNick, kickerLogin, kickerHostname, recipientNick, reason));

		//pluginManager.onKick(channel, kickerNick, kickerLogin, kickerHostname, recipientNick, reason);
	}
	
	@Override
	protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
		logger.trace(String.format("QUIT : %s / %s / %s / %s", sourceNick, sourceLogin, sourceHostname, reason));

		//pluginManager.onQuit(sourceNick, sourceLogin, sourceHostname, reason);
	}
	
	@Override
    protected void onTopic(String channel, String topic, String setBy, long date, boolean changed) {
		logger.trace(String.format("TOPIC : %s / %s / %s / %s / %s", channel, topic, setBy, date, changed));

		//pluginManager.onTopic(channel, topic, setBy, date, changed);
    }
	
	@Override
    protected void onChannelInfo(String channel, int userCount, String topic) {
		logger.trace(String.format("CHANNEL_INFO : %s / %s / %s", channel, userCount, topic));

		//pluginManager.onTopic(channel, userCount, topic);
    }
	
	@Override
    protected void onMode(String channel, String sourceNick, String sourceLogin, String sourceHostname, String mode) {
    	logger.trace(String.format("MODE : %s / %s / %s / %s / %s", channel, sourceNick, sourceLogin, sourceHostname, mode));
    }
	
	@Override
    protected void onUserMode(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String mode) {
		
	}
	
	@Override
    protected void onOp(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
    	
    }
	
	@Override
    protected void onDeop(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
		
	}
	
	@Override
    protected void onVoice(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
    	
    }
	
	@Override
    protected void onDeVoice(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
		
	}
	
	@Override
    protected void onSetChannelKey(String channel, String sourceNick, String sourceLogin, String sourceHostname, String key) {
		
	}
	
	@Override
    protected void onRemoveChannelKey(String channel, String sourceNick, String sourceLogin, String sourceHostname, String key) {
    	
    }
    
    @Override
    protected void onSetChannelLimit(String channel, String sourceNick, String sourceLogin, String sourceHostname, int limit) {
    	
    }
    
    @Override
    protected void onRemoveChannelLimit(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    	
    }
    
    @Override
    protected void onSetChannelBan(String channel, String sourceNick, String sourceLogin, String sourceHostname, String hostmask) {
    	
    }
    
    @Override
    protected void onRemoveChannelBan(String channel, String sourceNick, String sourceLogin, String sourceHostname, String hostmask) {
    	
    }
    
    @Override
    protected void onSetTopicProtection(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    	
    }
    
    @Override
    protected void onRemoveTopicProtection(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    	
    }
    
    @Override
    protected void onSetNoExternalMessages(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    	
    }
    
    @Override
    protected void onRemoveNoExternalMessages(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    	
    }
    
    @Override
    protected void onSetInviteOnly(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    	
    }
    
    @Override
    protected void onRemoveInviteOnly(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    	
    }
    
    @Override
    protected void onSetModerated(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    	
    }
    
    @Override
    protected void onRemoveModerated(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    	
    }
    
    @Override
    protected void onSetPrivate(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    	
    }
    
    @Override
    protected void onRemovePrivate(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    	
    }
    
    @Override
    protected void onSetSecret(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    	
    }
    
    @Override
    protected void onRemoveSecret(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
    	
    }
    
    @Override
    protected void onInvite(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String channel)  {
    	
    }
    
    @Override    
    protected void onIncomingFileTransfer(DccFileTransfer transfer) {
    	
    }
    
    @Override
    protected void onFileTransferFinished(DccFileTransfer transfer, Exception e) {
    	
    }
    
    @Override
    protected void onIncomingChatRequest(DccChat chat) {
    	
    }
}