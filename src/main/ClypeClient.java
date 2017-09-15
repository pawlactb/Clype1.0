package main;

import data.*;

/**
 * @author pawlactb
 *
 */
public class ClypeClient {
	
	private static final int    DEFAULT_PORT = 7000;
	private static final String DEFAULT_HOST = "localhost";
	
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
	public ClypeClint(String userName) {
		this(userName, DEFAULT_HOST, DEFAULT_PORT);
	}
	
	

}
