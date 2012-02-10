package org.manalith.ircbot.plugin.bsh;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.manalith.ircbot.BotMain;
import org.manalith.ircbot.plugin.AbstractBotPlugin;
import org.manalith.ircbot.resources.Empty;
import org.manalith.ircbot.resources.MessageEvent;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.TargetError;

public class BshPlugin extends AbstractBotPlugin {
	private Logger logger = Logger.getLogger(getClass());
	private static final String NAMESPACE = "eval";
	private OwnerExecute internalExecute = null;
	private Properties commands;

	protected BshPlugin(String name) {
		loadInternalCommands();
	}

	public String getName() {
		return "BeanShell";
	}

	public String getCommands() {
		return NAMESPACE;
	}

	public String getHelp() {
		return "사용법 : eval [코드]";
	}

	public void onMessage(MessageEvent event) {
		String message = event.getMessage();
		String channel = event.getChannel();

		if (message.equals(NAMESPACE + ":help"))
			bot.sendLoggedMessage(channel, getHelp());
		else if (message.equalsIgnoreCase("reload")) {
			loadInternalCommands();
			bot.sendLoggedMessage(channel, "명령어 목록을 재구성합니다.");
		} else if (message.length() > 4
				&& message.substring(0, 4).equalsIgnoreCase("eval")) {
			Object o = executeRemoteCommand(message.substring(4));
			if (o != null) {
				if (o instanceof Empty) {
					// nothing
				} else {
					bot.sendLoggedMessage(channel, o.toString());
				}
			} else {
				bot.sendLoggedMessage(channel, "응답 결과가 없습니다.");
			}
		} else if (message.length() > 6
				&& message.substring(0, 6).equalsIgnoreCase("j:oper")) {
			// Owner 전용으로 시간 제한이 풀어져 있고, IRCController 사용이 가능하며, 명령이 기억된다.
			// if(isOwner(sender)){
			// Object o = executeRemoteOperation(sender, channel,
			// message.substring(6));
			// if(o != null){
			// if(o instanceof Empty){
			// //nothing
			// }else{
			// BotMain.BOT.sendLoggedMessage(channel, o.toString());
			// }
			// }else{
			// BotMain.BOT.sendLoggedMessage(channel, "응답 결과가 없습니다.");
			// }
			// }else{
			// BotMain.BOT.sendLoggedMessage(channel, "주인에게만 반응합니다.");
			// }
		} else if (message.length() >= 6
				&& message.substring(0, 2).equals("j:")) {
			String s = commands.getProperty(message.substring(0, 6).replace(
					":", "."));
			if (s != null) {
				bot.sendLoggedMessage(channel,
						executeInternalCommand(s));
			} else {
				bot.sendLoggedMessage(channel, "해당 명령은 없습니다.");
			}
		}
	}

	private void loadInternalCommands() {
		java.io.InputStream in = null;
		try {
			in = new FileInputStream(new File("commands.properties"));
			commands = new Properties();
			commands.load(in);
			in.close();
		} catch (IOException e) {
			logger.error(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e);
				} finally {
					in = null;
				}
			}
		}
	}

	// 일반 유저등 누구나 호출 가능한 메서드
	private Object executeRemoteCommand(String command) {
		Object result = null;
		long started = System.currentTimeMillis();
		RemoteExecute re = new RemoteExecute(command);

		while (!re.isExecuted()) {
			if (System.currentTimeMillis() - started > 5000) {
				result = new TimeoutException("5초의 시간이 경과했으므로 종료합니다.");
				re.stop();
				break;
			}
		}
		if (result == null)
			result = re.getResult();

		return result;
	}

	// 봇의 지정된 소유주만 사용 가능
	private Object executeRemoteOperation(String sender, String channel,
			String command) {
		Object result = null;
		long started = System.currentTimeMillis();
		if (internalExecute == null)
			internalExecute = new OwnerExecute();
		internalExecute.exec(command);

		while (!internalExecute.isExecuted()) {
			if (System.currentTimeMillis() - started > 10000) {
				result = new TimeoutException(
						"10초의 시간이 경과했으므로 종료합니다. 기억된 내용이 상실됩니다.");
				internalExecute.stop();
				internalExecute = null;
				break;
			}
		}
		if (result == null)
			result = internalExecute.getResult();

		return result;
	}

	private String executeInternalCommand(String commandValue) {
		Interpreter i = new Interpreter();
		Object result = null;
		try {
			result = i.eval(commandValue);
			if (result == null)
				result = "응답 결과가 없습니다.";
		} catch (TargetError ex) {
			try {
				Exception e = (Exception) ex.getTarget();
				throw e;
			} catch (Exception e) {
				result = e.toString();
				logger.error(e);
			}
		} catch (EvalError ex) {
			result = ex.getMessage();
			logger.error(ex);
		} finally {
			i = null;
		}
		return result.toString();
	}

}
