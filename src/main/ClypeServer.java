package main;

import data.ClypeData;


/**
 * @author pawlactb
 *
 */
public class ClypeServer {
	private int port;
	private boolean closeConnection;
	private ClypeData dataToReceiveFromClient;
	private ClypeData dataToSendToClient;
	static int DEFAULTPORT = 7000;
	
	
	/**
	 * @param port port number for incoming connections.
	 */
	public ClypeServer( int port ) {
		this.closeConnection = false;
		this.port = port;
		this.dataToReceiveFromClient = null;
		this.dataToSendToClient = null;
	}
	
	
	/**
	 * Default port is 7000
	 */
	public ClypeServer() {
		this(DEFAULTPORT);
	}
	
	
	public void start() {}
	
	/**
	 * Receives data from the client...
	 */
	public void receiveData() {}
	
	/**
	 * Sends data to the client...
	 */
	public void sendData() {}
	
	/**
	 * Returns this object's port number.
	 * @return
	 */
	public int getPort() { return this.port; }
	
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + port;
		if (dataToSendToClient != null)
			result = 37 * result + dataToSendToClient.hashCode();
		if (dataToReceiveFromClient != null)
			result = 37 * result + dataToReceiveFromClient.hashCode();
		return result;
	}
	
	/**
	 * Compares two objects, ensures that they can both be cast to ClypeServer objects,
	 * and returns whether the two objects contain all of the same data with the same values.
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ClypeServer))
			return false;
		ClypeServer cs = (ClypeServer) o;
		
		if (cs.port == this.port &&
				cs.closeConnection == this.closeConnection &&
				cs.dataToReceiveFromClient.equals(this.dataToReceiveFromClient) &&
				cs.dataToSendToClient.equals(this.dataToSendToClient))
			return true;
		return false;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ClypeClient.\n" +
				"Data to send to client: " + dataToSendToClient.toString() + "\n" +
				"Data to receive from client: " + dataToReceiveFromClient.toString() + "\n" +
				"Connection: " + this.closeConnection + "\n" +
				"Port: " + this.port + "\n";
				
	}
}