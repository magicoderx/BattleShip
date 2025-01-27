import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {
    // Initialize private variables for room
    private long id;
    private int len;
    private boolean begin;
    private boolean end;
    private String turn;
    // Initialize public variables for players in room
    public Player[] p;

    // Class constructor, initialize room with id and array of players
    public Room(long id) {
        this.id = id;
        this.p = new Player[2];
        this.len = 0;
        this.begin = false;
        this.end = false;
    }

    // Public function to get room id
    public long getId(){
        return this.id;
    }
    
    // Public function to add player to the room
    public void addPlayer(Player player) { 
        // If the room is empty, add player to the first position otherwise add to the second position
        if(this.p[0]==null && this.p[1]==null){
            this.p[0]=player;
            this.len=1;
        }else if (this.p[0]!=null && this.p[1]==null){
            this.p[1]=player;
            this.len=2;
        }else{
            System.out.println("Cannot add player. Game full!");
        }
        return; 
    }

    // Public function to begin match for the room
    public void beginRoom(){
        this.begin = true;
        this.turn = this.p[0].getName();
        return;
    }

    // Public function to end match for the room
    public void endRoom(){
        this.end = true;
        return;
    }

    // Public function for change turn
    public void nextTurn(){
        if(this.p[0].getName().equals(this.turn)){
            this.turn = this.p[1].getName();
        }else{
            this.turn = this.p[0].getName();
        }
    }

    // Public function to check if the game is starded
    public boolean isGameStarted(){
        return this.begin;
    }

    // Public function to check if the game is finished
    public boolean isGameFinished(){
        return this.end;
    }

    // Public function to check if the game is starded
    public String getTurn(){
        return this.turn;
    }
    
    // Public function to get player info (return entire object)
    public Player getPlayer(String username){
        for(int i=0;i<this.len;i++){
            if(this.p[i].getName().equals(username)){
                return this.p[i];
            }
        }
        return null;
    }

    // Public function to get opponent player info (return entire object of the opposite player username given in input)
    public Player getOpponent(String username){
        for(int i=0;i<this.len;i++){
            if(this.p[i].getName().equals(username)){
                if(i==0){
                    return this.p[i+1];
                }else{
                    return this.p[i-1];
                }
            }
        }
        return null;
    }

    // Public function to get number of playrs in the room
    public int getNPlayers(){
        return this.len;
    }
    
}
