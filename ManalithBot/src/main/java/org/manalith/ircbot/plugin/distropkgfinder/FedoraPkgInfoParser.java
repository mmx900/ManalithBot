package org.manalith.ircbot.plugin.distropkgfinder;

import org.manalith.ircbot.plugin.distropkgfinder.exceptions.EmptyTokenStreamException;

public class FedoraPkgInfoParser {
	private TokenArray array;

	public FedoraPkgInfoParser() {
		this.array = null;
	}

	public FedoraPkgInfoParser(TokenArray newArray) {
		this.array = newArray;
	}

	public PkgTable generatePkgTable(PkgTable currentPkgTable)
			throws EmptyTokenStreamException {
		PkgTable result;

		String GroupName = "";
		String PkgName = "";
		String Version = "";
		String Description = "";

		boolean checkTheFirstElement = false;

		if (currentPkgTable == null) {
			result = new PkgTable();
			GroupName = "Exact Hits";
		} else {
			result = currentPkgTable;
			if (currentPkgTable.size() == 0)
				GroupName = "";
			else
				GroupName = "Other Hits";
		}

		if (array == null)
			throw new EmptyTokenStreamException();

		int cnt = array.getSize();
		if (cnt == 0)
			throw new EmptyTokenStreamException();

		int i = 0;

		String[] tagnattr = null;
		String[] keyval = null;
		String value = "";

		String PkgStatusUrl = "";
		String data = "";

		String str = "";

		try {
			while (i < cnt) {
				if (array.getElement(i++).getTokenSubtype() == TokenSubtype.DivOpen) {
					while (array.getElement(i).getTokenSubtype() != TokenSubtype.DivClose) {
						if (array.getElement(i++).getTokenSubtype() == TokenSubtype.DlOpen) {
							while (array.getElement(i).getTokenSubtype() != TokenSubtype.DlClose) {
								if (array.getElement(i++).getTokenSubtype() == TokenSubtype.DtOpen) {
									while (array.getElement(i)
											.getTokenSubtype() != TokenSubtype.DtClose) {
										if (array.getElement(i)
												.getTokenSubtype() == TokenSubtype.AOpen) {
											tagnattr = array.getElement(i)
													.getTokenString()
													.split("\\s");
											keyval = tagnattr[1].split("\\=");
											String temp = keyval[1] + "="
													+ keyval[2];
											value = temp.substring(1,
													temp.length() - 1);

											PkgStatusUrl = "https://admin.fedoraproject.org"
													+ value;

											StreamDownloader downloader = new StreamDownloader(
													PkgStatusUrl);
											data = downloader
													.downloadDataStream();

											FedoraPkgStatusTokenAnalyzer analyzer = new FedoraPkgStatusTokenAnalyzer(
													data);
											TokenArray arr = analyzer
													.analysisTokenStream();
											FedoraPkgStatusParser parser = new FedoraPkgStatusParser(
													arr);

											// pkgversion
											Version = parser.getPkgVersion();

											i++;
											while (array.getElement(i)
													.getTokenSubtype() != TokenSubtype.AClose) {
												if (array.getElement(i)
														.getTokenSubtype() == TokenSubtype.TextString) {
													PkgName = array.getElement(
															i).getTokenString();
													i++;
												}
											}
											i++;

										} else {
											i++;
											continue;
										}

										if (array.getElement(i)
												.getTokenSubtype() == TokenSubtype.TextString) {
											str = array.getElement(i)
													.getTokenString();

											while (str.charAt(0) == '-') {
												str = str.substring(1,
														str.length());
											}
											Description = str.trim();

											if (((GroupName
													.equals("Other Hits") && checkTheFirstElement) || GroupName
													.equals("Exact Hits"))
													|| GroupName.equals("")) {
												PkgUnit newPkgUnit = new PkgUnit(
														PkgName, Version,
														Description);
												result.addElement(GroupName,
														newPkgUnit);
											} else {
												checkTheFirstElement = true;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
			return result;
		}

		return result;
	}
}
