package info.jakedavies.innav.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;


import info.jakedavies.innav.adapter.LocationListAdapter;
import info.jakedavies.innav.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class LocationSelectFragment extends Fragment {


    final static String[] locations = {
            "University Of British Columbia",
            "Lakeview Market"
    };
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_select_location, container, false);
        final ListView locationListView = (ListView) v.findViewById(R.id.locations);
        ArrayAdapter<String> locationViewAdapter = new LocationListAdapter(this.getContext(), R.layout.location_item, locations);
        locationListView.setAdapter(locationViewAdapter);
        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                // set Fragmentclass Arguments
                String locationName = (String)locationListView.getItemAtPosition(position);


                Bundle bundle = new Bundle();
                bundle.putString("location_name",locationName);
                // Create fragment and give it an argument specifying the article it should show
                SectionSelectFragment newFragment = new SectionSelectFragment();
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
