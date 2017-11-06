package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import data.ClypeData;
import data.MessageClypeData;

/**
 * @author pawlactb
 * 
 */
public class ClypeServer {
	private final static int DEFAULT_PORT = 7000;
	private final static int MIN_PORT = 1024;

	private int port;
	private boolean closedConnection;

	private ArrayList<ServerSideClientIO> serverSideClientIOList;

	/**
	 * @param port port number to open for connections.
	 */
	public ClypeServer(int port) throws IllegalArgumentException {
		this.closedConnection = false;
		if(port < MIN_PORT){
			throw new IllegalArgumentException();
		}
		this.port = port;
		serverSideClientIOList = new ArrayList<ServerSideClientIO>();
	}

	/**
	 * Default port is 7000
	 */
	public ClypeServer() {
		this(DEFAULT_PORT);
	}
	
	public void start() {
		try {
			ServerSocket socket = new ServerSocket(port);
	
			while (!this.closed()) {
				Socket clientSocket = socket.accept();
				ServerSideClientIO newServerSideClientIO = new ServerSideClientIO(this, clientSocket);
	
				this.serverSideClientIOList.add(newServerSideClientIO);
				Thread newClientThread = new Thread(newServerSideClientIO);
				newClientThread.start();
			}
	
			socket.close();
	
		} catch (IOException ioe) {
			System.err.println("Issue with IO on server side");
		}
	}

	public synchronized void broadcast(ClypeData dataToBroadcastToClient, ServerSideClientIO client) {
		if (dataToBroadcastToClient.getType() == ClypeData.USERLIST) {
			MessageClypeData usersData = new MessageClypeData(client.getUserName(), this.listUsers(),
					ClypeData.USERLIST);
			client.sendData(usersData);
		} else
			client.sendData();
	}

	public synchronized void broadcast(ClypeData dataToBroadcastToClients) {
		if (dataToBroadcastToClients.getType() != ClypeData.USERLIST) {
			for (ServerSideClientIO io : this.serverSideClientIOList) {
				io.setSendDataToClient(dataToBroadcastToClients);
				io.sendData();
			}
		} else {
			for (ServerSideClientIO io : this.serverSideClientIOList) {
				io.setSendDataToClient(dataToBroadcastToClients);
				io.sendData();
			}
		}
	}

	public synchronized void remove(ServerSideClientIO serverSideClientToRemove) {
		System.out.println("Closed Connection with: " + serverSideClientToRemove.getUserName() + " at " + new Date());
		this.serverSideClientIOList.remove(serverSideClientToRemove);
	}

	public boolean closed() {
		return this.closedConnection;
	}

	/**
	 * Sends data to the client...
	 */
	public void sendData() {
	}

	public String listUsers() {
		String listOfUsers = "";
		int userCounter = 1;
		for (ServerSideClientIO io : this.serverSideClientIOList) {
			listOfUsers += "User " + userCounter + ": " + io.getUserName() + "\n";
			++userCounter;
		}
		return listOfUsers;
}

	
	/**
	 * Returns this object's port number.
	 * 
	 * @return
	 */
	public int getPort() {
		return this.port;
	}

	public static void main(String[] args) {
			try {
				ClypeServer server;
				if (args.length > 0) {
					Scanner argScanner = new Scanner(args[0]);
					int port = argScanner.nextInt();
					argScanner.close();
					server = new ClypeServer(port);
				} else {
					server = new ClypeServer();
				}
	
				server.start();
	
			} catch (IllegalArgumentException iae) {
				System.err.println(iae.getMessage());
			}
	
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((serverSideClientIOList == null) ? 0 : serverSideClientIOList.hashCode());
		result = prime * result + (closedConnection ? 1231 : 1237);
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ClypeServer))
			return false;
		ClypeServer other = (ClypeServer) obj;
		if (serverSideClientIOList == null) {
			if (other.serverSideClientIOList != null)
				return false;
		} else if (!serverSideClientIOList.equals(other.serverSideClientIOList))
			return false;
		if (closedConnection != other.closedConnection)
			return false;
		if (port != other.port)
			return false;
		return true;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		return "ClypeServer [port="
				+ port
				+ ", closedConnection="
				+ closedConnection
				+ ", clientIOList="
				+ (serverSideClientIOList != null ? serverSideClientIOList.subList(0,
						Math.min(serverSideClientIOList.size(), maxLen)) : null) + "]";
	}
}