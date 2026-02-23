public class AdvocateProfileFragment extends Fragment {

private static final int
 
REQUEST_IMAGE_CAPTURE
 
= 1;
private static final int
 
REQUEST_IMAGE_PICK
 
= 2;
private static final int
 
REQUEST_PERMISSIONS
 
= 3;

private ImageView profileImage;
 
private EditText
editTextName, editTextAdvocateId, editTextEmail, editTextPhone,
editTextExperience, editTextAddress;

private Button buttonUploadPhoto, buttonSaveProfile, buttonEditProfile,
buttonViewProfile;

private Uri imageUri;

private DatabaseReference databaseReference;

private StorageReference storageReference;

private boolean isEditing = false; // Flag to determine if in editing mode

@Nullable
@Override

public View onCreateView(@NonNull LayoutInflater inflater, @Nullable

ViewGroup container, @Nullable Bundle savedInstanceState) {

View view = inflater.inflate(R.layout.
fragment_advocate_profile
, container,
false);

// Initialize Firebase references

databaseReference =

FirebaseDatabase.
getInstance
().getReference("profiles");
storageReference =

FirebaseStorage.
getInstance
().getReference("profile_images");

// Bind UI components

profileImage = view.findViewById(R.id.
profileImage
);
buttonUploadPhoto = view.findViewById(R.id.
buttonUploadPhoto
);
editTextName = view.findViewById(R.id.
editTextName
);
editTextAdvocateId = view.findViewById(R.id.
editTextAdvocateId
);
editTextEmail = view.findViewById(R.id.
editTextEmail
);
 
editTextPhone
= view.findViewById(R.id.
editTextPhone
);
 
editTextExperience =
view.findViewById(R.id.
editTextExperience
);
 
editTextAddress =
view.findViewById(R.id.
editTextAddress
);
 
buttonSaveProfile =
view.findViewById(R.id.
buttonSaveProfile
);
 
buttonEditProfile =
view.findViewById(R.id.
buttonEditProfile
);
 
buttonViewProfile =
view.findViewById(R.id.
buttonViewProfile
);

SharedPreferences sharedPrefs =

getActivity().getSharedPreferences("YourPrefs",
 
Context.
MODE_PRIVATE
);
String username = sharedPrefs.getString("username", ""); // Get username from
SharedPreferences

// Set click listeners

buttonUploadPhoto.setOnClickListener(v
 
-
> {

// Check for camera permission

if (ContextCompat.
checkSelfPermission
(requireContext(),

Manifest.permission.
CAMERA
) == PackageManager.
PERMISSION_GRANTED
) {

// Permission granted, open camera

openCamera();

} else {

// Request camera permission

ActivityCompat.
requestPermissions
(requireActivity(), new

String[]{Manifest.permission.
CAMERA
},
 
REQUEST_IMAGE_CAPTURE
);

}

});

buttonSaveProfile.setOnClickListener(v
 
-
> saveProfileToFirebase());
buttonEditProfile.setOnClickListener(v
 
-
> enableEditing());
buttonViewProfile.setOnClickListener(v
 
-
>

loadProfileFromFirebaseByUsername(username)); // Load profile based on
username

// Load profile when fragment is created
if (!username.isEmpty()) {

loadProfileFromFirebaseByUsername(username); // Automatically load
profile on fragment creation

}
return view;

}

private void openCamera() {

Intent takePictureIntent = new

Intent(MediaStore.
ACTION_IMAGE_CAPTURE
);
if

(takePictureIntent.resolveActivity(requireActivity().getPackageManager()) !=
null) {

startActivityForResult(takePictureIntent,
 
REQUEST_IMAGE_CAPTURE
);

} else {

Toast.
makeText
(getActivity(), "No app available to capture an image",

Toast.
LENGTH_SHORT
).show();

}

}

private void saveImageToGallery(Bitmap bitmap) {
try {

// Get the image URI

Uri imageUri = getImageUriFromBitmap(bitmap);

// Add the image to the gallery

MediaStore.Images.Media.
insertImage
(requireActivity().getContentResolver(),
bitmap, "Profile Image", "Captured by camera");

// Notify the gallery about the new image

requireActivity().sendBroadcast(new

Intent(Intent.
ACTION_MEDIA_SCANNER_SCAN_FILE
, imageUri));

Toast.
makeText
(getActivity(), "Image saved to gallery",

Toast.
LENGTH_SHORT
).show();

} catch (Exception e) {

Toast.
makeText
(getActivity(), "Failed to save image to gallery: " +
e.getMessage(), Toast.
LENGTH_SHORT
).show();

Log.
e
("AdvocateProfileFragment", "Error saving image to gallery: " +
e.getMessage());

}
}

private void requestPermissions() {

if (ContextCompat.
checkSelfPermission
(requireContext(),

Manifest.permission.
CAMERA
) != PackageManager.
PERMISSION_GRANTED
 
||

ContextCompat.
checkSelfPermission
(requireContext(),

Manifest.permission.
READ_EXTERNAL_STORAGE
) !=
PackageManager.
PERMISSION_GRANTED
) {

requestPermissions(new String[]{Manifest.permission.
CAMERA
,

Manifest.permission.
READ_EXTERNAL_STORAGE
},
 
REQUEST_PERMISSIONS
);

} else {

showImageOptions();

}

}

@Override

public void onRequestPermissionsResult(int requestCode, @NonNull String[]
permissions, @NonNull int[] grantResults) {
super.onRequestPermissionsResult(requestCode, permissions, grantResults);

if (requestCode ==
 
REQUEST_PERMISSIONS
) {

if (grantResults.length > 0 && grantResults[0] ==

PackageManager.
PERMISSION_GRANTED
 
&& grantResults[1] ==
PackageManager.
PERMISSION_GRANTED
) {

showImageOptions();

} else {

Toast.
makeText
(getActivity(), "Permissions are required to access
camera and gallery", Toast.
LENGTH_SHORT
).show();

}

}

}

private void showImageOptions() {

AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
builder.setTitle("Upload Photo")

.setItems(new String[]{"Take Photo", "Choose from Gallery"}, (dialog,
which)
 
-
> {

if (which == 0) {
Intent takePictureIntent = new

Intent(MediaStore.
ACTION_IMAGE_CAPTURE
);
if

(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
startActivityForResult(takePictureIntent,

REQUEST_IMAGE_CAPTURE
);

} else {

Toast.
makeText
(getActivity(), "No app available to capture an
image", Toast.
LENGTH_SHORT
).show();

}

} else {

Intent pickPhotoIntent = new Intent(Intent.
ACTION_PICK
,
MediaStore.Images.Media.
EXTERNAL_CONTENT_URI
);
 
if
(pickPhotoIntent.resolveActivity(getActivity().getPackageManager())
!= null) {
 
startActivityForResult(pickPhotoIntent,
REQUEST_IMAGE_PICK
);

} else {

Toast.
makeText
(getActivity(), "No app available to pick an
image", Toast.
LENGTH_SHORT
).show();

}

}

}).show();

}

public void onActivityResult(int requestCode, int resultCode, @Nullable
Intent data) {

super.onActivityResult(requestCode, resultCode, data);

if (requestCode ==
 
REQUEST_IMAGE_CAPTURE
 
&& resultCode ==

getActivity().
RESULT_OK
 
&& data != null) {
// Get the captured image bitmap

Bundle extras = data.getExtras();

Bitmap imageBitmap = (Bitmap) extras.get("data");

// Set the image to the ImageView
profileImage.setImageBitmap(imageBitmap);
saveImageToGallery(imageBitmap);
}

}

private Uri getImageUriFromBitmap(Bitmap bitmap) {

ByteArrayOutputStream bytes = new ByteArrayOutputStream();
bitmap.compress(Bitmap.CompressFormat.
JPEG
, 100, bytes);

String path =

MediaStore.Images.Media.
insertImage
(getActivity().getContentResolver(),
bitmap, "Title", null);

return Uri.
parse
(path);

}

private void uploadImageToFirebase(Uri uri) {
if (uri != null) {

StorageReference fileReference = storageReference.child("profile_" +
System.
currentTimeMillis
() + ".jpg");
 
fileReference.putFile(uri)

.addOnSuccessListener(taskSnapshot
 
-
>

Toast.
makeText
(getActivity(), "Image uploaded!",

Toast.
LENGTH_SHORT
).show())

.addOnFailureListener(e
 
-
> Toast.
makeText
(getActivity(), "Failed to
upload image: " + e.getMessage(), Toast.
LENGTH_SHORT
).show());

}

}

private void enableEditing() {

isEditing = true;

setInputFieldsEnabled(true);

Toast.
makeText
(getActivity(), "Editing mode enabled",

Toast.
LENGTH_SHORT
).show();

}

private void saveProfileToFirebase() {

String name = editTextName.getText().toString().trim();

String advocateId = editTextAdvocateId.getText().toString().trim();

String email = editTextEmail.getText().toString().trim();

String phone = editTextPhone.getText().toString().trim();

String experience = editTextExperience.getText().toString().trim();
String address = editTextAddress.getText().toString().trim();

SharedPreferences sharedPrefs =

getActivity().getSharedPreferences("YourPrefs", Context.
MODE_PRIVATE
);
String username = sharedPrefs.getString("username", "");

HashMap<String, String> profileMap = new HashMap<>();
profileMap.put("name", name);
profileMap.put("advocateId", advocateId);
profileMap.put("email", email);
 
profileMap.put("phone",
phone);
 
profileMap.put("experience",experience);
profileMap.put("address", address);
databaseReference.child(username).setValue(profileMap)

.addOnSuccessListener(aVoid
 
-
> {

Toast.makeText(getActivity(), "Profile saved!",

Toast.LENGTH_SHORT).show();

setInputFieldsEnabled(false); // Disable fields after saving

})

.addOnFailureListener(e
 
-
> {

Toast.makeText(getActivity(), "Failed to save profile: " +
e.getMessage(), Toast.LENGTH_SHORT).show();

});

}

private void loadProfileFromFirebaseByUsername(String username) {
databaseReference.child(username).addListenerForSingleValueEvent(new

ValueEventListener() {
@Override

public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
if (dataSnapshot.exists()) {

String name = dataSnapshot.child("name").getValue(String.class);
String advocateId =

dataSnapshot.child("advocateId").getValue(String.class);

String email = dataSnapshot.child("email").getValue(String.class);

String phone = dataSnapshot.child("phone").getValue(String.class);
String experience =

dataSnapshot.child("experience").getValue(String.class);
String address =

dataSnapshot.child("address").getValue(String.class);

editTextName.setText(name);
editTextAdvocateId.setText(advocateId);

editTextEmail.setText(email);
editTextPhone.setText(phone);
editTextExperience.setText(experience);

editTextAddress.setText(address);

} else {

Toast.makeText(getActivity(), "Profile not found",
Toast.LENGTH_SHORT).show();

}

}

@Override

public void onCancelled(@NonNull DatabaseError databaseError) {
Toast.makeText(getActivity(),
 
"Error
 
loading
 
profile:
 
"
 
+
databaseError.getMessage(), Toast.LENGTH_SHORT).show();

}

});

}

private void setInputFieldsEnabled(boolean enabled) {
editTextName.setEnabled(enabled);
editTextAdvocateId.setEnabled(enabled);
editTextEmail.setEnabled(enabled);
editTextPhone.setEnabled(enabled);
editTextExperience.setEnabled(enabled);

editTextAddress.setEnabled(enabled);

}
 
}
