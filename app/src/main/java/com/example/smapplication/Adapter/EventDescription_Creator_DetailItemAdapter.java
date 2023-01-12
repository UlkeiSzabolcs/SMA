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

public class EventDescription_Creator_DetailItemAdapter extends RecyclerView.Adapter<EventDescription_Creator_DetailItemAdapter.EventDescription_Creator_DetailItemViewHolder> {
	private ArrayList<Detail> detailList;
	private ArrayList<User> userList;
	private EventDescription_Creator_DetailItemViewHolder mHolder;
	private String thisEventID;

	private FirebaseDatabase db = FirebaseDatabase.getInstance("https://smapplication-c9fd9-default-rtdb.europe-west1.firebasedatabase.app");
	private DatabaseReference root = db.getReference();

	public EventDescription_Creator_DetailItemAdapter(ArrayList<Detail> detailList, String thisEventID) {
		this.detailList = detailList;
		this.thisEventID = thisEventID;
	}

	@NonNull
	@Override
	public EventDescription_Creator_DetailItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item_card_description_creator, parent, false);
		return new EventDescription_Creator_DetailItemAdapter.EventDescription_Creator_DetailItemViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull EventDescription_Creator_DetailItemViewHolder holder, int position) {
		mHolder = holder;
		Detail detail = detailList.get(position);

		holder.shortTextView.setText(detail.getShortDescriptor());
		holder.nameTextView.setText(detail.getName());
		holder.limitTextView.setText(String.valueOf(detail.getParticipantLimit()));
		if(detail.getParticipantLimit() == 0)
			holder.limitTextView.setVisibility(View.GONE);


		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				RecyclerView recyclerView = view.findViewById(R.id.usersRecycleViewCreator);
				if(detail.getParticipants().size() > 1)
					recyclerView.setVisibility(View.VISIBLE);

				LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
				userList = new ArrayList<User>();

				userList = detailList.get(position).getParticipants();
				UserItemAdapter userItemAdapter = new UserItemAdapter(userList, true, thisEventID, detailList, detail.getName());

				recyclerView.setAdapter(userItemAdapter);
				recyclerView.setLayoutManager(layoutManager);

				view.findViewById(R.id.CollapseButtonCreator).setVisibility(View.VISIBLE);

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
				holder.collapseButton.setVisibility(View.GONE);
				holder.recyclerView.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public int getItemCount() {
		return detailList.size();
	}

	public class EventDescription_Creator_DetailItemViewHolder extends RecyclerView.ViewHolder {

		TextView nameTextView;
		TextView shortTextView;
		TextView limitTextView;
		Button collapseButton;
		RecyclerView recyclerView;

		public EventDescription_Creator_DetailItemViewHolder(@NonNull View itemView) {
			super(itemView);
			nameTextView = (TextView) itemView.findViewById(R.id.textViewDetailNameCreator);
			shortTextView = (TextView) itemView.findViewById(R.id.textViewDetailShortRightCreator);
			collapseButton = (Button) itemView.findViewById(R.id.CollapseButtonCreator);
			limitTextView = (TextView) itemView.findViewById(R.id.textViewLimitCreator);
			recyclerView = (RecyclerView) itemView.findViewById(R.id.usersRecycleViewCreator);
		}
	}
}
