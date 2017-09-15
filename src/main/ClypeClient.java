package main;

import data.*;

/**
 * @author pawlactb
 *
 */
public class ClypeClient {
	
	private static final int    DEFAULT_PORT = 7000;
	private static final String DEFAULT_HOST = "localhost";
	private static final String DEFAULT_USER = "Anon";
	
	//members
	private String    userName, hostName;
	private int       port;
	private boolean   closeConnection;
	private ClypeData dataToSendToServer, dataToRecieveFromServer;
	
	/**
	 * @param userName Username of client.
	 * @param hostName Hostname of client.
	 * @param port Port number.
	 */
	public ClypeClient(String userName, String hostName, int port) {
		this.userName = userName;
		this.hostName = hostName;
		this.port = port;
		this.closeConnection = false;
		
		this.dataToSendToServer = null;
		this.dataToRecieveFromServer = null;
	}
	
	/**
	 * @param userName Username of client.
	 * @param hostName Hostname of client.
	 */
	public ClypeClient(String userName, String hostName) {
		this(userName, hostName, DEFAULT_PORT);
	}
	
	/**
	 * @param userName Username of client.
	 */
	public ClypeClient(String userName) {
		this(userName, DEFAULT_HOST, DEFAULT_PORT);
	}
	
	public ClypeClient() {
		this(DEFAULT_USER, DEFAULT_HOST, DEFAULT_PORT);
	}
	
	public String getUserName() {
		return userName;
	}

	public String getHostName() {
		return hostName;
	}

	public int getPort() {
		return port;
	}

	public void start() {}
	
	public void sendData() {}
	
	public void receiveData() {}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (closeConnection ? 1231 : 1237);
		result = prime
				* result
				+ ((dataToRecieveFromServer == null) ? 0
						: dataToRecieveFromServer.hashCode());
		result = prime
				* result
				+ ((dataToSendToServer == null) ? 0 : dataToSendToServer
						.hashCode());
		result = prime * result
				+ ((hostName == null) ? 0 : hostName.hashCode());
		result = prime * result + port;
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	public boolean equals(Object other) {
		boolean userName, hostName, port, connection, data;
		
		if(other instanceof ClypeClient)
		{
			other = ((ClypeClient)other);
		}
		else return false;
		
		userName = this.userName == other.userName;
		hostName = this.hostName == other.hostName;
		port = this.port == other.port;
		connection = this.closeConnection == other.closeConnection;
		
		this.dataToSendToServer = null;
		this.dataToRecieveFromServer = null;
	}
	
	public String toString() {
		return "ClypeClient\n User" + this.userName + "\n" +
				"host: " + this.hostName + "\n" +
				"port: " + this.port + "\n" +
				this.dataToSendToServer + this.dataToRecieveFromServer;
	}
	

}
