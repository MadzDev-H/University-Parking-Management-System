package smartpark.model;

public class Booking {
    public static final String ACTIVE = "ACTIVE";
    public static final String CANCELLED = "CANCELLED";
    public static final String EXPIRED = "EXPIRED";

    private String bookingId;
    private String vehicleNumber;
    private String spaceId;
    private String date;
    private int startMinute;
    private int duration;
    private String status;

    public Booking(String bookingId, String vehicleNumber, String spaceId,
                   String date, int startMinute, int duration) {
        this.bookingId = bookingId;
        this.vehicleNumber = vehicleNumber;
        this.spaceId = spaceId;
        this.date = date;
        this.startMinute = startMinute;
        this.duration = duration;
        status = ACTIVE;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public String getDate() {
        return date;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public int getDuration() {
        return duration;
    }

    public String getStatus() {
        return status;
    }

    public int getEndMinute() {
        return startMinute + duration;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
