package com.example.grievancesystem;
import android.Manifest; import android.content.Intent;
import android.content.pm.PackageManager; import
android.database.Cursor; import android.net.Uri; import
android.os.Bundle; import android.provider.MediaStore;
import android.view.LayoutInflater; import
android.view.View; import android.view.ViewGroup;
import android.widget.*; import
androidx.annotation.NonNull; import
androidx.annotation.Nullable; import
androidx.core.app.ActivityCompat; import
androidx.core.content.ContextCompat; import
androidx.fragment.app.Fragment; import
com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class FileComplaintFragment extends Fragment { private EditText
editTextComplaintID,editTextComplaintTitle,
editTextComplaintDescription,editTextemployeename; private Button
buttonAddEvidence, buttonSubmitComplaint,
buttonViewComplaint, buttonHome;
private Spinner spinnerSubmitTo;
private CheckBox checkBoxType1, checkBoxType2, checkBoxType3,
checkBoxType4;
private String evidencePath = null, complaintDetails = null, currentUsername;
private FirebaseDatabase database;
private DatabaseReference complaintsRef, userRef;
private static final int REQUEST_CODE_PICK_FILE = 1;
private static final int PERMISSION_REQUEST_CODE = 100;
@Nullable
@Override
public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
ViewGroup container, @Nullable Bundle savedInstanceState) { try {
View view = inflater.inflate(R.layout.fragment_file_complaint, container,
false);
// Initialize Firebase references
database = FirebaseDatabase.getInstance();
if (getArguments() != null) {
currentUsername = getArguments().getString("username");
} else {
Toast.makeText(getActivity(), "Username not provided",
Toast.LENGTH_SHORT).show();
currentUsername = "defaultUsername"; // Fallback if username not
passed
}
userRef =
database.getReference("users").child("employees").child(currentUsername);
complaintsRef = userRef.child("complaints");
// Initialize views
editTextComplaintID = view.findViewById(R.id.editTextComplaintID);
editTextemployeename =
view.findViewById(R.id.editTextemployeename);
editTextComplaintTitle = view.findViewById(R.id.editTextComplaintTitle);
editTextComplaintDescription =
view.findViewById(R.id.editTextComplaintDescription);
buttonAddEvidence = view.findViewById(R.id.buttonAddEvidence);
buttonSubmitComplaint =
view.findViewById(R.id.buttonSubmitComplaint);
buttonViewComplaint = view.findViewById(R.id.buttonViewComplaint);
buttonHome = view.findViewById(R.id.buttonHome); spinnerSubmitTo
= view.findViewById(R.id.spinnerSubmitTo); checkBoxType1 =
view.findViewById(R.id.checkBoxType1); checkBoxType2 =
view.findViewById(R.id.checkBoxType2); checkBoxType3 =
view.findViewById(R.id.checkBoxType3); checkBoxType4 =
view.findViewById(R.id.checkBoxType4);
// Set up spinner for "Submit To" with HR and Advocate usernames
ArrayAdapter<CharSequence> adapter =
ArrayAdapter.createFromResource(getActivity(),
R.array.submit_to_options, android.R.layout.simple_spinner_item);
adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdow
n_item);
spinnerSubmitTo.setAdapter(adapter);
// Submit complaint
buttonSubmitComplaint.setOnClickListener(v -> {
String id = editTextComplaintID.getText().toString();
String title = editTextComplaintTitle.getText().toString();
String description = editTextComplaintDescription.getText().toString();
String employeename = editTextemployeename.getText().toString();
String submitTo = spinnerSubmitTo.getSelectedItem().toString();
StringBuilder complaintType = new StringBuilder();
if (checkBoxType1.isChecked()) complaintType.append("Workplace
Harassment ");
if (checkBoxType2.isChecked()) complaintType.append("Salary Issues
");
if (checkBoxType3.isChecked()) complaintType.append("Discrimination
");
if (checkBoxType4.isChecked()) complaintType.append("Other");
if (title.isEmpty() || description.isEmpty()) {
Toast.makeText(getActivity(), "Please fill in all fields",
Toast.LENGTH_SHORT).show();
} else {
// Create a unique ID for each complaint
String complaintID = complaintsRef.push().getKey();
// Create complaint object
Complaint newComplaint = new Complaint(id,title, description,
"pending", submitTo,employeename, complaintType.toString().trim(),
evidencePath);
// Save complaint under the employee's node
complaintsRef.child(complaintID).setValue(newComplaint);
// Extract the selected HR or Advocate username
String[] submitToParts = submitTo.split(" - ");
String role = submitToParts[0]; // Either "HR" or "Advocate"
String username = submitToParts[1]; // The selected username
// Assign the complaint to the selected HR or Advocate
if (role.equals("HR")) { DatabaseReference hrRef =
database.getReference("users").child("hr").child(username).child("assignedCo
mplaints");
hrRef.child(complaintID).setValue(newComplaint);
} else if (role.equals("Advocate")) {
DatabaseReference advocateRef =
database.getReference("users").child("advocates").child(username).child("assig
nedComplaints");
advocateRef.child(complaintID).setValue(newComplaint);
}
Toast.makeText(getActivity(), "Complaint Submitted to " + submitTo,
Toast.LENGTH_SHORT).show();
complaintDetails = "Title: " + newComplaint.getTitle() +
"\nDescription: " + newComplaint.getDescription() + "\nType: " +
complaintType.toString();
}
});
// View complaint details
buttonViewComplaint.setOnClickListener(v -> {
if (complaintDetails != null) {
Intent intent = new Intent(getActivity(),
ViewComplaintActivity.class);
intent.putExtra("complaintDetails", complaintDetails);
startActivity(intent);
} else {
Toast.makeText(getActivity(), "No complaint submitted",
Toast.LENGTH_SHORT).show();
}
});
// Add evidence
buttonAddEvidence.setOnClickListener(v -> {
if (checkPermission()) {
pickFile();
} else {
requestPermission();
}
});
// Go back to Home
buttonHome.setOnClickListener(v -> {
Intent intent = new Intent(getActivity(), DashboardActivity.class);
startActivity(intent);
});
return view; }
catch (Exception e) {
e.printStackTrace();
Toast.makeText(getActivity(), "An error occurred",
Toast.LENGTH_SHORT).show();
return null;
}
}
private boolean checkPermission() {
int result = ContextCompat.checkSelfPermission(getActivity(),
Manifest.permission.READ_EXTERNAL_STORAGE);
return result == PackageManager.PERMISSION_GRANTED;
}
private void requestPermission() {
if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
Manifest.permission.READ_EXTERNAL_STORAGE)) {
Toast.makeText(getActivity(), "Storage permission is required to pick
evidence", Toast.LENGTH_LONG).show();
} else {
ActivityCompat.requestPermissions(getActivity(), new
String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
PERMISSION_REQUEST_CODE);
}
}
private void pickFile() {
Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
intent.setType("*/*");
startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
}
@Override
public void onActivityResult(int requestCode, int resultCode, @Nullable
Intent data) {
super.onActivityResult(requestCode, resultCode, data);
if (requestCode == REQUEST_CODE_PICK_FILE && resultCode ==
getActivity().RESULT_OK && data != null) {
Uri selectedFileUri = data.getData();
if (selectedFileUri != null) {
evidencePath = getRealPathFromURI(selectedFileUri);
Toast.makeText(getActivity(), "Evidence added: " + evidencePath,
Toast.LENGTH_SHORT).show();
}
}
}
private String getRealPathFromURI(Uri contentUri) {
String[] proj = {MediaStore.Images.Media.DATA};
try (Cursor cursor = getActivity().getContentResolver().query(contentUri,
proj, null, null, null)) {
if (cursor != null && cursor.moveToFirst()) {
int column_index =
cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
return cursor.getString(column_index);
}
} catch (Exception e) {
e.printStackTrace();
}
return null;
}
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[]
permissions, @NonNull int[] grantResults) {
super.onRequestPermissionsResult(requestCode, permissions, grantResults);
if (requestCode == PERMISSION_REQUEST_CODE) {
if (grantResults.length > 0 && grantResults[0] ==
PackageManager.PERMISSION_GRANTED) {
pickFile();
} else {
Toast.makeText(getActivity(), "Permission Denied",
Toast.LENGTH_SHORT).show();
}
}
} }
