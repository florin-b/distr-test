/**
 * @author florinb
 *
 */
package com.distributieTest.view;

import java.util.HashMap;
import java.util.List;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class BorderouriAdapter extends SimpleAdapter {

	

	private int[] colorTipBorderou = { 0x30EEAD0E, 0x302AC778, 0x30FFFFE0,
			0x30FAFAD2, 0x30C5C1AA };
	private String[] tipBorderou = { "Distributie", "Aprovizionare", "Service",
			"Inchiriere", "Paleti" };

	Context context;

	public BorderouriAdapter(Context context,
			List<HashMap<String, String>> items, int resource, String[] from,
			int[] to) {
		super(context, items, resource, from, to);
		this.context = context;

	}

	static class ViewHolder {
		public TextView textNrCrt, textCodBorderou, textDataBorderou,
				textTipBorderou, textEvenimentBorderou;

	}

	@SuppressWarnings("unchecked")
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		String localTipBord = "";

		try {

			if (null != view) {
				LayoutInflater vi = ((Activity) this.context)
						.getLayoutInflater();
				view = vi.inflate(R.layout.custom_row_list_borderouri, null);

				ViewHolder viewHolder = new ViewHolder();

				viewHolder.textNrCrt = (TextView) view
						.findViewById(R.id.textNrCrt);
				viewHolder.textCodBorderou = (TextView) view
						.findViewById(R.id.textCodBorderou);
				viewHolder.textDataBorderou = (TextView) view
						.findViewById(R.id.textDataBorderou);
				viewHolder.textTipBorderou = (TextView) view
						.findViewById(R.id.textTipBorderou);
				viewHolder.textEvenimentBorderou = (TextView) view
						.findViewById(R.id.textEvenimentBorderou);

				view.setTag(viewHolder);
				view.setFocusableInTouchMode(false);

			}

			ViewHolder holder = (ViewHolder) view.getTag();
			HashMap<String, String> artMap = (HashMap<String, String>) this
					.getItem(position);

			String tokNewVal = artMap.get("nrCrt");
			holder.textNrCrt.setText(tokNewVal);

			tokNewVal = artMap.get("codBorderou");
			holder.textCodBorderou.setText(tokNewVal);

			tokNewVal = artMap.get("dataBorderou");
			holder.textDataBorderou.setText(tokNewVal);

			tokNewVal = artMap.get("tipBorderou");
			holder.textTipBorderou.setText(tokNewVal);
			localTipBord = tokNewVal;

			tokNewVal = artMap.get("eveniment");
			holder.textEvenimentBorderou.setText(tokNewVal);

			view.setBackgroundColor(this.colorTipBorderou[getPosColorBord(localTipBord)]);

		} catch (Exception ex) {
			Toast.makeText(this.context, ex.toString(), Toast.LENGTH_LONG)
					.show();

		}

		return view;

	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		String localTipBord = "";
		try {

			if (null != view) {
				LayoutInflater vi = ((Activity) this.context)
						.getLayoutInflater();
				view = vi.inflate(R.layout.custom_row_list_borderouri, null);

				ViewHolder viewHolder = new ViewHolder();

				viewHolder.textNrCrt = (TextView) view
						.findViewById(R.id.textNrCrt);
				viewHolder.textCodBorderou = (TextView) view
						.findViewById(R.id.textCodBorderou);
				viewHolder.textDataBorderou = (TextView) view
						.findViewById(R.id.textDataBorderou);
				viewHolder.textTipBorderou = (TextView) view
						.findViewById(R.id.textTipBorderou);
				viewHolder.textEvenimentBorderou = (TextView) view
						.findViewById(R.id.textEvenimentBorderou);

				view.setTag(viewHolder);
				view.setFocusableInTouchMode(false);

			}

			ViewHolder holder = (ViewHolder) view.getTag();
			HashMap<String, String> artMap = (HashMap<String, String>) this
					.getItem(position);

			String tokNewVal = artMap.get("nrCrt");
			holder.textNrCrt.setText(tokNewVal);

			tokNewVal = artMap.get("codBorderou");
			holder.textCodBorderou.setText(tokNewVal);

			tokNewVal = artMap.get("dataBorderou");
			holder.textDataBorderou.setText(tokNewVal);

			tokNewVal = artMap.get("tipBorderou");
			holder.textTipBorderou.setText(tokNewVal);
			localTipBord = tokNewVal;

			tokNewVal = artMap.get("eveniment");
			holder.textEvenimentBorderou.setText(tokNewVal);

			view.setBackgroundColor(this.colorTipBorderou[getPosColorBord(localTipBord)]);

		} catch (Exception ex) {
			Toast.makeText(this.context, ex.toString(), Toast.LENGTH_LONG)
					.show();

		}

		return view;

	}

	public int getPosColorBord(String tipBord) {
		int pos = 0;

		for (int i = 0; i < tipBorderou.length; i++) {
			if (this.tipBorderou[i].equals(tipBord)) {
				pos = i;
				break;
			}
		}

		return pos;
	}

}
