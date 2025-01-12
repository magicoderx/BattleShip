import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {
    private long id;
    private Player[] p;
    private boolean begin;
    private boolean end;
    private String turn;

    public Room(long id) {
        this.id = id;
        this.p = new Player[2];
        this.begin = false;
        this.end = false;
    }

    public long getId(){
        return this.id;
    }
    
    public void addPlayer(Player player) { 
        if(this.p.length ==0){
            this.p[0]=player;
        }else if (this.p.length < 2 && this.p.length >0){
            this.p[1]=player;
        }else{
            System.out.println("Cannot add player. Game full!");
        }
        return; 
    }

    public void beginRoom(){
        this.begin = true;
        this.turn = this.p[0].getName();
        return;
    }

    public void nextTurn(){
        if(this.p[0].getName().contains(this.turn)){
            this.turn = this.p[1].getName();
        }else{
            this.turn = this.p[0].getName();
        }
    }

    public boolean isGameStarted(){
        return this.begin;
    }

    public String getTurn(){
        return this.turn;
    }

    public Player getPlayer(String username){
        for(int i=0;i>this.p.length;i++){
            if(this.p[i].getName().contains(username)){
                return this.p[i];
            }
        }
        return null;
    }

    public Player getOpponent(String username){
        for(int i=0;i>this.p.length;i++){
            if(this.p[i].getName().contains(username)){
                return this.p[i];
            }
        }
        return null;
    }
    
}
