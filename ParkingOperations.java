package smartpark.operations;

import smartpark.model.ParkingSpace;

import java.util.ArrayList;

// A simple list of tasks that a ParkingManager must provide.
public interface ParkingOperations {
    boolean addSpace(ParkingSpace space);

    ParkingSpace getSpace(int index);

    int findSpace(String id);

    ArrayList<ParkingSpace> getAllSpaces();

    ArrayList<ParkingSpace> getAvailableSpaces();

    boolean reserveSpace(String id);

    boolean occupySpace(String id);

    boolean releaseSpace(String id);
}
