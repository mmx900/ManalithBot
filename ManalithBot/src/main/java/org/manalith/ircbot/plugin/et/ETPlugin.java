package org.manalith.ircbot.plugin.et;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

	private final static long ONCE_PER_MINUTE = 1000 * 60;
	private final static long ONCE_PER_HOUR = ONCE_PER_MINUTE * 60;
	private final static long ONCE_PER_DAY = ONCE_PER_HOUR * 24;

	private String serverAddress;

	private int serverPort;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);

		playerManager = PlayerManager.instance();

		ETAlertTimerTask alertTask = new ETAlertTimerTask(this);
		Timer timer = new Timer(true);

		// 한 시간 뒤 00분에 첫 실행
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY) + 1); // 1~24
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.MILLISECOND, 0);
		Date firstTime = now.getTime();

		logger.info("ET 플러그인 실행 시간 : " + Calendar.getInstance().toString());
		logger.info("최초 실행 예정 시간 : " + now.toString());

		timer.scheduleAtFixedRate(alertTask, firstTime, ONCE_PER_HOUR);
	}

	@Override
	public String getName() {
		return "Enemy Territory";
	}

	@Override
	public String getCommands() {
		return NAMESPACE;
	}

	@Override
	public String getHelp() {
		// return
		// "et:help, et:register, et:unregister, et:ready, et:list, et:players, et:alert, et:connected";
		return "et:help, et:register, et:unregister, et:list, et:alert, et:connected";
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	@Override
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

			try {
				ServerStatusChecker checker = new ServerStatusChecker();
				ServerStatus status = checker.checkStatus(serverAddress,
						serverPort, true);
				List<ServerStatus.Player> players = status.getPlayers();
				if (players.isEmpty()) {
					event.respond(String.format(
							"%s(현재 맵 : %s) 에 접속중인 플레이어가 없습니다.", serverAddress,
							status.getMapName()));
				} else {
					Map<String, List<ServerStatus.Player>> playerMap = new LinkedHashMap<>();
					playerMap.put("Axis", new ArrayList<ServerStatus.Player>());
					playerMap.put("Allies",
							new ArrayList<ServerStatus.Player>());
					playerMap.put("Spectator",
							new ArrayList<ServerStatus.Player>());
					playerMap.put("Connecting",
							new ArrayList<ServerStatus.Player>());

					for (ServerStatus.Player player : players) {
						if (playerMap.containsKey(player.getTeam())) {
							playerMap.get(player.getTeam()).add(player);
						} else {
							playerMap.get("Connecting").add(player);
						}
					}

					StringBuilder sb = new StringBuilder();
					for (Entry<String, List<ServerStatus.Player>> entry : playerMap
							.entrySet()) {
						if (entry.getValue().isEmpty()) {
							continue;
						}

						sb.append(" ");
						sb.append(entry.getKey());
						sb.append(" ");

						for (ServerStatus.Player player : entry.getValue()) {
							sb.append(ColorConvertor
									.convertToPlainString(player.getName()));
							sb.append("(");
							sb.append(player.getXp());
							sb.append(")");
						}
					}

					event.respond(String.format(
							"%s(현재 맵 : %s)에 접속중인 플레이어 : %s", serverAddress,
							status.getMapName(), sb.toString()));
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

	/**
	 * @return the serverAddress
	 */
	public String getServerAddress() {
		return serverAddress;
	}

	/**
	 * @param serverAddress
	 *            the serverAddress to set
	 */
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	/**
	 * @return the serverPort
	 */
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * @param serverPort
	 *            the serverPort to set
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
}