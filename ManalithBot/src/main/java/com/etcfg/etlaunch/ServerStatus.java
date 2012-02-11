package com.etcfg.etlaunch;

import java.util.ArrayList;
import java.util.List;

public class ServerStatus {
	private String name;
	private String mod;
	private boolean punkbusterEnabled;
	private List<Player> players = new ArrayList<Player>();
	private int maxPlayers;
	private int privateSlots;
	private boolean passwordProtected;
	private int ping;
	private int botsplaying;
	private String mapName;
	private boolean slacEnabled;

	public boolean isSlacEnabled() {
		return slacEnabled;
	}

	public void setSlacEnabled(boolean slacEnabled) {
		this.slacEnabled = slacEnabled;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMod() {
		return mod;
	}

	public void setMod(String mod) {
		this.mod = mod;
	}

	public boolean isPunkbusterEnabled() {
		return punkbusterEnabled;
	}

	public void setPunkbusterEnabled(boolean punkbusterEnabled) {
		this.punkbusterEnabled = punkbusterEnabled;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public int getPrivateSlots() {
		return privateSlots;
	}

	public void setPrivateSlots(int privateSlots) {
		this.privateSlots = privateSlots;
	}

	public boolean isPasswordProtected() {
		return passwordProtected;
	}

	public void setPasswordProtected(boolean passwordProtected) {
		this.passwordProtected = passwordProtected;
	}

	public int getPing() {
		return ping;
	}

	public void setPing(int ping) {
		this.ping = ping;

	}

	public int getBotsplaying() {
		return botsplaying;
	}

	public void setBotsplaying(int botsplaying) {
		this.botsplaying = botsplaying;
	}

	public static class Player {
		private int xp;
		private int ping;
		private String team;
		private String name;

		public Player(int xp, int ping, String team, String name) {
			super();
			this.xp = xp;
			this.ping = ping;
			this.team = team;
			this.name = name;
		}

		public Player() {
			super();
			// TODO Auto-generated constructor stub
		}

		public int getXp() {
			return xp;
		}

		public void setXp(int xp) {
			this.xp = xp;
		}

		public int getPing() {
			return ping;
		}

		public void setPing(int ping) {
			this.ping = ping;
		}

		public String getTeam() {
			return team;
		}

		public void setTeam(String team) {
			this.team = team;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String toString()
		{
			return "[" + this.getTeam() + ":" + this.getXp() + "]" +ColorConvertor.convertToPlainString(this.getName()) + " - " + this.getPing();
		}

	}

	@Override
	public String toString() {
		String result;
		result = "ServerStatus [maxPlayers=" + maxPlayers + ", mod=" + mod
				+ ", name=" + name + ", passwordProtected=" + passwordProtected
				+ ", ping=" + ping + ", players=";

		for(int i=0; i<players.size(); i++)
		{
			result += players.get(i).toString();
		}

		result += ", privateSlots="
				+ privateSlots + ", punkbusterEnabled=" + punkbusterEnabled
				+ "]";

		return result;
	}

}
