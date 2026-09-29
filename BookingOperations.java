package smartpark.operations;

import smartpark.model.Booking;

import java.util.ArrayList;

// A simple list of tasks that a BookingManager must provide.
public interface BookingOperations {
    boolean createBooking(String vehicleNumber, String spaceId,
                          String date, int startMinute, int duration);

    ArrayList<Booking> getAllBookings();

    int findBooking(String id);

    boolean updateBooking(String id, String date,
                          int startMinute, int duration);

    boolean cancelBooking(String id);

    boolean checkArrival(String id, int arrivalMinute);

    boolean isAvailable(String spaceId, String date,
                        int startMinute, int duration);
}
