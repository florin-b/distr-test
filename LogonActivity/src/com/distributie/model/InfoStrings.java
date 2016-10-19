/**
 * @author florinb
 *
 */
package com.distributie.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.distributie.enums.TipBorderou;
import com.distributie.view.R;


public class InfoStrings {

	public static TipBorderou getStringTipBorderou(String codTip) {
		TipBorderou tipBorderou = null;

		if (codTip.equals("1110")) {
			tipBorderou = TipBorderou.DISTRIBUTIE;
		}

		if (codTip.equals("1120")) {
			tipBorderou = TipBorderou.APROVIZIONARE;
		}

		if (codTip.equals("1121")) {
			tipBorderou = TipBorderou.SERVICE;
		}

		if (codTip.equals("1122")) {
			tipBorderou = TipBorderou.INCHIRIERE;
		}

		if (codTip.equals("1123")) {
			tipBorderou = TipBorderou.PALETI;
		}

		return tipBorderou;
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
