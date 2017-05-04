/**
 * @author florinb
 *
 */
package com.distributie.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.distributie.adapters.SoferiAdapter;
import com.distributie.beans.BeanSofer;
import com.distributie.beans.InitStatus;
import com.distributie.enums.EnumNetworkStatus;
import com.distributie.enums.EnumOperatiiLogon;
import com.distributie.listeners.AsyncTaskListener;
import com.distributie.listeners.SoferiListener;
import com.distributie.model.HandleJSONData;
import com.distributie.model.LogonImpl;
import com.distributie.model.LogonListener;
import com.distributie.model.OperatiiSoferi;
import com.distributie.model.UserInfo;

public class LogonActivity extends Activity implements LogonListener, AsyncTaskListener, SoferiListener {

	int val = 0;

	ProgressBar progressBarWheel;
	TextView txtNumeSofer, txtDeviceId;
	private Handler logonHandler = new Handler();

	private String buildVer = "0";
	private boolean allowLogon = false;
	ImageView logonImage;
	private Timer myTimer;

	private OperatiiSoferi opSoferi;
	private Spinner spinnerSoferi;

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
			getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
			//

			PackageInfo pInfo = null;
			try {
				pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			} catch (Exception e) {
				Toast.makeText(LogonActivity.this, e.toString(), Toast.LENGTH_LONG).show();
			}

			buildVer = String.valueOf(pInfo.versionCode);

			spinnerSoferi = (Spinner) findViewById(R.id.spinnerSoferi);
			spinnerSoferi.setVisibility(View.VISIBLE);
			setSpinnerSoferiListener();

			progressBarWheel = (ProgressBar) findViewById(R.id.progress_bar_wheel);
			progressBarWheel.setVisibility(View.INVISIBLE);
			logonImage = (ImageView) findViewById(R.id.logonImage);
			addLogonImageListener();

			txtNumeSofer = (TextView) findViewById(R.id.txtNumeSofer);
			txtDeviceId = (TextView) findViewById(R.id.txtDeviceId);

			String deviceId = getDeviceId();

			txtDeviceId.setText(deviceId.replaceAll(".{3}", "$0 "));

			opSoferi = new OperatiiSoferi(this);
			opSoferi.setSoferiListener(this);
			opSoferi.getSoferi();

			 //getCodSofer(deviceId);
			 
			getCodSofer("860998032233102");
			
			 

		} catch (Exception ex) {
			Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
		}

	}

	class UpdateProgress extends TimerTask {
		public void run() {
			val++;
			if (progressBarWheel.getProgress() == 50) {
				logonHandler.post(new Runnable() {
					public void run() {
						myTimer.cancel();
						logonImage.setEnabled(false);
						checkForUpdate();

					}

				});

			} else {
				progressBarWheel.setProgress(val);
			}

		}
	}

	private void addLogonImageListener() {
		logonImage.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (allowLogon) {
						progressBarWheel.setVisibility(View.VISIBLE);
						progressBarWheel.setProgress(0);
						val = 0;
						myTimer = new Timer();
						myTimer.schedule(new UpdateProgress(), 40, 15);
					}
					return true;

				case MotionEvent.ACTION_UP:
					if (progressBarWheel.getVisibility() == View.VISIBLE) {
						myTimer.cancel();
						progressBarWheel.setVisibility(View.INVISIBLE);
						logonImage.setEnabled(true);
						return true;
					}
				}

				return false;
			}
		});
	}

	private String getDeviceId() {
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	private void getCodSofer(String codTableta) {

		LogonImpl logon = new LogonImpl(this);
		logon.setLogonListener(this);
		logon.getCodSofer(codTableta);

	}

	public void performLoginThread(String user, String password) {
		try {

			LogonImpl logon = new LogonImpl(this);
			logon.setLogonListener(this);
			logon.performLogon(user, password);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	public void checkForUpdate() {

		try {
			checkUpdate check = new checkUpdate(this);
			check.execute("dummy");

		} catch (Exception e) {
			Toast.makeText(LogonActivity.this, e.toString(), Toast.LENGTH_LONG).show();
		}

	}

	private class checkUpdate extends AsyncTask<String, Void, String> {
		String errMessage = "";

		Context mContext;
		private ProgressDialog dialog;

		private checkUpdate(Context context) {
			super();
			this.mContext = context;
		}

		protected void onPreExecute() {

			this.dialog = new ProgressDialog(mContext);
			this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog.setMessage("Verificare versiune aplicatie...");
			this.dialog.setCancelable(false);
			this.dialog.show();

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

					String sourceFile = "/Update/LiteSFA/DistributieVerTEST.txt";

					desFile2 = new FileOutputStream("sdcard/download/DistributieVerTEST.txt");
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
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}

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

			File fVer = new File(Environment.getExternalStorageDirectory() + "/download/DistributieVerTEST.txt");
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

		Context mContext;
		private ProgressDialog dialog;

		private downloadUpdate(Context context) {
			super();
			this.mContext = context;
		}

		protected void onPreExecute() {
			this.dialog = new ProgressDialog(mContext);
			this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog.setMessage("Descarcare...");
			this.dialog.setCancelable(false);
			this.dialog.show();

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

					String sourceFile = "/Update/LiteSFA/DistributieTEST.apk";
					desFile1 = new FileOutputStream("sdcard/download/DistributieTEST.apk");
					mFTPClient.retrieveFile(sourceFile, desFile1);

					sourceFile = "/Update/LiteSFA/DistributieVerTEST.txt";
					desFile2 = new FileOutputStream("sdcard/download/DistributieVerTEST.txt");
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

				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}

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

		String fileUrl = "/download/DistributieTEST.apk";
		String file = android.os.Environment.getExternalStorageDirectory().getPath() + fileUrl;
		File f = new File(file);

		if (f.exists()) {

			// start install
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + "DistributieTEST.apk")),
					"application/vnd.android.package-archive");
			startActivity(intent);

			finish();
		} else {
			Toast toast = Toast.makeText(LogonActivity.this, "Fisier corupt, repetati operatiunea!", Toast.LENGTH_SHORT);
			toast.show();

		}

	}

	private void redirectView() {

		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);
		finish();

	}

	boolean noActivity(InitStatus initStatus) {
		return initStatus.getClient() == null;
	}

	boolean activeDocument(InitStatus initStatus) {
		return initStatus.getDocument().equals(initStatus.getClient());
	}

	boolean activeClient(InitStatus initStatus) {
		return !initStatus.getDocument().equals(initStatus.getClient());
	}

	private void showNumeSofer(String result) {

		HandleJSONData objLogon = new HandleJSONData(this, result);
		objLogon.decodeLogonInfo();

		if (UserInfo.getInstance().getNume() != null) {
			txtNumeSofer.setText(UserInfo.getInstance().getNume());
			allowLogon = true;
		} else {
			txtNumeSofer.setText("Tableta nealocata");
			allowLogon = false;
		}

	}

	@Override
	public void logonComplete(EnumOperatiiLogon methodName, String result) {
		switch (methodName) {
		case GET_COD_SOFER:
			showNumeSofer(result);
			break;
		default:
			break;
		}

	}

	@Override
	public void onTaskComplete(String methodName, String result, EnumNetworkStatus networkStatus) {
		Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

	}

	
	private void setSpinnerSoferiListener()
	{
		spinnerSoferi.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				
				BeanSofer sofer = (BeanSofer) parent.getAdapter().getItem(position);
				getCodSofer(sofer.getCodTableta());
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
		});
	}
	
	@Override
	public void soferiComplete(List<BeanSofer> listSoferi) {
		
		SoferiAdapter soferiAdapter = new SoferiAdapter(getApplicationContext(), listSoferi);
		spinnerSoferi.setAdapter(soferiAdapter);
		allowLogon = true;

	}

}
