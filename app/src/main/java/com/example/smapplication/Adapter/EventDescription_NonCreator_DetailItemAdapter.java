package com.example.smapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smapplication.Activities.MainActivity;
import com.example.smapplication.Model.Detail;
import com.example.smapplication.Model.User;
import com.example.smapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventDescription_NonCreator_DetailItemAdapter extends RecyclerView.Adapter<EventDescription_NonCreator_DetailItemAdapter.DescriptionNonCreatorDetailItemViewHolder> {
	private ArrayList<Detail> detailList;
	private ArrayList<User> userList;
	private DescriptionNonCreatorDetailItemViewHolder mHolder;
	private String thisEventID;

	private FirebaseDatabase db = FirebaseDatabase.getInstance("https://smapplication-c9fd9-default-rtdb.europe-west1.firebasedatabase.app");
	private DatabaseReference root = db.getReference();

	public EventDescription_NonCreator_DetailItemAdapter(ArrayList<Detail> detailList, String thisEventID) {
		this.detailList = detailList;
		this.thisEventID = thisEventID;
	}

	@NonNull
	@Override
	public EventDescription_NonCreator_DetailItemAdapter.DescriptionNonCreatorDetailItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item_card_description_non_creator, parent, false);
		return new EventDescription_NonCreator_DetailItemAdapter.DescriptionNonCreatorDetailItemViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull EventDescription_NonCreator_DetailItemAdapter.DescriptionNonCreatorDetailItemViewHolder holder, int position) {
		mHolder = holder;
		Detail detail = detailList.get(position);

		holder.shortTextView.setText(detail.getShortDescriptor());
		holder.nameTextView.setText(detail.getName());
		holder.limitTextView.setText(String.valueOf(detail.getParticipantLimit()));
		if(detail.getParticipantLimit() == 0)
			holder.limitTextView.setVisibility(View.GONE);

		/*if(detail.getParticipants().contains(MainActivity.getSignedInUser()))
			holder.joinButton.setText("Leave");*/
		for(User user : detail.getParticipants())
			if(user.getId() == MainActivity.getSignedInUser().getId())
				holder.joinButton.setText("Leave");

		holder.joinButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(holder.joinButton.getText().toString().equals("Join")) {
					detailList.get(position).getParticipants().add(MainActivity.getSignedInUser());
					root.child("Events").child(thisEventID).child("Details").setValue(detailList);
					userList = detailList.get(position).getParticipants();
					holder.joinButton.setText("Leave");
				}
				else{
					ArrayList<User> newArrayList = new ArrayList<>();
					for(User u : detail.getParticipants())
						if(u.getId() != MainActivity.getSignedInUser().getId()) {
							newArrayList.add(u);
						}
					detailList.get(position).setParticipants(newArrayList);
					root.child("Events").child(thisEventID).child("Details").setValue(detailList);
					userList = detailList.get(position).getParticipants();
					holder.joinButton.setText("Join");
				}
			}
		});

		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				RecyclerView recyclerView = view.findViewById(R.id.usersRecycleView);
				if(detail.getParticipants().size() > 0)
					recyclerView.setVisibility(View.VISIBLE);

				LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
				userList = new ArrayList<User>();

				userList = detailList.get(position).getParticipants();
				UserItemAdapter userItemAdapter = new UserItemAdapter(userList, false, thisEventID, detailList, detail.getName());

				recyclerView.setAdapter(userItemAdapter);
				recyclerView.setLayoutManager(layoutManager);

				if(detailList.get(position).getParticipantLimit() > detailList.get(position).getParticipants().size() &&
						detail.getParticipantLimit() != 0)
					view.findViewById(R.id.JoinButton).setVisibility(View.VISIBLE);
				view.findViewById(R.id.CollapseButton).setVisibility(View.VISIBLE);

				root.child("Events").child(thisEventID).child("Details").addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(@NonNull DataSnapshot snapshot) {
						userItemAdapter.notifyDataSetChanged();
					}

					@Override
					public void onCancelled(@NonNull DatabaseError error) {

					}
				});
			}
		});

		holder.collapseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				holder.joinButton.setVisibility(View.GONE);
				holder.collapseButton.setVisibility(View.GONE);
				holder.recyclerView.setVisibility(View.GONE);
			}
		});


	}

	@Override
	public int getItemCount() {
		return detailList.size();
	}

	public class DescriptionNonCreatorDetailItemViewHolder extends RecyclerView.ViewHolder {

		TextView nameTextView;
		TextView shortTextView;
		TextView limitTextView;
		Button joinButton;
		Button collapseButton;
		RecyclerView recyclerView;

		public DescriptionNonCreatorDetailItemViewHolder(@NonNull View itemView) {
			super(itemView);
			nameTextView = (TextView) itemView.findViewById(R.id.textViewDetailName);
			shortTextView = (TextView) itemView.findViewById(R.id.textViewDetailShortRight);
			joinButton = (Button) itemView.findViewById(R.id.JoinButton);
			collapseButton = (Button) itemView.findViewById(R.id.CollapseButton);
			limitTextView = (TextView) itemView.findViewById(R.id.textViewLimit);
			recyclerView = (RecyclerView) itemView.findViewById(R.id.usersRecycleView);
		}
	}
}
