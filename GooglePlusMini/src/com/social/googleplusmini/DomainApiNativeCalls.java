package com.social.googleplusmini;

import java.io.IOException;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plusDomains.PlusDomains;
import com.google.api.services.plusDomains.model.Circle;
import com.google.api.services.plusDomains.model.CircleFeed;
import com.google.api.services.plusDomains.model.PeopleFeed;
import com.google.api.services.plusDomains.model.Person;

public class DomainApiNativeCalls extends Activity {
	String[] avail_accounts;
	private AccountManager mAccountManager;
	ListView list;
	ArrayAdapter<String> adapter;
	SharedPreferences pref;
	Button selectAccount, api_call;
	String token = null;
	Person mePerson = null;
	PlusDomains plusDomains;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.callnativeapidomain);
		selectAccount = (Button) findViewById(R.id.select_button);
		api_call = (Button) findViewById(R.id.apicall_button);
		avail_accounts = getAccountNames();
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, avail_accounts);
		pref = getSharedPreferences("AppPref", MODE_PRIVATE);
		selectAccount.setOnClickListener(new OnClickListener() {
			Dialog accountDialog;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (avail_accounts.length != 0) {
					accountDialog = new Dialog(DomainApiNativeCalls.this);
					accountDialog.setContentView(R.layout.accounts_dialog);
					accountDialog.setTitle("Select Google Account");
					list = (ListView) accountDialog.findViewById(R.id.list);
					list.setAdapter(adapter);
					list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							SharedPreferences.Editor edit = pref.edit();
							// Storing Data using SharedPreferences
							edit.putString("Email", avail_accounts[position]);
							edit.commit();
							new Authenticate().execute();
							accountDialog.cancel();
						}
					});
					accountDialog.show();
				} else {
					Toast.makeText(getApplicationContext(),
							"No accounts found, Add a Account and Continue.",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		api_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("token", token);
				if (token != null) {
					Log.d("in call--", " ----11--11--");
					new getname().execute();

				}

			}
		});
	}

	private String[] getAccountNames() {
		// TODO Auto-generated method stub
		mAccountManager = AccountManager.get(this);
		Account[] accounts = mAccountManager
				.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
		String[] names = new String[accounts.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = accounts[i].name;
		}
		return names;
	}

	private class Authenticate extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;
		String mEmail;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DomainApiNativeCalls.this);
			pDialog.setMessage("Authenticating....");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			mEmail = pref.getString("Email", "");
			pDialog.show();
		}

		@Override
		protected void onPostExecute(String token) {
			pDialog.dismiss();
			if (token != null) {
				SharedPreferences.Editor edit = pref.edit();
				// Storing Access Token using SharedPreferences
				edit.putString("Access Token", token);
				edit.commit();
				Log.i("Token", "Access Token retrieved:" + token);
				Toast.makeText(getApplicationContext(),
						"Access Token is " + token, Toast.LENGTH_SHORT).show();
				selectAccount.setText(pref.getString("Email", "")
						+ " is Authenticated");
			}
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			try {

				token = GoogleAuthUtil
						.getToken(
								DomainApiNativeCalls.this,
								mEmail,
								"oauth2:https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/plus.circles.read https://www.googleapis.com/auth/plus.profiles.read");

			} catch (IOException transientEx) {
				// Network or server error, try later
				Log.e("IOException", transientEx.toString());
			} catch (UserRecoverableAuthException e) {
				// Recover (with e.getIntent())
				startActivityForResult(e.getIntent(), 1001);
				Log.e("AuthException", e.toString());
			} catch (GoogleAuthException authEx) {
				// The call is not ever expected to succeed
				// assuming you have already verified that
				// Google Play services is installed.
				Log.e("GoogleAuthException", authEx.toString());
			}
			return token;
		}
	};

	public class getname extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			Log.d("inside second doin", token);
			// TODO Auto-generated method stub
			if (token != null) {
				System.out.println(token);

				GoogleCredential cred = new GoogleCredential()
						.setAccessToken(token);
				PlusDomains plusDomains = new PlusDomains.Builder(
						new NetHttpTransport(), new JacksonFactory(), cred)
						.setApplicationName("Google Plus Mini").build();
				Person mePerson = null;
				try {
					mePerson = plusDomains.people().get("me").execute();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (mePerson != null) {
					System.out.println(mePerson);
				}
				PlusDomains.Circles.List listCircles = null;
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
						System.out.println(circle.getDisplayName()
								+ " circle id=" + circle.getId());

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
				String circleId = "d1a101e0a381add";

				PlusDomains.People.ListByCircle listPeople = null;
				try {
					listPeople = plusDomains.people().listByCircle(circleId);
					System.out.println("!@#@#" + listPeople);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				listPeople.setMaxResults(100L);

				PeopleFeed peopleFeed = null;
				try {
					peopleFeed = listPeople.execute();
					System.out.println("``````" + peopleFeed);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Google+ users circled:");

				// This example only displays one page of results.
				if (peopleFeed.getItems() != null
						&& peopleFeed.getItems().size() > 0) {
					for (Person person : peopleFeed.getItems()) {
						// System.out.println("\t" + person.getDisplayName());
					}
				}

			}
			return null;

		}
	}

	public class getcircle extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub

			return null;
		}

	}
}
