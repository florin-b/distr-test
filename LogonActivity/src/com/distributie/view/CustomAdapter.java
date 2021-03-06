/**
 * @author florinb
 *
 */
package com.distributie.view;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.distributie.beans.InitStatus;
import com.distributie.dialog.MapRouteDialog;
import com.distributie.model.CurrentStatus;

public class CustomAdapter extends SimpleAdapter {

	private Context context;
	private boolean showTraseuBtn;
	private int[] colorEvents = new int[] { 0x307FFFD4, 0x30FFD700, 0x30EE9572 };

	static class ViewHolder {
		public TextView textNrCrt, textNumeClient, textCodClient, textAdresaClient, textEv1, textTimpEv1, textEv2, textTimpEv2, textCodAdresa, btnTraseu;
	}

	public CustomAdapter(Context context, List<HashMap<String, String>> items, int resource, String[] from, int[] to, boolean showTraseu) {
		super(context, items, resource, from, to);
		this.context = context;
		this.showTraseuBtn = showTraseu;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);

		String strEvent1 = "", strEvent2 = "";

		if (null != view) {
			LayoutInflater vi = ((Activity) this.context).getLayoutInflater();
			view = vi.inflate(R.layout.custom_row_list_facturi, null);

			ViewHolder viewHolder = new ViewHolder();

			viewHolder.textNrCrt = (TextView) view.findViewById(R.id.textNrCrt);
			viewHolder.textNumeClient = (TextView) view.findViewById(R.id.textNumeClient);

			viewHolder.textAdresaClient = (TextView) view.findViewById(R.id.textAdresaClient);

			viewHolder.textCodClient = (TextView) view.findViewById(R.id.textCodClient);
			viewHolder.textEv1 = (TextView) view.findViewById(R.id.textEv1);
			viewHolder.textTimpEv1 = (TextView) view.findViewById(R.id.textTimpEv1);
			viewHolder.textEv2 = (TextView) view.findViewById(R.id.textEv2);
			viewHolder.textTimpEv2 = (TextView) view.findViewById(R.id.textTimpEv2);
			viewHolder.textCodAdresa = (TextView) view.findViewById(R.id.textCodAdresa);
			viewHolder.btnTraseu = (TextView) view.findViewById(R.id.btnTraseu);

			view.setTag(viewHolder);

			view.setFocusableInTouchMode(false);

		}

		final ViewHolder holder = (ViewHolder) view.getTag();
		@SuppressWarnings("unchecked")
		HashMap<String, String> artMap = (HashMap<String, String>) this.getItem(position);

		String tokNewVal = artMap.get("nrCrt");
		holder.textNrCrt.setText(tokNewVal);

		tokNewVal = artMap.get("numeClient");
		holder.textNumeClient.setText(tokNewVal);

		tokNewVal = artMap.get("codClient");
		holder.textCodClient.setText(tokNewVal);

		tokNewVal = artMap.get("adresaClient");
		holder.textAdresaClient.setText(tokNewVal);

		tokNewVal = artMap.get("codAdresa");
		holder.textCodAdresa.setText(tokNewVal);

		tokNewVal = artMap.get("ev1");
		holder.textEv1.setText(tokNewVal);
		strEvent1 = tokNewVal;

		tokNewVal = artMap.get("timpEv1");
		holder.textTimpEv1.setText(tokNewVal);

		tokNewVal = artMap.get("ev2");
		holder.textEv2.setText(tokNewVal);
		strEvent2 = tokNewVal;

		tokNewVal = artMap.get("timpEv2");
		holder.textTimpEv2.setText(tokNewVal);

		if (!showTraseuBtn)
			holder.btnTraseu.setVisibility(View.INVISIBLE);
		else
			holder.btnTraseu.setVisibility(View.VISIBLE);

		
		holder.btnTraseu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MapRouteDialog dialog = new MapRouteDialog(context, holder.textCodAdresa.getText().toString());
				dialog.show();

			}
		});

		if (strEvent1.trim().equals("") && strEvent2.trim().equals("")) {
			view.setBackgroundColor(this.colorEvents[0]);
		} else {
			if (!strEvent1.trim().equals("") && strEvent2.trim().equals("")) {
				view.setBackgroundColor(this.colorEvents[1]);
			} else {
				if (!strEvent1.trim().equals("") && !strEvent2.trim().equals("")) {
					view.setBackgroundColor(this.colorEvents[2]);
				}
			}
		}

		return view;

	}

	boolean isCurrentClient(String localSelClient, String localCodAdresa) {
		return localSelClient.equals(CurrentStatus.getInstance().getCurrentClient())
				&& localCodAdresa.equals(CurrentStatus.getInstance().getCurentClientAddr());
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int arg0) {
		return true;
	}

}