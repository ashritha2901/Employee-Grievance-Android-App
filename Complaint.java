package com.example.grievancesystem;
import java.util.UUID; public
class Complaint { private
String id; private String title;
private String description;
private String status; private
String submitTo; private String
employeename; private String
complaintType;
private String evidencePath;
;
public Complaint() {
// Leave empty - Firebase will use this to create instances
}
// Constructor
public Complaint(String id,String title, String description, String status, String
submitTo,String employeename, String complaintType, String evidencePath) {
this.id = id; // Generate a unique ID for each complaint this.title = title;
this.description = description;
this.status = status; this.submitTo
= submitTo; this.employeename =
employeename; this.complaintType =
complaintType;
this.evidencePath = evidencePath;
}
// Getters public
String getId() {
return id;
}
public String getTitle() {
return title;
}
public String getDescription() {
return description;
}
public String getStatus() {
return status;
}
public String getSubmitTo() { // This is the new getter
return submitTo;
}
public String getEmployeename() {
return employeename;
}
public String getComplaintType() {
return complaintType;
}
public String getEvidencePath() {
return evidencePath;
}
// Setters
public void setId(String id) {
this.id = id;
}
public void setTitle(String title) {
this.title = title;
}
public void setDescription(String description) {
this.description = description;
}
public void setStatus(String status) {
this.status = status;
}
public void setSubmitTo(String submitTo) {
this.submitTo = submitTo;
}
public void setEmployeename(String employeename) {
this.submitTo = employeename;
}
public void setComplaintType(String complaintType) {
this.complaintType = complaintType;
}
public void setEvidencePath(String evidencePath) {
this.evidencePath = evidencePath;
}
}
