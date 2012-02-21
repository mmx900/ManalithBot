/*
 	org.manalith.ircbot.plugin.kvl/KVLRunner.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2012  Seong-ho, Cho <darkcircle.0426@gmail.com>

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

public class KVLRunner {

	private KVLTable kvlTable;

	public KVLRunner() {
		kvlTable = null;
	}

	private void initKVLRun() throws Exception {
		KVLTableBuilder tBuilder = new KVLTableBuilder(
				"http://kernel.org/index.shtml"/* array */);
		kvlTable = tBuilder.generateKernelVersionTable();
	}

	public String run(String arg) {
		String result = "";

		try {
			initKVLRun();
		} catch (Exception e) {
			result = e.getMessage();
			return result;
		}

		if (arg.equals("") || arg.equals("latest")) {
			result = kvlTable.toString();
		} else if (arg.equals("all")) {
			result = kvlTable.getAllVersionInfo();
		} else if (arg.equals("help")) {
			result = "!kernel (latest[default]|all|help)";
		} else {
			result = "인식할 수 없는 옵션.";
		}

		return result;
	}
}
