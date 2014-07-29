package com.hd.snscoins.core;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table events.
 */
public class Events {

    private Long id;
    /** Not-null value. */
    private String title;
    private String start_date;
    private String start_time;
    private String end_date;
    private String end_time;
    private String venue;
    private String details;
    private String image_url;
    private String image_path;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Events() {
    }

    public Events(Long id) {
        this.id = id;
    }

    public Events(Long id, String title, String start_date, String start_time, String end_date, String end_time, String venue, String details, String image_url, String image_path) {
        this.id = id;
        this.title = title;
        this.start_date = start_date;
        this.start_time = start_time;
        this.end_date = end_date;
        this.end_time = end_time;
        this.venue = venue;
        this.details = details;
        this.image_url = image_url;
        this.image_path = image_path;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getTitle() {
        return title;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    // KEEP METHODS - put your custom methods here
    public Events(Long id, String title, String image_path) {
        this.id = id;
        this.title = title;
        this.image_path = image_path;
    }
    // KEEP METHODS END

}
