package org.manalith.ircbot.plugin.distropkgfinder;

import java.util.ArrayList;

public class PkgList extends ArrayList<PkgUnit> {
	private static final long serialVersionUID = 1L;
	private int count;

	public PkgList() {
		super();
		this.count = 0;
	}

	public void addElement(PkgUnit newPkgUnit) {
		if (this.size() == 3)
			this.incCount();
		else
			this.add(newPkgUnit);
	}

	public int getCount() {
		return this.count;
	}

	public void incCount() {
		count++;
	}

	public String toString() {
		String result = "";
		int size = this.size();

		for (int i = 0; i < size; i++) {
			if (i != 0)
				result += ", " + this.get(i).toString();
			else
				result += this.get(i).toString();
		}

		/*
		 * if ( this.getCount() != 0 ) result += " (and " + this.getCount() +
		 * " more packages)"; //
		 */

		return result;
	}
}