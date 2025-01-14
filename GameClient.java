import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class GameClient{
    public static Room room;
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Battlefield bOwn = new Battlefield();
        Battlefield bOpponent = new Battlefield();

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            GameInterface game = (GameInterface) registry.lookup("GameServer");
            
            System.out.println("Enter your username: ");
            String username = scanner.nextLine();

            System.out.println("[1] Create a room\n[2] Join a room");
            int choice = scanner.nextInt();

            //TODO Gestione errori
            switch(choice){
                case 1:
                    room = game.createRoom(username);
                    clearScreen();
                    System.out.println("Room created with ID: " + room.getId());
                    break;
                case 2:
                    System.out.print("Enter room ID: ");
                    long roomId = scanner.nextLong();
                    room = game.joinRoom(roomId, username);
                    clearScreen();
                    System.out.println("Joined room: " + room.getId());
                    break;
            }
            do{
                if(room!=null && !game.gameStarted(room.getId())){
                    System.out.println("Game not started yet.");
                    System.out.println("[1] Insert ship\n[2] Show map\n[3] Check game status");
                    choice = scanner.nextInt();
                    switch(choice){
                        case 1:
                            if(game.getNShip(room.getId(),username)<6){
                                clearScreen();
                                printMap(game.getBattlefield(room.getId(),username));
                                //game.printMap(room.getId(),username);
                                System.out.println("Insert coordinates for 2x2 ship");
                                char yCoordChar = scanner.next().charAt(0);
                                int yCoord = toNumber(yCoordChar);
                                int xCoord = scanner.nextInt();
                                game.insertShip(room.getId(), username, xCoord, yCoord, new Ship(2,2));
                            }else{
                                clearScreen();
                                System.out.println("No more ship available");
                            }
                            break;
                        case 2:
                            clearScreen();
                            System.out.println();
                            printMap(game.getBattlefield(room.getId(),username));
                            //game.printMap(room.getId(),username);
                            System.out.println();
                            break;
                        case 3:
                            clearScreen();
                            break;
                    }
                }else if(room!=null && game.gameStarted(room.getId())){
                    System.out.println("Game started.");
                    System.out.println("[1] Show maps\n[2] Attack\n[3] Get turn");
                    choice = scanner.nextInt();
                    switch(choice){
                        case 1:
                            clearScreen();
                            System.out.println(game.getOpponentUsername(room.getId(),username)+"\n");
                            printMap(game.getBattlefield(room.getId(),game.getOpponentUsername(room.getId(),username)));
                            //game.printMap(room.getId(),game.getOpponentUsername(room.getId(),username));
                            System.out.println("\n======================================\n"+username+"\n");
                            printMap(game.getBattlefield(room.getId(),username));
                            //game.printMap(room.getId(),username);
                            break;
                        case 2:
                            clearScreen();
                            if(game.getTurn(room.getId()).contains(username)){
                                //Logica attacco
                            }else{
                                System.out.println("Not your turn.");
                                break;
                            }
                        case 3:
                            System.out.println(game.getTurn(room.getId())+"\'s turn.");
                            break;
                    }
                }else{
                    System.out.println("Game finished");
                }
            }while(room!=null);
            return;
        }catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    /*
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
    */

    static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  

    static int toNumber(char chr) {
        return (chr - 96);
    }

    static void printMap(Battlefield map){
        System.out.println("    a  b  c  d  e  f  g  h  i  j");
        for(int i=0;i<map.height;i++){
            if(i<9){
                System.out.print((i+1)+"  ");
            }else{
            System.out.print((i+1)+" ");
            }
            for(int j=0;j<map.width;j++){
                System.out.print("[" + map.matrix[i][j] + "]");
            }
            System.out.println();
        }
    }
}