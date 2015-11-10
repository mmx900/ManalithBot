package org.manalith.ircbot.plugin.sc2;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.Type;

@Entity
@Table
public class SC2Event implements Comparable<SC2Event> {

	private static MessageFormat FORMAT = new MessageFormat(
			"[{0}] {1} (출전 선수: {2})");

	private static DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	public Long id;

	@Type(type = "timestamp")
	@Column(nullable = false)
	private Date date;

	@Column(unique = false, nullable = false)
	private String title;

	@OneToMany(fetch = FetchType.EAGER)
	@Column(unique = false, nullable = false)
	private List<SC2Player> players;

	/**
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            설정할 id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            설정할 date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            설정할 title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return players
	 */
	public List<SC2Player> getPlayers() {
		return players;
	}

	/**
	 * @param players
	 *            설정할 players
	 */
	public void setPlayers(List<SC2Player> players) {
		this.players = players;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(SC2Event o) {
		return date.compareTo(o.getDate());
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		Object[] args = new Object[3];

		args[0] = DATE_FORMAT.format(date);
		args[1] = title;
		args[2] = StringUtils.join(players, ", ");

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