package com.mlkk.simulator.entities;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public
class Event {
    private int mId;
    private String mTitle;
    private LocalDateTime mDate;
    private String mDescription;
    private Address mAdress;
    private int mAmountAttendes;
    private boolean mDateVisible;
    private boolean mAttendeesVisible;

    private String mContactEmail;


    public Event(int id, String title, String date, String description, Address address, int amountAttendees, String contactEmail,boolean mAttendeesVisible, boolean dateVisible) {
        this.mId = id;
        this.mTitle = title;
        this.mDate = LocalDateTime.parse(date);
        this.mDescription = description;
        this.mAttendeesVisible = mAttendeesVisible;
        this.mAdress = address;
        this.mAmountAttendes = amountAttendees;
        this.mDateVisible = dateVisible;
        this.mContactEmail = contactEmail;
    }

    public String toJson() {
        return
            "{\n" +
                "\t\t\t\"event_id\": " + mId + ",\n" +
                "\t\t\t\"title\": \"" + mTitle + "\",\n" +
                "\t\t\t\"description\": \"" + mDescription + "\",\n" +
                "\t\t\t\"date\": \"" + mDate + "\",\n" +
                "\t\t\t\"date_visible\": " + mDateVisible + ",\n" +
                "\t\t\t\"attendees_visible\": " + mAttendeesVisible + ",\n" +
                "\t\t\t\"address\": " + (mAdress != null ? mAdress.toJson() : null) + ",\n" +
                "\t\t\t\"amount_attendees\": " + mAmountAttendes + ",\n" +
                "\t\t\t\"contact_email\": " + mContactEmail + "\n" +
            "\t\t}";
    }
}


