package smartpark.model;

// Motorcycle inherits the common data from Transport.
public class Motorcycle extends Transport {
    public Motorcycle(String vehicleNumber, String driverName,
                      String contactNumber, String model) {
        super(vehicleNumber, driverName, contactNumber, model);
    }

    @Override
    public String getVehicleType() {
        return "Motorcycle";
    }
}
