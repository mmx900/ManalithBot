package org.manalith.ircbot.plugin.curex;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenExchangeRate {

	public class CurrencyNameTable {

		// local file
		private PropertiesConfiguration config;
		private File f;
		private String filename;

		// local variable
		private String key;
		private String val;

		public CurrencyNameTable() throws Exception {
			// do not use :-P
			;
		}

		public CurrencyNameTable(String path) {

			filename = "currencylist.prop";
			f = new File(path + "/" + filename);

			if (f.exists()) {
				try {
					config = new PropertiesConfiguration(path + "/" + filename);
				} catch (ConfigurationException e) {
					e.printStackTrace();
				}

				return;
			}

			key = "";
			val = "";

			// if file doesn't exist
			try {
				config = new PropertiesConfiguration(path + "/" + filename);
			} catch (ConfigurationException e) {
				config = new PropertiesConfiguration();
				config.setFile(f);
				try {
					config.save(); // save an empty
					config = new PropertiesConfiguration(path + "/" + filename);
				} catch (ConfigurationException e1) {
					e1.printStackTrace();
				}
			}

			// fetch currency unit list
			try {
				Document doc = Jsoup
						.connect(
								"http://www.currency-iso.org/dam/downloads/table_a1.xml")
						.get();
				Elements ccytbl = doc.select("ISO_4217>CcyTbl>Ccyntry");

				int sz = ccytbl.size();
				for (int i = 0; i < sz; i++) {
					Element ccyntry = ccytbl.get(i);

					val = ccyntry.select("CtryNm").text();
					key = ccyntry.select("Ccy").text();

					if (StringUtils.isEmpty(key))
						continue;
					if (key.equals("XUA") || key.equals("XSU"))
						continue;
					if (key.equals("USD"))
						val = "UNITED STATES";
					if (key.equals("EUR"))
						val = "EUROPEAN UNITED";
					if (val.contains("\\u2019S"))
						val = val.replaceAll("\\u2019S", "'S");
					if (val.contains(", "))
						val = val.replaceAll(", ", " ");
					if (val.contains(","))
						val = val.replaceAll(",", "");

					config.setProperty(key, val);
				}

				config.setProperty("BTC", "BITCOIN");
				config.setProperty("EEK", "ESTONIA KROON");
				config.setProperty("MTL", "MALTA LIRA");
				config.setProperty("LVL", "LATVIJAS REPUBLIKA");
				config.setProperty("ZMK", "ZAMBIA KWACHA");
				config.save();

			} catch (IOException | ConfigurationException e) {
				e.printStackTrace();
			}
		}

		public String[] getCurrencyList() {
			ArrayList<String> strarr = new ArrayList<>();
			String[] result;
			Iterator<String> list = config.getKeys();
			while (list.hasNext())
				strarr.add(list.next());

			result = new String[strarr.size()];
			strarr.toArray(result);

			return result;
		}

		public String getCountryNameByCurrency(String currency) {
			if (!StringUtils.isEmpty(config.getString(currency)))
				return config.getString(currency);
			else
				return "";
		}
	}

	private String base_url;
	private String app_id;
	private CurrencyNameTable table;

	private HashMap<String, Double> rates;

	public OpenExchangeRate(String path, String new_app_id) throws IOException {

		base_url = "https://openexchangerates.org/api/latest.json?app_id=";
		app_id = new_app_id;

		table = new CurrencyNameTable(path);
		rates = new HashMap<String, Double>();
		initCurrencyTable();

	}

	private void initCurrencyTable() throws IOException {
		if (StringUtils.isEmpty(app_id))
			throw new IllegalArgumentException();

		ObjectMapper om = new ObjectMapper();
		JsonNode node = om.readTree(new URL(base_url + app_id));
		node = node.get("rates");
		Iterator<String> keys = node.fieldNames();
		String k = "";
		while (keys.hasNext()) {
			k = keys.next();
			rates.put(k, Double.valueOf(node.path(k).asDouble()));
		}
	}

	public double calc(int val, String sourceUnit, String targetUnit) {
		double r = rates.get(targetUnit).doubleValue()
				/ rates.get(sourceUnit).doubleValue();
		return r * val;
	}

	public double calc(double val, String sourceUnit, String targetUnit) {
		double r = rates.get(targetUnit).doubleValue()
				/ rates.get(sourceUnit).doubleValue();
		return r * val;
	}

	public boolean isValidCurrency(String currencyUnit) {
		ArrayList<String> arr = getCurrencyList();
		return arr.contains(currencyUnit);
	}

	public ArrayList<String> getCurrencyList() {
		return new ArrayList<>(Arrays.asList(rates.keySet().toArray(
				new String[rates.size()])));
	}

	public String getCountryName(String currencyUnit) {
		return table.getCountryNameByCurrency(currencyUnit);
	}
}
