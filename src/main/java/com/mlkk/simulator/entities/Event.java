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


    public Event(int id, String title, String date, String description, Address address, int amountAttendees, boolean mAttendeesVisible, boolean dateVisible) {
        this.mId = id;
        this.mTitle = title;
        this.mDate = LocalDateTime.parse(date);
        this.mDescription = description;
        this.mAttendeesVisible = mAttendeesVisible;
        this.mAdress = address;
        this.mAmountAttendes = amountAttendees;
        this.mDateVisible = dateVisible;
    }

    public String toJson() {
        return
            "{\n" +
                "\t\"event_id\": " + mId + ",\n" +
                "\t\"title\": \"" + mTitle + "\",\n" +
                "\t\"description\": \"" + mDescription + "\",\n" +
                "\t\"date\": \"" + mDate + "\",\n" +
                "\t\"date_visible\": " + mDateVisible + ",\n" +
                "\t\"attendees_visible\": " + mAttendeesVisible + ",\n" +
                "\t\"address\": " + (mAdress != null ? mAdress.toJson() : null) + ",\n" +
                "\t\"amount_attendees\": " + mAmountAttendes + "\n" +
            "}";
    }
}


