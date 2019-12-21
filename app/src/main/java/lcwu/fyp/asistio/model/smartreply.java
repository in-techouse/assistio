package lcwu.fyp.asistio.model;

import java.io.Serializable;
import java.util.List;

public class smartreply implements Serializable {
    private String id,message;
    private List<contact>contactList;

    public smartreply() {
    }

    public smartreply(String id, String message, List<contact> contactList) {
        this.id = id;
        this.message = message;
        this.contactList = contactList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<contact> contactList) {
        this.contactList = contactList;
    }
}
