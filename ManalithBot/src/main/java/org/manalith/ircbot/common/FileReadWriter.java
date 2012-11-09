/*
 	org.manalith.ircbot.common/FileReadWriter.java
 	ManalithBot - An open source IRC bot based on the PircBot Framework.
 	Copyright (C) 2011  Seong-ho, Cho <darkcircle.0426@gmail.com>

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
package org.manalith.ircbot.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileReadWriter {
	private File file;
	private InputStreamReader isr;
	private OutputStreamWriter osw;

	public FileReadWriter(String fileName) {
		file = allocateFileObject(fileName);
	}

	private File allocateFileObject(String fileName) {
		String dir = "";

		if (fileName.contains(Character.toString(File.separatorChar))) {
			int len = fileName.split("\\"
					+ Character.toString(File.separatorChar)).length - 1;

			for (int i = 0; i < len; i++) {
				if (i != 0)
					dir += "/";

				dir += fileName.split("\\/")[i];
			}
		}

		File t = new File(dir);
		if (!t.exists())
			t.mkdirs();

		return new File(fileName);
	}

	protected boolean exists() {
		return file.exists();
	}

	protected void createFile() throws FileNotFoundException, IOException {
		file.createNewFile();
		allocateStreamWriter();
	}

	protected void allocateStreamReader() throws FileNotFoundException {
		isr = new InputStreamReader(new FileInputStream(file));
	}

	protected void allocateStreamWriter() throws FileNotFoundException {
		osw = new OutputStreamWriter(new FileOutputStream(file));
	}

	public InputStreamReader getStreamReaderResource() {
		return isr;
	}

	public OutputStreamWriter getStreamWriterResource() {
		return osw;
	}
}
