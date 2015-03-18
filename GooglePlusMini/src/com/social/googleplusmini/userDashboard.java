package com.social.googleplusmini;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plusDomains.PlusDomains;
import com.google.api.services.plusDomains.model.Person;

public class userDashboard extends Activity {
	TextView Name, emailID;
	ImageView profilePic;
	SharedPreferences pref;
	Person mePerson = null;
	String token;
	Button getCircle;
	private static final int PROFILE_PIC_SIZE = 400;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_user);
		Intent intent = getIntent();
		token = intent.getStringExtra("passingdefault");

		// System.out.println("Token Received::"+value+"  defauolt value:"+value2);
		Name = (TextView) findViewById(R.id.userName);
		emailID = (TextView) findViewById(R.id.emailID);
		profilePic = (ImageView) findViewById(R.id.profilePic);
		new getname().execute();
		getCircle = (Button) findViewById(R.id.getCircles);
		getCircle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(userDashboard.this,
						circleList.class);

				myIntent.putExtra("passingdefault", token);// Optional
															// parameters
				userDashboard.this.startActivity(myIntent);
			}
		});

	}

	private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public LoadProfileImage(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		@Override
		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon = null;

			try {
				InputStream inputStream = new URL(urldisplay).openStream();
				mIcon = BitmapFactory.decodeStream(inputStream);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mIcon;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			bmImage.setImageBitmap(bitmap);
		}
	}

	public class getname extends AsyncTask<String, Void, Person> {

		@Override
		protected Person doInBackground(String... params) {
			Log.d("inside second doin", token);
			// TODO Auto-generated method stub
			if (token != null) {
				System.out.println(token);

				GoogleCredential cred = new GoogleCredential()
						.setAccessToken(token);

				PlusDomains plusDomains = new PlusDomains.Builder(
						new NetHttpTransport(), new JacksonFactory(), cred)
						.setApplicationName("Google Plus Mini").build();

				try {
					mePerson = plusDomains.people().get("me").execute();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			if (mePerson != null) {
				return mePerson;
			}
			return null;

		}

		protected void onPostExecute(Person mePerson) {
			if (mePerson != null) {
				System.out.println(mePerson);
				ArrayList<String> arr = new ArrayList<String>();
				JSONObject obj = new JSONObject(mePerson);
				Iterator iter = obj.keys();
				while (iter.hasNext()) {
					String key = (String) iter.next();
					try {
						arr.add(obj.getString(key));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println(arr);

				String name = mePerson.getDisplayName();
				Name.setText(name);
				// String emailid=mePerson.getEmails().toString();
				// emailID.setText(emailid);
				LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
				TextView tv1 = new TextView(userDashboard.this);
				tv1.setText("Organizations:");
				ll.addView(tv1);

				for (int i = 0; i < mePerson.getOrganizations().size(); i++) {
					TextView tv = new TextView(userDashboard.this);
					TextView tv2 = new TextView(userDashboard.this);
					TextView tv3 = new TextView(userDashboard.this);
					tv.setText("Name: "
							+ mePerson.getOrganizations().get(i).getName());
					tv2.setText("Title: "
							+ mePerson.getOrganizations().get(i).getTitle());
					tv3.setText("Type: "
							+ mePerson.getOrganizations().get(i).getType());
					ll.addView(tv);
					ll.addView(tv2);
					ll.addView(tv3);
				}

				// tv.setText("Name:"+mePerson.getOrganizations().
				// .get(0).getName()+
				// "Title:"+mePerson.getOrganizations().get(0).getTitle());

				/*
				 * if(!(mePerson.getOrganizations().isEmpty())) { int count=0;
				 * while(!(mePerson.getOrganizations().isEmpty())) { TextView
				 * tv=new TextView(userDashboard.this);
				 * tv.setText("Name:"+mePerson
				 * .getOrganizations().get(0).getName()+
				 * "Title:"+mePerson.getOrganizations().get(0).getTitle());
				 * ll.addView(tv); count++; } }
				 */
				String personPhotoUrl = mePerson.getImage().getUrl();
				personPhotoUrl = personPhotoUrl.substring(0,
						personPhotoUrl.length() - 2)
						+ PROFILE_PIC_SIZE;
				new LoadProfileImage(profilePic).execute(personPhotoUrl);

			}
		}

	}

	public class wrapper {
		String personPhotoUrl;
	}

}