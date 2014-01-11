package org.manalith.ircbot.plugin.et;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import org.apache.log4j.Logger;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.osgi.framework.BundleContext;
import org.springframework.stereotype.Component;

import com.etcfg.etlaunch.ColorConvertor;
import com.etcfg.etlaunch.ServerStatus;
import com.etcfg.etlaunch.ServerStatusChecker;

@Component("etPlugin")
public class ETPlugin extends SimplePlugin {
	private Logger logger = Logger.getLogger(getClass());

	private PlayerManager playerManager;

	private static final String NAMESPACE = "et";

	// private final static long ONCE_PER_DAY = 1000*60*60*24;
	// private final static long ONCE_PER_MINUTE = 1000*60;
	private final static long ONCE_PER_HOUR = 1000 * 60 * 60;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);

		playerManager = PlayerManager.instance();

		ETAlertTimerTask alertTask = new ETAlertTimerTask(this);
		Timer timer = new Timer(true);

		Date firstTime = null;

		// 한 시간 뒤 00분에 첫 실행
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY) + 1); // 1~24
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.MILLISECOND, 0);
		firstTime = now.getTime();

		logger.info("ET 플러그인 실행 시간 : " + Calendar.getInstance().toString());
		logger.info("최초 실행 예정 시간 : " + now.toString());

		timer.scheduleAtFixedRate(alertTask, firstTime, ONCE_PER_HOUR);
	}

	public String getName() {
		return "Enemy Territory";
	}

	public String getCommands() {
		return NAMESPACE;
	}

	public String getHelp() {
		// return
		// "et:help, et:register, et:unregister, et:ready, et:list, et:players, et:alert, et:connected";
		return "et:help, et:register, et:unregister, et:list, et:alert, et:connected";
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public void onMessage(MessageEvent event) {
		String message = event.getMessage();
		String sender = event.getUser().getNick();

		if (message.equals(NAMESPACE + ":register")) {
			Player p = new Player();
			p.setETName(sender);
			p.setIRCName(sender);
			try {
				playerManager.add(p);
				playerManager.save();
				event.respond(sender + ", 등록되었습니다.");
			} catch (AlreadyRegisteredException ex) {
				event.respond(sender + ", 이미 등록된 닉네임입니다.");
			}
		} else if (message.equals(NAMESPACE + ":unregister")) {
			Player p = new Player();
			p.setETName(sender);
			p.setIRCName(sender);
			try {
				playerManager.remove(p);
				playerManager.save();
				event.respond(sender + ", 삭제되었습니다.");
			} catch (NotRegisteredException e) {
				event.respond(sender + ", 등록되지 않은 닉네임입니다.");
			}
		}/*
		 * if (message.equals(NAMESPACE + ":ready")) { Player p = new Player();
		 * p.setETName(sender); p.setIRCName(sender); try{
		 * playerManager.setReady(p); playerManager.save(); event.respond(sender
		 * + ", 대기자 명단에 포함되었습니다."); }catch(NotRegisteredException ex){
		 * event.respond(sender + ", et:register 명령으로 먼저 등록해주세요."); } }
		 */
		if (message.equals(NAMESPACE + ":list")) {
			event.respond("등록된 플레이어 : " + playerManager.getPlayerNicks());
		}/*
		 * else if (message.equals(NAMESPACE + ":players")) {
		 * event.respond("금일 참가 예정 플레이어 : " + playerManager.getPlayerNicks()); }
		 */
		else if (message.equals("!et")
				|| message.equals(NAMESPACE + ":connected")) {
			final String serverAddress = "neogeo.co.kr";
			final int port = 27960;

			try {
				ServerStatusChecker checker = new ServerStatusChecker();
				ServerStatus status = checker.checkStatus(serverAddress, port,
						true);
				List<ServerStatus.Player> players = status.getPlayers();
				if (players.isEmpty()) {
					event.respond(String.format(
							"%s(현재 맵 : %s) 에 접속중인 플레이어가 없습니다.", serverAddress,
							status.getMapName()));
				} else {
					StringBuilder sb = new StringBuilder();
					List<ServerStatus.Player> axis = new ArrayList<ServerStatus.Player>();
					List<ServerStatus.Player> allies = new ArrayList<ServerStatus.Player>();
					List<ServerStatus.Player> spectator = new ArrayList<ServerStatus.Player>();
					List<ServerStatus.Player> connecting = new ArrayList<ServerStatus.Player>();

					for (ServerStatus.Player player : players) {
						if (player.getTeam().equals("Axis"))
							axis.add(player);
						else if (player.getTeam().equals("Allies"))
							allies.add(player);
						else if (player.getTeam().equals("Spectator"))
							spectator.add(player);
						else
							connecting.add(player);
					}

					if (axis.size() > 0) {
						sb.append(" [AXIS] ");
						for (ServerStatus.Player player : axis) {
							sb.append(ColorConvertor
									.convertToPlainString(player.getName())
									+ "(" + player.getXp() + ") ");
						}
					}

					if (allies.size() > 0) {
						sb.append(" [ALLIES] ");
						for (ServerStatus.Player player : allies) {
							sb.append(ColorConvertor
									.convertToPlainString(player.getName())
									+ "(" + player.getXp() + ") ");
						}
					}

					if (spectator.size() > 0) {
						sb.append(" [SPECTATOR] ");
						for (ServerStatus.Player player : spectator) {
							sb.append(ColorConvertor
									.convertToPlainString(player.getName())
									+ "(" + player.getXp() + ") ");
						}
					}

					if (connecting.size() > 0) {
						sb.append(" [CONNECTING] ");
						for (ServerStatus.Player player : connecting) {
							sb.append(ColorConvertor
									.convertToPlainString(player.getName())
									+ "(" + player.getXp() + ") ");
						}
					}

					event.respond(serverAddress + "(현재 맵 : "
							+ status.getMapName() + ") 에 접속중인 플레이어 :"
							+ sb.toString());
				}
			} catch (IOException e) {
				logger.warn(e);
				event.respond("서버 연결에 오류가 발생했습니다.");
			}

		} else if (message.equals(NAMESPACE + ":alert")) {
			event.respond(playerManager.getPlayerNicks() + " ET하자옹!");
		} else if (message.equals(NAMESPACE + ":help")) {
			event.respond(getHelp());
		}
	}
}
