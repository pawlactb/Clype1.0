package data;

import java.util.Date;

public abstract class ClypeData {

	protected String userName;
	private int type;
	private Date date;
	
	/**
	 * @param userName Username of the client sending data.
	 * @param type Type of transmission.
	 */
	public ClypeData(String userName, int type) {
		this.userName = userName;
		this.type = type;
		this.date = new Date();
	}
	
	/**
	 * @param type Type of transmission.
	 */
	public ClypeData(int type) {
		this("Anon", type);
	}
	
	public ClypeData() {
		this("Anon", 0);
	}

	/**
	 * @return username of the client making transmission.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return type of transmission.
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return date and time of transmission
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * @return returns contents of transmission.
	 */
	public abstract String getData();

}
