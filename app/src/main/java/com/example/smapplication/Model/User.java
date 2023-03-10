package com.example.smapplication.Model;

public class User {
	private int id;
	private String username;
	private String password;
	private static int id_counter = 0;

	public User() {
		id = id_counter++;
	}

	public User(String username, String password) {
		id = id_counter++;
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static int getId_counter() {
		return id_counter;
	}

	public static void setId_counter(int id_counter) {
		User.id_counter = id_counter;
	}

	public int getId() {
		return id;
	}
}
