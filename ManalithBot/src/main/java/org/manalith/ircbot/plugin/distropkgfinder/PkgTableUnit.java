package org.manalith.ircbot.plugin.distropkgfinder;

public class PkgTableUnit {
	private String GroupName;
	private PkgList array;

	public PkgTableUnit() {
		this.setGroupName("");
		this.array = new PkgList();
	}

	public PkgTableUnit(String newGroupName) {
		this.setGroupName(newGroupName);
		this.array = new PkgList();
	}

	public void setGroupName(String newGroupName) {
		this.GroupName = newGroupName;
	}

	public String getGroupName() {
		return this.GroupName;
	}

	public int getSize() {
		return this.array.size();
	}

	public void incCount() {
		this.array.incCount();
	}

	public void addElement(PkgUnit newPkgUnit) {
		array.addElement(newPkgUnit);
	}

	public String toString() {
		String result = "";
		/*
		 * if ( !this.getGroupName().equals("") ) result += "<" +
		 * this.getGroupName() + "> "; //
		 */
		result += this.array.toString();
		return result;
	}
}
