package model;

public class Event {

    private String eventId;
    private String eventName;
    private String description;
    private String date;
    private String time;
    private String venue;
    private int capacity;
    private String organizerId;

    public Event(String eventId, String eventName, String description,
                 String date, String time, String venue,
                 int capacity, String organizerId) {

        this.eventId = eventId;
        this.eventName = eventName;
        this.description = description;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.capacity = capacity;
        this.organizerId = organizerId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getVenue() {
        return venue;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void displayEvent() {

        System.out.println("Event ID: " + eventId);
        System.out.println("Event Name: " + eventName);
        System.out.println("Description: " + description);
        System.out.println("Date: " + date);
        System.out.println("Time: " + time);
        System.out.println("Venue: " + venue);
        System.out.println("Capacity: " + capacity);
    }
}