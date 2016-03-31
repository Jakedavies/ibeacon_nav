package info.jakedavies.innav.fragment;

import android.content.Context;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import info.jakedavies.innav.R;
import info.jakedavies.innav.lib.map.Intersection;
import info.jakedavies.innav.sensor.Heading;
import info.jakedavies.innav.sensor.Position;
import info.jakedavies.innav.view.Map;


public class BlindNavigationFragment extends Fragment implements Heading.HeadingChangedListener {

    private Map mapView;
    private Heading headingSensor;
    private int mapID;
    private BeaconManager mBeaconManager;
    private AutoCompleteTextView section;
    private ArrayList<Intersection> goals;
    private int heading;
    private View v;
    private List<Beacon> beacons;
    TextView locationName;
    TextView locationResponse;
    TextView headingView;
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", "B9407F30-F5F8-466E-AFF9-012345678910", null, null);
    private static final String TRACKING_SERVER = "http://192.168.0.136:3000";
    private info.jakedavies.innav.lib.map.Map map;
    Handler h = new Handler();
    int delay = 1500; //milliseconds
    private String uuid;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        goals = new ArrayList<Intersection>();
        headingSensor = new Heading(getActivity().getApplication().getApplicationContext(), this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        v = inflater.inflate(R.layout.fragment_blind_navigate, container, false);
        LinearLayout mapLayout = (LinearLayout) v.findViewById(R.id.map);

        mapID = getArguments().getInt("location");
        // initialize the map
        Gson g = new Gson();
        String json = getMapConfig(mapID);
        map  = g.fromJson(json, info.jakedavies.innav.lib.map.Map.class);
        mapView = new Map(getContext(), map);
        locationName = (TextView)  v.findViewById(R.id.location_name);
        locationResponse = (TextView) v.findViewById(R.id.location_response);
        headingView = (TextView) v.findViewById(R.id.heading);
        section = (AutoCompleteTextView) v.findViewById(R.id.section);
        uuid = UUID.randomUUID().toString();

        for(Intersection s : map.getSections()){
            if(s.canBeGoal()){
                goals.add(s);
            }
        }
        // Method called when a beacon gets...
        mBeaconManager = new BeaconManager(getActivity().getApplication().getApplicationContext());
        mBeaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> currentBeacons) {
                beacons = currentBeacons;
            }
        });
        locationName.setText(map.getName());
        mapView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));
        ArrayAdapter<Intersection> adapter = new ArrayAdapter<Intersection>(getActivity().getApplication().getApplicationContext(), R.layout.section_item,R.id.section_item_section_name, goals);
        section.setAdapter(adapter);
        section.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                hideKeyboard();
                section.dismissDropDown();
                section.clearFocus();
                mapView.requestFocus();
                mapView.setGoal(goals.get(pos).getName());
                mapView.invalidate();
            }
        });

        h.postDelayed(new Runnable(){
            public void run(){
                //do something
                Log.d("RUNNING", "sending request");
                sendToServer();
                h.postDelayed(this, delay);
            }
        }, delay);
        // by programmatically adding the view we can maintain a pointer to the view and modify data
        mapLayout.addView(mapView);
        return v;
    }
    @Override
    public void onStart(){
        super.onStart();
        // Should be invoked in #onStart.
        mBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override public void onServiceReady() {
                try {
                    mBeaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
                } catch (RemoteException e) {
                    Log.e("BEACON ERROR", "Cannot start ranging", e);
                }
            }
        });
        headingSensor.registerListener();
    }

    @Override
    public void onStop(){
        super.onStop();

        headingSensor.unregisterListener();
    }

    @Override
    public void headingChanged(int heading) {
        this.heading = heading;
        headingView.setText("Heading: " + heading);
    }
    private String getMapConfig(int id) {
        String json = null;
        try {
            InputStream is = this.getResources().openRawResource(id);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return json;
    }
    // heading sensor update event should push event to mapview to modify view
    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getActivity().getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    private void sendToServer(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this.getContext());

        JSONObject obj = new JSONObject();
        JSONArray beaconObjects = new JSONArray();
        if(beacons == null || beacons.size()  < 1 ) {
            return;
        }
        try{
            for(Beacon beacon : beacons) {
                JSONObject beaconObject = new JSONObject();
                beaconObject.put("major", beacon.getMajor());
                beaconObject.put("minor", beacon.getMinor());
                beaconObject.put("rssi", beacon.getRssi());
                beaconObject.put("uuid",beacon.getProximityUUID());

                beaconObjects.put(beaconObject);
            }
            obj.put("beacons", beaconObjects);
            obj.put("heading", heading);
            obj.put("uuid", uuid);
        } catch (Exception e) {
            Log.e("API", e.toString());
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, TRACKING_SERVER ,obj,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("POSITION", "New Position: " + response.toString());
                    int currentX = 0;
                    int currentY = 0;
                    try {
                        currentX = (int) response.getJSONObject("data").getJSONObject("position").getDouble("x");
                        currentY = (int) response.getJSONObject("data").getJSONObject("position").getDouble("y");
                    } catch(Exception e) {
                        Log.d("ERR", e.toString());
                    }
                    mapView.setStart(currentX,currentY);
                    mapView.invalidate();
                    locationResponse.setText(response.toString());
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    locationResponse.setText(error.toString());
                }
            });
        queue.add(jsObjRequest);
    }
}
