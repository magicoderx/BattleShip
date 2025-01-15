import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class GameServer extends UnicastRemoteObject implements GameInterface {
    private Map<Long, Room> rooms;

    public GameServer() throws RemoteException {
        rooms = new HashMap<>();
    }

    @Override
    public Room createRoom(String username) throws RemoteException {
        Room room = new Room(System.currentTimeMillis());
        Player player = new Player(username, 10);
        room.addPlayer(player);
        rooms.put(room.getId(), room);
        System.out.println("Created room with ID: "+room.getId());
        System.out.println(username+" joined game in "+room.getId());
        return room;
    }

    @Override
    public Room joinRoom(long roomId, String username) throws RemoteException {
        Room room = rooms.get(roomId);
        if (room != null) {
            if (room.getPlayer(username)!=null){
                System.out.println(username+" joined game in "+roomId);
                return room;
            }
            Player player = new Player(username, 10);
            room.addPlayer(player);
            System.out.println(username+" joined game in "+roomId);
        }
        return room;
    }

    @Override
    public Battlefield getBattlefield(long roomId, String username) throws RemoteException {
        Room room = rooms.get(roomId);
        return room.getPlayer(username).getMap();
    }

    @Override
    public void insertShip(long roomId, String username, int x, int y, Ship ship) throws RemoteException {
        Room room = rooms.get(roomId);
        if (room != null) {
            Player player = room.getPlayer(username);
            if (player != null && !room.isGameStarted() && player.getNShips()<2) {
                player.insertShip(x, y, ship);
                if(room.getNPlayers() == 2 && room.p[0].getNShips() == 2 && room.p[1].getNShips() == 2){
                    room.beginRoom();
                }
            }
        }
    }

    @Override
    public int getNShip(long roomId, String username) throws RemoteException{
        return 0;
    }
/*
    @Override
    public void attack(long roomId, String username, int x, int y) throws RemoteException {
        Room room = rooms.get(roomId);
        if (room != null && room.isGameStarted()) {
            String currentTurn = room.getTurn();
            if (currentTurn.equals(username)) {
                Player opponent = room.getOpponent(username);
                if (opponent != null) {
                    opponent.markHit(x, y);
                    room.nextTurn();
                }
            }
        }
    }
*/

    @Override
    public String getOpponentUsername(long roomId, String username) throws RemoteException {
        Room room = rooms.get(roomId);
        return room.getOpponent(username).getName();
    }

    @Override
    public String getTurn(long roomId) throws RemoteException {
        Room room = rooms.get(roomId);
        return (room != null) ? room.getTurn() : null;
    }

    @Override
    public boolean gameStarted(long roomId) throws RemoteException {
        Room room = rooms.get(roomId);
        return (room != null) && room.isGameStarted();
    }

    public static void main(String[] args) {
        try {
            GameServer server = new GameServer();
            java.rmi.Naming.rebind("//localhost/GameServer", server);
            System.out.println("Game Server pronto.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
