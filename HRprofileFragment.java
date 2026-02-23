package com.example.grievancesystem;
import android.os.AsyncTask; import
android.os.Bundle; import android.view.View; import
android.widget.Button; import
android.widget.EditText; import
android.widget.Toast; import
androidx.annotation.Nullable; import
androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater; import
androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.ViewGroup; import
com.mongodb.MongoClientSettings; import
com.mongodb.MongoException; import
com.mongodb.ServerAddress; import
com.mongodb.client.MongoClient; import
com.mongodb.client.MongoClients; import
com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.Collections;
public class HRprofileFragment extends Fragment {
private EditText editTextName, editTextHRId, editTextBranch, editTextPost,
editTextEmail, editTextPhone, editTextAge, editTextAddress; private
Button buttonSaveProfile; private MongoClient mongoClient; private
MongoDatabase mongoDatabase;
private MongoCollection<Document> profileCollection;
@Nullable
public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
ViewGroup container, @Nullable Bundle savedInstanceState) {
View view = inflater.inflate(R.layout.fragment_hr_profile, container, false);
// Initialize UI components
editTextName = view.findViewById(R.id.editTextName);
editTextHRId = view.findViewById(R.id.editTextHRId);
editTextBranch = view.findViewById(R.id.editTextBranch);
editTextPost = view.findViewById(R.id.editTextPost); editTextEmail
= view.findViewById(R.id.editTextEmail); editTextPhone =
view.findViewById(R.id.editTextPhone); editTextAge =
view.findViewById(R.id.editTextAge); editTextAddress =
view.findViewById(R.id.editTextAddress); buttonSaveProfile =
view.findViewById(R.id.buttonSaveProfile);
initializeMongoDB();
buttonSaveProfile.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
saveProfileInfo();
}
});
return view;
}
private void initializeMongoDB() {
Toast.makeText(getActivity(), "Mongodb Entered",
Toast.LENGTH_SHORT).show();
MongoClient mongoClient =
MongoClients.create(MongoClientSettings.builder()
.applyToClusterSettings(builder ->
builder.hosts(Collections.singletonList(new
ServerAddress("10.0.2.2", 27017))))
.build());
mongoDatabase = mongoClient.getDatabase("grievancesystem");
profileCollection = mongoDatabase.getCollection("Profile");
Toast.makeText(getActivity(), "Mongodb initiated",
Toast.LENGTH_SHORT).show();
}
private void saveProfileInfo() {
String Name = editTextName.getText().toString();
String HrId = editTextHRId.getText().toString();
String Branch = editTextBranch.getText().toString();
String Post = editTextPost.getText().toString();
String Email = editTextEmail.getText().toString();
String Phone = editTextPhone.getText().toString();
String Age = editTextAge.getText().toString();
String Address = editTextAddress.getText().toString();
if (Name.isEmpty() || HrId.isEmpty()) {
Toast.makeText(getActivity(), "Please fill all fields",
Toast.LENGTH_SHORT).show();
return;
}
// Create a new Document to insert with more fields
Document profileDoc = new Document("name", Name)
.append("EmployeeId", HrId)
.append("Branch", Branch)
.append("Post", Post)
.append("Email", Email)
.append("Phone", Phone)
.append("Age", Age)
.append("Address", Address);
// Use AsyncTask to save the document in the background
new InsertDiseaseTask().execute(profileDoc);
}
private class InsertDiseaseTask extends AsyncTask<Document, Void,
Boolean> {
@Override
protected Boolean doInBackground(Document... documents) {
try {
// Insert the document into the collection
profileCollection.insertOne(documents[0]);
return true; // Insert successful
} catch (MongoException e) {
e.printStackTrace(); return
false; // Insert failed
}
}
@Override
protected void onPostExecute(Boolean success) {
if (success) {
Toast.makeText(getActivity(), "Profile saved",
Toast.LENGTH_SHORT).show();
editTextName.setText(""); // Clear the input fields
editTextHRId.setText(""); editTextBranch.setText("");
editTextPost.setText(""); editTextEmail.setText("");
editTextPhone.setText(""); editTextAge.setText("");
editTextAddress.setText("");
} else {
Toast.makeText(getActivity(), "Profile not found",
Toast.LENGTH_SHORT).show();
}
}
} }
