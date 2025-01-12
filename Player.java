import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private Ship[] ships;
    private boolean[][] hits;
    private boolean ready;
    private Battlefield map;

    public Player(String name, int gridSize) {
        this.name = name;
        this.map = new Battlefield();
        this.ships = new Ship[5];
        this.ships[0]=new Ship(1,1);
        this.ships[1]=new Ship(1,1);
        this.ships[2]=new Ship(2,2);
        this.ships[3]=new Ship(4,1);
        this.ships[4]=new Ship(2,1);
        this.ready = false;
        this.hits = new boolean[gridSize][gridSize];
    }

    // getShips() In questo caso stesso dal client faccio player.map.print();
    public boolean[][] getHits() { return hits; }

    public boolean insertShip(int x, int y, Ship ship){
        if(checkCoord(x,y)){
            if(checkShipBattlefield(x,y,ship)){
                for(int i=x-1;i<ship.height+x-1;i++){
                    for(int j=y-1;j<ship.width+y-1;j++){
                        map.matrix[i][j]="X";
                    }
                }
                System.out.println("Ship inserted!");
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

    public void markHit(int x, int y) { hits[x][y] = true; }

    public String getName(){
        return this.name;
    }

    public void getMap(){
        this.map.print();
    }
}
