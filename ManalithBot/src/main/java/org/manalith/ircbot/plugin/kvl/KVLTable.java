/*
 	org.manalith.ircbot.plugin.kvl/KVLTable.java
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

import java.util.ArrayList;

public class KVLTable {
	private class KVLRecord {
		private String tag;
		private ArrayList<String> versionlist;

		public KVLRecord(String newTag) {
			setTag(newTag);
			versionlist = new ArrayList<>();
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

		public String getTag() {
			return tag;
		}

		public void addVersionElement(String versionElement) {
			versionlist.add(versionElement);
		}

		public String getLatestVersion() {
			return versionlist.get(0);
		}

		@Override
		public String toString() {
			String result = "";
			result += getTag() + " : ";
			int arraysize = versionlist.size();

			for (int i = 0; i < arraysize; i++) {
				result += versionlist.get(i);
				if (i != arraysize - 1)
					result += ", ";
			}

			return result;
		}
	}

	ArrayList<KVLRecord> records;

	public KVLTable() {
		records = new ArrayList<>();
	}

	public void addVersionInfo(String tag, String versionElement) {
		int arraysize = records.size();
		boolean isSaved = false;
		for (int i = 0; i < arraysize; i++) {
			if (records.get(i).getTag().equals(tag)) {
				isSaved = true;
				records.get(i).addVersionElement(versionElement);
			}
		}

		if (!isSaved) {
			KVLRecord newRecord = new KVLRecord(tag);
			newRecord.addVersionElement(versionElement);

			records.add(newRecord);
		}
	}

	public String getLatestVersions() {
		String result = "";

		int arraysize = records.size();

		for (int i = 0; i < arraysize; i++) {
			result += records.get(i).getTag() + " : "
					+ records.get(i).getLatestVersion();
			if (i != arraysize - 1)
				result += ", ";
		}

		return result;
	}

	public String getAllVersionInfo() {
		String result = "";
		int arraysize = records.size();

		for (int i = 0; i < arraysize; i++) {
			result += records.get(i).toString();
			if (i != arraysize - 1) {
				result += ", ";
			}
		}

		return result;
	}

	@Override
	public String toString() {
		return this.getLatestVersions();
	}
}
