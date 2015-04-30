package org.manalith.ircbot.plugin.sc2;

import java.text.MessageFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table
public class SC2Player {

	public static String PROFILE_URL = "http://wcs.battle.net/sc2/ko/players/";

	private static MessageFormat FORMAT = new MessageFormat("{0}({1})");

	@Id
	@Column(nullable = false)
	public String id;

	@Column(unique = false, nullable = false)
	public String nick;

	@Column(unique = false, nullable = false)
	public String name;

	@Column(unique = false, nullable = true)
	public String team;

	@Column(unique = false, nullable = false)
	public SC2Race race;

	@Column(unique = false, nullable = false)
	public Date lastUpdate;

	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            설정할 id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return nick
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * @param nick
	 *            설정할 nick
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            설정할 name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return team
	 */
	public String getTeam() {
		return team;
	}

	/**
	 * @param team
	 *            설정할 team
	 */
	public void setTeam(String team) {
		this.team = team;
	}

	/**
	 * @return race
	 */
	public SC2Race getRace() {
		return race;
	}

	/**
	 * @param race
	 *            설정할 race
	 */
	public void setRace(SC2Race race) {
		this.race = race;
	}

	/**
	 * @return lastUpdate
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @param lastUpdate
	 *            설정할 lastUpdate
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * @param uri
	 * @return
	 */
	public static String getIdFromUri(String uri) {
		if (uri == null) {
			throw new NullArgumentException("uri");
		}

		int index = uri.lastIndexOf("/");

		return uri.substring(index + 1);

	}

	/**
	 * @param id
	 * @return
	 */
	public static String getUriFromId(String id) {
		if (id == null) {
			throw new NullArgumentException("id");
		}

		return PROFILE_URL + id;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		Object[] args = new Object[2];

		args[0] = name;
		args[1] = race.getAbbrevation();

		return FORMAT.format(args);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else {
			return EqualsBuilder.reflectionEquals(this, obj);
		}
	}
}
