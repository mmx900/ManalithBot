package org.manalith.ircbot.plugin.dictionary;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DictionaryManager {
	private static DictionaryManager _instance;

	public static DictionaryManager instance() {
		if (_instance == null)
			_instance = new DictionaryManager();

		return _instance;
	}

	private DictionaryManager() {
		load();
	}

	private List<Word> list;

	public boolean hasWord(String word) {
		for (Word w : list) {
			if (w.word.equals(word)) {
				return true;
			}
		}

		return false;
	}

	public Word getWord(String word) throws NotRegisteredException {
		for (Word w : list) {
			if (w.word.equals(word)) {
				return w;
			}
		}

		throw new NotRegisteredException();
	}

	public void add(Word word) {
		if (hasWord(word.word)) {
			// 이미 단어가 있으면 지운다.
			try {
				remove(word.word);
			} catch (NotRegisteredException e) {
				// ignore
			}
		}

		list.add(word);
	}

	// public void add(Word word) throws AlreadyRegisteredException{
	// for(Word w : list){
	// if(w.word.equals(word.word)){
	// throw new AlreadyRegisteredException();
	// }
	// }
	//
	// list.add(word);
	// }

	public void remove(String word) throws NotRegisteredException {
		boolean removed = false;

		for (Word w : list) {
			if (w.word.equals(word)) {
				list.remove(w);
				removed = true;
				break;
			}
		}

		if (!removed)
			throw new NotRegisteredException();
	}

	public void load() {
		if (list == null) {
			try {
				FileInputStream os = new FileInputStream("dictionary.xml");
				XMLDecoder decoder = new XMLDecoder(os);
				list = (List<Word>) decoder.readObject();
				// Player p = (Player)decoder.readObject();
				decoder.close();
			} catch (IOException ex) {
				// ignore
				list = new ArrayList<Word>();
			}
		}
	}

	public void save() {
		if (list != null) {
			try {
				FileOutputStream os = new FileOutputStream("dictionary.xml");
				XMLEncoder encoder = new XMLEncoder(os);
				encoder.writeObject(list);
				encoder.close();
			} catch (IOException ex) {
				// ignore
			}
		}
	}
}
