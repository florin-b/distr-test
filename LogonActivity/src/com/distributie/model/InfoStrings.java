/**
 * @author florinb
 *
 */
package com.distributie.model;

import com.example.distributie.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class InfoStrings {

	public static void setEveniment(Context context, String strEveniment) {
		try {
			SharedPreferences sharedPreferences = context.getSharedPreferences("DRIVER_DATA", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString("eveniment", strEveniment);
			editor.commit();
		} catch (Exception e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}

	}

	public static String getEveniment(Context context) {

		String localEveniment = "";
		try {
			SharedPreferences pref2 = context.getSharedPreferences("DRIVER_DATA", Context.MODE_PRIVATE);
			localEveniment = pref2.getString("eveniment", "-1");

		} catch (Exception e) {
			localEveniment = "-1";
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}

		return localEveniment;
	}

	public static void setNrBorderou(Context context, String codBorderou) {
		try {
			SharedPreferences sharedPreferences = context.getSharedPreferences("DRIVER_DATA", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString("nrBord", codBorderou);
			editor.commit();
		} catch (Exception e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}

	}

	public static void setTipBorderou(Context context, String tipBorderou) {
		try {
			SharedPreferences sharedPreferences = context.getSharedPreferences("DRIVER_DATA", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString("tipBord", tipBorderou);
			editor.commit();
		} catch (Exception e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}

	}

	public static String getNrBorderou(Context context) {

		String localNrBord = "0";
		try {
			SharedPreferences pref2 = context.getSharedPreferences("DRIVER_DATA", Context.MODE_PRIVATE);
			localNrBord = pref2.getString("nrBord", "0");

		} catch (Exception e) {
			localNrBord = "-1";
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}

		return localNrBord;
	}

	public static String getTipBorderou(Context context) {

		String localTipBord = "0";
		try {
			SharedPreferences pref2 = context.getSharedPreferences("DRIVER_DATA", Context.MODE_PRIVATE);
			localTipBord = pref2.getString("tipBord", "0");

		} catch (Exception e) {
			localTipBord = "-1";
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}

		return localTipBord;
	}

	public static void setCurentClient(Context context, String strCodClient) {
		try {
			SharedPreferences sharedPreferences = context.getSharedPreferences("DRIVER_DATA", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString("codClient", strCodClient);
			editor.commit();
		} catch (Exception e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}

	}

	public static String getCurentClient(Context context) {

		String localCodClient = "0";
		try {
			SharedPreferences pref2 = context.getSharedPreferences("DRIVER_DATA", Context.MODE_PRIVATE);
			localCodClient = pref2.getString("codClient", "0");

		} catch (Exception e) {
			localCodClient = "-1";
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}

		return localCodClient;
	}

	public static void setCurentClientAddr(Context context, String strAdrClient) {
		try {
			SharedPreferences sharedPreferences = context.getSharedPreferences("DRIVER_DATA", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString("adrClient", strAdrClient);
			editor.commit();
		} catch (Exception e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}

	}

	public static String getCurentClientAddr(Context context) {

		String localCodClient = "0";
		try {
			SharedPreferences pref2 = context.getSharedPreferences("DRIVER_DATA", Context.MODE_PRIVATE);
			localCodClient = pref2.getString("adrClient", "0");

		} catch (Exception e) {
			localCodClient = "-1";
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}

		return localCodClient;
	}

	public static void setCurentClientName(Context context, String strCodClient) {
		try {
			SharedPreferences sharedPreferences = context.getSharedPreferences("DRIVER_DATA", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString("numeClient", strCodClient);
			editor.commit();
		} catch (Exception e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}

	}

	public static String getCurentClientName(Context context) {

		String localNumeClient = "0";
		try {
			SharedPreferences pref2 = context.getSharedPreferences("DRIVER_DATA", Context.MODE_PRIVATE);
			localNumeClient = pref2.getString("numeClient", "0");

		} catch (Exception e) {
			localNumeClient = "-1";
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}

		return localNumeClient;
	}

	public static void setEvenimentClient(Context context, String eveniment) {
		try {
			SharedPreferences sharedPreferences = context.getSharedPreferences("DRIVER_DATA", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString("evenimentClient", eveniment);
			editor.commit();
		} catch (Exception e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}

	}

	public static String getEvenimentClient(Context context) {

		String localEvenimentClient = "";
		try {
			SharedPreferences pref2 = context.getSharedPreferences("DRIVER_DATA", Context.MODE_PRIVATE);
			localEvenimentClient = pref2.getString("evenimentClient", "-1");

		} catch (Exception e) {
			localEvenimentClient = "-1";
			Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		}

		return localEvenimentClient;
	}

	public static String getStringTipBorderou(String codTip) {
		String tipBorderou = "nedefinit";

		if (codTip.equals("1110")) {
			tipBorderou = "Distributie";
		}

		if (codTip.equals("1120")) {
			tipBorderou = "Aprovizionare";
		}

		if (codTip.equals("1121")) {
			tipBorderou = "Service";
		}

		if (codTip.equals("1122")) {
			tipBorderou = "Inchiriere";
		}

		if (codTip.equals("1123")) {
			tipBorderou = "Paleti";
		}

		return tipBorderou;
	}

	public static String getKMFromFMS(String fmsString) {
		String kmValue = "0";

		if (!fmsString.equals("0")) {
			String[] fmsToken = fmsString.split("#");
			String[] kmToken1 = fmsToken[5].split(",");
			String[] kmToken2 = kmToken1[1].split("\\*");

			kmValue = kmToken2[0];
		}
		return kmValue;
	}

	public static String getFuelLevelFromFMS(String fmsString) {
		String fuelLevel = "0";

		if (!fmsString.equals("0")) {
			String[] fmsToken = fmsString.split("#");
			String[] kmToken1 = fmsToken[3].split(",");
			String[] kmToken2 = kmToken1[1].split("\\*");

			fuelLevel = kmToken2[0];
		}
		return fuelLevel;
	}

	public static boolean isGPSEnabled(Context context) {
		boolean statusOfGPS = false;
		try {
			LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
			statusOfGPS = false;
			Log.e("Error", ex.toString());
		}

		// TEST!
		statusOfGPS = true;

		return statusOfGPS;

	}

	public static void showGPSDisabledAlert(Context context) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setMessage("Activati GPS si reluati operatiunea dupa 1 minut!").setCancelable(false);
		alertDialogBuilder.setNegativeButton("Inchide", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

	public static void showCustomToast(Context context, String infoMessage) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.cust_toast_layout, null);
		TextView myTextView = (TextView) view.findViewById(R.id.textViewMessage);
		myTextView.setText(infoMessage);

		Toast toast = new Toast(context);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.show();
	}

}
