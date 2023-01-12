package com.example.smapplication.Activities.ui.browse;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smapplication.Model.Event;
import com.example.smapplication.Activities.EventCreationActivity;
import com.example.smapplication.Adapter.EventItemAdapter;
import com.example.smapplication.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BrowseFragment extends Fragment {

	private FragmentHomeBinding binding;

	ArrayList<Event> events;
	EventItemAdapter eventItemAdapter;

	private FirebaseDatabase db = FirebaseDatabase.getInstance("https://smapplication-c9fd9-default-rtdb.europe-west1.firebasedatabase.app");
	private DatabaseReference dbRoot = db.getReference();
	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {
		BrowseViewModel homeViewModel =
				new ViewModelProvider(this).get(BrowseViewModel.class);

		binding = FragmentHomeBinding.inflate(inflater, container, false);
		View root = binding.getRoot();

		LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());

		RecyclerView recyclerView = binding.recyclerView;

		events = new ArrayList<>();

		eventItemAdapter = new EventItemAdapter(events);

		recyclerView.setAdapter(eventItemAdapter);
		recyclerView.setLayoutManager(layoutManager);

		FloatingActionButton fab = binding.floatingActionButton;
		fab.setOnClickListener(this::onCreateButtonClicked);


		dbRoot.child("Events").addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				events.clear();
				for(DataSnapshot child : snapshot.getChildren()) {
					Event event = child.getValue(Event.class);
					events.add(event);
					if(event.getId() >= Event.getId_counter()) Event.setId_counter(event.getId() + 1);
				}

				eventItemAdapter.notifyDataSetChanged();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				Toast.makeText(root.getContext(), "No value in database", Toast.LENGTH_SHORT).show();
			}
		});


		return root;
	}


	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

	public void onCreateButtonClicked(View view){
		//instantiate the popup.xml layout file
		Intent intent = new Intent(view.getContext(), EventCreationActivity.class);
		startActivity(intent);

	}
}