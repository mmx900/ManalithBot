package com.etcfg.etlaunch;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ETServer implements Comparable<ETServer> {

	private String serverName;
	private String ip;
	private int port;
	private String password;
	private Map<String, String> additionalParametes = new HashMap<>();

	private transient ServerStatus serverStatus;
	private transient ServerStatus lastServertatus;
	private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);
	public transient static Pattern SERVER_PATTERN = Pattern
			.compile("(/?connect)[\\s]+(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})?(\\b[\\w]+\\.[\\w]+(\\.[\\w]+)*)?(:\\d+)?(;\\s*password[\\s]+(.+))?");

	public static ETServer parseServerFromString(String source) {

		if (source == null || source.isEmpty()) {
			return null;
		}
		source = source.trim();
		if (source.startsWith("/connect ") || source.startsWith("connect ")) {
			return parseAsCommand(source);
		} else {
			return parseAsPlain(source);
		}

	}

	private static ETServer parseAsCommand(String source) {
		// \d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}

		Matcher matcher = SERVER_PATTERN.matcher(source);
		if (matcher.find()) {
			StringBuilder adress = new StringBuilder();
			if (matcher.group(2) != null) {
				adress.append(matcher.group(2));
			} else if (matcher.group(3) != null) {
				adress.append(matcher.group(3));
			}

			if (matcher.group(5) != null) {
				adress.append(matcher.group(5));

			}
			try {

				ETServer etServer = parseAdress(adress.toString());

				if (matcher.group(7) != null) {
					etServer.setPassword(matcher.group(7));
				}
				return etServer;
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	private static ETServer parseAdress(String adress)
			throws URISyntaxException {
		URI uri = new URI("http://" + adress);
		ETServer etServer = new ETServer();
		etServer.setIp(uri.getHost());
		if (uri.getPort() > -1) {
			etServer.setPort(uri.getPort());
		} else {
			etServer.setPort(27960);
		}
		return etServer;
	}

	private static ETServer parseAsPlain(String source) {
		source = source.trim();
		int indexOf = source.indexOf(" ");
		if (indexOf < 0) {
			indexOf = source.length();
		}
		try {

			ETServer etServer = parseAdress(source.substring(0, indexOf));

			if (indexOf != source.length()) {
				etServer.setPassword(source.substring(indexOf + 1, source
						.length()));
			}
			return etServer;

		} catch (Exception e) {

		}
		return null;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public synchronized ServerStatus getServerStatus() {

		return serverStatus;
	}

	public synchronized void setServerStatus(ServerStatus serverStatus) {
		if (serverStatus == null || serverStatus.getPing() == 999) {
			if (this.serverStatus != null && this.serverStatus.getPing() < 999) {
				lastServertatus = this.serverStatus;
			}
		}
		this.serverStatus = serverStatus;
		if (serverStatus != null && serverStatus.getName() != null) {
			setServerName(serverStatus.getName());
		}

		firePropertyChange("serverStatus", null, serverStatus);
	}

	public Map<String, String> getAdditionalParametes() {
		return additionalParametes;
	}

	public void setAdditionalParametes(Map<String, String> additionalParametes) {
		this.additionalParametes = additionalParametes;
	}

	public int compareTo(ETServer o) {
		return o.compareTo(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ETServer other = (ETServer) obj;
		if (ip == null) {
			if (other.ip != null) {
				return false;
			}
		} else if (!ip.equals(other.ip)) {
			return false;
		}
		if (port != other.port) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ETServer [ip=" + ip + ", password=" + password + ", port="
				+ port + ", serverName=" + serverName + ", serverStatus="
				+ serverStatus + "]";
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void fireIndexedPropertyChange(String propertyName, int index,
			boolean oldValue, boolean newValue) {
		propertyChangeSupport.fireIndexedPropertyChange(propertyName, index,
				oldValue, newValue);
	}

	public void fireIndexedPropertyChange(String propertyName, int index,
			int oldValue, int newValue) {
		propertyChangeSupport.fireIndexedPropertyChange(propertyName, index,
				oldValue, newValue);
	}

	public void fireIndexedPropertyChange(String propertyName, int index,
			Object oldValue, Object newValue) {
		propertyChangeSupport.fireIndexedPropertyChange(propertyName, index,
				oldValue, newValue);
	}

	public void firePropertyChange(PropertyChangeEvent evt) {
		propertyChangeSupport.firePropertyChange(evt);
	}

	public void firePropertyChange(String propertyName, boolean oldValue,
			boolean newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue,
				newValue);
	}

	public void firePropertyChange(String propertyName, int oldValue,
			int newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue,
				newValue);
	}

	public void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue,
				newValue);
	}

	public PropertyChangeListener[] getPropertyChangeListeners() {
		return propertyChangeSupport.getPropertyChangeListeners();
	}

	public PropertyChangeListener[] getPropertyChangeListeners(
			String propertyName) {
		return propertyChangeSupport.getPropertyChangeListeners(propertyName);
	}

	public boolean hasListeners(String propertyName) {
		return propertyChangeSupport.hasListeners(propertyName);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(propertyName,
				listener);
	}

	public ServerStatus getLastServerStatus() {
		return lastServertatus;
	}

}
