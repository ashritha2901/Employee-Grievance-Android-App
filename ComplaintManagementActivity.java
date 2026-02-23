package com.example.grievancesystem;
import android.content.Intent;
import android.os.Bundle; import
android.view.View; import
android.widget.AdapterView; import
android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
public class ComplaintManagementActivity extends AppCompatActivity {
private ListView listViewComplaints;
private List<complaint1> complaintsList;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_complaint_management);
listViewComplaints = findViewById(R.id.listViewComplaints);
complaintsList = DataStore1.getComplaints(); // Fetching complaints from the
DataStore
ArrayAdapter<complaint1> adapter = new ArrayAdapter<>(this,
android.R.layout.simple_list_item_1, complaintsList);
listViewComplaints.setAdapter(adapter);
listViewComplaints.setOnItemClickListener(new
AdapterView.OnItemClickListener() {
@Override
public void onItemClick(AdapterView<?> parent, View view, int position,
long id) {
complaint1 selectedComplaint = complaintsList.get(position);
Intent intent = new Intent(ComplaintManagementActivity.this,
UpdateComplaintStatusActivity.class);
intent.putExtra("complaintId", selectedComplaint.getId());
startActivity(intent);
}
});
} }
