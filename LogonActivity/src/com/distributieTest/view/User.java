/**
 * @author florinb
 *
 */
package com.distributieTest.view;

import com.distributieTest.model.UserInfo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class User extends Activity {

	private TextView textNumeSofer, textFilialaSofer, textCodSofer, textNrAuto;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.LRTheme);
		setContentView(R.layout.user);

		InitialUISetup();

	}

	private void InitialUISetup() {

		try {

			ActionBar actionBar = getActionBar();
			actionBar.setTitle("Sofer");
			actionBar.setDisplayHomeAsUpEnabled(true);

			textNumeSofer = (TextView) findViewById(R.id.textNumeSofer);
			textNumeSofer.setText(UserInfo.getInstance().getNume());

			textFilialaSofer = (TextView) findViewById(R.id.textFilialaSofer);
			textFilialaSofer.setText(UserInfo.getInstance().getFiliala());

			textCodSofer = (TextView) findViewById(R.id.textCodSofer);
			textCodSofer.setText(UserInfo.getInstance().getId());

			textNrAuto = (TextView) findViewById(R.id.textNrAuto);
			textNrAuto.setText("");

		} catch (Exception ex) {
			Toast.makeText(User.this, ex.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:

			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
			startActivity(nextScreen);
			finish();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onBackPressed() {

		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);
		finish();
		return;
	}

}
