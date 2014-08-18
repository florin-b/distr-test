/**
 * @author florinb
 *
 */
package com.distributieTest.view;





import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;


public class MainMenu extends Activity {

	GridView mainGridView;

	private String[] btnNames = { "Sofer", "Borderouri", "Livrare", "Istoric", "Test", "Iesire" };

	private int[] btnIcons = new int[] { R.drawable.chauffeur_hat, R.drawable.documents_icon, R.drawable.delivery, R.drawable.history,R.drawable.test_connection,
			R.drawable.exit };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.LRTheme);
		setContentView(R.layout.main_menu);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Distributie");
		actionBar.setDisplayHomeAsUpEnabled(true);

		try {

			this.mainGridView = (GridView) findViewById(R.id.mainGridView);
			this.mainGridView.setAdapter(new ButtonAdapter(this));

		} catch (Exception ex) {
			Toast.makeText(getBaseContext(), ex.toString(), Toast.LENGTH_LONG).show();
		}

	}

	public class ButtonAdapter extends BaseAdapter {
		private Context mContext;

		public ButtonAdapter(Context c) {
			this.mContext = c;
		}

		public int getCount() {
			return getNrBtns(); // nr. butoane
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			Button btn;
			Typeface font = Typeface.SERIF;

			if (convertView == null) {

				btn = new Button(this.mContext);
				btn.setLayoutParams(new GridView.LayoutParams(130, 110));

			} else {
				btn = (Button) convertView;
			}

			btn.setText(getBtnName(position));

			btn.setCompoundDrawablesWithIntrinsicBounds(0, getBtnIcon(position), 0, 0);

			btn.setId(position);
			btn.setTextSize(19);
			btn.setTextColor(android.graphics.Color.rgb(11, 86, 111));
			btn.setTypeface(font);
			btn.setOnClickListener(new MyOnClickListener(position));

			btn.setBackgroundResource(R.drawable.grid_button_style);

			return btn;

		}

		class MyOnClickListener implements OnClickListener {
			private final int position;

			public MyOnClickListener(int position) {
				this.position = position;
			}

			public void onClick(View v) {

				String selectedBtnName = getBtnName(this.position);

				// info
				if (selectedBtnName.equalsIgnoreCase("Sofer")) {

					try {

						Intent nextScreen = new Intent(MainMenu.this, User.class);
						startActivity(nextScreen);

						finish();

					} catch (Exception e) {
						Toast.makeText(MainMenu.this, e.toString(), Toast.LENGTH_SHORT).show();
					}

				}

				// evenimente
				if (selectedBtnName.equalsIgnoreCase("Borderouri")) {

					try {

						Intent nextScreen = new Intent(MainMenu.this, Evenimente.class);
						startActivity(nextScreen);

						finish();

					} catch (Exception e) {
						Toast.makeText(MainMenu.this, e.toString(), Toast.LENGTH_SHORT).show();
					}

				}

				// livrare
				if (selectedBtnName.equalsIgnoreCase("Livrare")) {

					try {

						Intent nextScreen = new Intent(MainMenu.this, Livrare.class);
						startActivity(nextScreen);

						finish();

					} catch (Exception e) {
						Toast.makeText(MainMenu.this, e.toString(), Toast.LENGTH_SHORT).show();
					}

				}
				
				
				// test
				if (selectedBtnName.equalsIgnoreCase("Test")) {

					try {

						Intent nextScreen = new Intent(MainMenu.this, TestConnection.class);
						startActivity(nextScreen);

						finish();

					} catch (Exception e) {
						Toast.makeText(MainMenu.this, e.toString(), Toast.LENGTH_SHORT).show();
					}

				}				

				// istoric
				if (selectedBtnName.equalsIgnoreCase("Istoric")) {

					try {

						Intent nextScreen = new Intent(MainMenu.this, AfisBorderouri.class);
						startActivity(nextScreen);

						finish();

					} catch (Exception e) {
						Toast.makeText(MainMenu.this, e.toString(), Toast.LENGTH_SHORT).show();
					}

				}

				// exit
				if (selectedBtnName.equalsIgnoreCase("Iesire")) {

					System.exit(0);

				}

			}

		}

	}

	private int getNrBtns() {
		return btnNames.length;
	}

	private String getBtnName(int btnPos) {
		return this.btnNames[btnPos];

	}

	private int getBtnIcon(int btnPos) {
		return this.btnIcons[btnPos];
	}

}
