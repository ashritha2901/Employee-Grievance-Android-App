package com.example.grievancesystem;
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
java.util.HashMap; public class
AssignedComplaintsFragment
extends Fragment {
private ListView complaintsListView;
private TextView noComplaintsTextView;
private DatabaseReference complaintsRef;
private ArrayList<Complaint> complaintList;
private ComplaintAdapter complaintAdapter;
private String currentHRUsername;
public AssignedComplaintsFragment() {
// Required empty public constructor
}
@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
// Retrieve the current HR username from shared preferences
currentHRUsername = getActivity().getSharedPreferences("YourPrefs",
getContext().MODE_PRIVATE)
.getString("hrUsername", "Lily"); // Default to "Lily" if not found
Log.d("HRUsername", "Current HR Username: " + currentHRUsername);
}
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
Bundle savedInstanceState) {
View view = inflater.inflate(R.layout.fragment_view_complaints, container,
false);
// Initialize views
complaintsListView = view.findViewById(R.id.complaintsListView);
noComplaintsTextView = view.findViewById(R.id.noComplaintsTextView);
// Initialize the listand adapter
complaintList = new ArrayList<>();
complaintAdapter = new ComplaintAdapter(getActivity(), complaintList);
complaintsListView.setAdapter(complaintAdapter);
// Initialize Firebase Database reference
complaintsRef = FirebaseDatabase.getInstance().getReference("users")
.child("hr").child(currentHRUsername).child("assignedComplaints");
// Fetch complaints from the database
loadAssignedComplaintsFromFirebase();
return view;
}
private void loadAssignedComplaintsFromFirebase() {
complaintsRef.addValueEventListener(new ValueEventListener() {
@Override
public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
complaintList.clear();
if (!dataSnapshot.exists() || !dataSnapshot.hasChildren()) {
Log.d("FirebaseData", "No assigned complaints found for HR: " +
currentHRUsername);
noComplaintsTextView.setVisibility(View.VISIBLE);
complaintsListView.setVisibility(View.GONE); return;
}
for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
// Check if the snapshot value is a HashMap
if (snapshot.getValue() instanceof HashMap) {
// If it's a HashMap, cast and retrieve values
HashMap<String, Object> complaintMap = (HashMap<String,
Object>) snapshot.getValue();
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
// Create a Complaint object and add it to the list
Complaint complaint = new Complaint(id,title, description, status,
submitTo,employeename, complaintType, evidencePath);
complaintList.add(complaint);
} else {
// If it's not a HashMap, it's likely a Complaint object
Complaint complaint = snapshot.getValue(Complaint.class);
if (complaint != null) {
complaintList.add(complaint);
Log.d("FirebaseData", "Complaint added: " +
complaint.getTitle());
} else {Log.w("FirebaseData", "Complaint data is null for snapshot:
" + snapshot.getKey());
}
}
}
// Notify the adapter that the data has changed
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
Toast.makeText(getActivity(), "Failed to load assigned complaints: " +
databaseError.getMessage(), Toast.LENGTH_SHORT).show();
Log.e("FirebaseData", "Error loading assigned complaints: " +
databaseError.getMessage());
}
});
} }
