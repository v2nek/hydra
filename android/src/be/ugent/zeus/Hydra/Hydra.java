package be.ugent.zeus.Hydra;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import be.ugent.zeus.Hydra.R;
import be.ugent.zeus.Hydra.data.services.HTTPIntentService;
import be.ugent.zeus.Hydra.data.services.UpdaterService;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.zubhium.ZubhiumSDK;

/**
 *
 * @author Thomas Meire
 */
public class Hydra extends SherlockActivity {

  ZubhiumSDK sdk;

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

    sdk = ZubhiumSDK.getZubhiumSDKInstance(getApplicationContext(), "e5e19897556febaf2ecd9be4d04fe0");
    if(sdk != null){
        // We are registering update receiver
    	sdk.registerUpdateReceiver(Hydra.this);
    }
    
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

  /*
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getSupportMenuInflater();
    inflater.inflate(R.menu.hydra, menu);
    return true;
  }
  */

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
      case R.id.settings:
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    EasyTracker.getInstance().activityStart(this); // Add this method.
  }

  @Override
  public void onStop() {
    super.onStop();
    EasyTracker.getInstance().activityStop(this); // Add this method.
  }
  
  @Override
  protected void onDestroy() {
	if(sdk != null){
		sdk.unRegisterUpdateReceiver();     // Don't forget to unregister receiver
	}
    super.onDestroy();
  }
}