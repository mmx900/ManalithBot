//
// KVLMain.java
// darkcircle dot 0426 at gmail dot com
//
// This source can be distributed under the terms of GNU General Public License version 3
// which is derived from the license of Manalith bot.
//
// This class is just a test.
//

package org.manalith.ircbot.plugin.kvl;

public class KVLMain {
	public static void main(String[] args) {
		// inger.

		KVLTable kvlTable = null;
		try {
			StreamDownloader d = new StreamDownloader(
					"http://kernel.org/index.shtml");
			KVLTableTokenAnalyzer tAnalyzer = new KVLTableTokenAnalyzer(
					d.downloadDataStream());
			TokenArray array = tAnalyzer.analysisTokenStream();

			// TODO
			KVLTableBuilder tBuilder = new KVLTableBuilder(array);
			kvlTable = tBuilder.generateKernelVersionTable();

			System.out.println(kvlTable.getLatestVersions());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
