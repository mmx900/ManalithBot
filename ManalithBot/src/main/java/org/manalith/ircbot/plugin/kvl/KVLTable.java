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
			this.setTag(newTag);
			this.versionlist = new ArrayList<String>();
		}

		public void setTag(String newTag) {
			this.tag = newTag;
		}

		public String getTag() {
			return this.tag;
		}

		public void addVersionElement(String newVerElement) {
			this.versionlist.add(newVerElement);
		}

		public String getLatestVersion() {
			return this.versionlist.get(0);
		}

		public String toString() {
			String result = "";
			result += this.getTag() + " : ";
			int arraysize = this.versionlist.size();
			for (int i = 0; i < arraysize; i++) {
				result += this.versionlist.get(i);
				if (i != arraysize - 1)
					result += ", ";
			}
			return result;
		}
	}

	ArrayList<KVLRecord> array;

	public KVLTable() {
		array = new ArrayList<KVLRecord>();
	}

	public void addVersionInfo(String newTag, String newVerElement) {
		int arraysize = this.array.size();
		boolean isSaved = false;
		for (int i = 0; i < arraysize; i++) {
			if (array.get(i).getTag().equals(newTag)) {
				isSaved = true;
				array.get(i).addVersionElement(newVerElement);
			}
		}

		if (!isSaved) {
			KVLRecord newRecord = new KVLRecord(newTag);
			newRecord.addVersionElement(newVerElement);

			array.add(newRecord);
		}
	}

	public String getLatestVersions() {
		String result = "";

		int arraysize = this.array.size();

		for (int i = 0; i < arraysize; i++) {
			result += this.array.get(i).getTag() + " : "
					+ this.array.get(i).getLatestVersion();
			if (i != arraysize - 1)
				result += ", ";
		}

		return result;
	}

	public String getAllVersionInfo() {
		String result = "";
		int arraysize = this.array.size();

		for (int i = 0; i < arraysize; i++) {
			result += this.array.get(i).toString();
			if (i != arraysize - 1) {
				result += ", ";
			}
		}

		return result;
	}

	public String toString() {
		return this.getLatestVersions();
	}
}
