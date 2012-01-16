package org.manalith.ircbot.plugin.missedmessage;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.manalith.ircbot.common.PropertyManager;

public class MissedMessageRunner {
	private String ResourcePath;
	private static String filename = "msglist.prop";

	public MissedMessageRunner() {
		this.setResourcePath("");
	}

	public MissedMessageRunner(String newResourcePath) {
		this.setResourcePath(newResourcePath);
	}

	private void setResourcePath(String newResourcePath) {
		File f = new File(newResourcePath);
		if (!f.exists())
			f.mkdirs();

		this.ResourcePath = newResourcePath;
	}

	private String getResourcePath() {
		return this.ResourcePath;
	}

	public String addMsg(String sender, String receiver, String msg) {
		StringBuilder result = new StringBuilder();
		PropertyManager prop = new PropertyManager(this.getResourcePath(),
				MissedMessageRunner.filename);

		boolean savedMsg = false;
		int msglen = msg.length();

		try {
			prop.loadProperties();
			String[] userlist = prop.getKeyList();
			if (userlist != null) {
				for (String key : userlist) {
					if (key.equals(receiver)) {
						if (prop.getValue(receiver).length() != 0) {
							result.append(StringUtils.split(receiver, '.')[1]);
							String str = prop.getValue(receiver);
							
							result.append("님의 남은 메시지 공간 ( ");
							int availableSpace = 3 - str.split("\\:\\:").length;
							if (availableSpace != 0) {
								if (msglen <= 150) {
									
									prop.setValue(receiver, str + "::" + "[남김:"
											+ sender + "] " + msg);
									result.append(availableSpace - 1);
									result.append(" / 3 )");
								} else {
									result.delete(0, result.length());
									result.append("메시지 길이가 150자를 넘었습니다.");
								}
							} else {
								result.append(availableSpace);
								result.append(" / 3 ) : 더이상 메시지를 남길 수 없습니다");
							}
						} else // if key has no value
						{
							if (msglen <= 150) {
								prop.setValue(receiver, "[남김:" + sender + "] "
										+ msg);
								result.append(StringUtils.split(receiver,'.')[1]);
								result.append("님의 남은 메시지 공간 ( 2 / 3 )");
							} else {
								result.delete(0, result.length());
								result.append("메시지 길이가 150자를 넘었습니다.");
							}
						}
						savedMsg = true;
						prop.storeProperties();
						break;
					}
				}

				if (!savedMsg) {
					result.append(receiver.split("\\.")[1]
							+ "님은 이 채널에 방문했거나 대화 후 나간 적이 없습니다.");
				}
			}
		} catch (IOException ioe) {
			return ioe.getMessage();
		}

		return result.toString();
	}

	public String[] getMsg(String newRecv) {
		String[] msgs = null;

		PropertyManager prop = new PropertyManager(this.getResourcePath(),
				MissedMessageRunner.filename);
		try {
			
			// init!
			try {
				prop.loadProperties();
			} catch (IOException e) {
				prop.setProp(new Properties());
				prop.storeProperties();
				return msgs;
			}

			if ( this.isMatchedNickinList(newRecv) )
			{
				if (prop.getValue(newRecv).length() != 0) {
					msgs = prop.getValue(newRecv).split("\\:\\:");
					
					for ( int i = 0 ; i < msgs.length; i++ )
						msgs[i] = newRecv.split("\\.")[1] + ", " + msgs[i];
					
					prop.setValue(newRecv, "");
					prop.storeProperties();
				}
			}
		} catch (IOException ioe) {
			msgs = new String[1];
			msgs[0] = ioe.getMessage();
		}

		return msgs;
	}

	public void addMsgSlot(String newRecv) {
		PropertyManager prop = new PropertyManager(this.getResourcePath(),
				MissedMessageRunner.filename);
		try {
			try {
				prop.loadProperties();
			} catch (IOException ioe) {
				// create empty properties
				prop.setProp(new Properties());
				prop.storeProperties();
			}

			// if user not found, init msgslot
			prop.setValue(newRecv, "");
			prop.storeProperties();

		} catch (IOException ioe) {
			; // ignore exception
		}
	}
	
	public boolean isMatchedNickinList ( String newRecv )
	{
		boolean result = false;
		PropertyManager prop = new PropertyManager ( this.getResourcePath(), MissedMessageRunner.filename);
		
		try {
			try {
				prop.loadProperties();
			} catch (IOException ioe) {
				// create empty properties
				prop.setProp(new Properties());
				prop.storeProperties();
			}

			String[] userlist = prop.getKeyList();
			if (userlist != null) {
				for (String u : userlist) {
					// if user found from userlist, just break this routine
					if (u.equals(newRecv))
					{
						result = true;
						break;
					}
				}
			}
		}
		catch ( IOException e )
		{
			; // ignore
		}
		
		return result;
	}

}
