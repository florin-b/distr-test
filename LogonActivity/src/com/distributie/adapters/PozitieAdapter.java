package com.distributie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.distributie.view.R;

public class PozitieAdapter extends BaseAdapter {

	private Context context;
	private String[] pozitii;

	public PozitieAdapter(Context context, String[] pozitii) {
		this.context = context;
		this.pozitii = pozitii;
	}

	static class ViewHolder {
		TextView textPozitie;
	}

	@Override
	public int getCount() {
		return pozitii.length;
	}

	@Override
	public String getItem(int pos) {
		return pozitii[pos];
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_pozitie, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textPozitie = (TextView) convertView.findViewById(R.id.textPozitie);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.textPozitie.setText(String.valueOf(pozitii[position]));

		return convertView;
	}

}
