package smartpark.model;

// Parent class shared by Car and Motorcycle.
public class Transport {
    private String vehicleNumber;
    private String driverName;
    private String contactNumber;
    private String model;
    private String vehicleType;

    public Transport(String vehicleNumber, String driverName,
                     String contactNumber, String model) {
        this.vehicleNumber = vehicleNumber;
        this.driverName = driverName;
        this.contactNumber = contactNumber;
        this.model = model;
        vehicleType = "Transport";
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getModel() {
        return model;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
