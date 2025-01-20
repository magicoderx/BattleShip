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
                                printMyMap(game.getBattlefield(room.getId(),username));
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
                            printMyMap(game.getBattlefield(room.getId(),username));
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
                            System.out.println(game.getOpponentPlayer(room.getId(),username).getName()+"\n");
                            printOpponentMap(game.getOpponentPlayer(room.getId(),username));
                            System.out.println("\n======================================\n"+username+"\n");
                            printMyMap(game.getBattlefield(room.getId(),username));
                            break;
                        case 2:
                            clearScreen();
                            if(game.getTurn(room.getId()).contains(username)){
                                System.out.println("Insert coordinates for 2x2 ship");
                                char yCoordChar = scanner.next().charAt(0);
                                int yCoord = toNumber(yCoordChar);
                                int xCoord = scanner.nextInt();
                                game.attack(room.getId(), username, xCoord, yCoord);
                                break;
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

    static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  

    static int toNumber(char chr) {
        return (chr - 96);
    }

    static void printMyMap(Battlefield map){
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

    static void printOpponentMap(Player opponent){
        System.out.println("    a  b  c  d  e  f  g  h  i  j");
        for(int i=0;i<opponent.getMap().height;i++){
            if(i<9){
                System.out.print((i+1)+"  ");
            }else{
            System.out.print((i+1)+" ");
            }
            for(int j=0;j<opponent.getMap().width;j++){
                if(opponent.hits[i][j]==1){
                    System.out.print("[O]");
                }else if(opponent.hits[i][j]==2){
                    System.out.print("[X]");
                }else if(opponent.hits[i][j]==-1){
                    System.out.print("[-]");
                }else{
                    System.out.print("[ ]");
                }
            }
            System.out.println();
        }
    }
}