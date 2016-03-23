package info.jakedavies.innav.lib;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.estimote.sdk.Beacon;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by jakedavies on 16-03-17.
 */
public class iBeaconServerWrapper {
    // TODO: finish this class
    public iBeaconServerWrapper(String serverURL){

    }

    private void sendToServer(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject obj = new JSONObject();
        JSONArray positions = new JSONArray();
        try{
            for(Beacon beacon : currentBeacons) {
                JSONObject beaconObject = new JSONObject();
                beaconObject.put("major", beacon.getMajor());
                beaconObject.put("minor", beacon.getMinor());
                beaconObject.put("rssi", beacon.getRssi());
                beaconObject.put("uuid",beacon.getProximityUUID());

                positions.put(beaconObject);
            }
            obj.put("positions", positions);
            obj.put("heading", heading);
        } catch (Exception e) {
            Log.e("API", e.toString());
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,"http://192.168.0.136:4000",obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        t.setText(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        t.setText(error.toString());
                    }
                });
        queue.add(jsObjRequest);
    }


}
