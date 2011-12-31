package org.manalith.ircbot.plugin.distropkgfinder;

import org.manalith.ircbot.plugin.distropkgfinder.exceptions.EmptyTokenStreamException;

public class FedoraPkgBuildTableLatestVersionParser {

	private TokenArray array;

	public FedoraPkgBuildTableLatestVersionParser() {
		this.setArray(null);
	}

	public FedoraPkgBuildTableLatestVersionParser(TokenArray newArray) {
		this.setArray(newArray);
	}

	public void setArray(TokenArray newArray) {
		this.array = newArray;
	}

	public TokenArray getArray() {
		return this.array;
	}

	public String[] getPkgVersion() throws EmptyTokenStreamException {
		String[] result = new String[3];

		if (array == null)
			throw new EmptyTokenStreamException();

		int cnt = array.getSize();
		if (cnt == 0)
			throw new EmptyTokenStreamException();

		int i = 0;
		int vercnt = 0;

		boolean bString = false;

		String[] versionstrings;
		String value = "";
		String str = "";

		try {
			while (i < cnt) {
				if (array.getElement(i++).getTokenSubtype() == TokenSubtype.TrOpen) {
					while (array.getElement(i).getTokenSubtype() != TokenSubtype.TrClose) {
						if (array.getElement(i++).getTokenSubtype() == TokenSubtype.TdOpen) {
							while (array.getElement(i).getTokenSubtype() != TokenSubtype.TdClose) {
								if (array.getElement(i).getTokenSubtype() == TokenSubtype.AOpen) {
									if (array.getElement(i++).getTokenString()
											.contains("buildinfo"))
										bString = true;

									while (array.getElement(i)
											.getTokenSubtype() != TokenSubtype.AClose) {
										if (array.getElement(i)
												.getTokenSubtype() == TokenSubtype.TextString
												&& bString == true) {
											str = array.getElement(i)
													.getTokenString();
											versionstrings = str.split("\\-");
											value = "";

											for (int l = 0; l < versionstrings.length; l++) {
												if ((versionstrings[l]
														.charAt(0) >= '0' && versionstrings[l]
														.charAt(0) <= '9')
														|| (versionstrings[l]
																.charAt(0) == 'b' || versionstrings[l]
																.charAt(0) == 'a')
														|| versionstrings[l]
																.charAt(0) == 'p') {
													if (!value.equals(""))
														value += "-"
																+ versionstrings[l];
													else {
														if (versionstrings[l]
																.contains("."))
															value += versionstrings[l];
													}
												}
											}

											result[vercnt++] = value;
											bString = false;
										} else {
											i++;
										}
									}
								} else {
									i++;
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			result = null;
		}

		return result;
	}
}
