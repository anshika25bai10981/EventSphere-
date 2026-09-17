package repository;

import java.util.ArrayList;
import model.Registration;

public class RegistrationRepository {

    private ArrayList<Registration> registrations = new ArrayList<>();

    public void addRegistration(Registration registration) {
        registrations.add(registration);
    }

    public ArrayList<Registration> getAllRegistrations() {
        return registrations;
    }

    public boolean isAlreadyRegistered(
            String studentId, String eventId) {

        for (Registration registration : registrations) {

            if (registration.getStudentId().equals(studentId)
                    && registration.getEventId().equals(eventId)) {

                return true;
            }
        }

        return false;
    }
}