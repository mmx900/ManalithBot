package org.manalith.ircbot.plugin.twitreader;

import com.google.gson.annotations.SerializedName;

public class Tweet {
	public long id;
	public String text;
	public User user;
	@SerializedName("created_at")
	public String createdAt;

	public class User {
		public long id;
		public String name;
		@SerializedName("screen_name")
		public String screenName;
	}
}
