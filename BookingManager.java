package smartpark.manager;

import smartpark.model.*;
import smartpark.operations.BookingOperations;

import java.util.ArrayList;

// BookingManager uses ParkingManager to reserve and release a parking space.
public class BookingManager implements BookingOperations {
    private ArrayList<Booking> bookings;
    private ParkingManager parkingManager;
    private int nextId;

    public BookingManager(ParkingManager parkingManager) {
        this.parkingManager = parkingManager;
        bookings = new ArrayList<Booking>();
        nextId = 1;
    }

    public boolean createBooking(String vehicleNumber, String spaceId,
                                 String date, int startMinute, int duration) {
        if (vehicleNumber == null || vehicleNumber.trim().equals("")) {
            return false;
        }

        if (spaceId == null || spaceId.trim().equals("")) {
            return false;
        }

        if (!validTime(date, startMinute, duration)) {
            return false;
        }

        if (!isAvailable(spaceId, date, startMinute, duration)) {
            return false;
        }

        if (!parkingManager.reserveSpace(spaceId)) {
            return false;
        }

        String bookingId = "B" + nextId;
        nextId++;

        Booking booking = new Booking(bookingId, vehicleNumber.trim(),
                spaceId.trim(), date.trim(), startMinute, duration);
        bookings.add(booking);
        return true;
    }

    public ArrayList<Booking> getAllBookings() {
        return new ArrayList<Booking>(bookings);
    }

    public int findBooking(String id) {
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getBookingId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public boolean updateBooking(String id, String date,
                                 int startMinute, int duration) {
        int index = findBooking(id);

        if (index == -1 || !validTime(date, startMinute, duration)) {
            return false;
        }

        Booking booking = bookings.get(index);
        if (!booking.getStatus().equals(Booking.ACTIVE)) {
            return false;
        }

        int spaceIndex = parkingManager.findSpace(booking.getSpaceId());
        if (spaceIndex == -1) {
            return false;
        }

        ParkingSpace space = parkingManager.getSpace(spaceIndex);
        if (!space.getStatus().equals(ParkingSpace.RESERVED)) {
            return false;
        }

        if (hasOverlap(id, booking.getSpaceId(), date.trim(),
                startMinute, duration)) {
            return false;
        }

        booking.setDate(date.trim());
        booking.setStartMinute(startMinute);
        booking.setDuration(duration);
        return true;
    }

    public boolean cancelBooking(String id) {
        int index = findBooking(id);

        if (index == -1) {
            return false;
        }

        Booking booking = bookings.get(index);
        if (!booking.getStatus().equals(Booking.ACTIVE)) {
            return false;
        }

        int spaceIndex = parkingManager.findSpace(booking.getSpaceId());
        if (spaceIndex == -1) {
            return false;
        }

        ParkingSpace space = parkingManager.getSpace(spaceIndex);
        if (!space.getStatus().equals(ParkingSpace.RESERVED)) {
            return false;
        }

        if (!parkingManager.releaseSpace(booking.getSpaceId())) {
            return false;
        }

        booking.setStatus(Booking.CANCELLED);
        return true;
    }

    public boolean checkArrival(String id, int arrivalMinute) {
        int index = findBooking(id);

        if (index == -1 || arrivalMinute < 0 || arrivalMinute >= 1440) {
            return false;
        }

        Booking booking = bookings.get(index);
        if (!booking.getStatus().equals(Booking.ACTIVE)) {
            return false;
        }

        int startMinute = booking.getStartMinute();

        if (arrivalMinute >= startMinute
                && arrivalMinute <= startMinute + 15) {
            return true;
        }

        if (arrivalMinute > startMinute + 15) {
            int spaceIndex = parkingManager.findSpace(booking.getSpaceId());
            if (spaceIndex == -1) {
                return false;
            }

            ParkingSpace space = parkingManager.getSpace(spaceIndex);
            if (!space.getStatus().equals(ParkingSpace.RESERVED)) {
                return false;
            }

            if (parkingManager.releaseSpace(booking.getSpaceId())) {
                booking.setStatus(Booking.EXPIRED);
            }
        }

        return false;
    }

    public boolean isAvailable(String spaceId, String date,
                               int startMinute, int duration) {
        if (!validTime(date, startMinute, duration)) {
            return false;
        }

        int spaceIndex = parkingManager.findSpace(spaceId);
        if (spaceIndex == -1) {
            return false;
        }

        ParkingSpace space = parkingManager.getSpace(spaceIndex);
        if (!space.getStatus().equals(ParkingSpace.AVAILABLE)) {
            return false;
        }

        return !hasOverlap("", spaceId, date.trim(),
                startMinute, duration);
    }

    private boolean hasOverlap(String ignoredId, String spaceId, String date,
                               int startMinute, int duration) {
        int newEnd = startMinute + duration;

        for (int i = 0; i < bookings.size(); i++) {
            Booking oldBooking = bookings.get(i);

            if (oldBooking.getBookingId().equals(ignoredId)) {
                continue;
            }

            if (oldBooking.getStatus().equals(Booking.ACTIVE)
                    && oldBooking.getSpaceId().equals(spaceId)
                    && oldBooking.getDate().equals(date)) {
                int oldStart = oldBooking.getStartMinute();
                int oldEnd = oldBooking.getEndMinute();

                if (oldStart < newEnd && startMinute < oldEnd) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean validTime(String date, int startMinute, int duration) {
        if (date == null || date.trim().equals("")) {
            return false;
        }

        if (startMinute < 0 || startMinute >= 1440) {
            return false;
        }

        if (duration <= 0 || duration > 1440 - startMinute) {
            return false;
        }

        return true;
    }
}
