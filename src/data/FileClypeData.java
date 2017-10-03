package data;

import java.io.*;

/**
 * @author pawlactb
 *
 */
public class FileClypeData extends ClypeData {
	
	private String fileName, fileContents;
	
	
	/**
	 * @param userName Username of the client sending data.
	 * @param fileName Filename of the file sent.
	 * @param type Type of transmission.
	 */
	public FileClypeData(String userName, String fileName, int type) throws IllegalArgumentException  {
		super(userName, type);
		this.fileName = fileName;
		this.fileContents = "";
		
		if(type != ClypeData.FILE){
			throw new IllegalArgumentException("FileClypeData instantiated with non-file type.");
		}
	}
	
	public FileClypeData() {
		super();
		this.fileName = "";
		this.fileContents = "";
	}
	
	/**
	 * @return Returns the filename of the sent file.
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName New path to file.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	
	public void readFileContents() throws IOException {
		FileReader fr = null;
		try {
			fr = new FileReader(this.fileName);
			fr.read(this.fileContents.toCharArray());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		finally{
			try {
				if(fr != null)
				{
					fr.close();
				}
				
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * @param key encryption key
	 * @throws IOException if there is an issue reading file.
	 */
	public void readFileContents(String key) throws IOException {
		this.readFileContents();
		this.fileContents = this.encrypt(this.fileContents, key);
	}

	/**
	 * 
	 */
	public void writeFileContents() {
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(this.fileName);
			fw.write(this.fileContents);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		finally{
			try{
				if(fw != null) {
					fw.close();
				}
			}
			catch(IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * @param key decryption key
	 */
	public void writeFileContents(String key) {
		String fc = this.decrypt(this.fileContents, key);
		this.writeFileContents();
	}

	/* (non-Javadoc)
	 * @see data.ClypeData#getData()
	 */
	public String getData() {
		return this.fileContents;
	}
	
	public String getData(String key) {
		return this.decrypt(this.fileContents, key);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fileContents == null) ? 0 : fileContents.hashCode());
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		boolean data, fileContents, type, userName;
		
		if (!(other instanceof ClypeData))
		{
			return false;
		}
		
		if (other instanceof FileClypeData) {
			data = ((FileClypeData) other).getData() == this.getData();
		}
		else return false;
		
		type = this.getType() == ((FileClypeData)other).getType();
		userName = this.getUserName() == ((FileClypeData)other).getUserName();
		
		return data && type && userName;
		
	}

}
