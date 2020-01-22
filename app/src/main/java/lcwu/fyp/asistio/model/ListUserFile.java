package lcwu.fyp.asistio.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListUserFile implements Serializable {
    private List<UserFile> userFiles;

    public ListUserFile() {
        userFiles = new ArrayList<>();
    }

    public List<UserFile> getUserFiles() {
        return userFiles;
    }

    public void setUserFiles(List<UserFile> userFiles) {
        this.userFiles = userFiles;
    }
}
