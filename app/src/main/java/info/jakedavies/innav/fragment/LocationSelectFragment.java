package info.jakedavies.innav.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

import info.jakedavies.innav.adapter.Location;
import info.jakedavies.innav.adapter.LocationListAdapter;
import info.jakedavies.innav.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class LocationSelectFragment extends Fragment {


    Location[] locations = {new Location(1,"location1"), new Location(2,"location2")};
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_select_location, container, false);
        final ListView locationListView = (ListView) v.findViewById(R.id.locations);
        ArrayAdapter<Location> locationViewAdapter = new LocationListAdapter(this.getContext(), R.layout.location_item, readLocations());
        locationListView.setAdapter(locationViewAdapter);
        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                // set Fragmentclass Arguments
                Location location = (Location) locationListView.getItemAtPosition(position);


                Bundle bundle = new Bundle();
                bundle.putInt("location",location.getId());
                // Create fragment and give it an argument specifying the article it should show
                BlindNavigationFragment newFragment = new BlindNavigationFragment();
                newFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });
        return v;
    }

    private String getMapConfig(int resourceId) {
        String json = null;
        try {
            InputStream is = this.getResources().openRawResource(resourceId);
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
    private Location[] readLocations(){
        Field[] fields=R.raw.class.getFields();
        Location[] locations = new Location[fields.length];
        for(int count=0; count < fields.length; count++){
            try {
                int resourceID = fields[count].getInt(fields[count]);
                String resourceName = fields[count].getName();
                locations[count] =  new Location(resourceID, resourceName);
            } catch (IllegalAccessException e) {
                Log.d("ERRRR", "error reading resource name");
                e.printStackTrace();
            }

        }
        return locations;
    }


    @Override
    public void onStart(){
        super.onStart();
    }
    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
