package info.jakedavies.innav;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import info.jakedavies.innav.fragment.LocationSelectFragment;


public class CalibrationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);
        Window w = getWindow();
        w.setTitle("Settings");
    }
}
