/*
 	org.manalith.ircbot.plugin.nvidiadrivernews/NvidiaDriverNewsPlugin.java
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
package org.manalith.ircbot.plugin.nvidiadrivernews;

import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.plugin.SimplePlugin;
import org.springframework.stereotype.Component;

@Component
public class NvidiaDriverNewsPlugin extends SimplePlugin {

	private NvidiaDriverNewsReader reader = new NvidiaDriverNewsReader();

	@Override
	public String getName() {
		return "Nvidia최신";
	}

	@Override
	public String getDescription() {
		return "Nvidia 그래픽 디스플레이 드라이버의 최신 버전을 보여줍니다.";
	}

	@BotCommand("nvidia")
	public String getNews() {
		return reader.read();
	}
}
