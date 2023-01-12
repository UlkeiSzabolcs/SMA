package com.example.smapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.smapplication.Adapter.EventCreation_DetailItemAdapter;
import com.example.smapplication.Adapter.EventDescription_Creator_DetailItemAdapter;
import com.example.smapplication.Adapter.EventDescription_NonCreator_DetailItemAdapter;
import com.example.smapplication.Model.Detail;
import com.example.smapplication.Model.Event;
import com.example.smapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EventDescriptionActivity extends AppCompatActivity {


	private FirebaseDatabase db = FirebaseDatabase.getInstance("https://smapplication-c9fd9-default-rtdb.europe-west1.firebasedatabase.app");
	private DatabaseReference root = db.getReference();

	ArrayList<Detail> details;
	private String eventID;
	private Event thisEvent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_description);
		eventID = getIntent().getStringExtra("eventID");

		TextView textView = findViewById(R.id.titleDescriptionTextView);
		Button deleteButton = findViewById(R.id.deleteEventButton);


		root.child("Events").child(eventID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DataSnapshot> task) {
				Event event = task.getResult().getValue(Event.class);
				thisEvent = event;

				if(MainActivity.getSignedInUser().getId() != thisEvent.getCreator_id())
					deleteButton.setVisibility(View.GONE);

				textView.setText(event.getTitle());

				RecyclerView recyclerView = findViewById(R.id.descriptionRecycleView);
				LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
				details = new ArrayList<Detail>();

				for(DataSnapshot detailSnapshot : task.getResult().child("Details").getChildren())
					details.add(detailSnapshot.getValue(Detail.class));

				EventDescription_Creator_DetailItemAdapter detailItemAdapter_creator = new EventDescription_Creator_DetailItemAdapter(details,String.valueOf(thisEvent.getId()));
				EventDescription_NonCreator_DetailItemAdapter detailItemAdapter_nonCreator = new EventDescription_NonCreator_DetailItemAdapter(details, String.valueOf(thisEvent.getId()));

				if(thisEvent.getCreator_id() == MainActivity.getSignedInUser().getId())
					recyclerView.setAdapter(detailItemAdapter_creator);
				else
					recyclerView.setAdapter(detailItemAdapter_nonCreator);
				

				recyclerView.setLayoutManager(layoutManager);

			}
		});

		deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				root.child("Events").child(eventID).removeValue();
				finish();
			}
		});
	}
}