/**
 *
 * @author Tom Naessens Tom.Naessens@UGent.be 3de Bachelor Informatica Universiteit Gent
 *
 */
package be.ugent.zeus.hydra.util.facebook.event.tasks;

import android.R;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import be.ugent.zeus.hydra.util.facebook.FacebookSession;
import be.ugent.zeus.hydra.util.facebook.event.data.AttendingStatus;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author silox
 */
public class AsyncComingGetter extends AsyncTask<Void, Void, JSONObject> {

    Context context;
    String eventId;
    Button button;

    public AsyncComingGetter(Context context, String eventId, Button button) {
        this.context = context;
        this.eventId = eventId;
        this.button = button;
    }

    @Override
    protected void onPreExecute() {
        button.setText("Laden...");
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        Log.i(FacebookSession.TAG, "Fetching event with id " + eventId);

        String query = String.format("SELECT rsvp_status FROM event_member"
            + " WHERE eid = '%s' AND uid = me()", eventId);
        Request requestWithQuery = FacebookSession.requestWithQuery(query);
        Response response = requestWithQuery.executeAndWait();

        if (response.getError() != null) {
            Log.e(FacebookSession.TAG, response.getError().getErrorCode() + ": " + response.getError().getErrorMessage());
            return null;
        }
        GraphObject object = response.getGraphObject();
        JSONObject obj = null;
        try {
            obj = ((JSONArray) object.getProperty("data")).getJSONObject(0);
        } catch (JSONException ex) {
        }

        return obj;
    }

    @Override
    protected void onPostExecute(JSONObject result) {

        if (result != null && result.length() > 0) {
            try {
                button.setText(AttendingStatus.valueOf(result.getString("rsvp_status").toUpperCase()).toButtonString(context));
            } catch (JSONException ex) {
                Logger.getLogger(AsyncComingGetter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            button.setText(AttendingStatus.valueOf("UNDECIDED").toButtonString(context));
        }
    }
}
