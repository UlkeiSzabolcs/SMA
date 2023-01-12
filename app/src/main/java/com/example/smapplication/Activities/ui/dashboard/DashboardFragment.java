package com.example.smapplication.Activities.ui.dashboard;

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
import com.example.smapplication.databinding.FragmentDashboardBinding;
import com.example.smapplication.Adapter.EventItemAdapter;
import com.example.smapplication.Activities.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

	private FragmentDashboardBinding binding;
	ArrayList<Event> events;
	EventItemAdapter eventItemAdapter;

	private FirebaseDatabase db = FirebaseDatabase.getInstance("https://smapplication-c9fd9-default-rtdb.europe-west1.firebasedatabase.app");
	private DatabaseReference dbRoot = db.getReference();
	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {
		DashboardViewModel dashboardViewModel =
				new ViewModelProvider(this).get(DashboardViewModel.class);

		binding = FragmentDashboardBinding.inflate(inflater, container, false);
		View root = binding.getRoot();

		LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());

		RecyclerView recyclerView = binding.recyclerView;

		events = new ArrayList<>();

		eventItemAdapter = new EventItemAdapter(events);

		recyclerView.setAdapter(eventItemAdapter);
		recyclerView.setLayoutManager(layoutManager);

		Toast.makeText(this.getContext(),String.valueOf( MainActivity.getSignedInUser().getId() ), Toast.LENGTH_SHORT).show();

		dbRoot.child("Events").addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				for(DataSnapshot child : snapshot.getChildren()){
					Event event = child.getValue(Event.class);
					assert event != null;
					if(event.getCreator_id() == MainActivity.getSignedInUser().getId())
						events.add(event);
				}
				eventItemAdapter.notifyDataSetChanged();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {

			}
		});

		return root;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
}