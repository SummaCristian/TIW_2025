package it.polimi.tiw.beans;

/*
 * This is a Java Bean corresponding to the Database's Images table.
 * Each Image contains a unique ID, a String containing the file name and another String
 * containing the actual file path to retrieve the associated image resource.
 */
public class Image {
    private int id;
    private String fileName;
    private String filePath;
    
    // --- Constructors ---
    
    // Empty Constructor
    public Image() {}
    
    // Full Constructor
    public Image(int id, String fileName, String filePath) {
    	this.id = id;
    	this.fileName = fileName;
    	this.filePath = filePath;
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}