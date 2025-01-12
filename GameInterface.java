import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameInterface extends Remote {
    Room createRoom(String username) throws RemoteException;
    Room joinRoom(long roomId, String username) throws RemoteException;
    int getNShip(long roomId, String username) throws RemoteException;
    void insertShip(long roomId, String username, int x, int y, Ship ship) throws RemoteException;
    String getOpponentUsername(long roomId, String username) throws RemoteException;
    //void attack(long roomId, String username, int x, int y) throws RemoteException;
    void printMap(long roomId, String username) throws RemoteException;
    String getTurn(long roomId) throws RemoteException;
    boolean gameStarted(long roomId) throws RemoteException;
}
