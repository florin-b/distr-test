package com.distributie.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.distributie.beans.BeanSofer;
import com.distributie.view.R;

public class SoferiAdapter extends BaseAdapter {

	private Context context;
	private List<BeanSofer> listSoferi;

	public SoferiAdapter(Context context, List<BeanSofer> listSoferi) {
		this.context = context;
		this.listSoferi = listSoferi;
	}

	private static class ViewHolder {
		TextView textFiliala, textNume;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_sofer, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textFiliala = (TextView) convertView.findViewById(R.id.textFiliala);
			viewHolder.textNume = (TextView) convertView.findViewById(R.id.textNume);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanSofer sofer = getItem(position);

		viewHolder.textFiliala.setText(sofer.getFiliala());
		viewHolder.textNume.setText(sofer.getNume());

		return convertView;
	}

	@Override
	public int getCount() {
		return listSoferi.size();
	}

	@Override
	public BeanSofer getItem(int pos) {
		return listSoferi.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

}
