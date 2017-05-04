package com.distributie.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.distributie.beans.Articol;
import com.distributie.view.R;

public class ArticoleEtapaAdapter extends BaseAdapter {

	private List<Articol> listArticole;
	private Context context;

	public ArticoleEtapaAdapter(Context context, List<Articol> listArticole) {
		this.context = context;
		this.listArticole = listArticole;
	}

	private static class ViewHolder {
		TextView textPozitie, textNume, textCantitate, textUmCant, textMasa, textUmMasa;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_articol, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.textPozitie = (TextView) convertView.findViewById(R.id.textPozitie);
			viewHolder.textNume = (TextView) convertView.findViewById(R.id.textNume);
			viewHolder.textCantitate = (TextView) convertView.findViewById(R.id.textCantitate);
			viewHolder.textUmCant = (TextView) convertView.findViewById(R.id.textUmCantitate);
			viewHolder.textMasa = (TextView) convertView.findViewById(R.id.textMasa);
			viewHolder.textUmMasa = (TextView) convertView.findViewById(R.id.textUmMasa);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final Articol articol = getItem(position);

		viewHolder.textPozitie.setText(String.valueOf(position + 1));
		viewHolder.textNume.setText(articol.getNume());
		viewHolder.textCantitate.setText(articol.getCantitate());
		viewHolder.textUmCant.setText(articol.getUmCant());
		viewHolder.textMasa.setText(articol.getGreutate());
		viewHolder.textUmMasa.setText(articol.getUmGreutate());

		return convertView;
	}

	@Override
	public int getCount() {
		return listArticole.size();
	}

	@Override
	public Articol getItem(int position) {
		return listArticole.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

}
