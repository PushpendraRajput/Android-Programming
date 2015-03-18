package com.social.googleplusmini;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class firstActivitySignIn extends Activity {

	Button googleSignin, info;
	String[] avail_accounts;
	private AccountManager mAccountManager;

	ListView list;
	String token = null;
	ArrayAdapter<String> adapter;
	SharedPreferences pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the view
		setContentView(R.layout.signin_activity_first);
		// get id of sigin button
		googleSignin = (Button) findViewById(R.id.googleSignin);
		// get id of information button
		info = (Button) findViewById(R.id.info);
		// get list of available accounts
		avail_accounts = getAccountNames();
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, avail_accounts);
		// Get list of available accounts and perform sign in operation
		googleSignin.setOnClickListener(new OnClickListener() {
			Dialog accountDialog;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// if there are accounts in the phone show account picker dailog
				// box
				if (avail_accounts.length != 0) {
					accountDialog = new Dialog(firstActivitySignIn.this);
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
		info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showdailog();
			}
		});
		if (isFirstTime()) {
			showdailog();
		}

	}

	// Check if application is running for first time or not
	// if first time show information dailog to user
	private boolean isFirstTime() {
		pref = getPreferences(MODE_PRIVATE);
		boolean ranBefore = pref.getBoolean("RanBefore", false);
		if (!ranBefore) {
			// first time
			SharedPreferences.Editor editor = pref.edit();
			editor.putBoolean("RanBefore", true);
			editor.commit();
		}
		return !ranBefore;
	}

	// Information Dailog builder function
	public void showdailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"This Application only works with Google App Accounts. Please Login using your Google App Account. Thank you!")
				.setCancelable(false).setTitle("Welcome to Goolge Plus Mini")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do things
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	// Get list of google accounts
	private String[] getAccountNames() {
		// TODO Auto-generated method stub
		mAccountManager = AccountManager.get(this);
		Account[] accounts = mAccountManager
				.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
		String[] names = new String[accounts.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = accounts[i].name;
		}
		System.out.println(names);
		return names;
	}

	// perform authentication of user
	private class Authenticate extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;
		String mEmail;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(firstActivitySignIn.this);
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

				Toast.makeText(getApplicationContext(), "Welcome",
						Toast.LENGTH_SHORT).show();
				Intent myIntent = new Intent(firstActivitySignIn.this,
						Profile_Activity_Main.class);
				String temp = pref.getString("Access Token", "");
				System.out.println("temp---" + temp);
				myIntent.putExtra("tokenpass", temp);
				myIntent.putExtra("passingdefault", token);// Optional
															// parameters
				firstActivitySignIn.this.startActivity(myIntent);

			}
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub

			try {

				token = GoogleAuthUtil
						.getToken(
								firstActivitySignIn.this,
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

}
