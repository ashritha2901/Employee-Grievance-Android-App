package com.example.grievancesystem;
import android.app.AlertDialog; import
android.content.SharedPreferences; import
android.os.Bundle; import android.util.Log;
import android.view.LayoutInflater; import
android.view.View; import
android.view.ViewGroup; import
android.widget.AdapterView; import
android.widget.ArrayAdapter; import
android.widget.ListView; import
android.widget.Spinner; import
android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DataSnapshot; import
com.google.firebase.database.DatabaseError; import
com.google.firebase.database.DatabaseReference; import
com.google.firebase.database.FirebaseDatabase; import
com.google.firebase.database.ValueEventListener;
import java.util.ArrayList; import
java.util.HashMap;
import java.util.List;
public class UpdateComplaintStatusFragment extends Fragment {
private ListView complaintListView; private
ArrayList<Complaint> complaintList; private
ComplaintAdapter complaintAdapter; private
List<String> hrUsernames; private Spinner
employeeSpinner; private String
selectedEmployeeUsername;
private TextView nocomplaintsTextView;
public UpdateComplaintStatusFragment() {
// Required empty public constructor
}
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
Bundle savedInstanceState) {
View view = inflater.inflate(R.layout.fragment_update_complaint_status,
container, false);
complaintListView = view.findViewById(R.id.complaintsListView);
employeeSpinner = view.findViewById(R.id.employeeSpinner);
nocomplaintsTextView = view.findViewById(R.id.noComplaintsTextView);
complaintList = new ArrayList<>();
complaintAdapter = new ComplaintAdapter(getActivity(), complaintList);
complaintListView.setAdapter(complaintAdapter);
// Retrieve advocate usernames from SharedPreferences
SharedPreferences prefs = getActivity().getSharedPreferences("YourPrefs",
getContext().MODE_PRIVATE); hrUsernames = new ArrayList<>();
String usernames = prefs.getString("hrUsernames", "Lily,Janani");
if (usernames != null) {
for (String username : usernames.split(",")) {
hrUsernames.add(username.trim());
}
}
Log.d("HRUsernames", "Hr Usernames: " + hrUsernames);
// Set up employee spinner
String[] employeeUsernames = {"Ashu", "Harini"};
ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(),
android.R.layout.simple_spinner_item, employeeUsernames);
spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_d
ropdown_item);
employeeSpinner.setAdapter(spinnerAdapter);
employeeSpinner.setOnItemSelectedListener(new
AdapterView.OnItemSelectedListener() {
@Override
public void onItemSelected(AdapterView<?> parent, View view, int
position, long id) {
selectedEmployeeUsername =
parent.getItemAtPosition(position).toString();
loadAssignedComplaintsFromFirebase(); // Reload complaints
}
@Override
public void onNothingSelected(AdapterView<?> parent) {
// Handle no selection
}
});
complaintListView.setOnItemClickListener((parent, view1, position, id) -> {
Complaint complaint = complaintList.get(position);
showStatusUpdateDialog(complaint);
});
return view;
}
private void showStatusUpdateDialog(final Complaint complaint) {
AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
builder.setTitle("Update Complaint Status");
final Spinner statusSpinner = new Spinner(getActivity());
ArrayAdapter<CharSequence> adapter =
ArrayAdapter.createFromResource(getActivity(),
R.array.complaint_status_array,
android.R.layout.simple_spinner_item);
adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdow
n_item);
statusSpinner.setAdapter(adapter);
int currentStatusIndex = adapter.getPosition(complaint.getStatus());
statusSpinner.setSelection(currentStatusIndex);
builder.setView(statusSpinner);
builder.setPositiveButton("Update", (dialog, which) -> {
String newStatus = statusSpinner.getSelectedItem().toString();
updateComplaintStatusInFirebase(complaint, newStatus);
});
builder.setNegativeButton("Cancel", null);
builder.show();
}
private void updateComplaintStatusInFirebase(Complaint complaint, String
newStatus) {
String complaintId = complaint.getId();
// Update in advocate node
for (String hrUsername : hrUsernames) {
DatabaseReference advocateComplaintRef =
FirebaseDatabase.getInstance().getReference("users")
.child("hr").child(hrUsername).child("assignedComplaints").child(complaintId);
advocateComplaintRef.child("status").setValue(newStatus)
.addOnSuccessListener(aVoid -> Log.d("UpdateCaseStatus", "Status
updated for advocate: " + hrUsername))
.addOnFailureListener(e -> Log.e("UpdateCaseStatus", "Failed to
update advocate status", e));
}
// Update in employee node
DatabaseReference employeeComplaintRef =
FirebaseDatabase.getInstance().getReference("users")
.child("employees").child(selectedEmployeeUsername).child("complaints").chil
d(complaintId);
employeeComplaintRef.child("status").setValue(newStatus)
.addOnSuccessListener(aVoid -> {
Toast.makeText(getActivity(), "Status updated to: " + newStatus,
Toast.LENGTH_SHORT).show();
complaint.setStatus(newStatus);
complaintAdapter.notifyDataSetChanged();
})
.addOnFailureListener(e -> {
Toast.makeText(getActivity(), "Failed to update status for employee",
Toast.LENGTH_SHORT).show();
Log.e("UpdateCaseStatus", "Failed to update employee status", e);
});
}
private void loadAssignedComplaintsFromFirebase() {
complaintList.clear();
for (String hrUsername : hrUsernames) {
DatabaseReference complaintsRef =
FirebaseDatabase.getInstance().getReference("users")
.child("hr").child(hrUsername).child("assignedComplaints");
complaintsRef.addValueEventListener(new ValueEventListener() {
@Override
public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
HashMap<String, Object> complaintMap = (HashMap<String,
Object>) snapshot.getValue();
String id = snapshot.getKey();
String title = (String) complaintMap.get("title");
String description = (String) complaintMap.get("description");
String status = (String) complaintMap.get("status");
String submitTo = (String) complaintMap.get("submitTo");
String employeename = (String)
complaintMap.get("employeename");
String complaintType = (String)
complaintMap.get("complaintType");
String evidencePath = (String)
complaintMap.get("evidencePath");
Complaint complaint = new Complaint(id, title, description,
status, submitTo,employeename, complaintType, evidencePath);
complaintList.add(complaint);
}
complaintAdapter.notifyDataSetChanged();
if (complaintList.isEmpty()) {
nocomplaintsTextView.setVisibility(View.VISIBLE);
complaintListView.setVisibility(View.GONE);
} else {
nocomplaintsTextView.setVisibility(View.GONE);
complaintListView.setVisibility(View.VISIBLE);
}
}
@Override
public void onCancelled(@NonNull DatabaseError databaseError) {
Toast.makeText(getActivity(), "Failed to load cases",
Toast.LENGTH_SHORT).show();
Log.e("LoadCases", "Error loading cases",
databaseError.toException());
}
});
}
} }
