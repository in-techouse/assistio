package lcwu.fyp.asistio.model;

import java.io.Serializable;

public class LastLocation implements Serializable {
    private long timeStamps;
    private String datetime, address;
    private double latitude, longitude;

    public LastLocation() {
    }

    public LastLocation(long timeStamps, String datetime, String address, double latitude, double longitude) {
        this.timeStamps = timeStamps;
        this.datetime = datetime;
        this.address = address;
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
