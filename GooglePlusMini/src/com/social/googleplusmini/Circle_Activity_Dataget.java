package com.social.googleplusmini;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plusDomains.PlusDomains;
import com.google.api.services.plusDomains.model.Circle;
import com.google.api.services.plusDomains.model.PeopleFeed;
import com.google.api.services.plusDomains.model.Person;

public class Circle_Activity_Dataget extends ActionBarActivity {
	ListView list;
	String token;
	String circleId;
	// Setting up Arrary List
	ArrayList<String> friendNames;
	ArrayList<String> friendIds;
	ArrayList<Bitmap> friendImages;
	// setting up adapter
	UserListHolder adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set up view
		setContentView(R.layout.activity_cricle);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		// Get Data from Intent
		Bundle extras = getIntent().getExtras();
		// if null get from shared preferences
		if (extras == null) {
			SharedPreferences sharedPref = PreferenceManager
					.getDefaultSharedPreferences(this);
			token = sharedPref.getString("token", "");
			circleId = sharedPref.getString("circleId", "");
		} else if (extras != null) {
			token = extras.getString("token");
			System.out.println("token: " + token);
			circleId = extras.getString("circleId");
			System.out.println("circleId: " + circleId);
		}

		// Create object of SharedPreferences.
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("token", token);
		editor.putString("circleId", circleId);
		editor.commit();

		friendNames = new ArrayList<String>();
		friendIds = new ArrayList<String>();
		friendImages = new ArrayList<Bitmap>();

		new FriendsTask().execute();
		// set up list view
		list = (ListView) findViewById(R.id.friendList);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_cricle, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private class FriendsTask extends
			AsyncTask<String, Void, Map<String, String>> {
		protected Bitmap image;

		@Override
		protected Map<String, String> doInBackground(String... params) {
			// set up profile info
			Map<String, String> profileInfo = new HashMap<String, String>();
			GoogleCredential googleCredential = new GoogleCredential()
					.setAccessToken(token);
			PlusDomains plusDomains = new PlusDomains.Builder(
					new NetHttpTransport(), new JacksonFactory(),
					googleCredential).setApplicationName("GPlusLab").build();
			try {

				Circle circle = plusDomains.circles().get(circleId).execute();

				// Person mePerson = plusDomains.people().get("me").execute();

				// Loop until no additional pages of results are available.
				Log.i("Circles: ", "");
				if (circle != null) {
					// Log.i("",circle.getDisplayName()+"  "+circle.getId());

					// to fetch people in circle
					PlusDomains.People.ListByCircle listPeople = plusDomains
							.people().listByCircle(circleId);
					listPeople.setMaxResults(100L);

					PeopleFeed peopleFeed = listPeople.execute();

					// This example only displays one page of results.
					if (peopleFeed.getItems() != null
							&& peopleFeed.getItems().size() > 0) {
						for (Person person : peopleFeed.getItems()) {
							Log.i("Person Name: ", person.getDisplayName());
							friendNames.add(person.getDisplayName());
							friendIds.add(person.getId());
							System.out.println("Profile Pic URL: "
									+ person.getImage().getUrl());
							try {
								String profilePicURL = person
										.getImage()
										.getUrl()
										.substring(
												0,
												person.getImage().getUrl()
														.length() - 6);
								InputStream in = new java.net.URL(profilePicURL)
										.openStream();
								image = BitmapFactory.decodeStream(in);
							} catch (Exception e) {
								Log.e("Error", e.getMessage());
								e.printStackTrace();
							}
							friendImages.add(image);
						}
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			return profileInfo;
		}

		@Override
		protected void onPostExecute(Map<String, String> profileInfo) {
			super.onPostExecute(profileInfo);

			adapter = new UserListHolder(Circle_Activity_Dataget.this,
					friendNames, friendImages);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// start intent based upon the friend selected
					Intent i = new Intent(Circle_Activity_Dataget.this,
							CircleUserProfile.class);
					i.putExtra("token", token);
					i.putExtra("circleId", circleId);
					i.putExtra("friendId", friendIds.get(position));
					startActivity(i);

				}
			});
		}
	}
}