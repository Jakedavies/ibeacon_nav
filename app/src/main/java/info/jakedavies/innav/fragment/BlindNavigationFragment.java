package info.jakedavies.innav.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import info.jakedavies.innav.R;
import info.jakedavies.innav.sensor.Heading;
import info.jakedavies.innav.view.Map;

public class BlindNavigationFragment extends Fragment implements Heading.HeadingChangedListener {

    private Map mapView;
    private Heading  mSensor;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mSensor = new Heading(getActivity().getApplication().getApplicationContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_blind_navigate, container, false);
        LinearLayout mapLayout = (LinearLayout) v.findViewById(R.id.map);

        mapView = new Map(getContext());
        mapView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));

        // by programmatically adding the view we can maintain a pointer to the view and modify data
        mapLayout.addView(mapView);

        return v;
    }
    @Override
    public void onStart(){
        mSensor.registerListener();
    }

    @Override
    public void onStop(){
        mSensor.unregisterListener();
    }

    @Override
    public void headingChanged(int heading) {

        mapView.translateToPosition(heading);
    }

    // heading sensor update event should push event to mapview to modify view

}
