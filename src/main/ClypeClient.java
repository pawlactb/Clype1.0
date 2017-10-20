package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import data.ClypeData;
import data.FileClypeData;
import data.MessageClypeData;

/**
 * @author pawlactb
 *
 */
public class ClypeClient {
	
	private static final int    DEFAULT_PORT = 7000;
	private static final String DEFAULT_HOST = "localhost";
	private static final String DEFAULT_USER = "Anon";
	
	private static final String key = null;
	
	//members
	private String    userName, hostName;
	private int       port;
	private boolean   closeConnection;
	private ClypeData dataToSendToServer, dataToReceiveFromServer;
	
	private Scanner inFromStd = null;
	
	private ObjectInputStream inFromServer;
	private ObjectOutputStream outToServer;
	
	
	/**
	 * @param userName Username of client.
	 * @param hostName Hostname of client.
	 * @param port Port number.
	 */
	public ClypeClient(String userName, String hostName, int port) throws IllegalArgumentException {
		this.userName = userName;
		this.hostName = hostName;
		this.port = port;
		
		if(userName == null || hostName == null || port < 1024)
		{
			throw new IllegalArgumentException();
		}
		
		this.closeConnection = false;
		
		this.dataToSendToServer = null;
		this.dataToReceiveFromServer = null;
		
		this.inFromServer = null;
		this.outToServer = null;
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
	
	public void start() {
		inFromStd = new Scanner(System.in);
		
		try {
			this.readClientData();
		}
		catch (IllegalArgumentException e) {
			System.err.println("Illegal Argument!");
			e.printStackTrace(System.err);
		}
		catch (IOException e) {
			System.err.println("File error!");
			e.printStackTrace(System.err);
		}
		
		Socket sock;
		
		try {
			sock = new Socket(hostName, port);
			this.inFromServer = new ObjectInputStream(sock.getInputStream());
			this.outToServer = new ObjectOutputStream(sock.getOutputStream());
		}
		catch (UnknownHostException e) {
			System.err.println("Unknown host!");
			e.printStackTrace();
		}
		catch (IOException e) {
			System.err.println("IO error!");
			e.printStackTrace(System.err);
		}
		
		
		
		this.printData();
	}
	
	public void readClientData() throws FileNotFoundException, IOException {
		System.out.println("Select Session Type");
		System.out.println("0: List users");
		System.out.println("1: Close Connection");
		System.out.println("2: Send File");
		System.out.println("3: Send Message");
		
		int session = inFromStd.nextInt();
		
		if (session == ClypeData.LOGOUT) {
			closeConnection = true; 
		}
		else if (session == ClypeData.FILE) {
			System.out.println("Enter filename");
			String in = inFromStd.next();
			dataToSendToServer = new FileClypeData(this.userName, in, ClypeData.FILE);
			
			try {
				((FileClypeData) dataToSendToServer).readFileContents();
			} catch (IOException ex) {
				System.err.print("File error." + ex.getStackTrace());
				dataToSendToServer = null;
			}
		}
		else if (session == ClypeData.USERLIST) {
			//TODO
		}
		
		else { //message
			System.out.println("Message");
			String in = inFromStd.next();
			dataToSendToServer = new MessageClypeData(this.userName, in, ClypeData.MESSAGE);
		}	
	}
	
	public void printData() {
		if (this.dataToReceiveFromServer.getType() == ClypeData.FILE) {
			((FileClypeData) dataToReceiveFromServer).writeFileContents();
		}
		else if (this.dataToReceiveFromServer.getType() == ClypeData.MESSAGE) {
			System.out.println(((MessageClypeData) dataToReceiveFromServer).getData());
		}
	}
	
	public void sendData() {
		try {
			this.outToServer.writeObject(dataToSendToServer);
		} catch (IOException e) {
			System.err.println("File error!");
			e.printStackTrace(System.err);
		}
		
	}
	
	public void receiveData() {
		
		try {
			this.dataToReceiveFromServer = (ClypeData)this.inFromServer.readObject();
		}
		
		catch (ClassNotFoundException e) {
			System.err.println("Strange error!");
			e.printStackTrace(System.err);
		} catch (IOException e) {
			System.err.println("File error!");
			e.printStackTrace(System.err);
		}
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (closeConnection ? 1231 : 1237);
		result = prime
				* result
				+ ((dataToReceiveFromServer == null) ? 0
						: dataToReceiveFromServer.hashCode());
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
		
		userName = this.userName == ((ClypeClient) other).getUserName();
		hostName = this.hostName == ((ClypeClient) other).getHostName();
		port = this.port == ((ClypeClient) other).getPort();
		connection = this.closeConnection == ((ClypeClient) other).closeConnection;
		
		data = this.dataToReceiveFromServer == ((ClypeClient) other).dataToReceiveFromServer && 
				this.dataToSendToServer == ((ClypeClient) other).dataToSendToServer;
		
		return userName && hostName && port && connection && data;
		
		
	}
	
	public String toString() {
		return "ClypeClient\n User" + this.userName + "\n" +
				"host: " + this.hostName + "\n" +
				"port: " + this.port + "\n" +
				this.dataToSendToServer + this.dataToReceiveFromServer;
	}
	
	public static void main(String[] argv, int argc) {
		switch(argc) {
		
		
		
		}
	}
	

}
