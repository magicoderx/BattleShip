import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class GameServer extends UnicastRemoteObject implements GameInterface {
    // Initialize private variable for rooms that maps room id to room
    private Map<Long, Room> rooms;

    // Class constructor, initialize rooms with an hashmap because it's easier to find a room by id
    public GameServer() throws RemoteException {
        rooms = new HashMap<>();
    }

    // RMI function to create a room
    @Override
    public Room createRoom(String username) throws RemoteException {
        // Create a new room with the current time as id
        Room room = new Room(System.currentTimeMillis());
        Player player = new Player(username, 10);
        room.addPlayer(player);
        rooms.put(room.getId(), room);
        System.out.println("Created room with ID: "+room.getId());
        System.out.println(username+" joined game in "+room.getId());
        return room;
    }

    // RMI function to join a room
    @Override
    public Room joinRoom(long roomId, String username) throws RemoteException {
        Room room = rooms.get(roomId);
        if (room != null) {
            // If there are two players in the room, and one of them is not the current player, return an error because the room is full
            if(room.getPlayer(username)==null && room.getNPlayers()==2){
                throw new RemoteException("Room not found or full try again");
            } else if (room.getPlayer(username)!=null){
                // If the player is already in the room, return the room
                System.out.println(username+" joined game in "+roomId);
                return room;
            }
            Player player = new Player(username, 10);
            room.addPlayer(player);
            System.out.println(username+" joined game in "+roomId);
        }else{
            
        }
        return room;
    }

    // RMI function to get the battlefield of a player
    @Override
    public Battlefield getBattlefield(long roomId, String username) throws RemoteException {
        Room room = rooms.get(roomId);
        return room.getPlayer(username).getMap();
    }

    //Public function to get next player ship to insert
    @Override
    public Ship getNextShip(long roomId, String username) throws RemoteException {
        Room room = rooms.get(roomId);
        return room.getPlayer(username).getShip();
    }

    // RMI function to insert a ship into the battlefield of a player
    @Override
    public String insertShip(long roomId, String username, int x, int y) throws RemoteException {
        Room room = rooms.get(roomId);
        if (room != null) {
            Player player = room.getPlayer(username);
            // If the player placed less than 5 ships, insert the ship
            if (player != null && !room.isGameStarted() && player.getNShips()<5) {
                if(player.insertShip(x, y)){
                    // If both players placed 5 ships, begin the match
                    if(room.getNPlayers() == 2 && room.p[0].getNShips() == 5 && room.p[1].getNShips() == 5){
                        room.beginRoom();
                        return "Ship inserted successfully. Game started!";
                    }
                    return "Ship inserted successfully";
                }else{
                    return "Error. Another ship already placed here or exceeded the coordinates";
                }
            }
        }
        return "Error. Room not found.";
    }

    // RMI function to get the number of ships of a player
    @Override
    public int getNShip(long roomId, String username) throws RemoteException{
        Room room = rooms.get(roomId);
        return room.getPlayer(username).getNShips();
    }

    // RMI function to attack a ship in the battlefield of the opponent. It is String because it returns a message
    @Override
    public String attack(long roomId, String username, int x, int y) throws RemoteException {
        Room room = rooms.get(roomId);
        if (room != null && room.isGameStarted()) {
            if (room.getTurn().equals(username)) {
                Player opponent = room.getOpponent(username);
                if (opponent != null) {
                    // Mark the hit in the opponent battlefield with the coordinates decreased by 1 because the battlefield is 0-indexed
                    if(opponent.markHit(x-1, y-1)){
                        // If opponent has no ships, the player wins
                        if(opponent.getNShips()==0){
                            room.endRoom();
                            return "Game over! "+username+" wins!";
                        }else{
                            // Otherwise, change the turn only if the player doesn't hit a ship
                            if(opponent.hits[x-1][y-1]==1){
                                return "Hit!";
                            }else if(opponent.hits[x-1][y-1]==2){
                                return "Ship Destroyed!";
                            }else{
                                room.nextTurn();
                                return "Miss!";
                            }
                        }
                    // If markHit returns false, the player already hit this position
                    }else{
                        return "You already hit this position!";
                    }
                }
            }else{
                return "Not your turn";
            }
        }
        return "Error";
    }

    // RMI function to get the opponent player
    @Override
    public Player getOpponentPlayer(long roomId, String username) throws RemoteException {
        Room room = rooms.get(roomId);
        return room.getOpponent(username);
    }

    // RMI function to get the player turn username
    @Override
    public String getTurn(long roomId) throws RemoteException {
        Room room = rooms.get(roomId);
        return (room != null) ? room.getTurn() : null;
    }

    // RMI function to check if the game is started
    @Override
    public boolean gameStarted(long roomId) throws RemoteException {
        Room room = rooms.get(roomId);
        return (room != null) && room.isGameStarted();
    }

    // RMI function to check if the game is finished
    @Override
    public boolean gameFinished(long roomId) throws RemoteException {
        Room room = rooms.get(roomId);
        return (room != null) && room.isGameFinished();
    }

    // RMI function to get the winner of the game
    @Override
    public Player getWinner(long roomId) throws RemoteException {
        Room room = rooms.get(roomId);
        return room.getWinnerRoom();
    }

    public static void main(String[] args) {
        try {
            // Create a new GameServer and bind it to the RMI registry
            GameServer server = new GameServer();
            java.rmi.Naming.rebind("//localhost/GameServer", server);
            System.out.println("Game Server pronto.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
