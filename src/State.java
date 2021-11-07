import java.util.ArrayList;

public class State
{
    private int left_cannibals,left_missionaries=0;
    private int right_cannibals,right_missionaries=0;
    private char boat;//boat location "L" for Left "R" for  Right
    private int boat_seats,max_seats;
    private State father=null;

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

                        children.add(child);
                        child.setFather(this);
                    }
                }

            }
        }
        return children;
    }

    @Override
    public String toString() {
        return "State{" +
                "left_cannibals=" + this.left_cannibals +
                ", left_missionaries=" + this.left_missionaries +
                ",boat"+this.boat+
                ", father=" + this.father +
                '}';
    }
}
