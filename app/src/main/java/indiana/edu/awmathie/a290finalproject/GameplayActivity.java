package indiana.edu.awmathie.a290finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

/*
Created by Andrew Mathies: 2/28/18
This is the activity where the game is played, I add the image buttons and a speed seek bar
(that doesn't show up =( ) then I check if I need to load a saved game or just start a new one,
and finally I add the view object that actually displays the game
 */

public class GameplayActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private GameView gameView;
    public static final String SAVE_GAME = "SAVE_GAME";
    private String gameData;
  //  private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.activity_game, null);

        setContentView(layout);

        ImageButton playButton = layout.findViewById(R.id.btn_Play);
        playButton.setOnClickListener(this);

        ImageButton stopButton = layout.findViewById(R.id.btn_Stop);
        stopButton.setOnClickListener(this);

        ImageButton fastFButton = layout.findViewById(R.id.btn_Fast_Forward);
        fastFButton.setOnClickListener(this);

        ImageButton saveButton = layout.findViewById(R.id.btn_Save_Game);
        saveButton.setOnClickListener(this);

        ImageButton clearButton = layout.findViewById(R.id.btn_Clear_Screen);
        clearButton.setOnClickListener(this);

        SeekBar speedBar = layout.findViewById(R.id.speedBar);
        speedBar.setMax(9);
        speedBar.setProgress(4);
        speedBar.setOnSeekBarChangeListener(this);

        Intent intentHere = getIntent();

        // this is checking which activity loaded this activity, if it's from SavedGamesActivity,
        // then I need to load the save data instead of just starting a new game
        if (intentHere.getStringExtra("id").equals("SavedGamesActivity")) {
            // we need to reload the old game
            gameData = intentHere.getStringExtra("SAVE_GAME");
            gameView = new GameView(this, null, Resources.getSystem().getDisplayMetrics().widthPixels,
                    Resources.getSystem().getDisplayMetrics().heightPixels - 260, gameData);
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(Resources.getSystem().getDisplayMetrics().widthPixels,
                    Resources.getSystem().getDisplayMetrics().heightPixels - 260);
            gameView.setLayoutParams(layoutParams);
        } else {
            // new game
            gameView = new GameView(this, Resources.getSystem().getDisplayMetrics().widthPixels,
                    Resources.getSystem().getDisplayMetrics().heightPixels - 260);
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(Resources.getSystem().getDisplayMetrics().widthPixels,
                    Resources.getSystem().getDisplayMetrics().heightPixels - 260);
            gameView.setLayoutParams(layoutParams);

        }
//was 170, now 260
        ConstraintLayout item = layout.findViewById(R.id.game_Layout);
        item.addView(gameView);

      //  game = new Game(gameView, 100, 100);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Play:
                // start evolving generations
                gameView.startGame();
               // runOnUiThread(game);
                break;
            case R.id.btn_Stop:
                // stop evolving generations
                gameView.stopGame();
                break;
            case R.id.btn_Fast_Forward:
                // step forward one generation
                gameView.ff();
                break;
            case R.id.btn_Save_Game:
                // save the current game state
                Intent saveGameIntent = new Intent(this, SavedGamesActivity.class);
                String save = gameView.getSaveGame();
                Log.v(SAVE_GAME, "got JSON data, trying to save");
                saveGameIntent.putExtra("id", "game");
                saveGameIntent.putExtra(SAVE_GAME, save);
                startActivity(saveGameIntent);
                break;
            case R.id.btn_Clear_Screen:
                // get rid of the current game state and make a fresh new one
                gameView.reset();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (b) {
            gameView.speed = i + 1;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
