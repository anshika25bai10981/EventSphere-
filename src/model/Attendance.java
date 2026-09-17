package model;

public class Attendance {

    private String studentId;
    private String eventId;
    private String status;

    public Attendance(String studentId, String eventId, String status) {
        this.studentId = studentId;
        this.eventId = eventId;
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void displayAttendance() {
        System.out.println("Student ID : " + studentId);
        System.out.println("Event ID   : " + eventId);
        System.out.println("Status     : " + status);
    }
}