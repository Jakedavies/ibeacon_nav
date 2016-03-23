package info.jakedavies.innav;

import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.JsonArray;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import info.jakedavies.innav.fragment.AvailableBeaconFragment;
import info.jakedavies.innav.fragment.BlindNavigationFragment;
import info.jakedavies.innav.fragment.LocationSelectFragment;
import info.jakedavies.innav.lib.map.Intersection;
import info.jakedavies.innav.sensor.Heading;
import info.jakedavies.innav.sensor.Position;


public class MainActivity extends BaseActivity implements Heading.HeadingChangedListener {


    private BeaconManager mBeaconManager;
    private String scanId;
    private static final String TAG = "MainActivity";
    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);
    private TextView t;
    private Button b;
    private int heading = 0;
    private List<Beacon> currentBeacons = new ArrayList<Beacon>();

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBeaconManager = new BeaconManager(getApplication());


        mBeaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
                currentBeacons = beacons;
            }
        });

        t = (TextView) findViewById(R.id.current_position);
        b = (Button) findViewById(R.id.button);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToServer();
            }
        });

    }

    @Override
    public void headingChanged(int heading) {

    }

    @Override
    public void onStart(){
        super.onStart();
        // Should be invoked in #onStart.
        mBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override public void onServiceReady() {
                try {
                    mBeaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
                } catch (RemoteException e) {
                    Log.e(TAG, "Cannot start ranging", e);
                }
            }
        });
    }
    @Override
    public void onStop(){
        super.onStop();
        // Should be invoked in #onStop.
        mBeaconManager.stopNearableDiscovery(scanId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // When no longer needed. Should be invoked in #onDestroy.
        mBeaconManager.disconnect();
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
