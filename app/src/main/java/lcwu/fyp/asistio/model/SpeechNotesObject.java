package lcwu.fyp.asistio.model;

import java.io.Serializable;

public class SpeechNotesObject implements Serializable {
    private String id, note;

    public SpeechNotesObject() {
    }

    public SpeechNotesObject(String id, String note) {
        this.id = id;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
