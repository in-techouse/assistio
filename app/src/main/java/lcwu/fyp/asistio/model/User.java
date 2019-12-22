package lcwu.fyp.asistio.model;

import java.io.Serializable;

public class User implements Serializable {
    private String first_Name, last_Name,id,phone_no,email;

    public User() {
    }

    public User(String first_Name, String last_Name, String id, String phone_no, String email) {
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.id = id;
        this.phone_no = phone_no;
        this.email = email;
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
}


