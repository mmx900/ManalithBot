package org.manalith.ircbot.plugin.distropkgfinder;

import org.manalith.ircbot.plugin.distropkgfinder.exceptions.EmptyTokenStreamException;

public class FedoraPkgStatusParser {
	private TokenArray array;

	public FedoraPkgStatusParser() {
		this.array = null;
	}

	public FedoraPkgStatusParser(TokenArray newArray) {
		this.array = newArray;
	}

	public String getPkgVersion() throws EmptyTokenStreamException {
		String result = "";

		if (array == null)
			throw new EmptyTokenStreamException();

		int cnt = array.getSize();
		if (cnt == 0)
			throw new EmptyTokenStreamException();

		int i = 0;
		int version = 0;

		String[] tagnattr = null;
		String[] keyval = null;
		String value = "";

		String str = "";
		String url = "";
		String[] pkgversion = new String[3];

		try {
			while (i < cnt) {
				if (array.getElement(i).getTokenSubtype() == TokenSubtype.POpen) {
					i++;
					if (array.getElement(i).getTokenSubtype() == TokenSubtype.PClose) {
						i++;
						continue;
					} else if (array.getElement(i).getTokenSubtype() == TokenSubtype.TextString) {
						if (array.getElement(i).getTokenString()
								.equals("Contents:")) {
							i++;
							while (array.getElement(i).getTokenSubtype() != TokenSubtype.PClose) {
								if (array.getElement(i++).getTokenSubtype() == TokenSubtype.UlOpen) {
									while (array.getElement(i)
											.getTokenSubtype() != TokenSubtype.UlClose) {
										if (array.getElement(i++)
												.getTokenSubtype() == TokenSubtype.LiOpen) {
											while (array.getElement(i)
													.getTokenSubtype() != TokenSubtype.LiClose) {
												if (array.getElement(i++)
														.getTokenSubtype() == TokenSubtype.AOpen) {
													while (array.getElement(i)
															.getTokenSubtype() != TokenSubtype.AClose) {
														if (array
																.getElement(i)
																.getTokenSubtype() == TokenSubtype.TextString) {
															String[] DistVersion = array
																	.getElement(
																			i)
																	.getTokenString()
																	.split("\\s");

															if (version == 0) {
																try {
																	version = Integer
																			.parseInt(DistVersion[1]);
																} catch (Exception e) {
																	i++;
																}
															} else {
																i++;
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					} else {
						i++;
					}
				} else if (array.getElement(i).getTokenSubtype() == TokenSubtype.UlOpen) {
					String temp = array.getElement(i).getTokenString();
					tagnattr = temp.substring(1, temp.length() - 1)
							.split("\\s");
					if (tagnattr.length != 1) {
						keyval = tagnattr[1].split("\\=");
						value = keyval[1].substring(1, keyval[1].length() - 1);
						if (value.equals("actions")) {
							while (array.getElement(i).getTokenSubtype() != TokenSubtype.UlClose) {
								if (array.getElement(i++).getTokenSubtype() == TokenSubtype.LiOpen) {
									while (array.getElement(i)
											.getTokenSubtype() != TokenSubtype.LiClose) {
										if (array.getElement(i)
												.getTokenSubtype() == TokenSubtype.AOpen) {
											str = array.getElement(i)
													.getTokenString();
											str = str.substring(1,
													str.length() - 1);
											tagnattr = str.split("\\s");
											int len = tagnattr.length;

											for (int l = 1; l < len; l++) {
												keyval = tagnattr[l]
														.split("\\=");
												if (keyval[0].equals("class")) {
													str = keyval[1]
															.substring(
																	1,
																	keyval[1]
																			.length() - 1);
												} else if (keyval[0]
														.equals("href")
														&& str.equals("koji")) {
													String tmp = "";
													for (int ll = 1; ll < keyval.length; ll++) {
														if (!tmp.equals(""))
															tmp += "="
																	+ keyval[ll];
														else
															tmp += keyval[ll];
													}

													url = tmp.substring(1,
															tmp.length() - 1);

													StreamDownloader downloader = new StreamDownloader(
															url);
													String data = downloader
															.downloadDataStream();

													FedoraPkgBuildTableTokenAnalyzer analyzer = new FedoraPkgBuildTableTokenAnalyzer(
															data);
													TokenArray arr = analyzer
															.analysisTokenStream();
													FedoraPkgBuildTableLatestVersionParser parser = new FedoraPkgBuildTableLatestVersionParser(
															arr);
													pkgversion = parser
															.getPkgVersion();

												}
											}
											i++;
										} else {
											i++;
										}
									}
								}
							}
						}
					}
				} else {
					i++;
				}
			}
		} catch (Exception e) {
			;
		}

		for (int j = 0; j < pkgversion.length; j++) {
			if (pkgversion[j] == null)
				break;
			String[] pkgndist = pkgversion[j].split("fc");
			pkgndist[0] = pkgndist[0].substring(0, pkgndist[0].length() - 1); // remove
																				// dot.
			if (version != 0) {
				if (pkgndist[1].contains(".")) {
					pkgndist[1] = pkgndist[1].split("\\.")[0];
				}

				if (Integer.parseInt(pkgndist[1]) > version)
					continue;
				else
					result = pkgndist[0];
			} else {
				result = pkgndist[0];
			}
		}

		return result;
	}
}
