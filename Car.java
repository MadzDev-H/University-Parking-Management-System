package smartpark.model;

// Car inherits the common data from Transport.
public class Car extends Transport {
    public Car(String vehicleNumber, String driverName,
               String contactNumber, String model) {
        super(vehicleNumber, driverName, contactNumber, model);
    }

    @Override
    public String getVehicleType() {
        return "Car";
    }
}
