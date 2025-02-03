import java.io.Serializable;

public class Player implements Serializable {
    // Initialize private variables for player
    private String name;
    private Ship[] ships;
    private int nShip;
    private Battlefield map;
    // Initialize public variables for player's hits
    public int[][] hits;

    // Class constructor, initialize player putting 0s in hits map 
    public Player(String name, int gridSize) {
        this.name = name;
        this.map = new Battlefield();
        this.ships = new Ship[5];
        this.ships[0] = new Ship(5,2);
        this.ships[1] = new Ship(4,1);
        this.ships[2] = new Ship(2,1);
        this.ships[3] = new Ship(1,1);
        this.ships[4] = new Ship(1,1);
        this.nShip=0;
        this.hits = new int[gridSize][gridSize];
        for(int i=0;i<gridSize;i++){
            for(int j=0;j<gridSize;j++){
                hits[i][j]=0;
            }
        }
    }

    // Function to insert ship into the battlefield
    public boolean insertShip(int x, int y){
        if(checkCoord(x,y)){
            if(checkShipBattlefield(x,y,this.ships[this.nShip])){
                // If right value of coordinates, insert ship into the battlefield placing a X in the matrix
                for(int i=x-1;i<this.ships[this.nShip].height+x-1;i++){
                    for(int j=y-1;j<this.ships[this.nShip].width+y-1;j++){
                        this.map.matrix[i][j]="X";
                    }
                }
                // Decrease 1 to x and y to match the matrix indexes
                this.ships[this.nShip].xCoord=x-1;
                this.ships[this.nShip].yCoord=y-1;
                this.nShip++;
                return true;
            } else {
                return false;
            }
        } else {
            System.out.println("Wrong coordinates! Please enter coordinates from a-j and from 1-10");
            return false;
        }
    }

    // Function to check if the ship can be placed in the battlefield
    private boolean checkShipBattlefield(int x, int y, Ship ship){
        for(int i=x-1;i<ship.height+x-1;i++){
            for(int j=y-1;j<ship.width+y-1;j++){
                // If the position is already occupied, return false
                if(j>this.map.width-1 || i> this.map.height-1 || this.map.matrix[i][j].contains("X")){
                    return false;
                }
            }
        }
        return true;
    }

    // Function to check if the coordinates are right
    private boolean checkCoord(int x, int y){
        if(x>this.map.height || y>this.map.width){
            return false;
        } else {
            return true;
        }
    }

    // Function to mark the hit in the battlefield
    public boolean markHit(int x, int y) { 
        // Check if the position was already hit
        if(this.hits[x][y] != 0){
            System.out.println("You already hit this position!");
            return false;
        }else if(this.map.matrix[x][y].contains("X")){ 
            // If there is a ship in the position, mark the hit with 1, if not, mark with -1
            this.hits[x][y] = 1; 
            // Cycle for every ship in the array and increase the hits variable of the ship
            for(int i=0;i<this.ships.length;i++){
                if(this.ships[i].xCoord <= x && x<= this.ships[i].xCoord+this.ships[i].height-1 && this.ships[i].yCoord <= y &&  y <= this.ships[i].yCoord+this.ships[i].width-1 ){
                    this.ships[i].hits++;
                    // If the hits variable of the ship is equal to the ship size, the ship is destroyed
                    if(this.ships[i].hits==this.ships[i].height*this.ships[i].width){
                        this.ships[i].destroyed=true;
                        // Then, mark the hits in the battlefield with 2 to show the ship was destroyed
                        for(int sx=this.ships[i].xCoord;sx<=this.ships[i].xCoord+this.ships[i].height-1;sx++){
                            for(int sy=this.ships[i].yCoord;sy<=this.ships[i].yCoord+this.ships[i].width-1;sy++){
                                this.hits[sx][sy] = 2; 
                            }
                        }
                        // Decrease available player's ships
                        this.nShip--;
                        System.out.println("Ship destroyed!");
                    }
                }
            }
            return true;
        } else { 
            this.hits[x][y] = -1; 
            return true;
        }
    }

    // Public function to get the player's name
    public String getName(){
        return this.name;
    }

    // Public function to get the number of dhips of the player
    public int getNShips(){
        return this.nShip;
    }

    // Public function to get the player's ship
    public Ship getShip(){
        return this.ships[this.nShip];
    }

    // Public function to get the player's battlefield
    public Battlefield getMap(){
        return this.map;
    }
}
