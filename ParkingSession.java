package smartpark.model;

public class ParkingSession {
    public static final String ACTIVE = "ACTIVE";
    public static final String COMPLETED = "COMPLETED";

    private String sessionId;
    private String bookingId;
    private String vehicleNumber;
    private String spaceId;
    private int entryMinute;
    private int exitMinute;
    private int reservedEndMinute;
    private String status;

    public ParkingSession(String sessionId, String bookingId,
                          String vehicleNumber, String spaceId,
                          int entryMinute, int reservedEndMinute) {
        this.sessionId = sessionId;
        this.bookingId = bookingId;
        this.vehicleNumber = vehicleNumber;
        this.spaceId = spaceId;
        this.entryMinute = entryMinute;
        this.reservedEndMinute = reservedEndMinute;
        exitMinute = 0;
        status = ACTIVE;
    }

    public String getSessionId() {
        return sessionId;
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

    public int getEntryMinute() {
        return entryMinute;
    }

    public int getExitMinute() {
        return exitMinute;
    }

    public String getStatus() {
        return status;
    }

    public void setExitMinute(int exitMinute) {
        this.exitMinute = exitMinute;
    }

    public void complete() {
        status = COMPLETED;
    }

    public int getDuration() {
        return exitMinute - entryMinute;
    }

    public boolean isOverstay() {
        return exitMinute > reservedEndMinute;
    }
}
