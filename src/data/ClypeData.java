package data;

import java.io.Serializable;
import java.util.Date;

public abstract class ClypeData implements Serializable {

	public static final int USERLIST = 0;
	public static final int LOGOUT = 1;
	public static final int FILE = 2;
	public static final int MESSAGE = 3;
	
	
	
	protected String userName;
	private Date date;
	
	
	private int type;
	
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
	 * @param key decryption key.
	 * @return contents of transmission.
	 */
	public abstract String getData(String key);
	
	/**
	 * @return contents of transmission.
	 */
	public abstract String getData();
	
	//helper for encrypt
	private String repeat(String key, int length) {
		int fullRepititions = key.length() / length;

		StringBuilder sb = new StringBuilder(length);
		
		for (int i = 0; i < fullRepititions; ++i) {
			sb.append(key);
		}
		
		sb.append(key.substring(0, key.length() % length));
		return sb.toString();		
	}
	
	/**
	 * @param inputString
	 * @param key
	 * @return
	 */
	protected String encrypt(String inputString, String key) {
		StringBuilder encrypted = new StringBuilder(inputString.length());
		
		key = repeat(key, inputString.length());
		
		for(int i = 0; i < inputString.length(); ++i) {
			char c = inputString.charAt(i);
			char k = key.charAt(i);
			
			if ( !Character.isLetter(inputString.charAt(i)) )
			{
				continue;
			}
			if (Character.isUpperCase(c)) {
				encrypted.append(((c + k - 2 * 'A') % 26 + 'A'));
			}
			else {
				encrypted.append(((c + k - 2 * 'a') % 26 + 'a'));
			}
		}
		
		return encrypted.toString();
	}
	
	/**
	 * @param inputString
	 * @param key
	 * @return
	 */
	protected String decrypt(String inputString, String key) {
		StringBuilder decrypted = new StringBuilder(inputString.length());
		inputString = inputString.toLowerCase();
		
		key = repeat(key, inputString.length());
		
		for(int i = 0; i < inputString.length(); ++i) {
			char c = inputString.charAt(i);
			char k = key.charAt(i);
			
			if ( !Character.isLetter(inputString.charAt(i)) )
			{
				continue;
			}
			
			if(Character.isUpperCase(c)) {
				decrypted.append((c + k) - 'A' % 'A');
			}
			else {
				decrypted.append((c + k) - 'a' % 'a');
			}
			
		}
		
		return decrypted.toString();
	}

}
