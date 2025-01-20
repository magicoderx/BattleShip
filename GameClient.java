import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class GameClient{
    // Iitialize public variable room
    public static Room room;

    public static void main(String[] args){
        // Initialize scanner for user input
        Scanner scanner = new Scanner(System.in);

        try {
            // Get the registry from the server and the GameServer object from the registry
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            GameInterface game = (GameInterface) registry.lookup("GameServer");
            
            // Ask player for username
            System.out.println("Enter your username: ");
            String username = scanner.nextLine();

            // Ask player to create or join a room
            System.out.println("[1] Create a room\n[2] Join a room");
            int choice = scanner.nextInt();

            //TODO Error management
            switch(choice){
                // If player chooses to create a room, create a room and print the room ID
                case 1:
                    room = game.createRoom(username);
                    clearScreen();
                    System.out.println("Room created with ID: " + room.getId());
                    break;
                // Otherwise, ask player for the room ID and join the room
                case 2:
                    System.out.print("Enter room ID: ");
                    long roomId = scanner.nextLong();
                    room = game.joinRoom(roomId, username);
                    clearScreen();
                    System.out.println("Joined room: " + room.getId());
                    break;
            }
            // Begin loop until the room is null (game is finished)
            do{
                // If the game is not started yet, ask player to insert a ship, show the map or check the game status
                if(room!=null && !game.gameStarted(room.getId())){
                    System.out.println("Game not started yet.");
                    System.out.println("[1] Insert ship\n[2] Show map\n[3] Check game status");
                    choice = scanner.nextInt();
                    switch(choice){
                        // If player chooses to insert a ship, ask for the coordinates and insert the ship
                        case 1:
                            if(game.getNShip(room.getId(),username)<6){
                                clearScreen();
                                printMyMap(game.getBattlefield(room.getId(),username));
                                System.out.println("Insert coordinates for 2x2 ship");
                                // Get letteral coordinate and convert it to number
                                char yCoordChar = scanner.next().charAt(0);
                                int yCoord = toNumber(yCoordChar);
                                int xCoord = scanner.nextInt();
                                game.insertShip(room.getId(), username, xCoord, yCoord, new Ship(2,2));
                            }else{
                                clearScreen();
                                System.out.println("No more ship available");
                            }
                            break;
                        // If player chooses to show the map, print the map
                        case 2:
                            clearScreen();
                            System.out.println();
                            printMyMap(game.getBattlefield(room.getId(),username));
                            System.out.println();
                            break;
                        // Otherwise, do nothing because recylcing the loop will show the game status
                        case 3:
                            clearScreen();
                            break;
                    }
                // If the game is started, ask player to show the maps, attack or get the turn
                }else if(room!=null && game.gameStarted(room.getId())){
                    System.out.println("Game started.");
                    System.out.println("[1] Show maps\n[2] Attack\n[3] Get turn");
                    choice = scanner.nextInt();
                    switch(choice){
                        // If player chooses to show the maps, print the maps in vertical order (the opponent's map is on top)
                        case 1:
                            clearScreen();
                            System.out.println(game.getOpponentPlayer(room.getId(),username).getName()+"\n");
                            printOpponentMap(game.getOpponentPlayer(room.getId(),username));
                            System.out.println("\n======================================\n"+username+"\n");
                            printMyMap(game.getBattlefield(room.getId(),username));
                            break;
                        // If player chooses to attack, ask for the coordinates and attack the opponent
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
                        // Otherwise, get the turn of the player
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

    // Function to clear the screen (https://stackoverflow.com/questions/2979383/how-to-clear-the-console-using-java)
    static void clearScreen() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  

    // Function to convert a letteral coordinate to a number
    static int toNumber(char chr) {
        // Use ASCII code to convert the letteral coordinate to a number
        return (chr - 96);
    }

    // Function to print the player's map
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

    // Function to print the opponent's map
    static void printOpponentMap(Player opponent){
        System.out.println("    a  b  c  d  e  f  g  h  i  j");
        for(int i=0;i<opponent.getMap().height;i++){
            if(i<9){
                System.out.print((i+1)+"  ");
            }else{
            System.out.print((i+1)+" ");
            }
            for(int j=0;j<opponent.getMap().width;j++){
                // For each column, if hits value is 1, then print a "O" (hit)
                if(opponent.hits[i][j]==1){
                    System.out.print("[O]");
                // If hits value is 2, then print a "X" (destroyed ship)
                }else if(opponent.hits[i][j]==2){
                    System.out.print("[X]");
                // If hits value is -1, then print a "-" (miss)
                }else if(opponent.hits[i][j]==-1){
                    System.out.print("[-]");
                // Otherwise, print a " " (empty)
                }else{
                    System.out.print("[ ]");
                }
            }
            System.out.println();
        }
    }
}