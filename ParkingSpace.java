package smartpark.model;

public class ParkingSpace {
    public static final String AVAILABLE = "AVAILABLE";
    public static final String RESERVED = "RESERVED";
    public static final String OCCUPIED = "OCCUPIED";

    private String spaceId;
    private String zone;
    private String vehicleType;
    private String status;

    public ParkingSpace(String spaceId, String zone, String vehicleType) {
        this.spaceId = spaceId;
        this.zone = zone;
        this.vehicleType = vehicleType;
        status = AVAILABLE;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public String getZone() {
        return zone;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getStatus() {
        return status;
    }

    public boolean reserve() {
        if (status.equals(AVAILABLE)) {
            status = RESERVED;
            return true;
        }
        return false;
    }

    public boolean occupy() {
        if (status.equals(RESERVED)) {
            status = OCCUPIED;
            return true;
        }
        return false;
    }

    public void release() {
        status = AVAILABLE;
    }
}
