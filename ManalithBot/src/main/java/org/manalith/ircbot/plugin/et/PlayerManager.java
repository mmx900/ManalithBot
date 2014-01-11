package org.manalith.ircbot.plugin.et;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
	private static PlayerManager _instance;
	private static final String MEMBER_LIST_DB = "et_members.xml";

	public static PlayerManager instance() {
		if (_instance == null)
			_instance = new PlayerManager();

		return _instance;
	}

	private PlayerManager() {
		load();
	}

	private List<Player> list;

	public String getPlayerNicks() {
		StringBuilder sb = new StringBuilder();

		for (Player p : list) {
			sb.append(p.getIRCName() + " ");
		}

		return sb.toString();
	}

	public String getReadyNicks() {
		StringBuilder sb = new StringBuilder();

		for (Player p : list) {
			if (p.isReady())
				sb.append(p.getIRCName() + " ");
		}

		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public void load() {
		if (list == null) {
			try {
				FileInputStream os = new FileInputStream(MEMBER_LIST_DB);
				XMLDecoder decoder = new XMLDecoder(os);
				list = (List<Player>) decoder.readObject();
				// Player p = (Player)decoder.readObject();
				decoder.close();
			} catch (IOException ex) {
				// ignore
				list = new ArrayList<Player>();
			}
		}
	}

	public void add(Player player) throws AlreadyRegisteredException {
		for (Player p : list) {
			if (p.equals(player)) {
				throw new AlreadyRegisteredException();
			}
		}

		list.add(player);
	}

	public void remove(Player player) throws NotRegisteredException {
		boolean removed = false;

		for (Player p : list) {
			if (p.equals(player)) {
				list.remove(p);
				removed = true;
				break;
			}
		}

		if (!removed)
			throw new NotRegisteredException();
	}

	public void setReady(Player player) throws NotRegisteredException {
		boolean changed = false;

		for (Player p : list) {
			if (p.equals(player)) {
				p.setReady(true);
				changed = true;
				break;
			}
		}

		if (!changed)
			throw new NotRegisteredException();
	}

	public void save() {
		if (list != null) {
			try {
				FileOutputStream os = new FileOutputStream(MEMBER_LIST_DB);
				XMLEncoder encoder = new XMLEncoder(os);
				encoder.writeObject(list);
				encoder.close();
			} catch (IOException ex) {
				// ignore
			}
		}
	}
}
