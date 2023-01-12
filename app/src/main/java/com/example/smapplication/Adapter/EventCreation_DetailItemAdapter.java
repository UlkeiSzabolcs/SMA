package com.example.smapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smapplication.Model.Detail;
import com.example.smapplication.R;

import java.util.ArrayList;

public class EventCreation_DetailItemAdapter extends RecyclerView.Adapter<EventCreation_DetailItemAdapter.CreationDetailItemViewHolder> {
	private ArrayList<Detail> detailList;
	private CreationDetailItemViewHolder mHolder;

	public EventCreation_DetailItemAdapter(ArrayList<Detail> detailList) {
		this.detailList = detailList;
	}

	@NonNull
	@Override
	public CreationDetailItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item_card_create, parent, false);
		return new CreationDetailItemViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull CreationDetailItemViewHolder holder, int position) {
		mHolder = holder;
		Detail detail = detailList.get(position);
		holder.shortTextView.setText(detail.getShortDescriptor());
		holder.nameTextView.setText(detail.getName());
		holder.limitTextView.setText(String.valueOf(detail.getParticipantLimit()));
		if(detail.getParticipantLimit() == 0)
			holder.limitTextView.setVisibility(View.GONE);

		if(detail.getName().equals("ADD") && detail.getShortDescriptor().equals("NEW")){
			mHolder.shortEditText.setVisibility(View.GONE);
			mHolder.nameEditText.setVisibility(View.GONE);
			mHolder.addButton.setVisibility(View.GONE);
			mHolder.shortTextView.setVisibility(View.GONE);
			mHolder.cancelButton.setVisibility(View.GONE);
			mHolder.nameTextView.setVisibility(View.GONE);
			mHolder.limitTextView.setVisibility(View.GONE);
			mHolder.limitEditText.setVisibility(View.GONE);
			mHolder.addView.setVisibility(View.VISIBLE);
		}
		holder.addButton.setOnClickListener(this::onAddClicked);
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(((TextView)view.findViewById(R.id.textViewDetailShortRight)).getText().toString().equals("NEW")){
					mHolder.nameTextView.setVisibility(View.GONE);
					mHolder.shortEditText.setVisibility(View.VISIBLE);
					mHolder.nameEditText.setVisibility(View.VISIBLE);
					mHolder.addButton.setVisibility(View.VISIBLE);
					mHolder.shortTextView.setVisibility(View.GONE);
					mHolder.cancelButton.setVisibility(View.VISIBLE);
					mHolder.limitEditText.setVisibility(View.VISIBLE);
					mHolder.limitTextView.setVisibility(View.GONE);
				}
				else {
					detailList.remove(position);
					notifyDataSetChanged();
				}
			}
		});
	}

	@Override
	public int getItemCount() {
		return detailList.size();
	}

	public ArrayList<Detail> getDetailList(){return detailList;}

	public void onAddClicked(View view){
		int participantLimit;
		if(mHolder.limitEditText.getText().toString().equals(""))
			participantLimit = 0;
		else
			participantLimit = Integer.parseInt(mHolder.limitEditText.getText().toString());

		Detail detail = new Detail(mHolder.nameEditText.getText().toString(),
				mHolder.shortEditText.getText().toString(),
				participantLimit);
		detailList.add(detailList.size() - 1, detail);


		mHolder.shortEditText.setVisibility(View.GONE);
		mHolder.nameEditText.setVisibility(View.GONE);
		mHolder.addButton.setVisibility(View.GONE);
		mHolder.shortTextView.setVisibility(View.VISIBLE);
		mHolder.cancelButton.setVisibility(View.GONE);
		mHolder.nameTextView.setVisibility(View.VISIBLE);
		mHolder.addView.setVisibility(View.GONE);
		mHolder.limitEditText.setVisibility(View.GONE);
		mHolder.limitTextView.setVisibility(View.VISIBLE);
		if(detailList.get(mHolder.getAdapterPosition()).getParticipantLimit() == 0)
			mHolder.limitTextView.setVisibility(View.GONE);


		this.notifyDataSetChanged();
	}

	public void onCancelClicked(View view){
		mHolder.shortEditText.setVisibility(View.GONE);
		mHolder.nameEditText.setVisibility(View.GONE);
		mHolder.addButton.setVisibility(View.GONE);
		mHolder.shortTextView.setVisibility(View.VISIBLE);
		mHolder.cancelButton.setVisibility(View.GONE);
		mHolder.nameTextView.setVisibility(View.VISIBLE);
		mHolder.limitEditText.setVisibility(View.GONE);
		mHolder.limitTextView.setVisibility(View.GONE);
		this.notifyDataSetChanged();
	}

	public class CreationDetailItemViewHolder extends RecyclerView.ViewHolder{
		TextView nameTextView;
		TextView shortTextView;
		TextView limitTextView;
		EditText shortEditText;
		EditText nameEditText;
		EditText limitEditText;
		Button addButton;
		Button cancelButton;
		ImageView addView;

		public CreationDetailItemViewHolder(@NonNull View itemView){
			super(itemView);
			nameEditText = (EditText) itemView.findViewById(R.id.editTextName);
			shortEditText = (EditText) itemView.findViewById(R.id.editTextShort);
			nameTextView = (TextView) itemView.findViewById(R.id.textViewDetailName);
			shortTextView = (TextView) itemView.findViewById(R.id.textViewDetailShortRight);
			addButton = (Button) itemView.findViewById(R.id.addDetailButton);
			cancelButton = (Button) itemView.findViewById(R.id.CancelDetailButton);
			addView = (ImageView) itemView.findViewById(R.id.addImageView);
			limitEditText = (EditText) itemView.findViewById(R.id.editTextLimit);
			limitTextView = (TextView) itemView.findViewById(R.id.textViewLimit);
		}
	}

}
