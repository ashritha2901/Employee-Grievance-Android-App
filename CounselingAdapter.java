package com.example.grievancesystem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View; import
android.view.ViewGroup; import
android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
public class CounselingAdapter extends ArrayAdapter<Counseling> {
// Constructor
public CounselingAdapter(Context context, List<Counseling> counselings) {
super(context, 0, counselings);
}
@Override
public View getView(int position, View convertView, ViewGroup parent) {
// Get the data item for this position
Counseling counseling = getItem(position);
// Check if an existing view is being reused, otherwise inflate the view
if (convertView == null) {
convertView =
LayoutInflater.from(getContext()).inflate(R.layout.item_counseling, parent,
false);
}
// Lookup view for data population
TextView nameTextView =
convertView.findViewById(R.id.counselingName);
TextView dateTextView = convertView.findViewById(R.id.counselingDate);
TextView timeTextView = convertView.findViewById(R.id.counselingTime);
TextView typeTextView = convertView.findViewById(R.id.counselingType);
// Populate the data into the template view using the data object
if (counseling != null) {
nameTextView.setText(counseling.getName());
dateTextView.setText(counseling.getDate());
timeTextView.setText(counseling.getTime());
typeTextView.setText(counseling.getMeetingType());
}
// Return the completed view to render on screen
return convertView;
} }
