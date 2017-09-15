package data;

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
	public FileClypeData(String userName, String fileName, int type) {
		super(userName, type);
		this.fileName = fileName;
		this.fileContents = "";
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

	
	//void just to avoid the balking of the compiler
	public void readFileContents() {}

	public void writeFileContents() {}

	/* (non-Javadoc)
	 * @see data.ClypeData#getData()
	 */
	public String getData() {
		return this.fileContents;
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
