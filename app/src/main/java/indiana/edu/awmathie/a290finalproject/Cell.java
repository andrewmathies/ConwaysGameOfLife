package indiana.edu.awmathie.a290finalproject;

/**
 * Created by awmathie on 3/1/2018.
 * Just a simple data structure for a cell, this used to contain more, now it doesn't
 * really make sense to have it since it's just a boolean...
 */

public class Cell {

    public boolean alive;

    public Cell() {
        alive = false;
    }

    public String toString() {
        return "alive: " + alive;
    }

}
