/*
 * Created on 2005. 8. 11
 */
package org.manalith.ircbot.resources;

import java.util.List;


public class ChannelOwnersPair {
	private String channel;
	private List<String> owners;
	
	public ChannelOwnersPair(String channel, List<String> owners){
		this.channel = channel;
		this.owners = owners;
	}

	
	/**
	 * @return Returns the channel.
	 */
	public String getChannel() {
		return channel;
	}

	
	/**
	 * @param channel The channel to set.
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}

	
	/**
	 * @return Returns the owners.
	 */
	public List<String> getOwners() {
		return owners;
	}

	
	/**
	 * @param owners The owners to set.
	 */
	public void setOwners(List<String> owners) {
		this.owners = owners;
	}
	
	
}
