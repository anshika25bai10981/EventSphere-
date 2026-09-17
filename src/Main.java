import java.util.Scanner;
import java.time.LocalDate;
import java.io.*;

import model.*;
import repository.UserRepository;
import repository.EventRepository;
import repository.RegistrationRepository;
import util.InputValidator;
import util.Logger;

public class Main {

    static Scanner sc = new Scanner(System.in);

    static UserRepository userRepository = new UserRepository();
    static EventRepository eventRepository = new EventRepository();
    static RegistrationRepository registrationRepository =
            new RegistrationRepository();

    static final String DATA_FOLDER = "data";
    static final String USERS_FILE = "data/users.txt";
    static final String EVENTS_FILE = "data/events.txt";
    static final String REGISTRATIONS_FILE = "data/registrations.txt";

    public static void main(String[] args) {

        createDataFolder();
        loadData();

        while (true) {

            System.out.println("\n===== Welcome to EventSphere =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    registerUser();
                    break;

                case 2:
                    loginUser();
                    break;

                case 3:
                    saveData();
                    Logger.log("Application closed.");
                    System.out.println(
                            "Thank you for using EventSphere!"
                    );
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // ================= REGISTER USER =================

    static void registerUser() {

        System.out.println("\n===== REGISTER =====");
        System.out.println("1. Student");
        System.out.println("2. Organizer");

        int role = readInt("Enter choice: ");

        if (role != 1 && role != 2) {
            System.out.println("Invalid choice.");
            return;
        }

        String id = readText("Enter ID: ");

        if (userRepository.findUserById(id) != null) {
            System.out.println("User ID already exists.");
            return;
        }

        String name = readText("Enter name: ");
        String email = readText("Enter email: ");

        if (!InputValidator.isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return;
        }

        String password = readText("Enter password: ");

        if (role == 1) {

            Student student = new Student(
                    id,
                    name,
                    email,
                    password
            );

            userRepository.addUser(student);

            System.out.println(
                    "Student registered successfully."
            );

            Logger.log("Student registered: " + id);

        } else {

            String department =
                    readText("Enter organizer department: ");

            Organizer organizer = new Organizer(
                    id,
                    name,
                    email,
                    password,
                    department
            );

            userRepository.addUser(organizer);

            System.out.println(
                    "Organizer registered successfully."
            );

            Logger.log("Organizer registered: " + id);
        }

        saveData();
    }

    // ================= LOGIN =================

    static void loginUser() {

        System.out.println("\n===== LOGIN =====");

        String id = readText("Enter ID: ");
        String password = readText("Enter password: ");

        User user = userRepository.findUserById(id);

        if (user == null ||
                !user.getPassword().equals(password)) {

            System.out.println("Invalid ID or password.");

            Logger.log(
                    "Failed login attempt for ID: " + id
            );

            return;
        }

        System.out.println("\nLogin successful!");
        System.out.println("Welcome, " + user.getName());

        Logger.log(
                user.getRole() + " logged in: " + user.getId()
        );

        if (user instanceof Student) {

            studentMenu((Student) user);

        } else if (user instanceof Organizer) {

            organizerMenu((Organizer) user);
        }
    }

    // ================= ORGANIZER MENU =================

    static void organizerMenu(Organizer organizer) {

        while (true) {

            System.out.println("\n===== ORGANIZER MENU =====");
            System.out.println("1. Create Event");
            System.out.println("2. View Events");
            System.out.println("3. Mark Attendance");
            System.out.println("4. View My Event Reports");
            System.out.println("5. Logout");

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    createEvent(organizer);
                    break;

                case 2:
                    viewEvents();
                    break;

                case 3:
                    markAttendance(organizer);
                    break;

                case 4:
                    showOrganizerReports(organizer);
                    break;

                case 5:

                    Logger.log(
                            "Organizer logged out: "
                                    + organizer.getId()
                    );

                    System.out.println(
                            "Logged out successfully."
                    );

                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // ================= CREATE EVENT =================

    static void createEvent(Organizer organizer) {

        System.out.println("\n===== CREATE EVENT =====");

        String eventId =
                readText("Enter event ID: ");

        if (eventRepository.findEventById(eventId) != null) {

            System.out.println(
                    "Event ID already exists."
            );

            return;
        }

        String eventName =
                readText("Enter event name: ");

        String description =
                readText("Enter description: ");

        String date =
                readText("Enter date (DD-MM-YYYY): ");

        String time =
                readText("Enter time: ");

        String venue =
                readText("Enter venue: ");

        int capacity =
                readInt("Enter capacity: ");

        if (!InputValidator.isPositive(capacity)) {

            System.out.println(
                    "Capacity must be greater than 0."
            );

            return;
        }

        Event event = new Event(
                eventId,
                eventName,
                description,
                date,
                time,
                venue,
                capacity,
                organizer.getId()
        );

        eventRepository.addEvent(event);

        saveData();

        System.out.println(
                "Event created successfully."
        );

        Logger.log(
                "Event created: " + eventId
        );
    }

    // ================= VIEW EVENTS =================

    static void viewEvents() {

        System.out.println(
                "\n===== AVAILABLE EVENTS ====="
        );

        if (eventRepository.getAllEvents().isEmpty()) {

            System.out.println(
                    "No events available."
            );

            return;
        }

        for (Event event :
                eventRepository.getAllEvents()) {

            int registered =
                    getRegisteredCount(
                            event.getEventId()
                    );

            int remaining =
                    event.getCapacity() - registered;

            System.out.println(
                    "\n------------------------------"
            );

            System.out.println(
                    "Event ID: "
                            + event.getEventId()
            );

            System.out.println(
                    "Event Name: "
                            + event.getEventName()
            );

            System.out.println(
                    "Description: "
                            + event.getDescription()
            );

            System.out.println(
                    "Date: "
                            + event.getDate()
            );

            System.out.println(
                    "Time: "
                            + event.getTime()
            );

            System.out.println(
                    "Venue: "
                            + event.getVenue()
            );

            System.out.println(
                    "Capacity: "
                            + event.getCapacity()
            );

            System.out.println(
                    "Registered: "
                            + registered
            );

            System.out.println(
                    "Seats Remaining: "
                            + remaining
            );

            if (remaining == 0) {

                System.out.println(
                        "STATUS: EVENT FULL"
                );

            } else if (remaining <= 5) {

                System.out.println(
                        "STATUS: LIMITED SEATS"
                );

            } else {

                System.out.println(
                        "STATUS: AVAILABLE"
                );
            }
        }

        System.out.println(
                "\n------------------------------"
        );
    }

    // ================= STUDENT MENU =================

    static void studentMenu(Student student) {

        while (true) {

            System.out.println("\n===== STUDENT MENU =====");
            System.out.println("1. View Events");
            System.out.println("2. Register for Event");
            System.out.println("3. View My Registrations");
            System.out.println("4. Cancel Registration");
            System.out.println("5. Logout");

            int choice =
                    readInt("Enter choice: ");

            switch (choice) {

                case 1:
                    viewEvents();
                    break;

                case 2:
                    registerForEvent(student);
                    break;

                case 3:
                    viewMyRegistrations(student);
                    break;

                case 4:
                    cancelRegistration(student);
                    break;

                case 5:

                    Logger.log(
                            "Student logged out: "
                                    + student.getId()
                    );

                    System.out.println(
                            "Logged out successfully."
                    );

                    return;

                default:
                    System.out.println(
                            "Invalid choice."
                    );
            }
        }
    }

    // ================= SHORT EVENT LIST =================

    static void showEventsForRegistration() {

        System.out.println(
                "\n===== EVENTS FOR REGISTRATION ====="
        );

        if (eventRepository.getAllEvents().isEmpty()) {

            System.out.println(
                    "No events available."
            );

            return;
        }

        for (Event event :
                eventRepository.getAllEvents()) {

            int registered =
                    getRegisteredCount(
                            event.getEventId()
                    );

            int remaining =
                    event.getCapacity()
                            - registered;

            String status;

            if (remaining == 0) {

                status = "FULL";

            } else if (remaining <= 5) {

                status = "LIMITED SEATS";

            } else {

                status = "AVAILABLE";
            }

            System.out.println(
                    "ID: " + event.getEventId()
                            + " | Name: "
                            + event.getEventName()
                            + " | Seats Remaining: "
                            + remaining
                            + " | Status: "
                            + status
            );
        }
    }

    // ================= REGISTER FOR EVENT =================

    static void registerForEvent(Student student) {

        System.out.println(
                "\n===== REGISTER FOR EVENT ====="
        );

        if (eventRepository.getAllEvents().isEmpty()) {

            System.out.println(
                    "No events available."
            );

            return;
        }

        showEventsForRegistration();

        String eventId =
                readText("Enter event ID: ");

        Event event =
                eventRepository.findEventById(eventId);

        if (event == null) {

            System.out.println(
                    "Event not found."
            );

            return;
        }

        if (registrationRepository.isAlreadyRegistered(
                student.getId(), eventId)) {

            System.out.println(
                    "You are already registered for this event."
            );

            return;
        }

        int registered =
                getRegisteredCount(eventId);

        int remaining =
                event.getCapacity() - registered;

        if (remaining <= 0) {

            System.out.println(
                    "Sorry, this event is full."
            );

            Logger.log(
                    "Registration failed - event full: "
                            + eventId
            );

            return;
        }

        String registrationId =
                "R" + (
                        registrationRepository
                                .getAllRegistrations()
                                .size() + 1
                );

        Registration registration =
                new Registration(
                        registrationId,
                        student.getId(),
                        eventId,
                        LocalDate.now().toString(),
                        "Active"
                );

        registrationRepository.addRegistration(
                registration
        );

        saveData();

        System.out.println(
                "\nRegistration successful!"
        );

        System.out.println(
                "Event: "
                        + event.getEventName()
        );

        System.out.println(
                "Seats Remaining: "
                        + (remaining - 1)
        );

        Logger.log(
                "Student "
                        + student.getId()
                        + " registered for event "
                        + eventId
        );
    }

    // ================= VIEW MY REGISTRATIONS =================

    static void viewMyRegistrations(
            Student student) {

        System.out.println(
                "\n===== MY REGISTRATIONS ====="
        );

        boolean found = false;

        for (Registration registration :
                registrationRepository
                        .getAllRegistrations()) {

            if (registration.getStudentId()
                    .equals(student.getId())) {

                Event event =
                        eventRepository.findEventById(
                                registration.getEventId()
                        );

                System.out.println(
                        "\n-------------------------"
                );

                System.out.println(
                        "Registration ID: "
                                + registration
                                .getRegistrationId()
                );

                if (event != null) {

                    System.out.println(
                            "Event: "
                                    + event.getEventName()
                    );

                    System.out.println(
                            "Date: "
                                    + event.getDate()
                    );

                    System.out.println(
                            "Venue: "
                                    + event.getVenue()
                    );
                }

                System.out.println(
                        "Registration Date: "
                                + registration
                                .getRegistrationDate()
                );

                System.out.println(
                        "Status: "
                                + registration.getStatus()
                );

                found = true;
            }
        }

        if (!found) {

            System.out.println(
                    "You have no registrations."
            );
        }
    }

    // ================= CANCEL REGISTRATION =================

    static void cancelRegistration(
            Student student) {

        System.out.println(
                "\n===== CANCEL REGISTRATION ====="
        );

        boolean found = false;

        for (Registration registration :
                registrationRepository
                        .getAllRegistrations()) {

            if (registration.getStudentId()
                    .equals(student.getId())
                    && registration.getStatus()
                    .equalsIgnoreCase("Active")) {

                Event event =
                        eventRepository.findEventById(
                                registration.getEventId()
                        );

                System.out.println(
                        "Registration ID: "
                                + registration
                                .getRegistrationId()
                );

                if (event != null) {

                    System.out.println(
                            "Event: "
                                    + event.getEventName()
                    );
                }

                found = true;
            }
        }

        if (!found) {

            System.out.println(
                    "No active registrations found."
            );

            return;
        }

        String registrationId =
                readText(
                        "Enter registration ID to cancel: "
                );

        for (Registration registration :
                registrationRepository
                        .getAllRegistrations()) {

            if (registration.getRegistrationId()
                    .equals(registrationId)
                    && registration.getStudentId()
                    .equals(student.getId())
                    && registration.getStatus()
                    .equalsIgnoreCase("Active")) {

                registration.setStatus(
                        "Cancelled"
                );

                saveData();

                System.out.println(
                        "Registration cancelled successfully."
                );

                Logger.log(
                        "Registration cancelled: "
                                + registrationId
                );

                return;
            }
        }

        System.out.println(
                "Registration not found."
        );
    }

    // ================= MARK ATTENDANCE =================

    static void markAttendance(
            Organizer organizer) {

        System.out.println(
                "\n===== MARK ATTENDANCE ====="
        );

        String eventId =
                readText("Enter event ID: ");

        Event event =
                eventRepository.findEventById(
                        eventId
                );

        if (event == null) {

            System.out.println(
                    "Event not found."
            );

            return;
        }

        // Organizer can only manage own event
        if (!event.getOrganizerId()
                .equals(organizer.getId())) {

            System.out.println(
                    "You can only manage your own events."
            );

            return;
        }

        boolean found = false;

        for (Registration registration :
                registrationRepository
                        .getAllRegistrations()) {

            if (registration.getEventId()
                    .equals(eventId)
                    && registration.getStatus()
                    .equalsIgnoreCase("Active")) {

                System.out.println(
                        "Student ID: "
                                + registration
                                .getStudentId()
                );

                System.out.println(
                        "1. Present"
                );

                System.out.println(
                        "2. Absent"
                );

                int choice =
                        readInt("Enter attendance: ");

                if (choice == 1) {

                    System.out.println(
                            "Marked Present."
                    );

                } else if (choice == 2) {

                    System.out.println(
                            "Marked Absent."
                    );

                } else {

                    System.out.println(
                            "Invalid choice."
                    );
                }

                found = true;
            }
        }

        if (!found) {

            System.out.println(
                    "No active registrations for this event."
            );
        }

        Logger.log(
                "Attendance processed for event: "
                        + eventId
        );
    }

    // ================= ORGANIZER REPORTS =================

    static void showOrganizerReports(
            Organizer organizer) {

        System.out.println(
                "\n===== MY EVENT REPORTS ====="
        );

        boolean found = false;

        for (Event event :
                eventRepository.getAllEvents()) {

            // Show only this organizer's events
            if (!event.getOrganizerId()
                    .equals(organizer.getId())) {

                continue;
            }

            int registered =
                    getRegisteredCount(
                            event.getEventId()
                    );

            int remaining =
                    event.getCapacity()
                            - registered;

            System.out.println(
                    "\n------------------------------"
            );

            System.out.println(
                    "Event ID: "
                            + event.getEventId()
            );

            System.out.println(
                    "Event Name: "
                            + event.getEventName()
            );

            System.out.println(
                    "Capacity: "
                            + event.getCapacity()
            );

            System.out.println(
                    "Registered: "
                            + registered
            );

            System.out.println(
                    "Seats Remaining: "
                            + remaining
            );

            found = true;
        }

        if (!found) {

            System.out.println(
                    "You have not created any events."
            );
        }
    }

    // ================= COUNT REGISTRATIONS =================

    static int getRegisteredCount(
            String eventId) {

        int count = 0;

        for (Registration registration :
                registrationRepository
                        .getAllRegistrations()) {

            if (registration.getEventId()
                    .equals(eventId)
                    && registration.getStatus()
                    .equalsIgnoreCase("Active")) {

                count++;
            }
        }

        return count;
    }

    // ================= INPUT METHODS =================

    static String readText(String message) {

        while (true) {

            System.out.print(message);

            String value =
                    sc.nextLine();

            if (!InputValidator.isEmpty(value)) {

                return value.trim();
            }

            System.out.println(
                    "Input cannot be empty."
            );
        }
    }

    static int readInt(String message) {

        while (true) {

            System.out.print(message);

            try {

                int value =
                        Integer.parseInt(
                                sc.nextLine()
                        );

                return value;

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }

    // ================= CREATE FOLDERS =================

    static void createDataFolder() {

        File dataFolder =
                new File(DATA_FOLDER);

        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File logsFolder =
                new File("logs");

        if (!logsFolder.exists()) {
            logsFolder.mkdirs();
        }
    }

    // ================= SAVE DATA =================

    static void saveData() {

        try {

            // -------- USERS --------

            PrintWriter userWriter =
                    new PrintWriter(
                            new FileWriter(USERS_FILE)
                    );

            for (User user :
                    userRepository.getAllUsers()) {

                if (user instanceof Student) {

                    userWriter.println(
                            "Student|"
                                    + user.getId()
                                    + "|"
                                    + user.getName()
                                    + "|"
                                    + user.getEmail()
                                    + "|"
                                    + user.getPassword()
                    );

                } else if (user instanceof Organizer) {

                    Organizer organizer =
                            (Organizer) user;

                    userWriter.println(
                            "Organizer|"
                                    + organizer.getId()
                                    + "|"
                                    + organizer.getName()
                                    + "|"
                                    + organizer.getEmail()
                                    + "|"
                                    + organizer.getPassword()
                                    + "|"
                                    + organizer.getDepartment()
                    );
                }
            }

            userWriter.close();

            // -------- EVENTS --------

            PrintWriter eventWriter =
                    new PrintWriter(
                            new FileWriter(EVENTS_FILE)
                    );

            for (Event event :
                    eventRepository.getAllEvents()) {

                eventWriter.println(
                        event.getEventId()
                                + "|"
                                + event.getEventName()
                                + "|"
                                + event.getDescription()
                                + "|"
                                + event.getDate()
                                + "|"
                                + event.getTime()
                                + "|"
                                + event.getVenue()
                                + "|"
                                + event.getCapacity()
                                + "|"
                                + event.getOrganizerId()
                );
            }

            eventWriter.close();

            // -------- REGISTRATIONS --------

            PrintWriter registrationWriter =
                    new PrintWriter(
                            new FileWriter(
                                    REGISTRATIONS_FILE
                            )
                    );

            for (Registration registration :
                    registrationRepository
                            .getAllRegistrations()) {

                registrationWriter.println(
                        registration
                                .getRegistrationId()
                                + "|"
                                + registration
                                .getStudentId()
                                + "|"
                                + registration
                                .getEventId()
                                + "|"
                                + registration
                                .getRegistrationDate()
                                + "|"
                                + registration
                                .getStatus()
                );
            }

            registrationWriter.close();

        } catch (IOException e) {

            System.out.println(
                    "Error saving data: "
                            + e.getMessage()
            );
        }
    }

    // ================= LOAD DATA =================

    static void loadData() {

        // -------- USERS --------

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(
                                    USERS_FILE
                            )
                    );

            String line;

            while ((line = reader.readLine())
                    != null) {

                String[] parts =
                        line.split("\\|");

                if (parts.length >= 5) {

                    String role = parts[0];
                    String id = parts[1];
                    String name = parts[2];
                    String email = parts[3];
                    String password = parts[4];

                    if (role.equals("Student")) {

                        userRepository.addUser(
                                new Student(
                                        id,
                                        name,
                                        email,
                                        password
                                )
                        );

                    } else if (
                            role.equals("Organizer")) {

                        String department =
                                parts.length > 5
                                        ? parts[5]
                                        : "Unknown";

                        userRepository.addUser(
                                new Organizer(
                                        id,
                                        name,
                                        email,
                                        password,
                                        department
                                )
                        );
                    }
                }
            }

            reader.close();

        } catch (FileNotFoundException e) {

            // File will be created later.

        } catch (Exception e) {

            System.out.println(
                    "Error loading users."
            );
        }

        // -------- EVENTS --------

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(
                                    EVENTS_FILE
                            )
                    );

            String line;

            while ((line = reader.readLine())
                    != null) {

                String[] parts =
                        line.split("\\|");

                /*
                 * New format:
                 * ID|Name|Description|Date|Time|
                 * Venue|Capacity|OrganizerID
                 *
                 * Old format had Category at index 3.
                 * This check allows old saved events
                 * to be read as well.
                 */

                if (parts.length >= 8) {

                    if (parts.length == 8) {

                        Event event =
                                new Event(
                                        parts[0],
                                        parts[1],
                                        parts[2],
                                        parts[3],
                                        parts[4],
                                        parts[5],
                                        Integer.parseInt(
                                                parts[6]
                                        ),
                                        parts[7]
                                );

                        eventRepository
                                .addEvent(event);

                    } else if (parts.length >= 9) {

                        // Old format:
                        // ID|Name|Description|Category|
                        // Date|Time|Venue|Capacity|OrganizerID

                        Event event =
                                new Event(
                                        parts[0],
                                        parts[1],
                                        parts[2],
                                        parts[4],
                                        parts[5],
                                        parts[6],
                                        Integer.parseInt(
                                                parts[7]
                                        ),
                                        parts[8]
                                );

                        eventRepository
                                .addEvent(event);
                    }
                }
            }

            reader.close();

        } catch (FileNotFoundException e) {

            // File will be created later.

        } catch (Exception e) {

            System.out.println(
                    "Error loading events."
            );
        }

        // -------- REGISTRATIONS --------

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(
                                    REGISTRATIONS_FILE
                            )
                    );

            String line;

            while ((line = reader.readLine())
                    != null) {

                String[] parts =
                        line.split("\\|");

                if (parts.length >= 5) {

                    Registration registration =
                            new Registration(
                                    parts[0],
                                    parts[1],
                                    parts[2],
                                    parts[3],
                                    parts[4]
                            );

                    registrationRepository
                            .addRegistration(
                                    registration
                            );
                }
            }

            reader.close();

        } catch (FileNotFoundException e) {

            // File will be created later.

        } catch (Exception e) {

            System.out.println(
                    "Error loading registrations."
            );
        }
    }
}