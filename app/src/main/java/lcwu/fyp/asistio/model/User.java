package lcwu.fyp.asistio.model;

import java.io.Serializable;

public class User implements Serializable {
    private String first_Name, last_Name, id, phone_no, email, command;
    private int contacts, documents, images, videos, audios, notes;
    private boolean isFirst;

    public User() {
    }

    public User(String first_Name, String last_Name, String id, String phone_no, String email, String command, int contacts, int documents, int images, int videos, int audios, int notes, boolean isFirst) {
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.id = id;
        this.phone_no = phone_no;
        this.email = email;
        this.command = command;
        this.contacts = contacts;
        this.documents = documents;
        this.images = images;
        this.videos = videos;
        this.audios = audios;
        this.notes = notes;
        this.isFirst = isFirst;
    }

    public String getFirst_Name() {
        return first_Name;
    }

    public void setFirst_Name(String first_Name) {
        this.first_Name = first_Name;
    }

    public String getLast_Name() {
        return last_Name;
    }

    public void setLast_Name(String last_Name) {
        this.last_Name = last_Name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getContacts() {
        return contacts;
    }

    public void setContacts(int contacts) {
        this.contacts = contacts;
    }

    public int getDocuments() {
        return documents;
    }

    public void setDocuments(int documents) {
        this.documents = documents;
    }

    public int getImages() {
        return images;
    }

    public void setImages(int images) {
        this.images = images;
    }

    public int getVideos() {
        return videos;
    }

    public void setVideos(int videos) {
        this.videos = videos;
    }

    public int getAudios() {
        return audios;
    }

    public void setAudios(int audios) {
        this.audios = audios;
    }

    public int getNotes() {
        return notes;
    }

    public void setNotes(int notes) {
        this.notes = notes;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }
}






