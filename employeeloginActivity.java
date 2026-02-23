package com.example.grievancesystem;
import android.content.Intent; import
android.os.Bundle; import android.view.View; import
android.widget.ArrayAdapter; import
android.widget.Button; import
android.widget.EditText; import
android.widget.Spinner; import
android.widget.TextView; import
android.widget.Toast; import
android.content.SharedPreferences; import
android.content.Context; import
androidx.annotation.NonNull; import
androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import
com.google.firebase.database.DatabaseReference;
import
com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import java.util.Objects;
public class employeeloginActivity extends AppCompatActivity {
private EditText loginUsername, loginPassword;
private Button loginButton; private TextView
signupRedirectText; private Spinner
roleSpinner;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);
// Initialize UI elements
loginUsername = findViewById(R.id.login_username);
loginPassword = findViewById(R.id.login_password); loginButton
= findViewById(R.id.login_button); signupRedirectText =
findViewById(R.id.signupRedirectText); roleSpinner =
findViewById(R.id.role_spinner);
// Populate the Spinner with roles: Employee, Advocate, HR
ArrayAdapter<CharSequence> adapter =
ArrayAdapter.createFromResource(this,
R.array.role_options, android.R.layout.simple_spinner_item);
adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdow
n_item);
roleSpinner.setAdapter(adapter);
// Handle Login Button Click
loginButton.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
if (!validateUsername() || !validatePassword()) {
return; // Validation failed
} else {
checkUser(); // Proceed to check user credentials
}
}
});
// Redirect to Signup activity when the TextView is clicked
signupRedirectText.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
Intent intent = new Intent(employeeloginActivity.this,
SignupActivity.class);
startActivity(intent);
}
});
}
// Method to validate the Username field
private boolean validateUsername() {
String val = loginUsername.getText().toString().trim();
if (val.isEmpty()) {
loginUsername.setError("Username cannot be empty");
return false;
}
loginUsername.setError(null);
return true;
}
// Method to validate the Password field
private boolean validatePassword() {
String val = loginPassword.getText().toString().trim();
if (val.isEmpty()) {
loginPassword.setError("Password cannot be empty");
return false;
}
loginPassword.setError(null);
return true;
}
// Method to check the user's credentials in the Firebase database
public void checkUser() {
String userUsername = loginUsername.getText().toString().trim();
String userPassword = loginPassword.getText().toString().trim();
String selectedRole = roleSpinner.getSelectedItem().toString();
// Reference to the Firebase database
DatabaseReference reference =
FirebaseDatabase.getInstance().getReference("users");
Query checkUserDatabase =
reference.orderByChild("username").equalTo(userUsername);
checkUserDatabase.addListenerForSingleValueEvent(new
com.google.firebase.database.ValueEventListener() {
@Override
public void onDataChange(@NonNull DataSnapshot snapshot) {
if (snapshot.exists()) {
// Username exists in the database
loginUsername.setError(null);
String passwordFromDB =
snapshot.child(userUsername).child("password").getValue(String.class);
if (Objects.equals(passwordFromDB, userPassword)) {
// Password matches
loginUsername.setError(null);
String roleFromDB =
snapshot.child(userUsername).child("role").getValue(String.class);
if (Objects.equals(roleFromDB, selectedRole)) {
// Role matches
Intent intent; switch
(selectedRole) {
case "Employee":
intent = new Intent(employeeloginActivity.this,
DashboardActivity.class);
break; case "Advocate":
intent = new Intent(employeeloginActivity.this,
AdvocateDashboardActivity.class);
break;
case "HR":
intent = new Intent(employeeloginActivity.this,
HRDashboardActivity.class);
break;
default:
Toast.makeText(employeeloginActivity.this, "Invalid Role
Selected", Toast.LENGTH_SHORT).show();
return;
}
// Store the username in SharedPreferences
SharedPreferences sharedPrefs =
getSharedPreferences("YourPrefs", Context.MODE_PRIVATE);
SharedPreferences.Editor editor = sharedPrefs.edit();
editor.putString("username", userUsername); // Store the username
editor.apply();
// Pass user data to the next activity
String nameFromDB =
snapshot.child(userUsername).child("name").getValue(String.class);
String emailFromDB =
snapshot.child(userUsername).child("email").getValue(String.class);
intent.putExtra("name", nameFromDB);
intent.putExtra("email", emailFromDB);
intent.putExtra("username", userUsername);
startActivity(intent);
} else {
// Role does not match
Toast.makeText(employeeloginActivity.this, "Role does not
match", Toast.LENGTH_SHORT).show();
}
} else {
// Password does not match
loginPassword.setError("Invalid Credentials");
loginPassword.requestFocus();
}
} else {
// Username does not exist
loginUsername.setError("User does not exist");
loginUsername.requestFocus();
}
}
@Override
public void onCancelled(@NonNull DatabaseError error) {
// Handle potential database errors
Toast.makeText(employeeloginActivity.this, "Database error: " +
error.getMessage(), Toast.LENGTH_SHORT).show();
}
});
} }
