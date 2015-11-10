package org.manalith.ircbot.plugin.sc2;

import org.apache.commons.lang.NullArgumentException;

public enum SC2Race {

	Terran("테란", "T"), Protoss("프로토스", "P"), Zerg("저그", "Z");

	private String name;

	private String abbrevation;

	private SC2Race(String name, String abbrevation) {
		this.name = name;
		this.abbrevation = abbrevation;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return abbrevation
	 */
	public String getAbbrevation() {
		return abbrevation;
	}

	public boolean matches(String profile) {
		if (profile == null) {
			throw new NullArgumentException("profile");
		}

		return profile.toLowerCase().contains(name().toLowerCase());
	}
}