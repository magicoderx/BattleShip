import java.io.Serializable;

public class Ship implements Serializable{
    public int height;
    public int width;
    public boolean destroyed;
    public int hits;

    public Ship(int height, int width){
        this.height=height;
        this.width=width;
        this.destroyed=false;
        this.hits=0;
    }
}