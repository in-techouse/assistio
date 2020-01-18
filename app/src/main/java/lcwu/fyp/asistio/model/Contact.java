package lcwu.fyp.asistio.model;

import java.io.Serializable;

public class Contact implements Serializable {
    private String name,number;
    private boolean selected;

    public Contact() {
    }

    public Contact(String name, String number, boolean selected) {
        this.name = name;
        this.number = number;
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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
