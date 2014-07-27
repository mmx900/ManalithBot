/*
 	org.manalith.ircbot.plugin/PluginManager.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2005, 2011, 2012  Ki-Beom, Kim
 	Copyright (C) 2012  Seong-ho, Cho  <darkcircle.0426@gmail.com>

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
package org.manalith.ircbot.plugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.manalith.ircbot.Configuration;
import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.common.stereotype.BotFilter;
import org.manalith.ircbot.common.stereotype.BotTimer;
import org.manalith.ircbot.plugin.admin.HelpPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PluginManager {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private List<Plugin> list = new ArrayList<>();
	private Map<Command, Object> commands = new HashMap<>();
	private Map<Method, Object> filters = new HashMap<>();
	private Map<Method, Object> timers = new HashMap<>();

	public PluginManager() {
		load(new HelpPlugin(this));
	}

	@PostConstruct
	public void onPostConstruct() {
		load(configuration.getPlugins());
	}

	@Autowired
	private Configuration configuration;

	public void load(List<Plugin> plugins) {
		for (Plugin plugin : plugins) {
			load(plugin);
		}
	}

	public void load(Plugin plugin) {
		extractEventDelegates(plugin);

		list.add(plugin);

		try {
			plugin.start();
			logger.info("{} 플러그인이 시작되었습니다.", plugin.getName());
		} catch (Exception e) {
			logger.error("{} 플러그인을 시작하지 못했습니다.", plugin.getName());
			logger.error(e.getMessage(), e);
			unload(plugin);
		}
	}

	public void unload(Plugin plugin) {
		try {
			plugin.stop();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		list.remove(plugin);
	}

	private void extractEventDelegates(Plugin plugin) {
		Method[] methods = plugin.getClass().getDeclaredMethods();
		for (Method method : methods) {
			BotCommand command = method.getAnnotation(BotCommand.class);
			if (command != null) {
				commands.put(new Command(method, plugin), plugin);
				continue;
			}

			BotFilter filter = method.getAnnotation(BotFilter.class);
			if (filter != null) {
				filters.put(method, plugin);
				continue;
			}

			BotTimer timer = method.getAnnotation(BotTimer.class);
			if (timer != null) {
				timers.put(method, plugin);
				continue;
			}
		}
	}

	public List<Plugin> getPlugins() {
		return list;
	}

	public Map<Command, Object> getCommands() {
		return commands;
	}

	public Map<Method, Object> getFilters() {
		return filters;
	}

	public Map<Method, Object> getTimers() {
		return timers;
	}
}
