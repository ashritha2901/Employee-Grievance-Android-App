package com.example.grievancesystem;
import android.content.Intent; import
android.os.Bundle; import android.view.MenuItem;
import android.view.LayoutInflater; import
android.view.View; import android.widget.Toast;
import android.widget.RatingBar; import
android.widget.EditText; import
android.content.DialogInterface; import
androidx.annotation.NonNull; import
androidx.appcompat.app.AlertDialog; import
androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment; import
com.google.android.material.bottomnavigation.Bott
omNavigationView; import
com.google.android.material.navigation.NavigationB
arView;
public class DashboardActivity extends AppCompatActivity {
private BottomNavigationView bottomNavigationView;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_dash);
bottomNavigationView = findViewById(R.id.bottomNavigationView);
// Set default fragment when activity is launched
loadFragment(new ProfileFragment());
// Bottom navigation item select listener
bottomNavigationView.setOnItemSelectedListener(new
NavigationBarView.OnItemSelectedListener() {
@Override
public boolean onNavigationItemSelected(@NonNull MenuItem item) {
Fragment selectedFragment = null; int itemId = item.getItemId();
if (itemId == R.id.navigation_profile) { selectedFragment = new
ProfileFragment(); } else if (itemId == R.id.navigation_file_complaint)
{ selectedFragment = new FileComplaintFragment(); }
else if (itemId == R.id.navigation_view_status) { selectedFragment
= new ViewStatusFragment(); } else if (itemId ==
R.id.navigation_counseling) { selectedFragment = new
CounselingFragment(); } else if (itemId == R.id.navigation_logout) {
showFeedbackDialog();
return true;
}
if (selectedFragment != null) {
getSupportFragmentManager().beginTransaction()
.replace(R.id.fragmentContainer, selectedFragment)
.commit();
}
return true;
}
});
}
// Load fragment method
private void loadFragment(Fragment fragment) {
getSupportFragmentManager().beginTransaction()
.replace(R.id.fragmentContainer, fragment)
.commit();
}
private void showFeedbackDialog() {
// Inflate the custom layout for the feedback dialog
View feedbackView =
getLayoutInflater().inflate(R.layout.activity_dialog_feedback, null); final
RatingBar ratingBar = feedbackView.findViewById(R.id.ratingBar); final
EditText editTextFeedback =
feedbackView.findViewById(R.id.editTextFeedback);
new AlertDialog.Builder(DashboardActivity.this)
.setTitle("Logout")
.setMessage("Please provide your feedback before logging out")
.setView(feedbackView)
.setPositiveButton(android.R.string.ok, new
DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialog, int which) {
// Handle feedback submission
float rating = ratingBar.getRating();
String comments =
editTextFeedback.getText().toString();
Toast.makeText(DashboardActivity.this, "Thank
you for your feedback!",
Toast.LENGTH_SHORT).show();
// Redirect to login screen
Intent intent = new Intent(DashboardActivity.this,
employeeloginActivity.class);
startActivity(intent);
}
})
.setNegativeButton(android.R.string.cancel, null)
.show();
} }
