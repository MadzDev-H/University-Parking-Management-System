package smartpark.ui;

import smartpark.manager.*;
import smartpark.model.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends JFrame implements ActionListener {
    private static final Color NAVY = new Color(25, 43, 72);
    private static final Color BLUE = new Color(48, 104, 184);
    private static final Color GREEN = new Color(45, 145, 93);
    private static final Color ORANGE = new Color(224, 143, 44);
    private static final Color RED = new Color(190, 65, 65);
    private static final Color LIGHT = new Color(242, 245, 249);
    private LoginManager loginManager;
    private LoginFrame loginFrame;
    private ParkingManager parkingManager;
    private BookingManager bookingManager;
    private SessionManager sessionManager;
    private ArrayList<Transport> transports;

    private JLabel totalSpacesLabel;
    private JLabel availableSpacesLabel;
    private JLabel reservedSpacesLabel;
    private JLabel occupiedSpacesLabel;
    private JLabel activeSessionsLabel;

    private DefaultTableModel parkingTableModel;
    private DefaultTableModel vehicleTableModel;
    private DefaultTableModel bookingTableModel;
    private DefaultTableModel sessionTableModel;

    private JTextField vehicleNumberField;
    private JTextField driverNameField;
    private JTextField contactField;
    private JTextField modelField;
    private JComboBox<String> vehicleTypeBox;

    private JTextField bookingIdField;
    private JComboBox<String> bookingVehicleBox;
    private JComboBox<String> bookingSpaceBox;
    private JTextField bookingDateField;
    private JTextField bookingStartField;
    private JTextField bookingDurationField;
    private JTextField arrivalTimeField;

    private JTextField entryBookingIdField;
    private JTextField entryTimeField;
    private JTextField exitSessionIdField;
    private JTextField exitTimeField;

    public MainFrame(LoginManager loginManager, LoginFrame loginFrame,
                     ParkingManager parkingManager,
                     BookingManager bookingManager,
                     SessionManager sessionManager,
                     ArrayList<Transport> transports) {
        this.loginManager = loginManager;
        this.loginFrame = loginFrame;
        this.parkingManager = parkingManager;
        this.bookingManager = bookingManager;
        this.sessionManager = sessionManager;
        this.transports = transports;

        setTitle("Campus Smart Parking Management System");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                logOut();
            }
        });

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(Color.WHITE);
        tabs.setForeground(NAVY);
        tabs.setFont(new Font("SansSerif", Font.BOLD, 12));
        tabs.addTab("Dashboard", makeDashboardTab());
        tabs.addTab("Parking spaces", makeParkingTab());
        tabs.addTab("Vehicles", makeVehicleTab());
        tabs.addTab("Bookings", makeBookingTab());
        tabs.addTab("Entry and exit", makeSessionTab());
        tabs.addTab("About", makeAboutTab());

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(LIGHT);
        root.add(makeHeader(), BorderLayout.NORTH);
        root.add(tabs, BorderLayout.CENTER);
        setContentPane(root);
        refreshAll();
    }

    private JPanel makeHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JLabel title = new JLabel("CAMPUS SMART PARKING");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(NAVY);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);
        right.add(new JLabel("Welcome, "
                + loginManager.getLoggedInName()));
        right.add(makeButton("LOG OUT", "logout"));

        header.add(title, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }
    private JPanel makeDashboardTab() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 10, 10));
        panel.setBackground(LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        totalSpacesLabel = new JLabel("0", JLabel.CENTER);
        availableSpacesLabel = new JLabel("0", JLabel.CENTER);
        reservedSpacesLabel = new JLabel("0", JLabel.CENTER);
        occupiedSpacesLabel = new JLabel("0", JLabel.CENTER);
        activeSessionsLabel = new JLabel("0", JLabel.CENTER);

        panel.add(makeStatCard("TOTAL SPACES", totalSpacesLabel, BLUE));
        panel.add(makeStatCard("AVAILABLE", availableSpacesLabel, GREEN));
        panel.add(makeStatCard("RESERVED", reservedSpacesLabel, ORANGE));
        panel.add(makeStatCard("OCCUPIED", occupiedSpacesLabel, RED));
        panel.add(makeStatCard("ACTIVE SESSIONS", activeSessionsLabel, NAVY));
        return panel;
    }

    private JPanel makeStatCard(String title, JLabel value, Color color) {
        JPanel card = new JPanel(new BorderLayout(5, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 234)),
                BorderFactory.createEmptyBorder(14, 12, 14, 12)));

        JLabel heading = new JLabel(title, JLabel.CENTER);
        heading.setForeground(NAVY);
        heading.setFont(new Font("SansSerif", Font.BOLD, 11));
        value.setForeground(color);
        value.setFont(new Font("SansSerif", Font.BOLD, 26));

        card.add(heading, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);
        return card;
    }
    private JPanel makeParkingTab() {
        JPanel panel = makePage();
        parkingTableModel = makeTableModel(
                new String[] {"Space ID", "Zone", "Vehicle type", "Status"});

        panel.add(makeButton("Refresh", "refresh"), BorderLayout.NORTH);
        panel.add(new JScrollPane(makeTable(parkingTableModel)),
                BorderLayout.CENTER);
        return panel;
    }

    private JPanel makeVehicleTab() {
        JPanel panel = makePage();
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JPanel form = makeForm();

        vehicleNumberField = new JTextField();
        driverNameField = new JTextField();
        contactField = new JTextField();
        modelField = new JTextField();
        vehicleTypeBox = new JComboBox<String>(
                new String[] {"Car", "Motorcycle", "Van", "Hybrid Car"});

        addField(form, "Vehicle number", vehicleNumberField);
        addField(form, "Driver name", driverNameField);
        addField(form, "Contact number", contactField);
        addField(form, "Model", modelField);
        addField(form, "Vehicle type", vehicleTypeBox);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setOpaque(false);
        buttons.add(makeButton("Add vehicle", "addVehicle"));
        buttons.add(makeButton("Clear", "clearVehicle"));
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        vehicleTableModel = makeTableModel(new String[] {
                "Vehicle number", "Driver", "Contact", "Model", "Type"});
        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(makeTable(vehicleTableModel)),
                BorderLayout.CENTER);
        return panel;
    }

    private JPanel makeBookingTab() {
        JPanel panel = makePage();
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JPanel form = makeForm();

        bookingIdField = new JTextField();
        bookingVehicleBox = new JComboBox<String>();
        bookingSpaceBox = new JComboBox<String>();
        bookingDateField = new JTextField();
        bookingStartField = new JTextField();
        bookingDurationField = new JTextField();
        arrivalTimeField = new JTextField();

        addField(form, "Booking ID (for update, cancel or arrival)",
                bookingIdField);
        addField(form, "Vehicle number", bookingVehicleBox);
        addField(form, "Space ID", bookingSpaceBox);
        addField(form, "Date (YYYY-MM-DD)", bookingDateField);
        addField(form, "Start time (HH:MM)", bookingStartField);
        addField(form, "Duration in minutes", bookingDurationField);
        addField(form, "Arrival time (HH:MM)", arrivalTimeField);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setOpaque(false);
        buttons.add(makeButton("Check availability", "availability"));
        buttons.add(makeButton("Create booking", "createBooking"));
        buttons.add(makeButton("Update booking", "updateBooking"));
        buttons.add(makeButton("Cancel booking", "cancelBooking"));
        buttons.add(makeButton("Check arrival", "checkArrival"));
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        bookingTableModel = makeTableModel(new String[] {
                "Booking ID", "Vehicle", "Space", "Date",
                "Start", "Duration", "Status"});
        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(makeTable(bookingTableModel)),
                BorderLayout.CENTER);
        return panel;
    }

    private JPanel makeSessionTab() {
        JPanel panel = makePage();
        JPanel form = makeForm();

        entryBookingIdField = new JTextField();
        entryTimeField = new JTextField();
        exitSessionIdField = new JTextField();
        exitTimeField = new JTextField();

        addField(form, "Booking ID", entryBookingIdField);
        addField(form, "Entry time (HH:MM)", entryTimeField);
        addField(form, "Session ID", exitSessionIdField);
        addField(form, "Exit time (HH:MM)", exitTimeField);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setOpaque(false);
        buttons.add(makeButton("Check in", "checkIn"));
        buttons.add(makeButton("Check out", "checkOut"));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        sessionTableModel = makeTableModel(new String[] {
                "Session ID", "Booking ID", "Vehicle", "Space",
                "Entry", "Exit", "Duration", "Status"});
        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(makeTable(sessionTableModel)),
                BorderLayout.CENTER);
        return panel;
    }

    private JPanel makeAboutTab() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBackground(LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        panel.add(new JLabel("About Campus Smart Parking"));
        panel.add(new JLabel("This system helps campus staff manage parking spaces and vehicle bookings."));
        panel.add(new JLabel("It records vehicle check-in and check-out and shows which spaces are available."));
        return panel;
    }
    private JPanel makePage() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        return panel;
    }

    private JPanel makeForm() {
        JPanel form = new JPanel(new GridLayout(0, 2, 7, 7));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 234)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        return form;
    }

    private void addField(JPanel panel, String label,
                          java.awt.Component field) {
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setForeground(NAVY);
        fieldLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        panel.add(fieldLabel);

        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(180, 30));
        if (field instanceof JTextField) {
            ((JTextField) field).setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(
                                    new Color(195, 205, 218)),
                            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        } else if (field instanceof JComboBox) {
            ((JComboBox<?>) field).setBorder(
                    BorderFactory.createLineBorder(
                            new Color(195, 205, 218)));
        }
        panel.add(field);
    }
    private JButton makeButton(String text, String command) {
        Color color = BLUE;

        if (command.equals("logout") || command.equals("cancelBooking")
                || command.equals("checkOut")) {
            color = RED;
        } else if (command.equals("updateBooking")) {
            color = ORANGE;
        } else if (command.equals("addVehicle")
                || command.equals("createBooking")
                || command.equals("checkIn")) {
            color = GREEN;
        } else if (command.equals("clearVehicle")) {
            color = NAVY;
        }

        JButton button = new JButton(text);
        button.setActionCommand(command);
        button.addActionListener(this);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 11));
        button.setFocusPainted(false);
        return button;
    }
    private DefaultTableModel makeTableModel(String[] headings) {
        return new DefaultTableModel(headings, 0);
    }

    private JTable makeTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setDefaultEditor(Object.class, null);
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);
        table.setShowGrid(false);
        table.getTableHeader().setBackground(NAVY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        return table;
    }
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        if (command.equals("logout")) {
            logOut();
        } else if (command.equals("refresh")) {
            refreshAll();
        } else if (command.equals("addVehicle")) {
            addVehicle();
        } else if (command.equals("clearVehicle")) {
            clearVehicleFields();
        } else if (command.equals("availability")) {
            checkAvailability();
        } else if (command.equals("createBooking")) {
            createBooking();
        } else if (command.equals("updateBooking")) {
            updateBooking();
        } else if (command.equals("cancelBooking")) {
            cancelBooking();
        } else if (command.equals("checkArrival")) {
            checkArrival();
        } else if (command.equals("checkIn")) {
            checkIn();
        } else if (command.equals("checkOut")) {
            checkOut();
        }
    }

    private void addVehicle() {
        String number = vehicleNumberField.getText().trim();
        String driver = driverNameField.getText().trim();
        String contact = contactField.getText().trim();
        String model = modelField.getText().trim();

        if (number.equals("") || driver.equals("")
                || contact.equals("") || model.equals("")) {
            showMessage("Complete all vehicle fields.");
            return;
        }

        for (int i = 0; i < transports.size(); i++) {
            if (transports.get(i).getVehicleNumber().equals(number)) {
                showMessage("That vehicle number is already registered.");
                return;
            }
        }

        String type = vehicleTypeBox.getSelectedItem().toString();
        Transport transport;

        // The parent type can refer to either child class (polymorphism).
        if (type.equals("Car")) {
            transport = new Car(number, driver, contact, model);
        } else if (type.equals("Motorcycle")) {
            transport = new Motorcycle(number, driver, contact, model);
        } else {
            transport = new Transport(number, driver, contact, model);
            transport.setVehicleType(type);
        }

        transports.add(transport);
        clearVehicleFields();
        refreshAll();
        showMessage("Vehicle added.");
    }

    private void clearVehicleFields() {
        vehicleNumberField.setText("");
        driverNameField.setText("");
        contactField.setText("");
        modelField.setText("");
    }

    private boolean bookingFieldsAreValid() {
        if (bookingVehicleBox.getSelectedItem() == null
                || bookingSpaceBox.getSelectedItem() == null) {
            showMessage("Add a vehicle and choose an available space.");
            return false;
        }

        if (bookingDateField.getText().trim().equals("")) {
            showMessage("Enter a booking date.");
            return false;
        }

        if (parseMinute(bookingStartField.getText()) == -1) {
            showMessage("Enter a valid start time.");
            return false;
        }

        if (parseDuration() <= 0) {
            showMessage("Enter a duration greater than zero.");
            return false;
        }

        return true;
    }

    private void checkAvailability() {
        if (!bookingFieldsAreValid()) {
            return;
        }

        String spaceId = bookingSpaceBox.getSelectedItem().toString();
        String date = bookingDateField.getText().trim();
        int start = parseMinute(bookingStartField.getText());
        int duration = parseDuration();

        if (bookingManager.isAvailable(spaceId, date, start, duration)) {
            showMessage("This space is available.");
        } else {
            showMessage("This space is not available.");
        }
    }

    private void createBooking() {
        if (!bookingFieldsAreValid()) {
            return;
        }

        String vehicleNumber = bookingVehicleBox.getSelectedItem().toString();
        String spaceId = bookingSpaceBox.getSelectedItem().toString();
        String date = bookingDateField.getText().trim();
        int start = parseMinute(bookingStartField.getText());
        int duration = parseDuration();

        if (!bookingManager.createBooking(
                vehicleNumber, spaceId, date, start, duration)) {
            showMessage("Booking could not be created.");
            return;
        }

        ArrayList<Booking> bookings = bookingManager.getAllBookings();
        Booking newBooking = bookings.get(bookings.size() - 1);
        bookingIdField.setText(newBooking.getBookingId());
        refreshAll();
        showMessage("Booking created: " + newBooking.getBookingId());
    }

    private void updateBooking() {
        String id = bookingIdField.getText().trim();
        String date = bookingDateField.getText().trim();
        int start = parseMinute(bookingStartField.getText());
        int duration = parseDuration();

        if (id.equals("") || date.equals("") || start == -1 || duration <= 0) {
            showMessage("Enter a booking ID, date, time and duration.");
            return;
        }

        if (!bookingManager.updateBooking(id, date, start, duration)) {
            showMessage("Booking could not be updated.");
            return;
        }

        refreshAll();
        showMessage("Booking updated.");
    }

    private void cancelBooking() {
        String id = bookingIdField.getText().trim();

        if (id.equals("")) {
            showMessage("Enter a booking ID.");
            return;
        }

        if (!bookingManager.cancelBooking(id)) {
            showMessage("Booking could not be cancelled.");
            return;
        }

        refreshAll();
        showMessage("Booking cancelled. The space is available again.");
    }

    private void checkArrival() {
        String id = bookingIdField.getText().trim();
        int arrival = parseMinute(arrivalTimeField.getText());

        if (id.equals("") || arrival == -1) {
            showMessage("Enter a booking ID and valid arrival time.");
            return;
        }

        if (bookingManager.checkArrival(id, arrival)) {
            showMessage("Arrival is within the 15-minute grace period.");
            return;
        }

        int index = bookingManager.findBooking(id);
        if (index >= 0
                && bookingManager.getAllBookings().get(index)
                .getStatus().equals(Booking.EXPIRED)) {
            refreshAll();
            showMessage("Booking expired. The space has been released.");
        } else {
            showMessage("Arrival is outside the allowed time.");
        }
    }

    private void checkIn() {
        String bookingId = entryBookingIdField.getText().trim();
        int entryMinute = parseMinute(entryTimeField.getText());

        if (bookingId.equals("") || entryMinute == -1) {
            showMessage("Enter a booking ID and valid entry time.");
            return;
        }

        if (!sessionManager.checkIn(bookingId, entryMinute)) {
            refreshAll();
            showMessage("Check-in failed. Check the booking and arrival time.");
            return;
        }

        ArrayList<ParkingSession> sessions = sessionManager.getAllSessions();
        ParkingSession session = sessions.get(sessions.size() - 1);
        exitSessionIdField.setText(session.getSessionId());
        refreshAll();
        showMessage("Vehicle checked in. Session ID: "
                + session.getSessionId());
    }

    private void checkOut() {
        String sessionId = exitSessionIdField.getText().trim();
        int exitMinute = parseMinute(exitTimeField.getText());

        if (sessionId.equals("") || exitMinute == -1) {
            showMessage("Enter a session ID and valid exit time.");
            return;
        }

        int index = sessionManager.findSession(sessionId);
        if (index == -1) {
            showMessage("Session was not found.");
            return;
        }

        if (!sessionManager.checkOut(sessionId, exitMinute)) {
            showMessage("Check-out failed. Check the exit time.");
            return;
        }

        ParkingSession session = sessionManager.getAllSessions().get(index);
        refreshAll();

        String message = "Checkout complete. Duration: "
                + session.getDuration() + " minutes.";

        if (session.isOverstay()) {
            message = message + " OVERSTAY: the reserved end time passed.";
        }

        showMessage(message);
    }

    private int parseDuration() {
        try {
            return Integer.parseInt(bookingDurationField.getText().trim());
        } catch (NumberFormatException error) {
            return -1;
        }
    }

    private int parseMinute(String time) {
        String[] parts = time.trim().split(":");

        if (parts.length != 2) {
            return -1;
        }

        try {
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);

            if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                return -1;
            }

            return hour * 60 + minute;
        } catch (NumberFormatException error) {
            return -1;
        }
    }

    private String showMinute(int time) {
        int hour = time / 60;
        int minute = time % 60;
        String hourText = String.valueOf(hour);
        String minuteText = String.valueOf(minute);

        if (hour < 10) {
            hourText = "0" + hourText;
        }
        if (minute < 10) {
            minuteText = "0" + minuteText;
        }
        return hourText + ":" + minuteText;
    }

    private void refreshAll() {
        refreshParkingTable();
        refreshVehicleTable();
        refreshBookingTable();
        refreshSessionTable();
        refreshChoices();
        refreshDashboard();
    }

    private void refreshParkingTable() {
        parkingTableModel.setRowCount(0);
        ArrayList<ParkingSpace> spaces = parkingManager.getAllSpaces();

        for (int i = 0; i < spaces.size(); i++) {
            ParkingSpace space = spaces.get(i);
            parkingTableModel.addRow(new Object[] {
                    space.getSpaceId(),
                    space.getZone(),
                    space.getVehicleType(),
                    space.getStatus()
            });
        }
    }

    private void refreshVehicleTable() {
        vehicleTableModel.setRowCount(0);

        for (int i = 0; i < transports.size(); i++) {
            Transport transport = transports.get(i);
            vehicleTableModel.addRow(new Object[] {
                    transport.getVehicleNumber(),
                    transport.getDriverName(),
                    transport.getContactNumber(),
                    transport.getModel(),
                    transport.getVehicleType()
            });
        }
    }

    private void refreshBookingTable() {
        bookingTableModel.setRowCount(0);
        ArrayList<Booking> bookings = bookingManager.getAllBookings();

        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);
            bookingTableModel.addRow(new Object[] {
                    booking.getBookingId(),
                    booking.getVehicleNumber(),
                    booking.getSpaceId(),
                    booking.getDate(),
                    showMinute(booking.getStartMinute()),
                    booking.getDuration(),
                    booking.getStatus()
            });
        }
    }

    private void refreshSessionTable() {
        sessionTableModel.setRowCount(0);
        ArrayList<ParkingSession> sessions = sessionManager.getAllSessions();

        for (int i = 0; i < sessions.size(); i++) {
            ParkingSession session = sessions.get(i);
            String exit = "-";
            String duration = "-";

            if (session.getStatus().equals(ParkingSession.COMPLETED)) {
                exit = showMinute(session.getExitMinute());
                duration = String.valueOf(session.getDuration());
            }

            sessionTableModel.addRow(new Object[] {
                    session.getSessionId(),
                    session.getBookingId(),
                    session.getVehicleNumber(),
                    session.getSpaceId(),
                    showMinute(session.getEntryMinute()),
                    exit,
                    duration,
                    session.getStatus()
            });
        }
    }

    private void refreshChoices() {
        bookingVehicleBox.removeAllItems();
        for (int i = 0; i < transports.size(); i++) {
            bookingVehicleBox.addItem(transports.get(i).getVehicleNumber());
        }

        bookingSpaceBox.removeAllItems();
        ArrayList<ParkingSpace> spaces = parkingManager.getAvailableSpaces();
        for (int i = 0; i < spaces.size(); i++) {
            bookingSpaceBox.addItem(spaces.get(i).getSpaceId());
        }
    }

    private void refreshDashboard() {
        int available = 0;
        int reserved = 0;
        int occupied = 0;
        ArrayList<ParkingSpace> spaces = parkingManager.getAllSpaces();

        for (int i = 0; i < spaces.size(); i++) {
            String status = spaces.get(i).getStatus();

            if (status.equals(ParkingSpace.AVAILABLE)) {
                available++;
            } else if (status.equals(ParkingSpace.RESERVED)) {
                reserved++;
            } else if (status.equals(ParkingSpace.OCCUPIED)) {
                occupied++;
            }
        }

        int active = 0;
        ArrayList<ParkingSession> sessions = sessionManager.getAllSessions();
        for (int i = 0; i < sessions.size(); i++) {
            if (sessions.get(i).getStatus().equals(ParkingSession.ACTIVE)) {
                active++;
            }
        }

        totalSpacesLabel.setText(String.valueOf(spaces.size()));
        availableSpacesLabel.setText(String.valueOf(available));
        reservedSpacesLabel.setText(String.valueOf(reserved));
        occupiedSpacesLabel.setText(String.valueOf(occupied));
        activeSessionsLabel.setText(String.valueOf(active));
    }

    private void logOut() {
        int answer = JOptionPane.showConfirmDialog(this,
                "Log out of the system?", "Log out",
                JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            dispose();
            loginFrame.setVisible(true);
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
