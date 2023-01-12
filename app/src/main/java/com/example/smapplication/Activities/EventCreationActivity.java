package com.example.smapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smapplication.Adapter.EventCreation_DetailItemAdapter;
import com.example.smapplication.Model.Detail;
import com.example.smapplication.Model.Event;
import com.example.smapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EventCreationActivity extends AppCompatActivity {

	ArrayList<Detail> details;

	private FirebaseDatabase db = FirebaseDatabase.getInstance("https://smapplication-c9fd9-default-rtdb.europe-west1.firebasedatabase.app");
	private DatabaseReference root = db.getReference();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_creation);

		Button addButton = findViewById(R.id.addButton);
		Button cancelButton = findViewById(R.id.cancelButton);
		EditText editText = findViewById(R.id.editTextName);

		RecyclerView recyclerView = findViewById(R.id.recyclerViewAdd);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
		details = new ArrayList<Detail>();
		details.add(new Detail("ADD", "NEW", 0));

		EventCreation_DetailItemAdapter detailItemAdapter = new EventCreation_DetailItemAdapter(details);

		recyclerView.setAdapter(detailItemAdapter);
		recyclerView.setLayoutManager(layoutManager);

		addButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Event event = new Event(MainActivity.getSignedInUser().getId(), editText.getText().toString(), null);
				details = detailItemAdapter.getDetailList();
				Toast.makeText(EventCreationActivity.this, String.valueOf(details.size()), Toast.LENGTH_SHORT).show();
				root.child("Events").child(String.valueOf(event.getId())).setValue(event);
				if(details.size() > 1) {
					details.remove(details.size() - 1);
					root.child("Events").child(String.valueOf(event.getId())).child("Details").setValue(details);
				}
				else
					Toast.makeText(EventCreationActivity.this, "no details", Toast.LENGTH_SHORT).show();

				Toast.makeText(EventCreationActivity.this, "no details", Toast.LENGTH_SHORT).show();

				finish();
			}
		});

		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}
}