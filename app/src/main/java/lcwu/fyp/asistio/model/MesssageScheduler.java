package lcwu.fyp.asistio.model;

import java.io.Serializable;
import java.util.List;

public class MesssageScheduler implements Serializable {
    private String id, message, time;
    private List<Contact> contactList;

    public MesssageScheduler() {
    }

    public MesssageScheduler(String id, String message, String time, List<Contact> contactList) {
        this.id = id;
        this.message = message;
        this.time = time;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }
}
