package org.manalith.ircbot.plugin.et;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Player")
public class Player {

	private long id;

	private String IRCName;

	private String ETName;

	private boolean ready;

	public boolean equals(Player p) {
		return p.getETName().equals(getETName())
				&& p.getIRCName().equals(getIRCName());
	}

	public String getETName() {
		return ETName;
	}

	public String getIRCName() {
		return IRCName;
	}

	public boolean isReady() {
		return ready;
	}

	public void setETName(String eTName) {
		ETName = eTName;
	}

	public void setIRCName(String iRCName) {
		IRCName = iRCName;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
