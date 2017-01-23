package pacman.entries.pacman;

import dataRecording.DataTuple;
import pacman.game.Constants;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;

/**
 * Created by Andréas Appelqvist on 2016.
 */
public class TreeNode implements Serializable {
    public String label;
    private boolean isLeaf = false;
    private Constants.MOVE move = Constants.MOVE.NEUTRAL;
    private HashMap<String, TreeNode> children = new HashMap<>(); //Key atrValue, Value children

    public void setMove(Constants.MOVE move) {
        this.move = move;
    }

    public Constants.MOVE getMove() {
        return this.move;
    }

    public void setAsLeaf() {
        this.isLeaf = true;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void addChildren(TreeNode node, String atrValue) {
        children.put(atrValue, node);
    }

    void print() {
        System.out.println("ROOT");
        print("  ");
    }

    private void print(String offset) {
        Map.Entry<String, TreeNode>[] mapChildren = children.entrySet().toArray(new Map.Entry[0]);

        if (isLeaf) {
            System.out.print(offset);
            System.out.println("  \\─  \033[32m " + move.toString() + "\033[39m");
        }

        for (int i = 0; i < mapChildren.length; i++) {
            System.out.print(offset);
            if (i == mapChildren.length - 1) {
                System.out.println("\\─ \"" + label + "\" = " + mapChildren[i].getKey());
                mapChildren[i].getValue().print(offset + "    ");
            } else {
                System.out.println("|─ \"" + label + "\" = " + mapChildren[i].getKey());
                mapChildren[i].getValue().print(offset + "|   ");
            }
        }

    }

    public TreeNode getLeaf(DataTuple tuple) {
        if (isLeaf) {
            System.out.println("leaf" + this.getMove());
            return this;
        } else {
            TreeNode child;
            if (label.contains("dir")) {
                if (label.contains("Blinky")) {
                    child = children.get(valueOf(tuple.blinkyDir));

                } else if (label.contains("Inky")) {
                    child = children.get(valueOf(tuple.inkyDir)).getLeaf(tuple);
                } else if (label.contains("Pinky")) {
                    child = children.get(valueOf(tuple.pinkyDir)).getLeaf(tuple);
                } else {
                    child = children.get(valueOf(tuple.pinkyDir)).getLeaf(tuple);
                }
            } else if (label.contains("distance")) {
                if (label.contains("Blinkey")) {
                    child = children.get(valueOf(tuple.discretizeDistance(tuple.blinkyDist))).getLeaf(tuple);
                } else if (label.contains("Inky")) {
                    child = children.get(valueOf(tuple.discretizeDistance(tuple.inkyDist))).getLeaf(tuple);
                } else if (label.contains("Pinky")) {
                    child = children.get(valueOf(tuple.discretizeDistance(tuple.pinkyDist))).getLeaf(tuple);
                } else {
                    child = children.get(valueOf(tuple.discretizeDistance(tuple.sueDist))).getLeaf(tuple);
                }
            } else if (label.contains("Edible")) {
                if (label.contains("Blinkey")) {
                    child = children.get(valueOf(tuple.isBlinkyEdible)).getLeaf(tuple);
                } else if (label.contains("Inky")) {
                    child = children.get(valueOf(tuple.isInkyEdible)).getLeaf(tuple);
                } else if (label.contains("Pinky")) {
                    child = children.get(valueOf(tuple.isPinkyEdible)).getLeaf(tuple);
                } else {
                    child = children.get(valueOf(tuple.isSueEdible)).getLeaf(tuple);
                }
            } else {
                if (label.contains("power")) {
                    child = children.get(valueOf(tuple.discretizeNumberOfPowerPills(tuple.numOfPowerPillsLeft))).getLeaf(tuple);
                } else {
                    child = children.get(valueOf(tuple.discretizeNumberOfPills(tuple.numOfPillsLeft))).getLeaf(tuple);
                }
            }
            return child;
        }
    }
}
