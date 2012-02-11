package com.etcfg.etlaunch;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.etcfg.etlaunch.ServerStatus.Player;

public class ServerStatusChecker {

	public static final String GAMENAME = "gamename";
	public static final String SV_PUNKBUSTER = "sv_punkbuster";
	public static final String SV_MAXCLIENTS = "sv_maxclients";
	public static final String SV_PRIVATECLIENTS = "sv_privateClients";

	DatagramSocket datagramSocket;
	private static final byte[] REQUEST_BODY = { -1, -1, -1, -1, 103, 101, 116,
			115, 116, 97, 116, 117, 115 };

	public ServerStatusChecker() throws SocketException {
		datagramSocket = new DatagramSocket();
		datagramSocket.setSoTimeout(1000);
	}

	public ServerStatus checkStatus(String adress, int port,
			boolean parsePlayers) throws IOException {

		byte[] response = new byte[1024 * 4];

		DatagramPacket requestPacket = new DatagramPacket(REQUEST_BODY,
				REQUEST_BODY.length, InetAddress.getByName(adress), port);
		DatagramPacket responsePacket = new DatagramPacket(response,
				response.length, InetAddress.getByName(adress), port);

		long startTime = System.currentTimeMillis();
		try {
			datagramSocket.send(requestPacket);
			datagramSocket.receive(responsePacket);
		} catch (SocketTimeoutException ex) {
			ServerStatus serverStatus = new ServerStatus();
			serverStatus.setName(adress + ":" + port);
			serverStatus.setPing(999);
			return serverStatus;
		}
		long ping = System.currentTimeMillis() - startTime;
		ServerStatus serverStatus = new ServerStatus();
		serverStatus.setPing((int) ping);

		ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(
				responsePacket.getData(), responsePacket.getOffset(),
				responsePacket.getLength());
		InputStreamReader inputStreamReader = new InputStreamReader(
				arrayInputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		bufferedReader.readLine();
		// toooooooooo long string.
		String parameters = bufferedReader.readLine();

		String[] split = parameters.split("\\\\");
		Set<ETCVars> etCvars = new HashSet<ETCVars>();
		HashMap<String,String> etCVarsHash = new HashMap<String,String>();
		ETCVars[] values = ETCVars.values();
		etCvars.addAll(Arrays.asList(values));
		for (int i = 1; i < split.length; i += 2) {

			String varKey = split[i];
			String varValue = split[i + 1];
			etCVarsHash.put(varKey,varValue);
			// System.out.println(split[i] + " : " + split[i+1]);

			Iterator<ETCVars> iterator = etCvars.iterator();

			while (iterator.hasNext()) {
				ETCVars next = iterator.next();

				if (next.name.equals(varKey)) {
					next.putValue(serverStatus, varValue);
					iterator.remove();
					break;
				}
			}
		}

		String teamFlagString = etCVarsHash.get("P");

		String readLine = bufferedReader.readLine();
	
		int j = 0;

		while (true) {
			// skip available slot
			if ( teamFlagString == null ) break;
			char teamChar = teamFlagString.charAt(j);
			if ( teamChar != '-' ) break;
			else j++;
		}

		while (readLine != null) {
			StringBuilder sb = new StringBuilder();
			Player player = new Player();
			int i;

			for (i = 0; i < readLine.length(); i++) {
				char carChar = readLine.charAt(i);
				if (carChar != ' ') {
					sb.append(carChar);
				} else {
					i++;
					break;
				}
			}

			player.setXp(Integer.parseInt(sb.toString()));
			sb = new StringBuilder();
			while (i < readLine.length()) {
				char carChar = readLine.charAt(i);
				if (carChar != ' ') {
					sb.append(carChar);
					i++;
				} else {
					i += 2;
					break;
				}
			}
			player.setPing(Integer.parseInt(sb.toString()));
			sb = new StringBuilder();
			while (i < readLine.length() - 1) {
				sb.append(readLine.charAt(i));
				i++;
			}
			player.setName(sb.toString());


			while (true) {	
				if ( teamFlagString == null ) break;
				char teamChar = teamFlagString.charAt(j);
				if ( teamChar == '-' )
				{
					j++;
					continue;
				}
				else
				{
					switch ( teamChar )
					{
						case '0':
							player.setTeam("Connecting");
							break;
						case '1':
							player.setTeam("Axis");
							break;
						case '2':
							player.setTeam("Allies");
							break;
						case '3':
							player.setTeam("Spectator");
							break;
						default:
							player.setTeam("UNKNOWN");
					}
					j++;
					break;
				}
			}
			
			serverStatus.getPlayers().add(player);
			readLine = bufferedReader.readLine();

		}

		return serverStatus;

	}

	public static void main(String[] args) throws UnknownHostException,
			IOException {

		ServerStatusChecker checker = new ServerStatusChecker();
		//
		ServerStatus checkStatus = checker.checkStatus("clanserver.etcfg.com",
				27960, false);
		System.out.println(checkStatus);

	}

	private enum ETCVars {
		GAMENAME(ServerStatusChecker.GAMENAME) {
			@Override
			public void putValue(ServerStatus status, String value) {
				status.setMod(value);
			}

		},
		SV_PUNKBUSTER(ServerStatusChecker.SV_PUNKBUSTER) {
			@Override
			public void putValue(ServerStatus status, String value) {
				status.setPunkbusterEnabled("1".equals(value));
			}
		},
		SV_MAXCLIENTS(ServerStatusChecker.SV_MAXCLIENTS) {
			@Override
			public void putValue(ServerStatus status, String value) {
				int parseInt = Integer.parseInt(value);
				status.setMaxPlayers(parseInt);
			}
		},
		SV_PRIVATECLIENTS(ServerStatusChecker.SV_PRIVATECLIENTS) {
			@Override
			public void putValue(ServerStatus status, String value) {
				int parseInt = Integer.parseInt(value);
				status.setPrivateSlots(parseInt);
			}
		},
		sv_hostname("sv_hostname") {
			@Override
			public void putValue(ServerStatus status, String value) {
				status.setName(value);
			}
		},
		omnibot_playing("omnibot_playing") {
			@Override
			public void putValue(ServerStatus status, String value) {
				int botNumber = Integer.parseInt(value);
				status.setBotsplaying(botNumber);

			}
		},
		mapname("mapname") {
			@Override
			public void putValue(ServerStatus status, String value) {
				status.setMapName(value);
			}

		},
		g_needpass("g_needpass") {

			@Override
			public void putValue(ServerStatus status, String value) {
				status.setPasswordProtected("1".equals(value));

			}

		},
		sl_sv_version("sl_sv_version") {

			@Override
			public void putValue(ServerStatus status, String value) {
				status.setSlacEnabled(true);

			}

		};
		public String name;

		ETCVars(String name) {
			this.name = name;

		}

		abstract void putValue(ServerStatus status, String value);
	}

}
