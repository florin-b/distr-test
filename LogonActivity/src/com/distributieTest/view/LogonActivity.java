/**
 * @author florinb
 *
 */
package com.distributieTest.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Locale;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.distributieTest.listeners.AsyncTaskListener;
import com.distributieTest.model.AsyncTaskWSCall;
import com.distributieTest.model.InfoStrings;
import com.distributieTest.model.UserInfo;
import com.distributieTest.model.Utils;

public class LogonActivity extends Activity implements AsyncTaskListener {

	int val = 0;

	private static final String METHOD_NAME = "userLogon";

	ProgressBar progressBarWheel;
	EditText txtUserName, txtPassword;

	ProgressWheel pw;
	RotaryKnobView jogView;
	private String buildVer = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.LRTheme);
		setContentView(R.layout.activity_logon);
		InitialUISetup();

	}

	private void InitialUISetup() {

		try {

			// formatare data si numere = en
			String languageToLoad = "en";
			Locale locale = new Locale(languageToLoad);
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config,
					getBaseContext().getResources().getDisplayMetrics());
			//

			PackageInfo pInfo = null;
			try {
				pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			} catch (Exception e) {
				Toast.makeText(LogonActivity.this, e.toString(), Toast.LENGTH_LONG).show();
			}

			buildVer = String.valueOf(pInfo.versionCode);

			pw = (ProgressWheel) findViewById(R.id.pw_spinner);
			pw.setVisibility(View.INVISIBLE);

			progressBarWheel = (ProgressBar) findViewById(R.id.progress_bar_wheel);

			progressBarWheel.setVisibility(View.INVISIBLE);
			txtUserName = (EditText) findViewById(R.id.txtUserName);
			txtUserName.setHint("Utilizator");

			txtPassword = (EditText) findViewById(R.id.txtPassword);
			txtPassword.setHint("Parola");

			txtUserName.setText("DVELICU");
			txtPassword.setText("1234");

			jogView = (RotaryKnobView) findViewById(R.id.jogView);
			jogView.setKnobListener(new RotaryKnobView.RotaryKnobListener() {

				@Override
				public void onKnobChanged(int arg) {

					if (0 == arg) {
						val = 0;
						progressBarWheel.setVisibility(View.INVISIBLE);

					} else {
						val += 1;
						progressBarWheel.setVisibility(View.VISIBLE);
						progressBarWheel.setProgress(val);

						if (progressBarWheel.getProgress() >= 50) {
							progressBarWheel.setVisibility(View.INVISIBLE);
							val = 0;
							jogView.setEnabled(false);
							startSpinner();
							performLoginThread();

						}

					}

				}

			});

		} catch (Exception ex) {
			Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
		}

	}

	public void performLoginThread() {
		try {

			HashMap<String, String> params = new HashMap<String, String>();

			String userN = txtUserName.getText().toString().trim();
			String passN = txtPassword.getText().toString().trim();

			params.put("userId", userN);
			params.put("userPass", passN);
			params.put("ipAdr", "-1");

			AsyncTaskWSCall call = new AsyncTaskWSCall(this, METHOD_NAME, params);
			call.getCallResults();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	public void validateLogin(String result) {
		if (!result.equals("-1") && result.length() > 0) {
			String[] token = result.split("#");

			if (token[0].equals("0")) {
				InfoStrings.showCustomToast(this, "Cont inexistent!");

			}
			if (token[0].equals("1")) {
				InfoStrings.showCustomToast(this, "Cont blocat 60 de minute!");

			}
			if (token[0].equals("2")) {
				InfoStrings.showCustomToast(this, "Parola incorecta!");
			}
			if (token[0].equals("3")) {

				if (token[5].equals("37")) // sofer
				{

					UserInfo uInfo = UserInfo.getInstance();

					String tempAgCod = token[4].toString();

					if (tempAgCod.equalsIgnoreCase("-1")) {
						Toast.makeText(getApplicationContext(), "Utilizator nedefinit!", Toast.LENGTH_SHORT).show();
						return;
					}

					StringBuffer sb = new StringBuffer();

					for (int i = 0; i < 8 - token[4].length(); i++) {
						sb.append('0').append(tempAgCod);
					}

					tempAgCod = sb.toString();
					uInfo.setNume(token[3]);
					uInfo.setFiliala(token[2]);
					uInfo.setCod(tempAgCod);
					uInfo.setUnitLog(Utils.getFiliala(token[2].toString()));

					// TEST!!
					uInfo.setCod("00120500");

					try {
						startSpinner();
						checkUpdate check = new checkUpdate();
						check.execute("dummy");

					} catch (Exception e) {
						Toast.makeText(LogonActivity.this, e.toString(), Toast.LENGTH_LONG).show();
					}

				} else {
					Toast.makeText(getApplicationContext(), "Acces interzis!", Toast.LENGTH_SHORT).show();

				}
			}
			if (token[0].equals("4")) {
				InfoStrings.showCustomToast(this, "Cont inactiv!");

			}
		} else {
			InfoStrings.showCustomToast(this, "Autentificare esuata!");

		}

	}

	private class checkUpdate extends AsyncTask<String, Void, String> {
		String errMessage = "";

		private checkUpdate() {
			super();

		}

		@Override
		protected String doInBackground(String... url) {
			String response = "";
			FTPClient mFTPClient = new FTPClient();
			FileOutputStream desFile2 = null;

			try {

				mFTPClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

				mFTPClient.connect("10.1.0.6", 21);

				if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {

					mFTPClient.login("litesfa", "egoo4Ur");

					mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
					mFTPClient.enterLocalPassiveMode();

					String sourceFile = "/Update/LiteSFA/DistributieVer.txt";

					desFile2 = new FileOutputStream("sdcard/download/DistributieVer.txt");
					mFTPClient.retrieveFile(sourceFile, desFile2);

				} else {
					errMessage = "Probeme la conectare!";
				}
			} catch (Exception e) {
				errMessage = e.getMessage();

			} finally {
				if (mFTPClient.isConnected()) {

					try {

						desFile2.close();
						mFTPClient.logout();
						mFTPClient.disconnect();
					} catch (IOException f) {
						errMessage = f.getMessage();
						Toast.makeText(LogonActivity.this, errMessage, Toast.LENGTH_LONG).show();
					}

				}
			}

			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				stopSpinner();

				if (!errMessage.equals("")) {
					Toast toast = Toast.makeText(LogonActivity.this, errMessage, Toast.LENGTH_SHORT);
					toast.show();
				} else {
					validateUpdate();
				}
			} catch (Exception e) {
				Toast.makeText(LogonActivity.this, e.toString(), Toast.LENGTH_LONG).show();
			}

		}

	}

	public void validateUpdate() throws IOException {

		FileInputStream fileIS = null;
		BufferedReader buf = null;

		try {

			File fVer = new File(Environment.getExternalStorageDirectory() + "/download/DistributieVer.txt");
			fileIS = new FileInputStream(fVer);
			buf = new BufferedReader(new InputStreamReader(fileIS));
			String readString = buf.readLine();
			String[] tokenVer = readString.split("#");

			if (!tokenVer[2].equals("0")) // 1 - fisierul este gata pentru
											// update, 0 - inca nu
			{

				if (Float.parseFloat(buildVer) < Float.parseFloat(tokenVer[3])) {
					// exista update
					try {
						downloadUpdate download = new downloadUpdate(this);
						download.execute("dummy");
					} catch (Exception e) {
						Toast.makeText(LogonActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
					}

				} else // nu exista update
				{
					stopSpinner();
					redirectView();
				}

			}

		} catch (Exception ex) {
			Toast.makeText(LogonActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
		} finally {

			if (fileIS != null)
				fileIS.close();

			if (buf != null)
				buf.close();

		}

	}

	private class downloadUpdate extends AsyncTask<String, Void, String> {
		String errMessage = "";

		private downloadUpdate(Context context) {
			super();

		}

		@Override
		protected String doInBackground(String... url) {
			String response = "";
			FTPClient mFTPClient = new FTPClient();
			FileOutputStream desFile1 = null, desFile2 = null;
			try {

				mFTPClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

				mFTPClient.connect("10.1.0.6", 21);

				if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {

					mFTPClient.login("litesfa", "egoo4Ur");

					mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
					mFTPClient.enterLocalPassiveMode();

					String sourceFile = "/Update/LiteSFA/Distributie.apk";
					desFile1 = new FileOutputStream("sdcard/download/Distributie.apk");
					mFTPClient.retrieveFile(sourceFile, desFile1);

					sourceFile = "/Update/LiteSFA/DistributieVer.txt";
					desFile2 = new FileOutputStream("sdcard/download/DistributieVer.txt");
					mFTPClient.retrieveFile(sourceFile, desFile2);

				} else {
					errMessage = "Probeme la conectare!";
				}
			} catch (Exception e) {
				errMessage = e.getMessage();
			} finally {
				if (mFTPClient.isConnected()) {

					try {
						if (desFile1 != null)
							desFile1.close();

						if (desFile2 != null)
							desFile2.close();

						mFTPClient.logout();
						mFTPClient.disconnect();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}

			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			try {

				stopSpinner();

				if (!errMessage.equals("")) {
					Toast toast = Toast.makeText(LogonActivity.this, errMessage, Toast.LENGTH_SHORT);
					toast.show();
				} else {
					startInstall();
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
			}
		}

	}

	public void startInstall() {

		String fileUrl = "/download/Distributie.apk";
		String file = android.os.Environment.getExternalStorageDirectory().getPath() + fileUrl;
		File f = new File(file);

		if (f.exists()) {

			// start install
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/"
					+ "Distributie.apk")), "application/vnd.android.package-archive");
			startActivity(intent);

			finish();
		} else {
			Toast toast = Toast
					.makeText(LogonActivity.this, "Fisier corupt, repetati operatiunea!", Toast.LENGTH_SHORT);
			toast.show();

		}

	}

	private void redirectView() {

		if (!InfoStrings.getCurentClient(getApplicationContext()).equals("0")) {
			Intent nextScreen = new Intent(getApplicationContext(), Livrare.class);
			startActivity(nextScreen);
			finish();
		}

		if (!InfoStrings.getNrBorderou(getApplicationContext()).equals("0")
				&& InfoStrings.getCurentClient(getApplicationContext()).equals("0")) {
			Intent nextScreen = new Intent(getApplicationContext(), Evenimente.class);
			startActivity(nextScreen);
			finish();
		}

		if (InfoStrings.getNrBorderou(getApplicationContext()).equals("0")
				&& InfoStrings.getCurentClient(getApplicationContext()).equals("0")) {
			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
			startActivity(nextScreen);
			finish();
		}

	}

	private void startSpinner() {
		pw.setVisibility(View.VISIBLE);
		pw.spin();

	}

	private void stopSpinner() {
		pw.setVisibility(View.INVISIBLE);
		pw.stopSpinning();
	}

	@Override
	public void onTaskComplete(String methodName, String result) {
		if (methodName.equals(METHOD_NAME)) {
			validateLogin(result);
		}

	}

}
