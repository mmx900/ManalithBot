/*
 	org.manalith.ircbot.plugin.kvl/KVLPlugin.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2011, 2012  Seong-ho, Cho <darkcircle.0426@gmail.com>

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
package org.manalith.ircbot.plugin.kvl;

import org.apache.log4j.Logger;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component("kvlPlugin")
public class KVLPlugin extends SimplePlugin {

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public String getName() {
		return "커널버전";
	}

	@Override
	public String getCommands() {
		return "!커널";
	}

	@Override
	public String getHelp() {
		return "설  명: 커널 버전의 목록을 보여줍니다, 사용법: !커널 (latest[default]|all|help)";
	}

	@Override
	public void onMessage(MessageEvent event) {
		parseEvent(event);
	}

	@Override
	public void onPrivateMessage(MessageEvent event) {
		parseEvent(event);
	}

	protected void parseEvent(MessageEvent event) {
		String[] command = event.getMessageSegments();

		if (command[0].equals("!커널") || command[0].equals("!kernel")) {
			if (command.length >= 3) {
				event.respond("Too many arguments!");
				return;
			}

			KVLRunner runner = new KVLRunner();

			try {
				if (command.length >= 2)
					event.respond(runner.run(command[1]));
				else
					event.respond(runner.run(""));
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
		}
	}

}