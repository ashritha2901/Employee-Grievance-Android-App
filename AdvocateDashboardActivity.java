package com.example.grievancesystem;
import android.os.Bundle; import
android.content.Intent; import
android.view.MenuItem; import
androidx.annotation.NonNull; import
androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment; import
androidx.appcompat.app.AlertDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
public class AdvocateDashboardActivity extends AppCompatActivity {
private BottomNavigationView bottomNavigationView;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_advocate_dashboard);
bottomNavigationView = findViewById(R.id.bottomNavigationView);
// Load the default fragment (Advocate Profile) when activity is launched
loadFragment(new AdvocateProfileFragment());
bottomNavigationView.setOnItemSelectedListener(new
NavigationBarView.OnItemSelectedListener() {
@Override
public boolean onNavigationItemSelected(@NonNull MenuItem item) {
Fragment selectedFragment = null;
if (item.getItemId() == R.id.navigation_profile) {
selectedFragment = new AdvocateProfileFragment(); } else if
(item.getItemId() == R.id.navigation_pending_cases) {
selectedFragment = new AdvocatePendingCasesFragment(); } else
if (item.getItemId() == R.id.navigation_update_case_status) {
selectedFragment = new updatecasestatus(); } else if
(item.getItemId() == R.id.navigation_logout) {
showLogoutDialog();
return true; // Return here as logout doesn't involve fragment
loading
}
if (selectedFragment != null) {
loadFragment(selectedFragment); }
return true;
}
});
}
private void loadFragment(Fragment fragment) {
getSupportFragmentManager().beginTransaction()
.replace(R.id.fragmentContainer, fragment)
.commit();
}
private void showLogoutDialog() { //
Show a confirmation dialog for logout
new AlertDialog.Builder(AdvocateDashboardActivity.this)
.setTitle("Logout")
.setMessage("Are you sure you want to logout?")
.setPositiveButton(android.R.string.ok, (dialog, which) -> {
// Logout and redirect to login screen
Intent intent = new Intent(AdvocateDashboardActivity.this,
employeeloginActivity.class);
startActivity(intent);
finish();
})
.setNegativeButton(android.R.string.cancel, null)
.show();
} }
