import java.io.Serializable;

public class Battlefield implements Serializable{
    // Initialize public variables for player's battlefield
    public int height;
    public int width;
    public String[][] matrix;

    // Class constructor, initialize the 10x10 map and fill matrix with empty spaces
    public Battlefield(){
        this.height=10;
        this.width=10;
        this.matrix = new String[height][width];
        for(int i=0;i<this.height;i++){
            for(int j=0;j<this.width;j++){
                this.matrix[i][j]=" ";
            }
        }
    }

    // Function to print the map
    public void print(){
        System.out.println("    a  b  c  d  e  f  g  h  i  j");
        for(int i=0;i<this.height;i++){
            if(i<9){
                System.out.print((i+1)+"  ");
            }else{
            System.out.print((i+1)+" ");
            }
            for(int j=0;j<this.width;j++){
                System.out.print("[" + this.matrix[i][j] + "]");
            }
            System.out.println();
        }
    }
}