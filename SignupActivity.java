package com.example.grievancesystem;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle; import
android.view.View; import
android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase; public class
SignupActivity extends AppCompatActivity {
EditText signupName, signupUsername, signupEmail, signupPassword;
TextView loginRedirectText;
Button signupButton;
FirebaseDatabase database;
DatabaseReference reference;
Spinner roleSpinner;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_signup);
signupName = findViewById(R.id.signup_name);
signupEmail = findViewById(R.id.signup_email);
signupUsername = findViewById(R.id.signup_username);
signupPassword = findViewById(R.id.signup_password);
loginRedirectText = findViewById(R.id.loginRedirectText);
signupButton = findViewById(R.id.signup_button);
roleSpinner = findViewById(R.id.role_spinner);
// Populate the Spinner with roles: Employee, Advocate, HR
ArrayAdapter<CharSequence> adapter =
ArrayAdapter.createFromResource(this,
R.array.role_options, android.R.layout.simple_spinner_item);
adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdow
n_item);
roleSpinner.setAdapter(adapter);
signupButton.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
database = FirebaseDatabase.getInstance();
reference = database.getReference("users"); String
name = signupName.getText().toString();
String email = signupEmail.getText().toString();
String username = signupUsername.getText().toString();
String password = signupPassword.getText().toString();
String role = roleSpinner.getSelectedItem().toString(); // Get the
selected role
// Create an instance of HelperClass including the role
HelperClass helperClass = new HelperClass(name, email, username,
password, role);
reference.child(username).setValue(helperClass);
Toast.makeText(SignupActivity.this, "You have signed up successfully!",
Toast.LENGTH_SHORT).show();
Intent intent = new Intent(SignupActivity.this,
employeeloginActivity.class);
startActivity(intent);
finish(); // Finish the activity to prevent going back to it
}
});
loginRedirectText.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
Intent intent = new Intent(SignupActivity.this,
employeeloginActivity.class);
startActivity(intent);
finish(); // Finish the activity to prevent going back to it
}
});
} }
