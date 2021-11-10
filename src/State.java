import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class State implements Comparable<State> {
    private int left_cannibals,left_missionaries=0;
    private int right_cannibals,right_missionaries=0;
    private char boat;//boat location "L" for Left "R" for  Right
    private int boat_seats,max_seats;
    private State father=null;
    private int score=0;

    public State(int right_cannibals,int left_cannibals, int right_missionaries,int left_missionaries, char boat, int  boat_seats)
    {
        this.right_cannibals=right_cannibals;
        this.left_cannibals=left_cannibals;
        this.right_missionaries=right_missionaries;
        this.left_missionaries=left_missionaries;
        this.boat=boat;
        this.boat_seats=boat_seats;
        this.max_seats=boat_seats;
    }
    boolean canPass()
    {
        if (this.left_missionaries >= 0 && this.right_missionaries >= 0 && this.left_cannibals >= 0 && this.right_cannibals >= 0
                && (this.left_missionaries == 0 || this.left_missionaries >= this.left_cannibals)
                && (this.right_missionaries == 0 || this.right_missionaries >= this.right_cannibals)) {
            return true;
        }
        return false;
    }

    boolean loadBoat()
    {
        if(this.boat_seats-1>=0)return true ; //maybe need too ensure that someone drives the boat
        return false;
    }
    boolean isFinal()
    {
        if(this.left_cannibals==0 && this.right_cannibals==0 && this.boat_seats==boat_seats)
        {
            return true;
        }
        return false;
    }
    void setFather(State father)
    {
        this.father=father;
    }
    State getFather()
    {
        return this.father;
    }

    ArrayList<State> getChildren()
    {
        ArrayList<State> children = new ArrayList<>();
        if(boat=='L') {
            //@TODO Make it close set
            //@TODO Stop single pass
            for(int i=1;i<=max_seats;i++) {
                for(int j=max_seats-i;j>=0;j--) {
                    State child = new State(right_cannibals+i, left_cannibals - i, right_missionaries+j, left_missionaries-j, 'R', boat_seats);
                    if (child.canPass()&& !child.equals(this.father)) {
                        child.evaluate();
                        children.add(child);
                        child.setFather(this);
                    }
                }

            }
        }
        else {
            for(int i=1;i<=max_seats;i++) {
                for(int j=max_seats-i;j>=0;j--) {
                    State child = new State(right_cannibals-i, left_cannibals + i, right_missionaries-j, left_missionaries+j, 'L', boat_seats);
                    if (child.canPass() && !child.equals(this.father)) {
                        child.evaluate();
                        children.add(child);
                        child.setFather(this);
                    }
                }

            }
        }
        return children;
    }
    private void evaluate()
    {
        if(boat_seats>1)
        {
            this.score= (int) (Math.ceil(2*(this.left_cannibals+this.left_missionaries)-3)/(this.boat_seats-1)+1);
        }
    }

    int identifier()
    {
        return (int) (Math.pow(2,(2*0)+0*this.left_cannibals)+Math.pow(2,(2*0)+1*this.right_cannibals)+Math.pow(2,(2*1)+0*this.left_missionaries)+Math.pow(2,(2*1)+1*this.right_missionaries));
    }
    @Override
    public boolean equals(Object obj)
    {
        if(this.left_missionaries != ((State)obj).left_missionaries) return false;
        if(this.right_missionaries != ((State)obj).right_missionaries) return false;
        if(this.left_cannibals != ((State)obj).left_cannibals) return false;
        if(this.right_cannibals != ((State)obj).right_cannibals) return false;
        if(this.boat != ((State)obj).boat) return false;
        return true;
    }

    @Override
    public int hashCode()
    {
        return this.left_cannibals + this.left_missionaries + this.right_cannibals+this.right_missionaries+this.identifier() ;//add others
    }
    @Override
    public String toString() {
        return "State{" +
                "left_cannibals=" + this.left_cannibals +
                ", left_missionaries=" + this.left_missionaries +
                ",boat"+this.boat+
                ",id "+this.identifier()+
                ", father=" + this.father +
                '}';
    }

    @Override
    public int compareTo(@NotNull State s)
    {
        return Double.compare(this.score, s.score); // compare based on the heuristic score.
    }
}
