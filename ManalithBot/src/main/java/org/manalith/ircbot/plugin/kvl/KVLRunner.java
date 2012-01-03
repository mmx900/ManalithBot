//
// KVLRunner.java
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.

package org.manalith.ircbot.plugin.kvl;

public class KVLRunner {

	private KVLTable kvlTable;

	public KVLRunner() {
		kvlTable = null;
	}

	private void initKVLRun() throws Exception {
//		StreamDownloader d = new StreamDownloader(
//				"http://kernel.org/index.shtml");
//		KVLTableTokenAnalyzer tAnalyzer = new KVLTableTokenAnalyzer(
//				d.downloadDataStream());
//		TokenArray array = tAnalyzer.analysisTokenStream();

		// TODO
		KVLTableBuilder tBuilder = new KVLTableBuilder("http://kernel.org/index.shtml"/*array*/);
		kvlTable = tBuilder.generateKernelVersionTable();
	}

	public String run(String arg) {
		String result = "";
		try {
			this.initKVLRun();
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
