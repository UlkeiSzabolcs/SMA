package com.example.smapplication.Activities;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.smapplication.Model.User;
import com.example.smapplication.databinding.ActivityLoginBinding;
import com.example.smapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

	private ActivityLoginBinding binding;

	private FirebaseDatabase db = FirebaseDatabase.getInstance("https://smapplication-c9fd9-default-rtdb.europe-west1.firebasedatabase.app");
	private DatabaseReference dbRoot = db.getReference();

	MutableLiveData<String> loginResult = new MutableLiveData<>();
	User logInUser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loginResult.setValue("pending");
		binding = ActivityLoginBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());


		final EditText usernameEditText = binding.username;
		final EditText passwordEditText = binding.password;
		final Button loginButton = binding.login;
		final ProgressBar loadingProgressBar = binding.loading;

		loginResult.observe(this, new Observer<String>() {
			@Override
			public void onChanged(String result) {
				loadingProgressBar.setVisibility(View.INVISIBLE);
				if(result.equals("login")){
					Intent mainIntent = new Intent(binding.getRoot().getContext(), MainActivity.class);
					mainIntent.putExtra("userID", String.valueOf(logInUser.getId()));
					startActivity(mainIntent);
					finish();
				}
				else{
					if(result.equals("popup")) {
						LayoutInflater inflater = (LayoutInflater) binding.getRoot().getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
						View popup = inflater.inflate(R.layout.register_popup, null);
						final PopupWindow popupWindow = new PopupWindow(popup, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, true);
						popupWindow.showAtLocation(binding.login, Gravity.CENTER, 0, 0);

						popup.findViewById(R.id.cancelRegistrationButton).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								popupWindow.dismiss();
							}
						});

						popup.findViewById(R.id.RegisterButton).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								User newUser = new User(usernameEditText.getText().toString(), passwordEditText.getText().toString());
								logInUser = newUser;
								dbRoot.child("Users").child(String.valueOf(newUser.getId())).setValue(newUser);

								popupWindow.dismiss();
								Toast.makeText(LoginActivity.this, "User created with ID" + String.valueOf(logInUser.getId()), Toast.LENGTH_SHORT).show();
							}
						});
					}
				}
			}
		});

		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadingProgressBar.setVisibility(View.VISIBLE);
				login(usernameEditText.getText().toString(),
						passwordEditText.getText().toString());

			}
		});
	}

	private void login(String username, String password){
		dbRoot.child("Users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DataSnapshot> task) {
				User signInUser = null;
				int counter = 0;
				for (DataSnapshot user : task.getResult().getChildren()) {
					signInUser = user.getValue(User.class);
					counter++;

					if (signInUser.getId() >= User.getId_counter())
						User.setId_counter(signInUser.getId() + 1);
					if (signInUser.getUsername().equals(username) &&
							signInUser.getPassword().equals(password)) {
						logInUser = signInUser;
						loginResult.setValue("login");
						break;
					}
					else if(counter == task.getResult().getChildrenCount())
						loginResult.setValue("popup");
				}
				if(counter == 0)
					loginResult.setValue("popup");
			}
		});

	}

	private void showLoginFailed(@StringRes Integer errorString) {
		Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
	}
}