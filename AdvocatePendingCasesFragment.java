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
com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList; import
java.util.HashMap;
import java.util.List;
public class AdvocatePendingCasesFragment extends Fragment {
private ListView pendingCasesListView;
private TextView noCasesTextView; private
DatabaseReference complaintsRef; private
ArrayList<Complaint> complaintList; private
ComplaintAdapter complaintAdapter;
private List<String> advocateUsernames;
public AdvocatePendingCasesFragment() {
// Required empty public constructor
}
@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
// Retrieve the list of advocate usernames from shared preferences
SharedPreferences prefs = getActivity().getSharedPreferences("YourPrefs",
getContext().MODE_PRIVATE);
advocateUsernames = new ArrayList<>();
// Example of how you might store multiple usernames as
commaseparated values
String usernames = prefs.getString("advocatesUsernames",
"Arvind,Selena"); // Default example usernames
if (usernames != null) {
for (String username : usernames.split(",")) {
advocateUsernames.add(username.trim());
}
}
Log.d("ADVOCATESUsernames", "Advocate Usernames: " +
advocateUsernames);
}
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
Bundle savedInstanceState) {
View view = inflater.inflate(R.layout.fragment_advocate_pending_cases,
container, false);
// Initialize views
pendingCasesListView = view.findViewById(R.id.pendingCasesListView);
noCasesTextView = view.findViewById(R.id.noCasesTextView);
// Initialize the list and adapter
complaintList = new ArrayList<>();
complaintAdapter = new ComplaintAdapter(getActivity(), complaintList);
pendingCasesListView.setAdapter(complaintAdapter);
// Load complaints for each advocate username
loadAssignedComplaintsFromFirebase();
return view;
}
private void loadAssignedComplaintsFromFirebase() {
for (String advocateUsername : advocateUsernames) {
complaintsRef = FirebaseDatabase.getInstance().getReference("users")
.child("advocates").child(advocateUsername).child("assignedComplaints");
complaintsRef.addValueEventListener(new ValueEventListener() {
@Override
public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
if (!dataSnapshot.exists()) {
Log.d("FirebaseData", "No assigned complaints found for
ADVOCATE: " + advocateUsername);
}
for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
// Retrieve complaint data
HashMap<String, Object> complaintMap = (HashMap<String,
Object>) snapshot.getValue();
if (complaintMap != null) {
String id = (String) complaintMap.get("id");
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
}
complaintAdapter.notifyDataSetChanged();
if (complaintList.isEmpty()) {
noCasesTextView.setVisibility(View.VISIBLE);
pendingCasesListView.setVisibility(View.GONE);
} else {
noCasesTextView.setVisibility(View.GONE);
pendingCasesListView.setVisibility(View.VISIBLE);
}
}
@Override
public void onCancelled(@NonNull DatabaseError databaseError) {
Toast.makeText(getActivity(), "Failed to load assigned complaints: "
+ databaseError.getMessage(), Toast.LENGTH_SHORT).show();
Log.e("FirebaseData", "Error loading assigned complaints: " +
databaseError.getMessage());
}
});
}
}
}
