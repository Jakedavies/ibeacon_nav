package info.jakedavies.innav.fragment;

import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import info.jakedavies.innav.R;
import info.jakedavies.innav.lib.map.Intersection;
import info.jakedavies.innav.sensor.Heading;
import info.jakedavies.innav.sensor.Position;
import info.jakedavies.innav.view.Map;


public class BlindNavigationFragment extends Fragment implements Heading.HeadingChangedListener, Position.PositionChangedListener {

    private Map mapView;
    private Heading headingSensor;
    private Position positionSensor;
    private int mapID;
    private Button section_button;
    private BeaconManager mBeaconManager;
    TextView locationName;
    TextView sectionName;
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", "B9407F30-F5F8-466E-AFF9-25556B57FE6D", null, null);
    private info.jakedavies.innav.lib.map.Map map;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        headingSensor = new Heading(getActivity().getApplication().getApplicationContext(), this);
        positionSensor = new Position(getActivity().getApplication().getApplicationContext(), this, mapView.getMap().getBeacons());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_blind_navigate, container, false);
        LinearLayout mapLayout = (LinearLayout) v.findViewById(R.id.map);

        mapID = getArguments().getInt("location");
        // initialize the map
        Gson g = new Gson();
        String json = getMapConfig(mapID);
        map  = g.fromJson(json, info.jakedavies.innav.lib.map.Map.class);
        mapView = new Map(getContext(), map);
        locationName = (TextView)  v.findViewById(R.id.location_name);
        sectionName  = (TextView) v.findViewById(R.id.section_name);
        mBeaconManager = new BeaconManager(getActivity().getApplication().getApplicationContext());

        // Method called when a beacon gets...
        mBeaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                Toast.makeText(getActivity().getApplicationContext(), "Entering Section", Toast.LENGTH_LONG);
            }

            @Override
            public void onExitedRegion(Region region) {
                Toast.makeText(getActivity().getApplicationContext(), "Exiting Section", Toast.LENGTH_LONG);
            }
        });
        // Connect to the beacon manager...
        mBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    // ... and start the monitoring
                    mBeaconManager.startMonitoring(ALL_ESTIMOTE_BEACONS);
                } catch (Exception e) {
                }
            }
        });
        locationName.setText(map.getName());
        mapView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));

        section_button = (Button) v.findViewById(R.id.section_button);
        section_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onSectionClick();
            }
        });

        // by programmatically adding the view we can maintain a pointer to the view and modify data
        mapLayout.addView(mapView);
        return v;
    }
    @Override
    public void onStart(){
        super.onStart();
        headingSensor.registerListener();
        positionSensor.registerListener();
    }

    @Override
    public void onStop(){
        super.onStop();
        headingSensor.unregisterListener();
        positionSensor.unregisterListener();
    }

    @Override
    public void headingChanged(int heading) {
        //mapView.translateToPosition(heading);
    }
    private void onSpeakerClick(View v) {
        mapView.setZoom();
    }
    private void onVibrateClick(View v) {
        mapView.toggleNorthLock();
    }
    private void onSectionClick(){
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(getActivity().getApplication().getApplicationContext(), section_button);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.section_menu, popup.getMenu());
        for(Intersection s : map.getSections()){
            if(s.canBeGoal()){
                popup.getMenu().add(s.getName());
            }
        }
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mapView.setGoal(item.getTitle().toString());
                mapView.invalidate();
                sectionName.setText("Navigating to" + item.getTitle().toString());
                return false;
            }
        });

        popup.show();//showing popup menu
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
    @Override
    public void positionChanged(Point p) {
        Log.d("Position", "New Position: " + p.toString());
        map.setStart(p.x, p.y);
    }
    // heading sensor update event should push event to mapview to modify view

}
