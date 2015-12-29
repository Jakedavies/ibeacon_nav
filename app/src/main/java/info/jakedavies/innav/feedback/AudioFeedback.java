package info.jakedavies.innav.feedback;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by jakedavies on 15-10-30.
 */
public class AudioFeedback implements FeedbackInterface, TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private boolean ready = false;

    public AudioFeedback(Context context){
        tts = new TextToSpeech(context, this);
    }

    @Override
    public void turnLeft() {
        speak("Turn Left");
    }

    @Override
    public void turnRight() {
        speak("Turn Right");
    }

    @Override
    public void straight(int x) {
        speak("Move "+x+" meters forward");
    }
    private void speak(CharSequence text){
        // Speak only if the TTS is ready
        // and the user has allowed speech

        if(ready) {
            HashMap<String, String> hash = new HashMap<>();
            hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                    String.valueOf(AudioManager.STREAM_NOTIFICATION));
            Date d = new Date();
            tts.speak(text, TextToSpeech.QUEUE_ADD, null, d.toString());
        } else {
            Log.d("DEBUG", "speak not ready");
        }
    }
    public void destroy(){
        tts.shutdown();
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            // Change this to match your
            // locale
            tts.setLanguage(Locale.CANADA);
            ready = true;
        }else{
            ready = false;
        }
    }
}
