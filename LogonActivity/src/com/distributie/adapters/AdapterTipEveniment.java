package com.distributie.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.distributie.beans.BeanAlarma;
import com.distributie.view.R;

public class AdapterTipEveniment extends BaseAdapter {

	private Context context;
	private List<BeanAlarma> listEvenimente;
	private int[] colors = new int[] { 0x3098BED9, 0x30E8E8E8 };

	public AdapterTipEveniment(Context context, List<BeanAlarma> listEvenimente) {
		this.context = context;
		this.listEvenimente = listEvenimente;
	}

	static class ViewHolder {
		TextView textNume;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.eveniment_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textNume = (TextView) convertView.findViewById(R.id.textNume);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		BeanAlarma alarma = getItem(position);
		viewHolder.textNume.setText(alarma.getNumeAlarma());

		convertView.setBackgroundColor(colors[position % colors.length]);

		return convertView;
	}

	@Override
	public int getCount() {
		return listEvenimente.size();
	}

	@Override
	public BeanAlarma getItem(int index) {
		return listEvenimente.get(index);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

}
