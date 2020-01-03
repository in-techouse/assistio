package lcwu.fyp.asistio.model;

import java.io.Serializable;
import java.util.List;

public class SmartReply implements Serializable {
    private String id,message;
    private List<Contact>contactList;

    public SmartReply() {
    }

    public SmartReply(String id, String message, List<Contact> contactList) {
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

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }
}
