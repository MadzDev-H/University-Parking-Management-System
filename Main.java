package smartpark.app;

import smartpark.manager.*;
import smartpark.model.*;
import smartpark.ui.LoginFrame;


import java.util.ArrayList;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                startApplication();
            }
        });
    }

    private static void startApplication() {
        ParkingManager parkingManager = new ParkingManager();

        parkingManager.addSpace(
                new ParkingSpace("A01", "Zone A", "Car"));
        parkingManager.addSpace(
                new ParkingSpace("A02", "Zone A", "Van"));
        parkingManager.addSpace(
                new ParkingSpace("A03", "Zone A", "Motorcycle"));
        parkingManager.addSpace(
                new ParkingSpace("B01", "Zone B", "Car"));
        parkingManager.addSpace(
                new ParkingSpace("B02", "Zone B", "Hybrid Car"));
        parkingManager.addSpace(
                new ParkingSpace("B03", "Zone B", "Any"));

        BookingManager bookingManager = new BookingManager(parkingManager);
        SessionManager sessionManager = new SessionManager(parkingManager, bookingManager);
        LoginManager loginManager = new LoginManager();
        ArrayList<Transport> transports = new ArrayList<Transport>();

        LoginFrame loginFrame = new LoginFrame(loginManager, parkingManager, bookingManager, sessionManager, transports);
        loginFrame.setVisible(true);
    }
}
