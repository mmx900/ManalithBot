/*
 	org.manalith.ircbot.common/PropFileReadWriter.java
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

import java.util.Properties;
import java.io.IOException;
import java.io.FileNotFoundException;

public class PropFileReadWriter extends FileReadWriter {
	public PropFileReadWriter() throws FileNotFoundException {
		super();
	}

	public PropFileReadWriter(String newFilename) throws FileNotFoundException {
		super(newFilename);
	}

	public Properties bringUpPropertyFromFile() throws IOException {
		this.allocateStreamReader();
		Properties result = new Properties();
		result.load(this.getStreamReaderResource());

		return result;
	}

	public void pushUpPropertyToFile(Properties newProperties)
			throws IOException {
		if (!this.exists())
			this.createFile();

		this.allocateStreamWriter();
		newProperties.store(this.getStreamWriterResource(), "");
	}
}
