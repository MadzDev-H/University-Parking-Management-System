package smartpark.manager;

import smartpark.model.*;
import smartpark.operations.SessionOperations;

import java.util.ArrayList;

// SessionManager collaborates with the booking and parking managers.
public class SessionManager implements SessionOperations {
    private ArrayList<ParkingSession> sessions;
    private ParkingManager parkingManager;
    private BookingManager bookingManager;
    private int nextId;

    public SessionManager(ParkingManager parkingManager,
                          BookingManager bookingManager) {
        this.parkingManager = parkingManager;
        this.bookingManager = bookingManager;
        sessions = new ArrayList<ParkingSession>();
        nextId = 1;
    }

    public boolean checkIn(String bookingId, int entryMinute) {
        int bookingIndex = bookingManager.findBooking(bookingId);
        if (bookingIndex == -1) {
            return false;
        }

        ArrayList<Booking> bookings = bookingManager.getAllBookings();
        Booking booking = bookings.get(bookingIndex);

        if (!booking.getStatus().equals(Booking.ACTIVE)) {
            return false;
        }

        if (!bookingManager.checkArrival(bookingId, entryMinute)) {
            return false;
        }

        int spaceIndex = parkingManager.findSpace(booking.getSpaceId());
        if (spaceIndex == -1) {
            return false;
        }

        ParkingSpace space = parkingManager.getSpace(spaceIndex);
        if (space == null || !space.getStatus().equals(ParkingSpace.RESERVED)) {
            return false;
        }

        if (!parkingManager.occupySpace(booking.getSpaceId())) {
            return false;
        }

        String sessionId = "S" + nextId;
        nextId++;

        ParkingSession session = new ParkingSession(
                sessionId, booking.getBookingId(), booking.getVehicleNumber(),
                booking.getSpaceId(), entryMinute, booking.getEndMinute());
        sessions.add(session);
        return true;
    }

    public boolean checkOut(String sessionId, int exitMinute) {
        int index = findSession(sessionId);
        if (index == -1) {
            return false;
        }

        ParkingSession session = sessions.get(index);
        if (!session.getStatus().equals(ParkingSession.ACTIVE)) {
            return false;
        }

        if (exitMinute < session.getEntryMinute() || exitMinute >= 1440) {
            return false;
        }

        if (!parkingManager.releaseSpace(session.getSpaceId())) {
            return false;
        }

        session.setExitMinute(exitMinute);
        session.complete();
        return true;
    }

    public int findSession(String id) {
        for (int i = 0; i < sessions.size(); i++) {
            if (sessions.get(i).getSessionId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public ArrayList<ParkingSession> getAllSessions() {
        return new ArrayList<ParkingSession>(sessions);
    }
}
