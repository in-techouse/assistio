package lcwu.fyp.asistio.model;

import java.io.Serializable;

public class UserFile implements Serializable {
    private String id, name, type, download_url;

    public UserFile() {
    }

    public UserFile(String id, String name, String type, String download_url) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.download_url = download_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }
}
