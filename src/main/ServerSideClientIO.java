package main;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.Scanner;

import data.ClypeData;


public class ServerSideClientIO implements Runnable {
	private boolean closeConnection;
	private ClypeData dataToRecieveFromClient;
	private ClypeData dataToSendToClient;
	private ObjectInputStream inFromClient;
	private ObjectOutputStream outToClient;
	private ClypeServer server;
	private Socket clientSocket;

	private String clientUserName;

	public ServerSideClientIO(ClypeServer server, Socket clientSocket) {
		this.server = server;
		this.clientSocket = clientSocket;

		this.closeConnection = false;

		this.dataToRecieveFromClient = this.dataToSendToClient = null;
		this.inFromClient = null;
		this.outToClient = null;
		this.clientUserName = null;
	}

	/***
	 * Runs the server side listener.
	 */
	public void run() {
		try {
			this.inFromClient = new ObjectInputStream(this.clientSocket.getInputStream());
			this.outToClient = new ObjectOutputStream(this.clientSocket.getOutputStream());

			recieveUserName();
			System.out.println("Accepted new client: " + clientUserName + ' ' + new Date());

			while (!this.closeConnection) {
				recieveData();
				if (this.dataToRecieveFromClient != null) {
					this.setSendDataToClient(this.dataToRecieveFromClient);
					this.broadcastToClients();
					this.dataToSendToClient = null;
				}
			}

			this.inFromClient.close();
			this.outToClient.close();
			this.clientSocket.close();
		} catch (IOException ioe) {
			System.err.println("Issue running server side IO");
		}
	}

	
	public void recieveData() {
		try {
			dataToRecieveFromClient = (ClypeData) inFromClient.readObject();
	
			if (dataToRecieveFromClient.getType() == (ClypeData.LOGOUT)) {
				this.dataToSendToClient = this.dataToRecieveFromClient;
				sendData();
				this.server.remove(this);
				dataToRecieveFromClient = null;
				
				//close streams
				this.inFromClient.close();
				this.outToClient.close();
				this.clientSocket.close();
				this.closeConnection = true;
			}
		} catch (NullPointerException npe) {
			System.err.println("Null pointer!");
			npe.printStackTrace();
			
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Class not found!");
					cnfe.printStackTrace();
			
		} catch (InvalidClassException ice) {
			System.err.println("Invalid class issue recieving data server side: " + ice.getMessage());
		} catch (SocketException se) {
			System.err.println("Socket issue recieving data server side: " + se.getMessage());
			closeConnection = true;
			this.server.remove(this);
			dataToRecieveFromClient = null;
			try {
				this.inFromClient.close();
				this.outToClient.close();
				this.clientSocket.close();
			} catch (IOException ioe) {
				System.err.println("Error closing streams and sockets server side: " + ioe.getMessage());
			}
		} catch (IOException ioe) {
			System.err.println("Issue recieving data server side: " + ioe.getMessage());
			closeConnection = true;
			this.server.remove(this);
			dataToRecieveFromClient = null;
			try {
				this.inFromClient.close();
				this.outToClient.close();
				this.clientSocket.close();
			} catch (IOException ioe2) {
				System.err.println("Error closing streams and sockets server side: " + ioe2.getMessage());
			}
		}
	}

	/***
	 * Sends data to client.
	 */
	public void sendData() {
		try {
			this.outToClient.writeObject(this.dataToSendToClient);
		} catch (InvalidClassException ice) {
			System.err.println("Sending invalid class server side: " + ice.getMessage());
		} catch (IOException ioe) {
			System.err.println("Issue sending data server side: " + ioe.getMessage());
		}
	}

	/***
	 * Sets data to send to client
	 * 
	 * @param dataToSendToClient
	 *            A ClypeData object to send to client.
	 */
	public void setSendDataToClient(ClypeData dataToSendToClient) {
		this.dataToSendToClient = dataToSendToClient;
	}

	public void recieveUserName() {
		recieveData();
		this.clientUserName = this.dataToRecieveFromClient.getUserName();
		this.dataToRecieveFromClient = null;
	}

	
	/***
	 * Broadcasts data to clients.
	 */
	public void broadcastToClients() {
		if (this.dataToRecieveFromClient.getType() != ClypeData.USERLIST) {
			this.server.broadcast(this.dataToSendToClient); // send to all clients
		} else {
			this.server.broadcast(this.dataToSendToClient, this);// send to only this client
		}
	
	}

	/***
	 * Sends ClypeData object to client.
	 * 
	 * @param toSendToClient
	 *            A ClypeData object to send to client.
	 */
	public void sendData(ClypeData toSendToClient) {
		try {
			this.outToClient.writeObject(toSendToClient);
		} catch (IOException ioe) {
			System.err.println("Issue sending data server side");
		}
	}

	public String getUserName() {
		return this.clientUserName;
	}
	
	
}