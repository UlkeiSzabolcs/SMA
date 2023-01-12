package com.example.smapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smapplication.Activities.MainActivity;
import com.example.smapplication.Model.Detail;
import com.example.smapplication.Model.User;
import com.example.smapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.TransferQueue;

public class UserItemAdapter extends RecyclerView.Adapter<UserItemAdapter.UserItemViewHolder> {
	ArrayList<User> userList;
	boolean isCreator;
	private String thisEventID;
	private ArrayList<Detail> detailList;
	private String detailName;

	private FirebaseDatabase db = FirebaseDatabase.getInstance("https://smapplication-c9fd9-default-rtdb.europe-west1.firebasedatabase.app");
	private DatabaseReference root = db.getReference();
	public UserItemAdapter(ArrayList<User> userList, boolean isCreator, String thisEventID, ArrayList<Detail> detailList, String detailName){
		this.userList = userList;
		this.isCreator = isCreator;
		this.thisEventID = thisEventID;
		this.detailList = detailList;
		this.detailName = detailName;
	}

	@NonNull
	@Override
	public UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_card, parent, false);
		return new UserItemViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull UserItemViewHolder holder, int position) {
		holder.usernameTextView.setText(userList.get(position).getUsername());
		if(!isCreator || userList.get(position).getUsername().equals(MainActivity.getSignedInUser().getUsername()))
			holder.removeButton.setVisibility(View.GONE);
		else
			holder.removeButton.setVisibility(View.VISIBLE);

		holder.removeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				userList.remove(position);
				for(Detail detail : detailList)
					if(detail.getName().equals(detailName))
						detail.setParticipants(userList);
				root.child("Events").child(thisEventID).child("Details").setValue(detailList);
			}
		});
	}

	@Override
	public int getItemCount() {
		return userList.size();
	}

	public class UserItemViewHolder extends RecyclerView.ViewHolder {

		TextView usernameTextView;
		Button removeButton;
		public UserItemViewHolder(@NonNull View itemView) {
			super(itemView);
			usernameTextView = (TextView) itemView.findViewById(R.id.textViewUserName);
			removeButton = (Button) itemView.findViewById(R.id.removeButton);
		}
	}
}
