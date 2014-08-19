/**
 * @author florinb
 *
 */
package com.distributieTest.view;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ArticoleLivrareAdapter extends SimpleAdapter {

	private String strTipOp = "", strHeaderOp = "";

	private Context context;

	public enum BackColor {

		BROWN(0x30D1BC71), LIGHTBROWN(0x30F6F2E4), BLUE(0x3098BED9), LIGHTBLUE(0x30E8E8E8);

		private int code;

		private BackColor(int c) {
			this.code = c;
		}

		public int getCode() {
			return this.code;
		}
	}

	static class ViewHolder {
		private TextView textNrCrt, textNumeArticol, textCantitate, textUnitMas, textTipOp, textGreutate,
				textUmGreutate;

	}

	public ArticoleLivrareAdapter(Context context, List<HashMap<String, String>> items, int resource, String[] from,
			int[] to) {
		super(context, items, resource, from, to);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);

		if (null != view) {
			LayoutInflater vi = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = vi.inflate(R.layout.custom_row_list_articole, null);

			ViewHolder viewHolder = new ViewHolder();

			viewHolder.textNrCrt = (TextView) view.findViewById(R.id.textNrCrt);
			viewHolder.textNumeArticol = (TextView) view.findViewById(R.id.textNumeArticol);
			viewHolder.textCantitate = (TextView) view.findViewById(R.id.textCantitate);
			viewHolder.textUnitMas = (TextView) view.findViewById(R.id.textUnitMas);
			viewHolder.textTipOp = (TextView) view.findViewById(R.id.textTipOp);
			viewHolder.textGreutate = (TextView) view.findViewById(R.id.textGreutate);
			viewHolder.textUmGreutate = (TextView) view.findViewById(R.id.textUnitMasGreutate);
			view.setTag(viewHolder);

			view.setFocusableInTouchMode(false);

		}

		final ViewHolder holder = (ViewHolder) view.getTag();
		@SuppressWarnings("unchecked")
		HashMap<String, String> artMap = (HashMap<String, String>) this.getItem(position);

		String tokNewVal = artMap.get("nrCrt");
		holder.textNrCrt.setText(tokNewVal);

		tokNewVal = artMap.get("numeArticol");
		holder.textNumeArticol.setText(tokNewVal);
		this.strHeaderOp = tokNewVal;

		tokNewVal = artMap.get("cantitate");
		holder.textCantitate.setText(tokNewVal);

		tokNewVal = artMap.get("unitMas");
		holder.textUnitMas.setText(tokNewVal);

		tokNewVal = artMap.get("tipOp");
		holder.textTipOp.setText(tokNewVal);

		tokNewVal = artMap.get("greutate");
		holder.textGreutate.setText(tokNewVal);

		tokNewVal = artMap.get("umGreutate");
		holder.textUmGreutate.setText(tokNewVal);

		this.strTipOp = tokNewVal;

		if (this.strHeaderOp.trim().toUpperCase(Locale.getDefault()).equals("INCARCARE")) {
			view.setBackgroundColor(BackColor.BLUE.getCode());
		}

		if (this.strTipOp.trim().toUpperCase(Locale.getDefault()).equals("INCARCARE")
				&& !this.strHeaderOp.trim().toUpperCase(Locale.getDefault()).equals("INCARCARE")) {
			view.setBackgroundColor(BackColor.LIGHTBLUE.getCode());
		}

		if (this.strHeaderOp.trim().toUpperCase(Locale.getDefault()).equals("DESCARCARE")) {
			view.setBackgroundColor(BackColor.BROWN.getCode());
		}

		if (this.strTipOp.trim().toUpperCase(Locale.getDefault()).equals("DESCARCARE")
				&& !this.strHeaderOp.trim().toUpperCase(Locale.getDefault()).equals("DESCARCARE")) {
			view.setBackgroundColor(BackColor.LIGHTBROWN.getCode());
		}

		return view;

	}

}