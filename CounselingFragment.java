package com.example.grievancesystem;
import android.app.AlertDialog; import
android.content.SharedPreferences; import
android.os.Bundle; import
androidx.fragment.app.Fragment; import
android.view.LayoutInflater; import
android.view.View; import
android.view.ViewGroup; import
android.widget.ArrayAdapter; import
android.widget.Button; import
android.widget.EditText; import
android.widget.RadioButton; import
android.widget.RadioGroup; import
android.widget.Spinner; import
android.widget.Toast;
import com.google.firebase.database.DatabaseReference; import
com.google.firebase.database.FirebaseDatabase;
public class CounselingFragment extends Fragment {
private EditText editTextName, editTextDate, editTextTime;
private RadioGroup radioGroupMeetingType;
private Spinner spinnerHR; private
Button buttonSubmitMeeting;
private DatabaseReference counselingRef;
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
Bundle savedInstanceState) {
View view = inflater.inflate(R.layout.fragment_counseling, container, false);
editTextName = view.findViewById(R.id.editTextName);
editTextDate = view.findViewById(R.id.editTextDate); editTextTime
= view.findViewById(R.id.editTextTime); radioGroupMeetingType
=
view.findViewById(R.id.radioGroupMeetingType);
spinnerHR = view.findViewById(R.id.spinnerHR);
buttonSubmitMeeting = view.findViewById(R.id.buttonSubmitMeeting);
counselingRef =
FirebaseDatabase.getInstance().getReference("counseling");
// Add two HR options to the Spinner
String[] hrOptions = {"Lily", "HR2"};
ArrayAdapter<String> hrAdapter = new ArrayAdapter<>(getContext(),
android.R.layout.simple_spinner_item, hrOptions);
hrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropd
own_item);
spinnerHR.setAdapter(hrAdapter);
buttonSubmitMeeting.setOnClickListener(v -> {
submitAppointment();
});
return view;
}
private void submitAppointment() {
String name = editTextName.getText().toString();
String date = editTextDate.getText().toString();
String time = editTextTime.getText().toString(); int
selectedMeetingTypeId =
radioGroupMeetingType.getCheckedRadioButtonId();
RadioButton selectedMeetingType =
getView().findViewById(selectedMeetingTypeId);
String meetingType = selectedMeetingType != null ?
selectedMeetingType.getText().toString() : "";
String selectedHR = spinnerHR.getSelectedItem().toString();
if (name.isEmpty() || date.isEmpty() || time.isEmpty() ||
meetingType.isEmpty() || selectedHR.isEmpty()) {
Toast.makeText(getActivity(), "Please fill in all fields",
Toast.LENGTH_SHORT).show();
} else {
// Store the appointment under the selected HR's ID in Firebase
Counseling counseling = new Counseling(name, date, time, meetingType,
selectedHR);
counselingRef.child(selectedHR).push().setValue(counseling)
.addOnSuccessListener(aVoid -> {
new AlertDialog.Builder(getActivity())
.setTitle("Meeting Appointment")
.setMessage("Meeting appointment booked with " +
selectedHR + " successfully!")
.setPositiveButton("OK", (dialog, which) -> {
editTextName.setText("");
editTextDate.setText("");
editTextTime.setText("");
radioGroupMeetingType.clearCheck();
})
.show();
})
.addOnFailureListener(e -> {
Toast.makeText(getActivity(), "Failed to book counseling: " +
e.getMessage(), Toast.LENGTH_SHORT).show();
});
SharedPreferences sharedPreferences =
getActivity().getSharedPreferences("CounselingPrefs",
getActivity().MODE_PRIVATE);
SharedPreferences.Editor editor = sharedPreferences.edit();
editor.putString("counselingName", name);
editor.putString("counselingDate", date);
editor.putString("counselingTime", time);
editor.putString("counselingMeetingType", meetingType);
editor.putString("selectedHR", selectedHR); editor.apply();
}
} }
