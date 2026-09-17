package model;

public class Organizer extends User {
    private String department;

    public Organizer(String id, String name, String email,
                     String password, String department) {
        super(id, name, email, password);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String getRole() {
        return "Organizer";
    }
}