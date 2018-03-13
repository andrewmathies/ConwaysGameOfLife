package indiana.edu.awmathie.a290finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by awmathie on 3/1/2018.
 * this saves and loads gson data to and from internal storage
 */

public class SavedGamesActivity extends Activity {

    private int numberOfSaves;
    private File gamesDir;
    private ArrayList<File> saves;
    private String saveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_games);

        Log.v("SAVE_GAME", "got to activity, trying to save");

        String path = getFilesDir().toString();

        gamesDir =  new File(path);
        if (!gamesDir.isDirectory()) {
            //Log.v("debug", "directory wasn't found");
            //making it here
            gamesDir.mkdir();
            numberOfSaves = 0;
        } else {
            numberOfSaves = folderSize();
            Log.v("debug", "path for dir is: " + gamesDir.toString());
            Log.v("debug", "number of files in folder is: " + numberOfSaves);
        }

        Intent intent = getIntent();

        if (intent.getStringExtra("id").equals("game")) {
            // we came here to save our game
            String save = intent.getStringExtra(GameplayActivity.SAVE_GAME);
            saveGame(save);
            Intent intentBackToGame = new Intent(this, GameplayActivity.class);
            intentBackToGame.putExtra("id", "SavedGamesActivity");
            intentBackToGame.putExtra("SAVE_GAME", saveData);

            // I need to do this so that hitting back from a loaded game doesn't take you to
            // the load a game activity
            TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(intentBackToGame)
                    .startActivities();

           // startActivity(intentBackToGame);
        } else {
            // we are coming from the start screen
            if (numberOfSaves == 0) {
                RelativeLayout relativeLayout = findViewById(R.id.layout_For_List);
                TextView noSaves = new TextView(this);
                noSaves.setText(R.string.NoSavesText);
                relativeLayout.addView(noSaves);
            }
            else {
                ListView listView = findViewById(R.id.list_Of_Saves);
                loadGames();
                ListAdapter itemsAdapter = new ListAdapter(this, R.layout.activity_save_games, saves);
                listView.setAdapter(itemsAdapter);
            }
        }
    }

    private void loadGames() {
        if (numberOfSaves == 0)
            return;

        saves = new ArrayList<File>();
       // File cur;

        //int i = 0;

        for (File file : gamesDir.listFiles()) {
            if (file.getName().contains("save")) {
                saves.add(file);
            }
        }
    }

    public void load(int i) {
        String filename = "save" + i;
        FileInputStream inputStream;
        StringBuffer stringBuffer = new StringBuffer("");
        int n;

        try {
            inputStream = openFileInput(filename);

            byte[] buffer = new byte[1024];

            while ((n = inputStream.read(buffer)) != -1) {
                stringBuffer.append(new String(buffer, 0, n));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String dataAsGson = stringBuffer.toString();

        Intent loadGame = new Intent(this, GameplayActivity.class);
        loadGame.putExtra("id", "SavedGamesActivity");
        loadGame.putExtra("SAVE_GAME", dataAsGson);
        Log.v("LOAD", "loaded game " + i);

        // I need to do this so that hitting back from a loaded game doesn't take you to
        // the load a game activity
        TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(loadGame)
                .startActivities();
    }

    public int folderSize() {
        int n = 0;
        for (File file : gamesDir.listFiles()) {
            if (file.isFile())
                n++;
        }
        return n;
    }

    private void saveGame(String saveAsGson) {
        String filename = "save" + numberOfSaves;
        FileOutputStream outputStream;

        saveData = saveAsGson;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(saveAsGson.getBytes());
            outputStream.close();
            Log.v("SAVE", "successfully saved game!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // this converts an arraylist of File objects into buttons that will load the save data in each
    // respective file
    private class ListAdapter extends ArrayAdapter<File> {

        private int layout;

        public ListAdapter(@NonNull Context context, int resource, @NonNull List<File> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(layout, parent, false);

            final Button curButton = new Button(parent.getContext());
            curButton.setTag("saveButton" + position);
            CharSequence buttonText = "Save " + position;
            curButton.setText(buttonText);
            curButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    load(position);
                }
            });

            return curButton;
        }
    }
}
