package com.example.unipitest;

import com.example.async.tasks.PostContactForm;
import com.example.preferences.SettingsActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactActivity extends MainActivity {

	Button send;
	ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getLayoutInflater().inflate(R.layout.activity_contact, frameLayout);
		Button send = (Button) findViewById(R.id.send);
		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendMessage();
			}
		});

	}

	public void sendMessage() {

		EditText name = (EditText) findViewById(R.id.edittextName);
		EditText email = (EditText) findViewById(R.id.edittextemail);
		EditText phone = (EditText) findViewById(R.id.edittextphone);
		EditText body = (EditText) findViewById(R.id.edittextbody);
		// edo balame ligi asfaleia gia ta stixeia pou tha stalthoun..
		// mail address to String
		String emailtest = email.getText().toString().trim();
		// test mail - emailPattern
		String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

		if (name.getText().toString().isEmpty()) {
			/* if the name is Empty */
			Toast.makeText(getApplicationContext(), R.string.form_name,
					Toast.LENGTH_SHORT).show();
		} else if (email.getText().toString().isEmpty()) {
			/* if the email is Empty */
			Toast.makeText(getApplicationContext(), R.string.form_email,
					Toast.LENGTH_SHORT).show();
		} else if (phone.getText().toString().isEmpty()) {
			/* if the phone is Empty */
			Toast.makeText(getApplicationContext(), R.string.form_phone,
					Toast.LENGTH_SHORT).show();
		} else if (phone.length() != 10) {
			/* the phone size = 10 */
			Toast.makeText(getApplicationContext(), R.string.form_phone_size,
					Toast.LENGTH_SHORT).show();
		} else if (body.getText().toString().isEmpty()) {
			/* if the body is Empty */
			Toast.makeText(getApplicationContext(), R.string.form_message,
					Toast.LENGTH_SHORT).show();
		} else if (emailtest.matches(emailPattern) && email.length() > 0) {
			/* if is all OK */
			progress = ProgressDialog.show(this,
					getString(R.string.Send_Message),
					getString(R.string.sending), true);
			PostContactForm datatask = new PostContactForm(this, progress);
			datatask.execute(name.getText().toString(), email.getText()
					.toString(), phone.getText().toString(), body.getText()
					.toString());
		} else {
			// Error mail - Invalid email address
			Toast.makeText(getApplicationContext(), R.string.Invalid_email,
					Toast.LENGTH_SHORT).show();
		}
	}

	public void showToast(String response) {
		Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent Settings = new Intent(ContactActivity.this,
					SettingsActivity.class);
			startActivity(Settings);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
