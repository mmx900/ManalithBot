package org.manalith.ircbot.plugin.distropkgfinder;

import org.manalith.ircbot.plugin.distropkgfinder.exceptions.EmptyTokenStreamException;

public class UbuntuPkgCurrentVerParser {
	private TokenArray array;

	public UbuntuPkgCurrentVerParser() {
		this.array = null;
	}

	public UbuntuPkgCurrentVerParser(TokenArray newArray) {
		this.array = newArray;
	}

	public String extractCurrentVersion() throws EmptyTokenStreamException {
		String result = "";
		if (array == null || array.getSize() == 0)
			throw new EmptyTokenStreamException();

		int size = array.getSize();
		int i = 0;

		String[] tagnattr;
		String[] keyval;
		String value;

		while (i < size) {
			if (array.getElement(i++).getTokenSubtype() == TokenSubtype.SelectOpen) {
				while (array.getElement(i).getTokenSubtype() != TokenSubtype.SelectClose) {
					if (array.getElement(i).getTokenSubtype() == TokenSubtype.OptionOpen) {

						tagnattr = array
								.getElement(i)
								.getTokenString()
								.substring(
										1,
										array.getElement(i).getTokenString()
												.length() - 1).split("\\s");
						if (tagnattr.length == 3) {
							keyval = tagnattr[2].split("\\=");
							value = keyval[1].substring(1,
									keyval[1].length() - 1);
							if (value.equals("selected")) {
								i++;
								result = array.getElement(i).getTokenString();
								return result;
							}

						} else {
							i++;
						}
					} else {
						i++;
					}
				}
			}
		}

		return result;
	}
}
