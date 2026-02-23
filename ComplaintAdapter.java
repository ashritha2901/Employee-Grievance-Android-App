package com.example.grievancesystem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View; import
android.view.ViewGroup; import
android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List; // Ensure this import is present
public class ComplaintAdapter extends ArrayAdapter<Complaint> {
private Context context;
private List<Complaint> complaints;
public ComplaintAdapter(Context context, List<Complaint> complaints) {
super(context, 0, complaints); this.context = context;
this.complaints = complaints;
}
@Override
public View getView(int position, View convertView, ViewGroup parent) {
if (convertView == null) { convertView =
LayoutInflater.from(context).inflate(R.layout.complaint_item, parent, false);
}
Complaint complaint = getItem(position);
if (complaint != null) {
TextView titleTextView = convertView.findViewById(R.id.complaintTitle);
TextView idTextView = convertView.findViewById(R.id.complaintId);
TextView statusTextView =
convertView.findViewById(R.id.complaintStatusTextView);
TextView descriptionTextView =
convertView.findViewById(R.id.complaintDescriptionTextView);
TextView submitToTextView =
convertView.findViewById(R.id.complaintSubmitToTextView);
TextView FromTextView=
convertView.findViewById(R.id.complaintFromTextView);
titleTextView.setText(complaint.getTitle());
idTextView.setText("ID: " + complaint.getId());
statusTextView.setText("Status: " + complaint.getStatus());
descriptionTextView.setText("Description: " +
complaint.getDescription());
submitToTextView.setText("Submitted To: " + complaint.getSubmitTo());
FromTextView.setText("employee name: " +
complaint.getEmployeename());
}
return convertView;
} }
