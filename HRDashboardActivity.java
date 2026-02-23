package com.example.grievancesystem;
import android.content.Intent; import android.os.Bundle; import
android.view.MenuItem; import android.view.View; import
android.widget.Toast; import android.widget.RatingBar; import
android.widget.EditText; import android.content.DialogInterface; import
androidx.annotation.NonNull; import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity; import
androidx.fragment.app.Fragment; import
com.google.android.material.bottomnavigation.BottomNavigationView; import
com.google.android.material.navigation.NavigationBarView;
public class HRDashboardActivity extends AppCompatActivity {
private BottomNavigationView bottomNavigationView;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_hrdashboard);
bottomNavigationView = findViewById(R.id.bottomNavigationView);
// Set default fragment when activity is launched
loadFragment(new AssignedComplaintsFragment());
// Bottom navigation item select listener
bottomNavigationView.setOnItemSelectedListener(new
NavigationBarView.OnItemSelectedListener() {
@Override
public boolean onNavigationItemSelected(@NonNull MenuItem item) {
Fragment selectedFragment = null; int itemId = item.getItemId();
if (itemId == R.id.navigation_view_complaints) {
selectedFragment = new AssignedComplaintsFragment(); } else
if (itemId == R.id.navigation_update_complaint_status) {
selectedFragment = new UpdateComplaintStatusFragment(); }
else if (itemId == R.id.navigation_view_counseling) {
selectedFragment = new ViewCounselingFragment(); } else if
(itemId == R.id.navigation_hr_profile) { selectedFragment =
new HRprofileFragment(); } else if (itemId ==
R.id.navigation_logout) { showLogoutDialog();
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
private void showLogoutDialog() { // Show a
confirmation dialog for logout new
AlertDialog.Builder(HRDashboardActivity.this)
.setTitle("Logout")
.setMessage("Are you sure you want to logout?")
.setPositiveButton(android.R.string.ok, (dialog, which) -> {
// Logout and redirect to login screen
Intent intent = new Intent(HRDashboardActivity.this,
employeeloginActivity.class);
startActivity(intent);
finish();
})
.setNegativeButton(android.R.string.cancel, null)
.show();
} }
