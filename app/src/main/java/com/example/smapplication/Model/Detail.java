package com.example.smapplication.Model;

import com.example.smapplication.Activities.MainActivity;

import java.util.ArrayList;

public class Detail{
	private String name;
	private String shortDescriptor;
	int participantLimit;
	ArrayList<User> participants;

	public Detail() {
	}

	public Detail(String name, String shortDescriptor, int participantLimit) {
		this.name = name;
		this.shortDescriptor = shortDescriptor;
		this.participantLimit = participantLimit;
		participants = new ArrayList<>();
		participants.add(MainActivity.getSignedInUser());
	}

	public ArrayList<User> getParticipants() {
		return participants;
	}

	public void setParticipants(ArrayList<User> participants) {
		this.participants = participants;
	}

	public int getParticipantLimit() {
		return participantLimit;
	}

	public void setParticipantLimit(int participantLimit) {
		this.participantLimit = participantLimit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortDescriptor() {
		return shortDescriptor;
	}

	public void setShortDescriptor(String shortDescriptor) {
		this.shortDescriptor = shortDescriptor;
	}
}
