/*
 	org.manalith.ircbot.plugin.kvl/KVLRunner.java
 	ManalithBot - An open source IRC bot based on the PircBotX Framework.
 	Copyright (C) 2012  Seong-ho, Cho <darkcircle.0426@gmail.com>

    This program is under the GNU Public License version 3 or
    (at your option) any later version.

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
		KVLTableBuilder tBuilder = new KVLTableBuilder("http://www.kernel.org");
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

		switch (arg) {
		case "":
		case "latest":
			result = kvlTable.toString();
			break;
		case "all":
			result = kvlTable.getAllVersionInfo();
			break;
		case "help":
			result = "!커널 (latest[default]|all|help)";
			break;
		default:
			result = "인식할 수 없는 옵션.";
			break;
		}

		return result;
	}
}
