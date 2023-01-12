package com.example.smapplication.Model;

import android.media.Image;

public class Event {
	private int id;
	private int creator_id;
	private static int id_counter = 0;
	private String title;
	private String decription;
	private Image image;

	public Event(){
	}

	public Event(int cid) {
		this.id = id_counter++;
		this.creator_id = cid;

	}

	public Event(int cid, String title, Image image) {
		this.id = id_counter++;
		this.title = title;
		this.creator_id = cid;
		this.image = image;

	}

	public int getCreator_id() {
		return creator_id;
	}

	public String getDecription() {
		return decription;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public int getId() {
		return id;
	}


	public static void setId_counter(int id_counter) {
		Event.id_counter = id_counter;
	}

	public static int getId_counter() {
		return id_counter;
	}

	public void setDecription(String decription) {
		this.decription = decription;
	}

}
