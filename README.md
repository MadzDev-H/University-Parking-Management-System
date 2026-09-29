# University Parking Management System

An individual first-year Software Engineering OOP academic project developed at the National Institute of Business Management (NIBM). This Java Swing desktop prototype demonstrates basic workflows for campus parking, booking, and vehicle sessions.

AI tools were used as learning and debugging assistance during development. The project was developed individually.

## Features

- Register and sign in to an in-memory campus user account
- Add vehicle records for cars, motorcycles, vans, and hybrid cars
- View predefined parking spaces and their status
- Create, update, and cancel bookings
- Check a booking against a 15-minute arrival grace period
- Record vehicle check-in and check-out sessions
- View dashboard counts for spaces and active sessions
- See completed session duration and an overstay indicator

## Technologies Used

- Java
- Java Swing
- Java AWT
- Java Collections Framework (`ArrayList`)

The project uses standard Java libraries and has no external dependencies.

## OOP Concepts Demonstrated

- Encapsulation through private model fields and public accessors
- Inheritance with `Car` and `Motorcycle` extending `Transport`
- Polymorphism through overridden vehicle-type methods
- Interface-based abstraction through the operations interfaces
- Constructors, objects, and associations between manager classes

## How to Run

Open the `Parking Management` project folder in IntelliJ IDEA. The project metadata currently specifies `openjdk-26`; configure an available compatible JDK in the IDE, then run `smartpark.app.Main`.

No external libraries are required. The current source has not been compiled in this review environment because `javac` was unavailable.

## Project Structure

```text
src/
└── smartpark/
    ├── app/
    │   └── Main.java
    ├── manager/
    │   ├── BookingManager.java
    │   ├── LoginManager.java
    │   ├── ParkingManager.java
    │   └── SessionManager.java
    ├── model/
    │   ├── Booking.java
    │   ├── Car.java
    │   ├── Motorcycle.java
    │   ├── ParkingSession.java
    │   ├── ParkingSpace.java
    │   ├── Transport.java
    │   └── UserAccount.java
    ├── operations/
    │   ├── BookingOperations.java
    │   ├── ParkingOperations.java
    │   └── SessionOperations.java
    └── ui/
        ├── LoginFrame.java
        └── MainFrame.java
```

## Current Limitations

- Accounts, vehicles, bookings, spaces, and sessions are held in memory and reset when the application closes.
- Passwords are stored as plain text; this sign-in flow is for demonstration only. Do not use real credentials or personal data.
- Booking availability is tied to a space-wide status, so separate time-slot bookings for the same space are not fully supported.
- Vehicle types are not checked against parking-space types.
- Booking dates are checked for non-empty input but are not validated as calendar dates.
- The application does not support bookings that cross midnight.

## Future Improvements

- Store records persistently and add automated tests.
- Hash passwords and improve account validation.
- Validate dates using a date type rather than free-text input.
- Enforce vehicle-to-space compatibility.
- Model availability by booking schedule so a space can support separate time slots.
- Separate the large UI class into smaller views and controllers.

## Screenshots

No screenshots are included yet. Add screenshots of the dashboard, booking workflow, and entry/exit session table using fictional demo details only. Do not include real contact information, usernames, or passwords.
