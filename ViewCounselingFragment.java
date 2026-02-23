package com.example.grievancesystem;
import android.content.SharedPreferences;
import android.os.Bundle; import
androidx.fragment.app.Fragment; import
android.view.LayoutInflater; import
android.view.View; import
android.view.ViewGroup; import
android.widget.ArrayAdapter; import
android.widget.ListView; import
android.widget.Toast;
import com.google.firebase.database.DataSnapshot; import
com.google.firebase.database.DatabaseError; import
com.google.firebase.database.DatabaseReference; import
com.google.firebase.database.FirebaseDatabase; import
com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
public class ViewCounselingFragment extends Fragment {
private ListView listViewAppointments;
private ArrayAdapter<String> adapter;
private List<String> appointmentList;
private DatabaseReference counselingRef;
private String hrUsername; // HR username, fetched dynamically from
SharedPreferences
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
Bundle savedInstanceState) {
View view = inflater.inflate(R.layout.fragment_view_counseling, container,
false);
// Initialize UI elements
listViewAppointments = view.findViewById(R.id.listViewCounseling);
appointmentList = new ArrayList<>();
adapter = new ArrayAdapter<>(getContext(),
android.R.layout.simple_list_item_1, appointmentList);
listViewAppointments.setAdapter(adapter);
// Initialize Firebase Database reference
counselingRef =
FirebaseDatabase.getInstance().getReference("counseling");
// Fetch HR username from SharedPreferences (you can replace this with
your authentication logic)
SharedPreferences sharedPreferences =
getContext().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
hrUsername = sharedPreferences.getString("loggedInHR", "Lily"); // Default
value set to "Lily"
// Fetch appointments for the logged-in HR
fetchAppointmentsForHR();
return view;
}
private void fetchAppointmentsForHR() {
counselingRef.child(hrUsername).addValueEventListener(new
ValueEventListener() {
@Override
public void onDataChange(DataSnapshot dataSnapshot) {
appointmentList.clear();
for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
Counseling counseling = snapshot.getValue(Counseling.class); if
(counseling != null) {
String appointmentInfo = "Name: " + counseling.getName() +
"\nDate: " + counseling.getDate() + "\nTime: " + counseling.getTime() +
"\nType: " + counseling.getMeetingType();
appointmentList.add(appointmentInfo);
}
}
adapter.notifyDataSetChanged();
}
@Override
public void onCancelled(DatabaseError databaseError) {
Toast.makeText(getActivity(), "Failed to load appointments: " +
databaseError.getMessage(), Toast.LENGTH_SHORT).show();
}
});
} }
