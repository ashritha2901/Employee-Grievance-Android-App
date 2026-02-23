package com.example.grievancesystem;
import android.content.SharedPreferences;
import android.os.Bundle; import
android.util.Log; import
android.view.LayoutInflater; import
android.view.View; import
android.view.ViewGroup; import
android.widget.ListView; import
android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DataSnapshot; import
com.google.firebase.database.DatabaseError; import
com.google.firebase.database.DatabaseReference; import
com.google.firebase.database.FirebaseDatabase; import
com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
public class ViewStatusFragment extends Fragment {
private ListView complaintsListView; private
ArrayList<Complaint> complaintList; private
ComplaintAdapter complaintAdapter; private
ArrayList<String> hrUsernames; private
ArrayList<String> advocateUsernames; private
TextView noComplaintsTextView;
public ViewStatusFragment() {
// Required empty public constructor
}
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
Bundle savedInstanceState) {
View view = inflater.inflate(R.layout.fragment_view_status, container,
false);
// Initialize views
complaintsListView = view.findViewById(R.id.complaintsListView);
noComplaintsTextView = view.findViewById(R.id.noComplaintsTextView);
// Initialize complaint list and adapter
complaintList = new ArrayList<>(); complaintAdapter
= new ComplaintAdapter(getActivity(), complaintList);
complaintsListView.setAdapter(complaintAdapter);
// Load usernames from SharedPreferences
SharedPreferences prefs = getActivity().getSharedPreferences("YourPrefs",
getContext().MODE_PRIVATE); hrUsernames = new ArrayList<>();
advocateUsernames = new ArrayList<>();
String hrList = prefs.getString("hrUsernames", "Lily,Janani"); // Default HR
usernames
String advocateList = prefs.getString("advocateUsernames",
"Arvind,Selena"); // Default advocate usernames
if (hrList != null) {
for (String username : hrList.split(",")) {
hrUsernames.add(username.trim());
}
}
if (advocateList != null) {
for (String username : advocateList.split(",")) {
advocateUsernames.add(username.trim());
}
}
// Load complaints from Firebase
loadAssignedComplaintsFromFirebase();
return view;
}
private void loadAssignedComplaintsFromFirebase() {
complaintList.clear();
// Fetch complaints assigned to HR
for (String hrUsername : hrUsernames) {
DatabaseReference hrComplaintsRef =
FirebaseDatabase.getInstance().getReference("users")
.child("hr").child(hrUsername).child("assignedComplaints");
fetchComplaintsFromDatabase(hrComplaintsRef);
}
// Fetch complaints assigned to Advocates
for (String advocateUsername : advocateUsernames) {
DatabaseReference advocateComplaintsRef =
FirebaseDatabase.getInstance().getReference("users")
.child("advocates").child(advocateUsername).child("assignedComplaints");
fetchComplaintsFromDatabase(advocateComplaintsRef);
}
}
private void fetchComplaintsFromDatabase(DatabaseReference
complaintsRef) {
complaintsRef.addValueEventListener(new ValueEventListener() {
@Override
public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
HashMap<String, Object> complaintMap = (HashMap<String,
Object>) snapshot.getValue();
if (complaintMap != null) {
String id = snapshot.getKey();
String title = (String) complaintMap.get("title");
String description = (String) complaintMap.get("description");
String status = (String) complaintMap.get("status");
String submitTo = (String) complaintMap.get("submitTo");
String employeename = (String)
complaintMap.get("employeename");
String complaintType = (String)
complaintMap.get("complaintType");
String evidencePath = (String) complaintMap.get("evidencePath");
Complaint complaint = new Complaint(id, title, description,
status, submitTo,employeename, complaintType, evidencePath);
complaintList.add(complaint);
}
}
complaintAdapter.notifyDataSetChanged();
// Show/hide views based on complaint list
if (complaintList.isEmpty()) {
noComplaintsTextView.setVisibility(View.VISIBLE);
complaintsListView.setVisibility(View.GONE);
} else {
noComplaintsTextView.setVisibility(View.GONE);
complaintsListView.setVisibility(View.VISIBLE);
}
}
@Override
public void onCancelled(@NonNull DatabaseError databaseError) {
Toast.makeText(getActivity(), "Failed to load complaints: " +
databaseError.getMessage(), Toast.LENGTH_SHORT).show();
Log.e("LoadComplaints", "Failed to load complaints",
databaseError.toException());
}
});
} }
