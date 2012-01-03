package org.manalith.ircbot.plugin;

import java.util.ArrayList;
import java.util.List;

import org.manalith.ircbot.resources.MessageEvent;

public class PluginManager {
	private List<IBotPlugin> list = new ArrayList<IBotPlugin>();

	public void add(IBotPlugin plugin) {
		list.add(plugin);
	}

	public void remove(IBotPlugin plugin) {
		list.remove(plugin);
	}

	public void onJoin(String channel, String sender, String login,
			String hostName) {
		for (IBotPlugin plugin : list)
			plugin.onJoin(channel, sender, login, hostName);
	}

	public void onMessage(String channel, String sender, String login,
			String hostName, String message) {
		MessageEvent msg = new MessageEvent(channel, sender, login, hostName,
				message);

		for (IBotPlugin plugin : list) {
			plugin.onMessage(msg);
			if (msg.isExecuted())
				break;
		}
	}

	public void onPrivateMessage(String sender, String login, String hostName,
			String message) {
		MessageEvent msg = new MessageEvent(sender, login, hostName, message);

		for (IBotPlugin plugin : list) {
			plugin.onPrivateMessage(msg);
			if (msg.isExecuted())
				break;
		}
	}

	public void onPart(String channel, String sender, String login,
			String hostName) {
		for (IBotPlugin plugin : list)
			plugin.onPart(channel, sender, login, hostName);
	}

	public List<IBotPlugin> getList() {
		return list;
	}

	public String getPluginInfo() {
		StringBuilder sb = new StringBuilder();
		String name = "";
		int i = 0; // To make well-formed message
		for (IBotPlugin p : list) {
			name = p.getName();
			if (name != null) {
				if (i != 0)
					sb.append(", "); // To make well-formed message
				else
					i++; //

				sb.append(name + "(" + p.getNamespace() + ")");
			}
		}

		return sb.toString();
	}
}
