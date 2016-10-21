package pacman.entries.pacman;

import dataRecording.DataTuple;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;
import pacman.game.Constants;
import pacman.game.Game;
import sun.reflect.generics.tree.Tree;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;

/**
 * Created by Andréas Appelqvist on 2016-10-19.
 */
public class TreeNode implements Serializable {
    private String label;
    private boolean isLeaf = false;
    private Constants.MOVE move = Constants.MOVE.NEUTRAL;
    private HashMap<String, TreeNode> children = new HashMap<>(); //Key atrValue, Value children

    public void setMove(Constants.MOVE move) {
        this.move = move;
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

    public Constants.MOVE makeMove(DataTuple tuple) {
        if (isLeaf) {
            return move;
        } else {
            TreeNode child;
            if (label.contains("Blinky")) {
                if (label.contains("dir")) {
                    child = children.get(valueOf(tuple.blinkyDir));
                    return child.makeMove(tuple);
                } else if (label.contains("distance")) {
                    child = children.get(valueOf(tuple.discretizeDistance(tuple.blinkyDist)));
                    return child.makeMove(tuple);
                } else if (label.contains("Edible")) {
                    child = children.get(valueOf(tuple.isBlinkyEdible));
                    return child.makeMove(tuple);
                }
            } else if (label.contains("Inky")) {
                if (label.contains("dir")) {
                    child = children.get(valueOf(tuple.inkyDir));
                    return child.makeMove(tuple);
                } else if (label.contains("distance")) {
                    child = children.get(valueOf(tuple.discretizeDistance(tuple.inkyDist)));
                    return child.makeMove(tuple);
                } else if (label.contains("Edible")) {
                    child = children.get(valueOf(tuple.isInkyEdible));
                    return child.makeMove(tuple);
                }
            } else if (label.contains("Pinky")) {
                if (label.contains("dir")) {
                    child = children.get(valueOf(tuple.pinkyDir));
                    return child.makeMove(tuple);
                } else if (label.contains("distance")) {
                    child = children.get(valueOf(tuple.discretizeDistance(tuple.pinkyDist)));
                    return child.makeMove(tuple);
                } else if (label.contains("Edible")) {
                    child = children.get(valueOf(tuple.isPinkyEdible));
                    return child.makeMove(tuple);
                }
            } else if (label.contains("Sue")) {
                if (label.contains("dir")) {
                    child = children.get(valueOf(tuple.sueDir));
                    return child.makeMove(tuple);
                } else if (label.contains("distance")) {
                    child = children.get(valueOf(tuple.discretizeDistance(tuple.sueDist)));
                    return child.makeMove(tuple);
                } else if (label.contains("Edible")) {
                    child = children.get(valueOf(tuple.isSueEdible));
                    return child.makeMove(tuple);
                }
            } else {
                if (label.contains("power")) {
                    child = children.get(valueOf(tuple.discretizeNumberOfPowerPills(tuple.numOfPowerPillsLeft)));
                    return child.makeMove(tuple);
                } else {
                    child = children.get(valueOf(tuple.discretizeNumberOfPills(tuple.numOfPillsLeft)));
                    return child.makeMove(tuple);
                }
            }
        }
        return Constants.MOVE.NEUTRAL;
    }
}
