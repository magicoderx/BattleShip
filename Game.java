import java.util.*;

public class Game{
    static Battlefield map= new Battlefield();
    static Ship[] army=new Ship[5];
    static boolean cont=true;
    public static void main(String[] args){
        army[0]=new Ship(1,1);
        army[1]=new Ship(1,1);
        army[2]=new Ship(2,2);
        army[3]=new Ship(4,1);
        army[4]=new Ship(2,1);
        Scanner scanner = new Scanner(System.in);
        int nship=5;
        do{
            System.out.println("[1] Insert ship");
            System.out.println("[2] Show map");
            System.out.println("[3] Exit");
            int choice = scanner.nextInt();
            switch(choice){
                case 1:
                    if(nship>0){
                        clearScreen();
                        map.print();
                        System.out.println("Insert coordinates for "+army[nship-1].height+"x"+army[nship-1].width);
                        char yCoordChar = scanner.next().charAt(0);
                        int yCoord = toNumber(yCoordChar);
                        int xCoord = scanner.nextInt();
                        if(insertShip(xCoord,yCoord,army[nship-1])){
                            nship--;
                            clearScreen();
                            map.print();
                        }
                    }else{
                        clearScreen();
                        System.out.println("No more ship available");
                    }
                    break;
                case 2:
                    clearScreen();
                    System.out.println();
                    map.print();
                    System.out.println();
                    break;
                case 3:
                    clearScreen();
                    cont=false;
                    break;
            }
        }while(cont);
        System.out.println("Game finished");
        //map.print();
        return;
    }

    static boolean insertShip(int x, int y, Ship ship){
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
    static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  

    static int toNumber(char chr) {
        return (chr - 96);
    }

    static boolean checkCoord(int x, int y){
        if(x>10 || y>10){
            return false;
        } else {
            return true;
        }
    }

    static boolean checkShipBattlefield(int x, int y, Ship ship){
        for(int i=x-1;i<ship.height+x-1;i++){
            for(int j=y-1;j<ship.width+y-1;j++){
                if(x>=map.height || y>=map.width || map.matrix[i][j].contains("X")){
                    return false;
                }
            }
        }
        return true;
    }
}