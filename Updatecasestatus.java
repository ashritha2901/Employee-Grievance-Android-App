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
public class updatecasestatus extends Fragment {
private ListView casesListView; private
ArrayList<Complaint> caseList; private
ComplaintAdapter complaintAdapter; private
List<String> advocateUsernames; private
Spinner employeeSpinner; private String
selectedEmployeeUsername;
private TextView nocasesTextView;
public updatecasestatus() {
// Required empty public constructor
}
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
Bundle savedInstanceState) {
View view = inflater.inflate(R.layout.fragment_update_case_status,
container, false);
casesListView = view.findViewById(R.id.casesListView);
employeeSpinner = view.findViewById(R.id.employeeSpinner);
nocasesTextView = view.findViewById(R.id.nocasesTextView); caseList =
new ArrayList<>();
complaintAdapter = new ComplaintAdapter(getActivity(), caseList);
casesListView.setAdapter(complaintAdapter);
// Retrieve advocate usernames from SharedPreferences
SharedPreferences prefs = getActivity().getSharedPreferences("YourPrefs",
getContext().MODE_PRIVATE);
advocateUsernames = new ArrayList<>();
String usernames = prefs.getString("advocatesUsernames",
"Arvind,Selena"); if
(usernames != null) {
for (String username : usernames.split(",")) {
advocateUsernames.add(username.trim());
}
}
Log.d("ADVOCATESUsernames", "Advocate Usernames: " +
advocateUsernames);
// Set up employee spinner
String[] employeeUsernames = {"Ashu", "Harini"}; // Replace with actual
usernames
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
casesListView.setOnItemClickListener((parent, view1, position, id) -> {
Complaint complaint = caseList.get(position);
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
for (String advocateUsername : advocateUsernames) {
DatabaseReference advocateComplaintRef =
FirebaseDatabase.getInstance().getReference("users")
.child("advocates").child(advocateUsername).child("assignedComplaints").child
(complaintId);
advocateComplaintRef.child("status").setValue(newStatus)
.addOnSuccessListener(aVoid -> Log.d("UpdateCaseStatus", "Status
updated for advocate: " + advocateUsername))
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
complaintAdapter.notifyDataSetChanged(); })
.addOnFailureListener(e -> {
Toast.makeText(getActivity(), "Failed to update status for employee",
Toast.LENGTH_SHORT).show();
Log.e("UpdateCaseStatus", "Failed to update employee status", e);
});
}
private void loadAssignedComplaintsFromFirebase() {
caseList.clear();
for (String advocateUsername : advocateUsernames) {
DatabaseReference complaintsRef =
FirebaseDatabase.getInstance().getReference("users")
.child("advocates").child(advocateUsername).child("assignedComplaints");
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
Complaint complaint = new Complaint(id, title, description, status,
submitTo,employeename, complaintType, evidencePath);
caseList.add(complaint);
}
complaintAdapter.notifyDataSetChanged();
if (caseList.isEmpty()) {
nocasesTextView.setVisibility(View.VISIBLE);
casesListView.setVisibility(View.GONE);
} else {
nocasesTextView.setVisibility(View.GONE);
casesListView.setVisibility(View.VISIBLE);
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
