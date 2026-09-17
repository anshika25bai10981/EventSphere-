package repository;

import java.util.ArrayList;
import model.Event;

public class EventRepository {

    private ArrayList<Event> events = new ArrayList<>();

    public void addEvent(Event event) {
        events.add(event);
    }

    public ArrayList<Event> getAllEvents() {
        return events;
    }

    public Event findEventById(String id) {
        for (Event event : events) {
            if (event.getEventId().equals(id)) {
                return event;
            }
        }
        return null;
    }

    public void deleteEvent(String id) {
        Event event = findEventById(id);

        if (event != null) {
            events.remove(event);
        }
    }
}