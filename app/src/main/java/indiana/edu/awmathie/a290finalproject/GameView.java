package indiana.edu.awmathie.a290finalproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.gson.Gson;

/**
 * Created by awmathie on 2/28/2018.
 * This is where everything related to the actual game happens, I tried seperating the different
 * components but sloppy code that works > beautiful code that does nothing
 */

public class GameView extends View {
    private Paint linePaint = new Paint();
    private int backgroundColor = Color.BLACK;

    public boolean active;
//    private Game game;
//    private ExecutorService gameExecutor;
//    private Future future;
//    public Handler handler;

    // this is all information about how to display the game
    private int maxColumns  = 22, maxRows;
    private int minCellWidth = 30;
    private int maxWidth = minCellWidth * maxColumns, maxHeight = maxWidth;

    public int speed;

    // I need two of these to see how a generation will evolve
    private Cell[][] cells, future;

    private int curColumns, curRows, curWidth, curHeight, curCellSize;

    //public boolean touched = false;


    // CONSTRUCTORS
    //-----------------------------------------------------------------------------------------------

    public GameView(Context context, int width, int height) {
        this(context, null, width, height);
    }

    public GameView(Context context, AttributeSet attrs, int width, int height, String gameData) {
        super(context, attrs);

        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setColor(getResources().getColor(R.color.lineColor));

        speed = 10;

        curWidth = width;
        curHeight = height + 60;

        curColumns = 20;

        curCellSize = curWidth / curColumns;
        curRows = (curHeight / curCellSize);
        maxRows = curRows;

        // this constructor is only called if we are loading a saved game, this just deserializes
        // the data from the save file
        Gson gson = new Gson();
        cells = gson.fromJson(gameData, Cell[][].class);
        future = new Cell[maxColumns][maxRows];

        for (int i = 0; i < maxColumns; i++) {
            for (int j = 0; j < maxRows; j++) {
                future[i][j] = new Cell();
            }
        }

        Log.v("LOAD", "loaded the cells back?");
    }

    public GameView(Context context, AttributeSet attrs, int width, int height) {
        super(context, attrs);

        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setColor(getResources().getColor(R.color.lineColor));

        speed = 10;

        curWidth = width;
        curHeight = height + 60;

        curColumns = 20;

        curCellSize = curWidth / curColumns;
        curRows = curHeight / curCellSize;
        maxRows = curRows;

        cells = new Cell[maxColumns][maxRows];
        future = new Cell[maxColumns][maxRows];

        for (int i = 0; i < maxColumns; i++) {
            for (int j = 0; j < maxRows; j++) {
                cells[i][j] = new Cell();
                future[i][j] = new Cell();
            }
        }

        active = false;

//        gameExecutor = Executors.newSingleThreadExecutor();
//        handler = new Handler(Looper.getMainLooper()) {
//            @Override
//            public void handleMessage(Message inputMessage) {
//                Log.v("MESSAGING", "got the cells from the game!");
//                dataRecieved = (Cell[][]) inputMessage.obj;
//
//                for (int i = 0; i < maxColumns; i++) {
//                    for (int j = 0; j < maxRows; j++) {
//                        cells[i][j] = dataRecieved[i][j];
//                    }
//                }
//            }
//        };
    }

    // GAME LOGIC
    //----------------------------------------------------------------------------------------------

    // this evolves the game one generation forward
    public void step() {
        int neighbors;
        Cell cur, temp;

        Log.v("GAME LOGIC", "starting to step");

        for (int i = 1; i < maxColumns - 1; i++) {
            //Log.v("WHY", "cell " + i + " " + 10 + " is: " + cells[i][10].alive);
            for (int j = 1; j < maxRows - 1; j++) {
                neighbors = neighbors(i, j);
                if ((cells[i][j].alive && neighbors == 2) || neighbors == 3) {
                    future[i][j].alive = true;
                }
                else {
                    future[i][j].alive = false;
                }
            }
        }

        Log.v("GAME LOGIC", "finished stepping, now copying");

        // this is copying what I calculated the next generation to be into the current cells array
        for (int i = 0; i < maxColumns; i++) {
            for (int j = 0; j < maxRows; j++) {
                cells[i][j] = future[i][j];
                future[i][j] = new Cell();
            }
        }

        Log.v("GAME LOGIC", "finished copying, now drawing");
    }

    // how many neighbors of a cell are alive
    public int neighbors(int x, int y) {
        int out = 0;

        if (cells[x-1][y-1].alive)
            out++;
        if (cells[x-1][y].alive)
            out++;
        if (cells[x-1][y+1].alive)
            out++;
        if (cells[x][y+1].alive)
            out++;
        if(cells[x][y-1].alive)
            out++;
        if(cells[x+1][y+1].alive)
            out++;
        if(cells[x+1][y].alive)
            out++;
        if(cells[x+1][y-1].alive)
            out++;

        return out;
    }

//    public void incNeighbors(int x, int y) {
//        for (int i = -1; i < 2; i++) {
//            for (int j = -1; j < 2; j++) {
//                if (i + x < maxColumns && i + x > 0 && j + y < maxRows && j + y > 0)
//                    cells[x+i][y+j].neighbors++;
//            }
//        }
//    }
//
//    public void decNeighbors(int x, int y) {
//        for (int i = -1; i < 2; i++) {
//            for (int j = -1; j < 2; j++) {
//                if (i + x < maxColumns && i + x > 0 && j + y < maxRows && j + y > 0)
//                    cells[x+i][y+j].neighbors--;
//            }
//        }
//    }


    // DRAWING
    //----------------------------------------------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(backgroundColor);

        //drawing cells
        for (int i = 0; i < curColumns - 1; i++) {
            for (int j = 0; j < curRows - 1; j++) {
                if (cells[i + 1][j + 1].alive) {
                    canvas.drawRect(i * curCellSize, j * curCellSize,
                            (i + 1) * curCellSize, (j + 1) * curCellSize,
                            linePaint);
                }
            }
        }

        //drawing lines
        for (int i = 1; i < curColumns; i++) {
            canvas.drawLine(i * curCellSize, 0, i * curCellSize, curHeight, linePaint);
        }

        for (int i = 1; i < curRows - 1; i++) {
            canvas.drawLine(0, i * curCellSize, curWidth, i * curCellSize, linePaint);
        }
    }

    //TOUCH INPUT
    //----------------------------------------------------------------------------------------------

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // this is for making a cell alive or dead
                int colPress = (int) event.getX() / curCellSize;
                int rowPress = (int) event.getY() / curCellSize;
                if (rowPress < maxRows - 1) {
                    cells[colPress + 1][rowPress + 1].alive = !cells[colPress + 1][rowPress + 1].alive;
                }
                //game.incNeighbors(colPress + startColumn, rowPress + startRow);
                //send message to game to inc neighbors
                invalidate();
        }

        return true;
    }

    //BUTTON FUNCTIONS
    //----------------------------------------------------------------------------------------------

    // starts a thread where the game logic is performed, this had to be seperate from the UI thread
    public void startGame() {
        active = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (active) {
                    step();
                    postInvalidate();
                    SystemClock.sleep(200/speed);
                }
            }
        }).start();

        //future = gameExecutor.submit(new Game(maxColumns, maxRows));
    }

    public void rewind() {
        if (active)
            stopGame();

        //?
    }

    public void ff() {
        if (active)
            stopGame();

        step();
        invalidate();
        // send message to game to step
    }

    public void stopGame() {
        //touched = true;
        if (!active)
            return;
        active = false;
        //future.cancel(true);
    }

    public void reset() {
        cells = new Cell[maxColumns][maxRows];

        for(int i = 0; i < maxColumns; i++) {
            for (int j = 0; j < maxRows; j++) {
                cells[i][j] = new Cell();
                future[i][j] = new Cell();
            }
        }

        invalidate();
        // send message to game to reset its 2d array
    }

    // SAVING
    //----------------------------------------------------------------------------------------------

    // serialize the game into Gson and pass it along
    public String getSaveGame() {
        String out = new Gson().toJson(cells);
        return out;
    }
}
