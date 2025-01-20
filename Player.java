import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private Ship[] ships;
    private int nShip;
    public int[][] hits;
    private boolean ready;
    private Battlefield map;

    public Player(String name, int gridSize, int nShips) {
        this.name = name;
        this.map = new Battlefield();
        this.ships = new Ship[nShips];
        this.nShip=0;
        this.ready = false;
        this.hits = new int[gridSize][gridSize];
        for(int i=0;i<gridSize;i++){
            for(int j=0;j<gridSize;j++){
                hits[i][j]=0;
            }
        }
    }

    public boolean insertShip(int x, int y, Ship ship){
        if(checkCoord(x,y)){
            if(checkShipBattlefield(x,y,ship)){
                for(int i=x-1;i<ship.height+x-1;i++){
                    for(int j=y-1;j<ship.width+y-1;j++){
                        map.matrix[i][j]="X";
                    }
                }
                System.out.println("Ship inserted by "+this.name+"!");
                this.ships[nShip]=ship;
                this.ships[nShip].xCoord=x-1;
                this.ships[nShip].yCoord=y-1;
                this.nShip++;
                return true;
            } else {
                System.out.println("Another ship already placed here");
                return false;
            }
        } else {
            System.out.println("Wrong coordinates! Please enter coordinates from a-j and from 1-10");
            return false;
        }
    }

    private boolean checkShipBattlefield(int x, int y, Ship ship){
        for(int i=x-1;i<ship.height+x-1;i++){
            for(int j=y-1;j<ship.width+y-1;j++){
                if(x>=map.height || y>=map.width || map.matrix[i][j].contains("X")){
                    return false;
                }
            }
        }
        return true;
    }
    private boolean checkCoord(int x, int y){
        if(x>10 || y>10){
            return false;
        } else {
            return true;
        }
    }

    public int markHit(int x, int y) { 
        if(map.matrix[x][y].contains("X")){ 
            this.hits[x][y] = 1; 
            for(int i=0;i<this.ships.length;i++){
                if(this.ships[i].xCoord <= x && x<= this.ships[i].xCoord+this.ships[i].height-1 && this.ships[i].yCoord <= y &&  y <= this.ships[i].yCoord+this.ships[i].width-1 ){
                    this.ships[i].hits++;
                    if(this.ships[i].hits==this.ships[i].height*this.ships[i].width){
                        this.ships[i].destroyed=true;
                        for(int sx=this.ships[i].xCoord;sx<=this.ships[i].xCoord+this.ships[i].height-1;sx++){
                            for(int sy=this.ships[i].yCoord;sy<=this.ships[i].yCoord+this.ships[i].width-1;sy++){
                                this.hits[sx][sy] = 2; 
                            }
                        }
                        this.nShip--;
                        System.out.println("Ship destroyed!");
                    }
                }
            }
        } else { 
            this.hits[x][y] = -1; 
        }
    }

    public String getName(){
        return this.name;
    }

    public int getNShips(){
        return this.nShip;
    }

    public Battlefield getMap(){
        return this.map;
    }
}
