import java.io.Serializable;

public class Ship implements Serializable{
    // Initialize public variables for ship
    public int height;
    public int width;
    public int xCoord;
    public int yCoord;
    public boolean destroyed;
    public int hits;

    // Class constructor, initialize ship with height and width
    public Ship(int height, int width){
        this.height=height;
        this.width=width;
        this.destroyed=false;
        this.hits=0;
        this.xCoord=0;
        this.yCoord=0;
    }
}