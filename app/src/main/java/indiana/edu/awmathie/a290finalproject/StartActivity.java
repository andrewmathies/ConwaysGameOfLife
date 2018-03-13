package indiana.edu.awmathie.a290finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


/**
 * Created by awmathie on 2/28/2018.
 * Activity file for start screen, also handles button presses
 */

public class StartActivity extends Activity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        View NewButton = findViewById(R.id.btn_New);
        NewButton.setOnClickListener(this);

        View SavedButton = findViewById(R.id.btn_Saved);
        SavedButton.setOnClickListener(this);

        View OptionsButton = findViewById(R.id.btn_Options);
        OptionsButton.setOnClickListener(this);

        View AboutButton = findViewById(R.id.btn_About);
        AboutButton.setOnClickListener(this);

        // this plays the background music
        startService(new Intent(this, BackgroundSoundService.class));
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        this.stopService(new Intent(this, BackgroundSoundService.class));
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        this.startService(new Intent(this, BackgroundSoundService.class));
//    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_New:
                // start a new game!
                Intent newGameIntent = new Intent(this, GameplayActivity.class);
                newGameIntent.putExtra("id", "StartActivity");
                startActivity(newGameIntent);
                break;
            case R.id.btn_Saved:
                // go to the activity where you can load saved games
                Intent savedGamesIntent = new Intent(this, SavedGamesActivity.class);
                savedGamesIntent.putExtra("id", "start");
                startActivity(savedGamesIntent);
                break;
            case R.id.btn_Options:
                // go to the options activity
                Intent optionsIntent = new Intent(this, OptionsActivity.class);
                startActivity(optionsIntent);
                break;
            case R.id.btn_About:
                // load the about dialog
                AlertDialog alertDialog = AboutDialog.create(this);
                alertDialog.show();
                break;
        }
    }
}
