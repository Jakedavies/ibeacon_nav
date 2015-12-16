package info.jakedavies.innav.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

import info.jakedavies.innav.R;
import info.jakedavies.innav.feedback.AudioFeedback;
import info.jakedavies.innav.lib.floorplan.Floorplan;
import info.jakedavies.innav.sensor.Heading;
import info.jakedavies.innav.view.Map;

public class BlindNavigationFragment extends Fragment implements Heading.HeadingChangedListener {

    private Map mapView;
    private Heading  mSensor;
    private TextView degree;
    private AudioFeedback af;
    private LinearLayout speakerButton;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mSensor = new Heading(getActivity().getApplication().getApplicationContext(), this);
        af = new AudioFeedback(getActivity().getApplication().getApplicationContext());
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
        degree = (TextView) v.findViewById(R.id.degree);
        speakerButton = (LinearLayout) v.findViewById(R.id.speakerButton);
        speakerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onSpeakerClick(v);
            }
        });

        // by programmatically adding the view we can maintain a pointer to the view and modify data
        mapLayout.addView(mapView);
        return v;
    }
    @Override
    public void onStart(){
        super.onStart();
        mSensor.registerListener();
    }

    @Override
    public void onStop(){
        super.onStop();
        mSensor.unregisterListener();
    }

    @Override
    public void headingChanged(int heading) {
        degree.setText(String.valueOf(heading));
        mapView.translateToPosition(heading);
    }
    public void onSpeakerClick(View v) {
        Log.d("DEBUG", "speaker clicked");
        Random rn = new Random();
        int selection = rn.nextInt(3);
        if(selection == 0){
            af.turnLeft();
        } else if(selection == 1){
            af.turnRight();
        } else {
            Random rn2 = new Random();
            int d2 = rn2.nextInt(20);
            af.straight(d2);
        }

    }

    // heading sensor update event should push event to mapview to modify view

}
