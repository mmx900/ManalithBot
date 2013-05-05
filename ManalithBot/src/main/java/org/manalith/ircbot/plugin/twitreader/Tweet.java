package org.manalith.ircbot.plugin.twitreader;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tweet {
	public long id;
	public String text;
	public User user;
	@JsonProperty("created_at")
	public String createdAt;

	@JsonIgnoreProperties(ignoreUnknown = true)
	public class User {
		public long id;
		public String name;
		@JsonProperty("screen_name")
		public String screenName;
	}
}
