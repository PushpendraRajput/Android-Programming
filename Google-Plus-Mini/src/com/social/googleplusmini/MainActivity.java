package com.social.googleplusmini;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ProgressDialog;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

public class MainActivity extends ActionBarActivity implements
		ConnectionCallbacks, OnConnectionFailedListener,
		ResultCallback<LoadPeopleResult> {

	private static final int RC_SIGN_IN = 0;
	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
	private static final int PROFILE_PIC_SIZE = 400;
	private static final String TAG = null;
	private GoogleApiClient mGoogleApiClient;
	private boolean mIntentInProgress;
	SignInButton signIn;
	private ProgressDialog mConnectionProgressDialog;
	private TextView welcome, emailID, Name;
	private ImageView profilePic;
	ArrayList<Contact> contactlist;
	// public static final Creator<Scope> CREATOR;
	private static List<String> SCOPE = Arrays.asList(
			"https://www.googleapis.com/auth/plus.me",
			"https://www.googleapis.com/auth/plus.stream.write");
	String scopeUri = "https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/plus.stream.write https://www.googleapis.com/auth/plus.circles.read";

	// public Scope (String scopeUri)

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN)
				.addScope(Plus.SCOPE_PLUS_PROFILE).build();

		mConnectionProgressDialog = new ProgressDialog(this);
		mConnectionProgressDialog.setMessage("Signing in...");
		// signIn = (SignInButton) findViewById(R.id.sign_in_button);
		Name = (TextView) findViewById(R.id.userName);
		emailID = (TextView) findViewById(R.id.emailID);
		profilePic = (ImageView) findViewById(R.id.profilePic);
		signIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mConnectionProgressDialog.show();
				mGoogleApiClient.connect();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		if (result.hasResolution()) {
			try {
				result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
			} catch (SendIntentException e) {
				mGoogleApiClient.connect();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		mConnectionProgressDialog.dismiss();
		/*
		 * String accountName = (mGoogleApiClient).get; accountName += "    " +
		 * (mGoogleApiClient).getCurrentPerson(); Toast.makeText(this,
		 * accountName + " is connected.", Toast.LENGTH_LONG) .show();
		 */

		if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
			String TAG = "---<><><>---";
			Person currentPerson = Plus.PeopleApi
					.getCurrentPerson(mGoogleApiClient);
			Log.e(TAG, "All Data=" + currentPerson.toString());
			String personName = currentPerson.getDisplayName();
			String personPhotoUrl = currentPerson.getImage().getUrl();
			String personGooglePlusProfile = currentPerson.getUrl();
			String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

			Log.e(TAG, "Name: " + personName + ", plusProfile: "
					+ personGooglePlusProfile + ", email: " + email
					+ ", Image: " + personPhotoUrl);
			Name.setText(personName);
			Name.setVisibility(View.VISIBLE);
			emailID.setText(email);
			emailID.setVisibility(View.VISIBLE);
			profilePic.setVisibility(View.VISIBLE);
			personPhotoUrl = personPhotoUrl.substring(0,
					personPhotoUrl.length() - 2)
					+ PROFILE_PIC_SIZE;
			new LoadProfileImage(profilePic).execute(personPhotoUrl);
			PendingResult<LoadPeopleResult> circlelist = Plus.PeopleApi
					.loadVisible(mGoogleApiClient, null);
			// PendingResult<LoadPeopleResult>
			// clist=Plus.PeopleApi.loadConnected(mGoogleApiClient);
			// Log.e(TAG, circlelist.toString());
			circlelist.setResultCallback(this);
			// clist.setResultCallback(this);
			// Plus.PeopleApi.loadVisible(mGoogleApiClient,
			// null).setResultCallback((ResultCallback<LoadPeopleResult>) this);

		}

	}

	/*
	 * public void setResultCallback (ResultCallback<LoadPeopleResult>
	 * peopleData) { Log.d(TAG, "onResult called - setting adapter");
	 * 
	 * Contact contact; ArrayList<Contact> arrayListContacts = new
	 * ArrayList<Contact>();
	 * 
	 * if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS)
	 * {
	 * 
	 * PersonBuffer personBuffer = peopleData.getPersonBuffer();
	 * Log.d("Contacts->", personBuffer.toString()); }
	 * 
	 * }
	 */
	public void onResult(LoadPeopleResult peopleData) {

		Log.d(TAG, "onResult called - setting adapter");

		/*
		 * Contact contact; ArrayList<Contact> arrayListContacts = new
		 * ArrayList<Contact>();
		 */
		if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {

			PersonBuffer personBuffer = peopleData.getPersonBuffer();
			Log.d("Contacts->", personBuffer.toString());
			try {
				int count = personBuffer.getCount();
				// System.out.println(" size of person"+count);
				// String countstring=(String)count;
				Log.d("contacts-->>Size<<", " size of person" + count);
				for (int i = 0; i < count; i++) {

					Contact contact = new Contact(
							personBuffer.get(i).hasId() ? personBuffer.get(i)
									.getId() : null, personBuffer.get(i)
									.hasDisplayName() ? personBuffer.get(i)
									.getDisplayName() : null, personBuffer.get(
									i).hasUrl() ? personBuffer.get(i).getUrl()
									: null,
							personBuffer.get(i).hasImage() ? personBuffer
									.get(i).getImage().getUrl() : null);
					String temp = "id: " + personBuffer.get(i).getId()
							+ " name:" + personBuffer.get(i).getDisplayName()
							+ " url:" + personBuffer.get(i).getUrl() + " pic:"
							+ personBuffer.get(i).getImage();
					Log.d("Data--", temp);
					// contactlist.add(contact);

				}

			} finally {
				personBuffer.close();
			}
		} else {
			Log.e(TAG,
					"Error requesting visible circles : "
							+ peopleData.getStatus());
		}
		// Log.e("Arrayllist",contactlist.toString());
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub

	}

	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	protected void onStop() {
		super.onStop();

		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
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

	public class Contact {

		private static final String TAG = "Contact";

		private final String _ID;
		private final String name;
		private final String profileUrl;
		private final String pictureUrl;

		public Contact(String _ID, String name, String profileUrl,
				String pictureUrl) {

			this._ID = _ID;
			this.name = name;
			this.profileUrl = profileUrl;

			if (pictureUrl != null) {
				int lastIndexForRedimensioning = pictureUrl.lastIndexOf("?sz=");
				this.pictureUrl = lastIndexForRedimensioning < 0 ? pictureUrl
						: pictureUrl.substring(0, lastIndexForRedimensioning);
			} else {
				this.pictureUrl = null;
			}

		}

		/**
		 * It returns the Google+ profile picture URL for the contact.
		 */
		public String getPictureUrl() {
			return pictureUrl;
		}

		/**
		 * It returns the display name for the contact.
		 */
		public String getName() {
			return name;
		}

		/**
		 * It returns the Google+ profile name for the contact.
		 */
		public String getProfileUrl() {
			return profileUrl;
		}

		@Override
		public String toString() {

			StringBuffer sb = new StringBuffer();

			sb.append("ID : " + _ID);
			sb.append("\nName : " + name);
			sb.append("\nprofileUrl : " + profileUrl);
			sb.append("\npictureUrl : " + pictureUrl);

			Log.d(TAG, sb.toString());

			return sb.toString();
		}

	}
}
