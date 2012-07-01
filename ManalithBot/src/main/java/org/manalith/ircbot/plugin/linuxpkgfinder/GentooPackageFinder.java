/*
 	org.manalith.ircbot.plugin.distopkgfinder/GentooPkgFinderRunner.java
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
package org.manalith.ircbot.plugin.linuxpkgfinder;

import org.manalith.ircbot.common.stereotype.BotCommand;
import org.manalith.ircbot.resources.MessageEvent;
import org.springframework.stereotype.Component;

@Component
public class GentooPackageFinder extends PackageFinder {

	protected String keyword;
	protected GentooSearchEngineProvider provider;

	@Override
	public String getName() {
		return "젠투";
	}

	@Override
	public String getCommands() {
		return "!gen";
	}

	public String getHelp() {
		return "설  명: 지정한 이름을 가진 젠투의 패키지를 검색합니다, 사용법: !gen [키워드]";
	}

	public GentooPackageFinder() {
		this.setKeyword("");
	}

	public GentooPackageFinder(String newKeyword) {
		this.setKeyword(newKeyword);
	}

	public void setProvider(GentooSearchEngineProvider provider) {
		this.provider = provider;
	}

	public void setKeyword(String newKeyword) {
		this.keyword = newKeyword;
	}

	public String getKeyword() {
		return this.keyword;
	}

	@BotCommand(value = { "!gen" }, minimumArguments = 1)
	public String find(MessageEvent event, String... args) {
		return this.find(args[0]);
	}

	public String find(String arg) {
		return this.provider.find(arg);
	}
}
