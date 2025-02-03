import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
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

            int choice;
            do{
                // Ask player to create or join a room
                System.out.println("[1] Create a room\n[2] Join a room");
                choice = scanner.nextInt();
                switch(choice){
                    // If player chooses to create a room, create a room and print the room ID
                    case 1:
                        try {
                            room = game.createRoom(username);
                            clearScreen();
                            System.out.println("Room created with ID: " + room.getId());
                        }catch(RemoteException e){
                            System.out.println("Error creating room");
                        }
                        break;
                    // Otherwise, ask player for the room ID and join the room
                    case 2:
                        try {
                            System.out.print("Enter room ID: ");
                            long roomId = scanner.nextLong();
                            room = game.joinRoom(roomId, username);
                            clearScreen();
                            System.out.println("Joined room: " + room.getId());
                        }catch(RemoteException e){
                            System.out.println("Error joining room");
                        }
                        break;
                    // Manage invalid choices
                    default:
                        clearScreen();
                        System.out.println("Invalid choice");
                        break;
                }
            }while(choice!=1 && choice!=2);
            
            // Begin loop until the room is null (game is finished)
            do{
                try{
                    // If the game is not started yet, ask player to insert a ship, show the map or check the game status
                    if(room!=null && !game.gameStarted(room.getId())){
                        System.out.println("Game not started yet.");
                        System.out.println("[1] Insert ship\n[2] Show map\n[3] Check game status");
                        choice = scanner.nextInt();
                        switch(choice){
                            // If player chooses to insert a ship, ask for the coordinates and insert the ship
                            case 1:
                                try{
                                    if(game.getNShip(room.getId(),username)<5){
                                        clearScreen();
                                        printMyMap(game.getBattlefield(room.getId(),username));
                                        System.out.println("Insert coordinates for "+game.getNextShip(room.getId(),username).width+"x"+game.getNextShip(room.getId(),username).height+" ship");
                                        char yCoordChar;
                                        int yCoord;
                                        int xCoord;
                                        try{
                                            // Get letteral coordinate and convert it to number
                                            yCoordChar = scanner.next().charAt(0);
                                            yCoord = toNumber(yCoordChar);
                                            xCoord = scanner.nextInt();
                                        }catch(Exception e){
                                            System.out.println("Invalid coordinates");
                                            System.out.println(e.getMessage());
                                            break;
                                        }
                                        System.out.println("xCoord: "+xCoord+" yCoord: "+yCoord);
                                        clearScreen();
                                        System.out.println(game.insertShip(room.getId(), username, xCoord, yCoord));
                                    }else{
                                        clearScreen();
                                        System.out.println("No more ship available");
                                    }
                                }catch(RemoteException e){
                                    System.out.println("Error inserting ship");
                                }
                                break;
                            // If player chooses to show the map, print the map
                            case 2:
                                clearScreen();
                                System.out.println();
                                try{
                                    printMyMap(game.getBattlefield(room.getId(),username));
                                    System.out.println();
                                }catch(RemoteException e){
                                    System.out.println("Error showing map");
                                }
                                break;
                            // Otherwise, do nothing because recylcing the loop will show the game status
                            case 3:
                                clearScreen();
                                break;
                            // Manage invalid choices
                            default:
                                clearScreen();
                                System.out.println("Invalid choice");
                                break;
                        }
                    // If the game is started, ask player to show the maps, attack or get the turn
                    }else if(room!=null && game.gameStarted(room.getId()) && !game.gameFinished(room.getId())){
                        System.out.println("Game started.");
                        System.out.println("[1] Show maps\n[2] Attack\n[3] Get turn");
                        choice = scanner.nextInt();
                        switch(choice){
                            // If player chooses to show the maps, print the maps in vertical order (the opponent's map is on top)
                            case 1:
                                clearScreen();
                                try{
                                    System.out.println(game.getOpponentPlayer(room.getId(),username).getName()+"\n");
                                    printOpponentMap(game.getOpponentPlayer(room.getId(),username));
                                    System.out.println("\n======================================\n"+username+"\n");
                                    printMyMap(game.getBattlefield(room.getId(),username));
                                }catch(RemoteException e){
                                    System.out.println("Error showing maps");
                                }
                                break;
                            // If player chooses to attack, ask for the coordinates and attack the opponent
                            case 2:
                                clearScreen();
                                try{
                                    if(game.getTurn(room.getId()).contains(username)){
                                        printOpponentMap(game.getOpponentPlayer(room.getId(),username));
                                        System.out.println("Insert coordinates for opponent ship");
                                        try{
                                            // Get letteral coordinate and convert it to number
                                            char yCoordChar = scanner.next().charAt(0);
                                            int yCoord = toNumber(yCoordChar);
                                            int xCoord = scanner.nextInt();
                                            // Print again the opponent's map with the hit result
                                            clearScreen();
                                            System.out.println(game.attack(room.getId(), username, xCoord, yCoord)+"\n");
                                            printOpponentMap(game.getOpponentPlayer(room.getId(),username));
                                        }catch(Exception e){
                                            System.out.println("Invalid coordinates");
                                            break;
                                        }
                                        break;
                                    }else{
                                        System.out.println("Not your turn.");
                                        break;
                                    }
                                }catch(RemoteException e){
                                    System.out.println("Error while attacking");
                                    break;
                                }
                            // Otherwise, get the turn of the player
                            case 3:
                                clearScreen();
                                try{
                                    System.out.println(game.getTurn(room.getId())+"\'s turn.\n");
                                }catch(RemoteException e){
                                    System.out.println("Error getting turn");
                                }
                                break;
                            // Manage invalid choices
                            default:
                                clearScreen();
                                System.out.println("Invalid choice");
                                break;
                        }
                    }else{
                        // If game is finished but I'm still in the loop, print the winner and set room to null
                        System.out.println("Game finished, "+game.getOpponentPlayer(room.getId(),username).getName()+" wins!");
                        room=null;
                    }
                }catch(RemoteException e){
                    System.out.println("Error commnicating with server");
                    room=null;
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