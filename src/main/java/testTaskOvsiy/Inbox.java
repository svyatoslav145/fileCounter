package testTaskOvsiy;

/**
 * Model of inputted folder
 */
public class Inbox {
    private String path;
    private int fileNumber;

    public void setPath(String path) {
        this.path = path;
    }

    public void setFileNumber(int fileNumber) {
        this.fileNumber = fileNumber;
    }

    String getPath() {
        return path;
    }

    int getFileNumber() {
        return fileNumber;
    }
}
