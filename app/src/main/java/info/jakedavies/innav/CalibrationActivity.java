package info.jakedavies.innav;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import info.jakedavies.innav.fragment.LocationSelectFragment;


public class CalibrationActivity extends BaseActivity {

    public static final String SETTINGS_NAME = "settings";
    public static final String SETTING_NAME = "num_steps";
    public static final int DEFAULT_NUM_STEPS = 50;
    private Button saveButton;
    private EditText stepInput;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);
        Window w = getWindow();
        w.setTitle("Settings");
        settings = getSharedPreferences(SETTINGS_NAME, 0);
        settings.getInt(SETTING_NAME, DEFAULT_NUM_STEPS);

        stepInput = (EditText) findViewById(R.id.step_input);
        saveButton = (Button) findViewById(R.id.step_finished);
        saveButton.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v){
               int numStepSetting = Integer.parseInt(stepInput.getText().toString());
               settings.edit().putInt(SETTING_NAME, numStepSetting).apply();
           }
        });
    }
}
