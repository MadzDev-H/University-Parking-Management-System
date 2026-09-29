package smartpark.operations;

import smartpark.model.ParkingSession;

import java.util.ArrayList;

// A simple list of tasks that a SessionManager must provide.
public interface SessionOperations {
    boolean checkIn(String bookingId, int entryMinute);

    boolean checkOut(String sessionId, int exitMinute);

    int findSession(String id);

    ArrayList<ParkingSession> getAllSessions();
}
