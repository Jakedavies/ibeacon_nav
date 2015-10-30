package info.jakedavies.innav.fragment;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import info.jakedavies.innav.sensor.Heading;
import info.jakedavies.innav.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class NavigationFragment extends Fragment implements Heading.HeadingChangedListener {



    private SensorManager mSensorManager;
    private Heading  mSensor;
    private TextView headingView;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mSensor = new Heading(getActivity().getApplication().getApplicationContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_navigate, container, false);
        headingView = (TextView) v.findViewById(R.id.heading);
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
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void headingChanged(int heading) {
        String instruction = "";
        if(heading > 340 || heading < 20){
            instruction = "Go Straight";
        }else if(heading > 180){
            instruction = "Turn Right";
        }else if(heading <= 180){
            instruction = "Turn Left";
        }
        headingView.setText(String.valueOf(heading)+" degrees of north " +instruction);
    }
}
