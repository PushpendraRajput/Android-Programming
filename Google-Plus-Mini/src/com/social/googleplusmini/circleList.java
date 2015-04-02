package com.social.googleplusmini;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plusDomains.PlusDomains;
import com.google.api.services.plusDomains.model.Circle;
import com.google.api.services.plusDomains.model.CircleFeed;
import com.google.api.services.plusDomains.model.Person;

public class circleList extends Activity {
	public static final String TAG = "circcle List";
	String token;
	Person mePerson = null;
	PlusDomains.Circles.List listCircles = null;
	// setting up arraylist
	ArrayList<circledata> arrayListContacts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setting up view
		setContentView(R.layout.list_circle);
		// get data from intent
		Intent intent = getIntent();
		token = intent.getStringExtra("passingdefault");
		System.out.println(token);
		// start AsyncTask
		new getcircle().execute();

	}

	private class getcircle extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {

			// TODO Auto-generated method stub
			circledata c;
			ArrayList<circledata> arrayListContacts = new ArrayList<circledata>();
			GoogleCredential cred = new GoogleCredential()
					.setAccessToken(token);
			PlusDomains plusDomains = new PlusDomains.Builder(
					new NetHttpTransport(), new JacksonFactory(), cred)
					.setApplicationName("Google Plus Mini").build();
			try {
				listCircles = plusDomains.circles().list("me");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			listCircles.setMaxResults(5L);
			CircleFeed circleFeed = null;
			try {
				circleFeed = listCircles.execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<Circle> circles = circleFeed.getItems();

			// Loop until no additional pages of results are available.
			while (circles != null) {
				System.out.println("---------" + circles);
				for (Circle circle : circles) {
					// System.out.println(circle.getDisplayName()+" circle id="+circle.getId()+" circle count="+circle.getPeople().getTotalItems());
					c = new circledata(circle.getDisplayName(), circle.getId(),
							circle.getPeople().getTotalItems().toString());
					arrayListContacts.add(c);
				}

				// When the next page token is null, there are no additional
				// pages of
				// results. If this is the case, break.
				if (circleFeed.getNextPageToken() != null) {
					// Prepare the next page of results
					listCircles.setPageToken(circleFeed.getNextPageToken());

					// Execute and process the next page request
					try {
						circleFeed = listCircles.execute();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					circles = circleFeed.getItems();
				} else {
					circles = null;
				}
			}

			return null;
		}

		protected void onPostExecute() {
			Log.e("in post exicute", "sdfsdfsdsfsdf");
			populateListView();
		}
	}

	public class circledata {
		private final String displayName;
		private final String id;
		private final String totalItems;

		public circledata(String displayName, String id, String totalItems) {
			this.displayName = displayName;
			this.id = id;
			this.totalItems = totalItems;

		}

		public String getdisplayName() {
			return displayName;
		}

		public String getid() {
			return id;
		}

		public String gettotalItems() {
			return totalItems;
		}

		@Override
		public String toString() {

			StringBuffer sb = new StringBuffer();

			sb.append("displayName : " + displayName);
			sb.append("\nid " + id);
			sb.append("\ntotalitems : " + totalItems);

			Log.d(TAG, sb.toString());

			return sb.toString();
		}
	}

	public void populateListView() {
		// TODO Auto-generated method stub
		System.out.println("is this it adpater=" + arrayListContacts);
		// get array

		// build adapter
		ArrayAdapter<circledata> adapter = new ArrayAdapter<circleList.circledata>(
				circleList.this, R.layout.list_circle_view, arrayListContacts);
		// configure list view
		ListView list = (ListView) findViewById(R.id.circlelisting);
		list.setAdapter(adapter);

	}
}
