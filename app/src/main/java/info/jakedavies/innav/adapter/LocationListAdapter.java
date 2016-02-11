package info.jakedavies.innav.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import info.jakedavies.innav.R;

/**
 * Created by jakedavies on 15-10-05.
 */
public class LocationListAdapter extends ArrayAdapter<Location> {

    private Location[] locations;

    public LocationListAdapter(Context context,int resource, Location[] locations) {
        super(context, resource, locations);
        this.locations = locations;
    }
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.location_item, null);
        }
        
        String name = locations[position].getName();
        TextView lname = (TextView) v.findViewById(R.id.location_name);
        lname.setText(name);

        return v;
    }
}
