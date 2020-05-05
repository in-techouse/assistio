package lcwu.fyp.asistio.model;

import java.io.Serializable;
import java.util.List;

public class AutoSmsReply implements Serializable {
    private String id, message, replyMessage;
    private List<Contact> contactList;

    public AutoSmsReply() {
    }

    public AutoSmsReply(String id, String message, String replyMessage, List<Contact> contactList) {
        this.id = id;
        this.message = message;
        this.replyMessage = replyMessage;
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

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }
}
