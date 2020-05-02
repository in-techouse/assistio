package lcwu.fyp.asistio.model;

import java.io.Serializable;

public class LastLocation implements Serializable {
    private long timeStamps;
    private String datetime, address, brand, model, serialNumber;
    private double latitude, longitude;

    public LastLocation() {
    }

    public LastLocation(long timeStamps, String datetime, String address, String brand, String model, String serialNumber, double latitude, double longitude) {
        this.timeStamps = timeStamps;
        this.datetime = datetime;
        this.address = address;
        this.brand = brand;
        this.model = model;
        this.serialNumber = serialNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getTimeStamps() {
        return timeStamps;
    }

    public void setTimeStamps(long timeStamps) {
        this.timeStamps = timeStamps;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
