package lcwu.fyp.asistio.model;

import java.util.ArrayList;
import java.util.List;

public class ListUserFile {
    private static List<UserFile> userFiles;
    private static final ListUserFile ourInstance = new ListUserFile();

    public static ListUserFile getInstance() {
        if(userFiles == null){
            userFiles = new ArrayList<>();
        }
        return ourInstance;
    }

    private ListUserFile() {
        userFiles = new ArrayList<>();
    }

    public List<UserFile> getUserFiles() {
        return userFiles;
    }

    public void setUserFiles(List<UserFile> userFiles) {
        this.userFiles = userFiles;
    }
}
