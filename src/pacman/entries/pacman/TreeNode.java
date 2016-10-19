package pacman.entries.pacman;
import pacman.game.Constants;

import java.io.Serializable;

/**
 * Created by Andréas Appelqvist on 2016-10-19.
 */
public class TreeNode implements Serializable {
    String nodeAttribute;
    String label;
    boolean isLeaf = false;
    Constants.MOVE move = Constants.MOVE.NEUTRAL;

    public TreeNode(){}
    public TreeNode(String attribute){
        this.nodeAttribute = attribute;
    }

    public void setMove(Constants.MOVE move){
        this.move = move;
    }

    public void setAsLeaf() {
        this.isLeaf = true;
    }

    public void setLabel(String label){
        this.label = label;
    }
}
