package be.ugent.zeus.hydra;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import be.ugent.zeus.hydra.data.services.HTTPIntentService;
import be.ugent.zeus.hydra.data.services.UpdaterService;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.zubhium.ZubhiumSDK;

/**
 *
 * @author Thomas Meire
 */
public class Hydra extends AbstractSherlockActivity {

	ZubhiumSDK sdk;
	public static final boolean BETA = true;

	private void link(int id, final Class activity) {
		findViewById(id).setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				startActivity(new Intent(Hydra.this, activity));
			}
		});
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// Zubhium
		sdk = ZubhiumSDK.getZubhiumSDKInstance(getApplicationContext(), "4837990a007ee67c597d1059742293");
		if (sdk != null) {
			// We are registering update receiver
			sdk.registerUpdateReceiver(Hydra.this);
		}

		// Google Analytics
		if (BuildConfig.DEBUG) {
			GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(getApplicationContext());
			googleAnalytics.setAppOptOut(true);
		}

		// Home screen: disable the button
		getActionBar().setDisplayHomeAsUpEnabled(false);

		setContentView(R.layout.hydra);
		setTitle("");

		link(R.id.home_btn_news, News.class);
		link(R.id.home_btn_calendar, Calendar.class);
		link(R.id.home_btn_info, Info.class);
		link(R.id.home_btn_menu, RestoMenu.class);
		link(R.id.home_btn_gsr, GSR.class);
		link(R.id.home_btn_schamper, SchamperDaily.class);

		Intent intent = new Intent(this, UpdaterService.class);
		intent.putExtra(HTTPIntentService.FORCE_UPDATE, true);
		startService(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.hydra, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
//			case R.id.settings:
//				Intent intent = new Intent(this, Settings.class);
//				startActivity(intent);
//				return true;
			case R.id.feedbackButton:
				setupFeedback();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {
		if (sdk != null) {
			sdk.unRegisterUpdateReceiver();     // Don't forget to unregister receiver
		}
		super.onDestroy();
	}

	protected void setupFeedback() {
		/**
		 * Now lets listen to users, by enabling in app help desk. *
		 */
		if (sdk != null) {
			sdk.openFeedbackDialog(Hydra.this);
		}
	}
}
