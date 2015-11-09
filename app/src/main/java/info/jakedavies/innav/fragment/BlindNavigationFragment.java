package info.jakedavies.innav.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import info.jakedavies.innav.R;
import info.jakedavies.innav.view.Map;

/**
 * Created by jakedavies on 15-10-30.
 */
public class BlindNavigationFragment extends Fragment {

    Map mapView;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
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

    // heading sensor update event should push event to mapview to modify view

}
