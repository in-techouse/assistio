package lcwu.fyp.asistio.model;

import java.io.Serializable;

public class contact implements Serializable {
    private String name,number;

    public contact() {
    }

    public contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
