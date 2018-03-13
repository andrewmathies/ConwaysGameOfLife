package indiana.edu.awmathie.a290finalproject;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.SeekBar;

/**
 * Created by awmathie on 3/1/2018.
 * just for changing volume
 */

public class OptionsActivity extends Activity implements SeekBar.OnSeekBarChangeListener{

    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        SeekBar slider = findViewById(R.id.volume_Seekbar);
        slider.setMax(maxVolume);
        slider.setProgress(curVolume);
        slider.setOnSeekBarChangeListener(this);
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
        if (fromUser) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
