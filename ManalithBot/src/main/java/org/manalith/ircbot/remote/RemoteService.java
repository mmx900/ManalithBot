package org.manalith.ircbot.remote;

import javax.annotation.security.RolesAllowed;

public interface RemoteService {
	@RolesAllowed("ROLE_ADMIN")
	public void sendMessage(String target, String message);
}
