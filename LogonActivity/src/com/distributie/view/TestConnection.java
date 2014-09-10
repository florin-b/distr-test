/**
 * @author florinb
 *
 */
package com.distributie.view;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.distributie.listeners.AsyncTaskListener;
import com.distributie.listeners.FmsTestListener;
import com.distributie.model.AsyncTaskWSCall;
import com.distributie.model.FmsDataTestImpl;
import com.distributie.model.InfoStrings;
import com.example.distributie.R;

public class TestConnection extends Activity implements FmsTestListener {

	private TextView textData, textKilometraj, textNivelCombustibil, textLocatie;
	private String strBTMacAddr;
	private ProgressWheel pw;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.LRTheme);
		setContentView(R.layout.test_conexiune);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Test conexiune");
		actionBar.setDisplayHomeAsUpEnabled(true);

		textData = (TextView) findViewById(R.id.textData);
		textKilometraj = (TextView) findViewById(R.id.textKilometraj);
		textNivelCombustibil = (TextView) findViewById(R.id.textNivelCombustibil);
		textLocatie = (TextView) findViewById(R.id.textLocatie);

		pw = (ProgressWheel) findViewById(R.id.pw_spinner);
		pw.setVisibility(View.INVISIBLE);

		strBTMacAddr = getBTMacAddr();

		if (strBTMacAddr.equals("-1")) {
			Toast.makeText(getApplicationContext(), "Dispozitiv FMS inexistent.", Toast.LENGTH_LONG).show();
		} else {
			getFmsData();
		}

	}

	private String getBTMacAddr() {
		String macAddr = "-1";

		try {
			BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

			if (btAdapter != null) {
				Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();

				if (pairedDevices.size() > 0) {

					for (BluetoothDevice device : pairedDevices) {

						if (device.getName().toLowerCase().contains("inv")) {
							macAddr = device.getAddress();
						}

					}
				}
			}
		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
			macAddr = "-1";
		}

		return macAddr;
	}

	private void startSpinner() {
		pw.setVisibility(View.VISIBLE);
		pw.spin();

	}

	private void stopSpinner() {
		pw.setVisibility(View.INVISIBLE);
		pw.stopSpinning();

	}

	private void getFmsData() {
		try {

			startSpinner();

			FmsDataTestImpl fmsTest = new FmsDataTestImpl(this);
			fmsTest.setFmsTestListener(this);
			fmsTest.getFmsData(getBTMacAddr());

			/*
			 * HashMap<String, String> params = new HashMap<String, String>();
			 * params.put("fmsMAC", getBTMacAddr());
			 * 
			 * AsyncTaskWSCall call = new AsyncTaskWSCall(this,
			 * "getFmsTestData", params); call.getCallResults1();
			 */

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private void populateFmsData(String fmsData) {

		if (fmsData.contains("!")) {
			String[] fmsToken = fmsData.split("!");

			Double latid = 0.0, longit = 0.0;

			if (fmsToken[2].contains(",")) {
				String[] tokCoord = fmsToken[2].split(",");
				latid = Double.valueOf(tokCoord[0]);
				longit = Double.valueOf(tokCoord[1]);
			}

			textData.setText(fmsToken[0] + " " + fmsToken[1].substring(0, 2) + ":" + fmsToken[1].substring(2, 4) + ":"
					+ fmsToken[1].substring(4, 6));

			textKilometraj.setText(InfoStrings.getKMFromFMS(fmsToken[3]) + " km");
			textNivelCombustibil.setText(InfoStrings.getFuelLevelFromFMS(fmsToken[3]) + "%");
			textLocatie.setText(getAddress(latid, longit));

		} else {
			Toast.makeText(TestConnection.this, "Nu exista informatii.", Toast.LENGTH_LONG).show();
		}

		stopSpinner();

	}

	private String getAddress(double latitude, double longitude) {
		StringBuilder result = new StringBuilder();
		try {
			Geocoder geocoder = new Geocoder(this, Locale.getDefault());
			List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
			if (addresses.size() > 0) {
				Address address = addresses.get(0);
				if (!addresses.get(0).getAddressLine(0).equals("")) {
					result.append(addresses.get(0).getAddressLine(0)).append(", ");
				}
				result.append(address.getLocality()).append("");

			}
		} catch (Exception e) {
			Toast.makeText(TestConnection.this, e.toString(), Toast.LENGTH_LONG).show();
		}

		if (result.length() == 0)
			result.append("nedefinita");

		return result.toString();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.action_refresh:

			if (pw.getVisibility() == View.INVISIBLE)
				getFmsData();
			break;

		case android.R.id.home:
			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
			startActivity(nextScreen);
			finish();

			return true;
		default:
			break;
		}

		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.connection_menu, menu);
		return true;
	}

	@Override
	public void onBackPressed() {

		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);
		finish();
		return;
	}

	@Override
	public void testComplete(String result) {
		populateFmsData(result);

	}

}
