package com.example.smapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smapplication.Activities.EventDescriptionActivity;
import com.example.smapplication.Model.Event;
import com.example.smapplication.R;

import java.util.ArrayList;

public class EventItemAdapter extends RecyclerView.Adapter<EventItemAdapter.EventItemViewHolder> {

	private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
	private ArrayList<Event> eventList;
	private Context context;

	public EventItemAdapter(ArrayList<Event> eventList) {
		this.eventList = eventList;
	}

	@NonNull
	@Override
	public EventItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item_card, parent, false);
		context = parent.getContext();
		return new EventItemViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull EventItemViewHolder holder, int position) {
		Event event = eventList.get(position);

		holder.title.setText(event.getTitle());
		holder.id.setText(String.valueOf(event.getId()));

		holder.itemView.setOnClickListener(this::onEventClicked);
	}

	@Override
	public int getItemCount() {
		return eventList.size();
	}

	public void onEventClicked(View view){
		Toast.makeText(view.getContext(), "asdasd", Toast.LENGTH_SHORT).show();
		Intent eventIntent = new Intent(view.getContext(), EventDescriptionActivity.class);
		eventIntent.putExtra("eventID", ((TextView)view.findViewById(R.id.idTextView)).getText());
		context.startActivity(eventIntent);
	}

	public class EventItemViewHolder extends RecyclerView.ViewHolder {
		TextView title;
		ImageView image;
		TextView id;

		public EventItemViewHolder(@NonNull View itemView) {
			super(itemView);
			title = (TextView) itemView.findViewById(R.id.titleTextView);

			image = (ImageView) itemView.findViewById(R.id.imageView);

			id = (TextView) itemView.findViewById(R.id.idTextView);
		}
	}
}
