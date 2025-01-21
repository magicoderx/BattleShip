import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameInterface extends Remote {
    Room createRoom(String username) throws RemoteException;
    Room joinRoom(long roomId, String username) throws RemoteException;
    int getNShip(long roomId, String username) throws RemoteException;
    void insertShip(long roomId, String username, int x, int y) throws RemoteException;
    Player getOpponentPlayer(long roomId, String username) throws RemoteException;
    Battlefield getBattlefield(long roomId, String username) throws RemoteException;
    String attack(long roomId, String username, int x, int y) throws RemoteException;
    String getTurn(long roomId) throws RemoteException;
    boolean gameStarted(long roomId) throws RemoteException;
    Ship getNextShip(long roomId, String username) throws RemoteException;
}
