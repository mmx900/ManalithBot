package org.manalith.ircbot.plugin.distropkgfinder;

import org.manalith.ircbot.plugin.distropkgfinder.exceptions.EmptyTokenStreamException;

public class FedoraRpmFindInfoParser {

	private TokenArray array;

	public FedoraRpmFindInfoParser() {
		array = null;
	}

	public FedoraRpmFindInfoParser(TokenArray newTokenStream) {
		array = newTokenStream;
	}

	public PkgTable generatePkgTable() throws EmptyTokenStreamException {
		PkgTable result = new PkgTable();

		if (array == null)
			throw new EmptyTokenStreamException();

		int len = array.getSize();
		int i = 0;
		int colCount = 0;

		boolean isTheFirstRow = false;
		String temp = "";
		String Dist = "";
		String PkgName = "";
		String Version = "";
		String Description = "";

		String GroupName = "Exact hits";

		while (i < len) {
			if (array.getElement(i++).getTokenSubtype() == TokenSubtype.TableOpen) {
				while (array.getElement(i).getTokenSubtype() != TokenSubtype.TableClose) {
					if (array.getElement(i++).getTokenSubtype() == TokenSubtype.TBodyOpen) {
						while (array.getElement(i).getTokenSubtype() != TokenSubtype.TBodyClose) {
							if (array.getElement(i++).getTokenSubtype() == TokenSubtype.TrOpen) {
								while (array.getElement(i).getTokenSubtype() != TokenSubtype.TrClose) {
									if (!isTheFirstRow) {
										while (array.getElement(i)
												.getTokenSubtype() != TokenSubtype.TrClose)
											i++;
										isTheFirstRow = true;
									} else {
										if (array.getElement(i++)
												.getTokenSubtype() == TokenSubtype.TdOpen) {
											while (array.getElement(i)
													.getTokenSubtype() != TokenSubtype.TdClose) {
												switch (colCount) {
												case 0:
													temp = array.getElement(i)
															.getTokenString();
													colCount++;
													break;
												case 1:
													Description = array
															.getElement(i)
															.getTokenString();
													colCount++;
													break;
												case 2:
													Dist = array.getElement(i)
															.getTokenString();
													colCount++;
													break;
												case 3:
													colCount++;
													break;
												}
												i++;

												if (Dist.contains("Fedora")
														&& colCount == 4) {
													String[] tmp = Dist
															.split("\\s");
													int DistVersion = 0;
													int startIndex = 0;

													try {
														DistVersion = Integer
																.parseInt(tmp[1]);
													} catch (Exception e) {
														;
													}

													if (DistVersion == 0) {
														colCount = 0;
														continue;
													}

													tmp = temp.split("\\.");
													int k = 1;
													while (!tmp[k]
															.contains("fc")) {
														tmp[startIndex] += ("." + tmp[k++]);
													}

													temp = tmp[0]; // package
																	// name and
																	// version.

													tmp = temp.split("\\-");

													k = 1;
													while (!(tmp[k].charAt(0) >= '0' && tmp[k]
															.charAt(0) <= '9')) {
														tmp[startIndex] += ("-" + tmp[k++]);
													}
													PkgName = tmp[startIndex];

													startIndex = k++;
													while (k < tmp.length) {
														tmp[startIndex] += ("-" + tmp[k++]);
													}
													Version = tmp[startIndex];

													PkgUnit newPkgUnit = new PkgUnit(
															PkgName, Version,
															Description);
													result.addElement(
															GroupName,
															newPkgUnit);

													colCount = 0;
													return result;
												}
												if (colCount == 4)
													colCount = 0;
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

		return result;
	}

}
