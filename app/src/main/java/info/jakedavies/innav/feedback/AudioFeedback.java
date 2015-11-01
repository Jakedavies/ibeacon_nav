package info.jakedavies.innav.feedback;

import android.os.Vibrator;

/**
 * Created by jakedavies on 15-10-30.
 */
public class AudioFeedback implements FeedbackInterface {

    private Vibrator vibrator;
    private long[] leftPattern = {50, 250, 500};
    private long[] rightPattern = {500, 250, 50};

    AudioFeedback(Vibrator v){
        vibrator = v;
    }

    @Override
    public void turnLeft() {
        vibrator.vibrate(leftPattern, 1);
    }

    @Override
    public void turnRight() {
        vibrator.vibrate(rightPattern, 1);
    }

    @Override
    public void straight(int x) {
        // if your going the right direction no input is needed?
    }
}
