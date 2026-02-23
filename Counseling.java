package com.example.grievancesystem;
public class Counseling {
private String name;
private String date; private
String time; private String
meetingType;
private String selectedHR; // Added HR field
public Counseling() {
// Default constructor required for calls to
DataSnapshot.getValue(Counseling.class)
}
public Counseling(String name, String date, String time, String meetingType,
String selectedHR) {
this.name = name;
this.date = date;
this.time = time;
this.meetingType = meetingType;
this.selectedHR = selectedHR;
}
// Getters and setters for all fields
public String getName() {
return name;
}
public void setName(String name) {
this.name = name;
}
public String getDate() {
return date;
}
public void setDate(String date) {
this.date = date;
}
public String getTime() {
return time;
}
public void setTime(String time) {
this.time = time;
}
public String getMeetingType() {
return meetingType;
}
public void setMeetingType(String meetingType) { this.meetingType =
meetingType;
}
public String getSelectedHR() {
return selectedHR;
}
public void setSelectedHR(String selectedHR) {
this.selectedHR = selectedHR;
} }
