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
public class SectionListAdapter extends ArrayAdapter<String> {

    private String[] objects;
    public SectionListAdapter(Context context,int resource, String[] objects) {
        super(context, resource, objects);
        this.objects = objects;
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.section_item, null);
        }

        String name = objects[position];
        TextView lname = (TextView) v.findViewById(R.id.section_name);
        lname.setText(name);

        return v;
    }
}
