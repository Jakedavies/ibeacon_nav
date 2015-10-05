package info.jakedavies.innav;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by jakedavies on 15-10-05.
 */
public class LocationListAdapter extends ArrayAdapter<String> {

    private String[] objects;
    public LocationListAdapter(Context context,int resource, String[] objects) {
        super(context, resource, objects);
        this.objects = objects;
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.location_item, null);
        }

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
        String name = objects[position];

        if (name != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView lname = (TextView) v.findViewById(R.id.location_name);

            // check to see if each individual textview is null.
            // if not, assign some text!
            if (lname != null) {
                lname.setText(name);
            }
        }

        // the view must be returned to our activity
        return v;
    }
}
