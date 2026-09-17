package model;

public class Registration {

    private String registrationId;
    private String studentId;
    private String eventId;
    private String registrationDate;
    private String status;

    public Registration(String registrationId, String studentId,
                        String eventId, String registrationDate,
                        String status) {

        this.registrationId = registrationId;
        this.studentId = studentId;
        this.eventId = eventId;
        this.registrationDate = registrationDate;
        this.status = status;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void displayRegistration() {
        System.out.println("Registration ID : " + registrationId);
        System.out.println("Student ID      : " + studentId);
        System.out.println("Event ID        : " + eventId);
        System.out.println("Registration Date: " + registrationDate);
        System.out.println("Status          : " + status);
    }
}
