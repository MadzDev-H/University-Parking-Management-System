package smartpark.manager;

import smartpark.model.ParkingSpace;
import smartpark.operations.ParkingOperations;

import java.util.ArrayList;

public class ParkingManager implements ParkingOperations {
    private ArrayList<ParkingSpace> spaces;

    public ParkingManager() {
        spaces = new ArrayList<ParkingSpace>();
    }

    public boolean addSpace(ParkingSpace space) {
        if (space == null || space.getSpaceId().trim().equals("")) {
            return false;
        }

        if (findSpace(space.getSpaceId()) != -1) {
            return false;
        }

        spaces.add(space);
        return true;
    }

    public ParkingSpace getSpace(int index) {
        if (index < 0 || index >= spaces.size()) {
            return null;
        }
        return spaces.get(index);
    }

    public int findSpace(String id) {
        for (int i = 0; i < spaces.size(); i++) {
            if (spaces.get(i).getSpaceId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public ArrayList<ParkingSpace> getAllSpaces() {
        return new ArrayList<ParkingSpace>(spaces);
    }

    public ArrayList<ParkingSpace> getAvailableSpaces() {
        ArrayList<ParkingSpace> availableSpaces =
                new ArrayList<ParkingSpace>();

        for (int i = 0; i < spaces.size(); i++) {
            ParkingSpace space = spaces.get(i);
            if (space.getStatus().equals(ParkingSpace.AVAILABLE)) {
                availableSpaces.add(space);
            }
        }

        return availableSpaces;
    }

    public boolean reserveSpace(String id) {
        int index = findSpace(id);
        if (index == -1) {
            return false;
        }
        return spaces.get(index).reserve();
    }

    public boolean occupySpace(String id) {
        int index = findSpace(id);
        if (index == -1) {
            return false;
        }
        return spaces.get(index).occupy();
    }

    public boolean releaseSpace(String id) {
        int index = findSpace(id);
        if (index == -1) {
            return false;
        }

        ParkingSpace space = spaces.get(index);
        if (space.getStatus().equals(ParkingSpace.AVAILABLE)) {
            return false;
        }

        space.release();
        return true;
    }
}
