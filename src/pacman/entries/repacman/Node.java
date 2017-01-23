package pacman.entries.repacman;

import dataRecording.DataTuple;
import pacman.game.Constants;

import javax.xml.crypto.Data;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andréas Appelqvist on 2017-01-21.
 */
public class Node implements Serializable {
    private String attribute = "-1";
    private boolean isLeaf = false;
    private Constants.MOVE move = Constants.MOVE.NEUTRAL;
    private HashMap<String, Node> children = new HashMap<>(); //Key = the attribute value, Value = children to that value.

    public void setLeaf(String attribute, Constants.MOVE move) {
        this.isLeaf = true;
        this.attribute = attribute;
        this.move = move;
    }

    public Constants.MOVE getMove(){
        return this.move;
    }

    public Node getLeafNode(DataTuple tuple) {
        if (this.isLeaf) {
            return this;
        } else {
            Field f = null;
            try {
                f = DataTuple.class.getDeclaredField(this.attribute);
                if (attribute.contains("Dist")) { //Needs to be discretized
                    String a = DataTuple.DiscreteTag.DiscretizeDouble(Integer.parseInt(f.get(tuple).toString())).toString();
                    return children.get(a).getLeafNode(tuple);

                } else {
                    return children.get(f.get(tuple).toString()).getLeafNode(tuple);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void addChild(String attribute, Node node) {
        children.put(attribute, node);
    }

    public void print() {
        System.out.println("ROOT");
        print("  ");
    }

    private void print(String offset) {
        Map.Entry<String, Node>[] mapChildren = children.entrySet().toArray(new Map.Entry[0]);

        if (isLeaf) {
            System.out.print(offset);
            System.out.println("  \\─  \033[32m " + move.toString() + "\033[39m");
        }

        for (int i = 0; i < mapChildren.length; i++) {
            System.out.print(offset);
            if (i == mapChildren.length - 1) {
                System.out.println("\\─ \"" + attribute + "\" = " + mapChildren[i].getKey());
                mapChildren[i].getValue().print(offset + "    ");
            } else {
                System.out.println("|─ \"" + attribute + "\" = " + mapChildren[i].getKey());
                mapChildren[i].getValue().print(offset + "|   ");
            }
        }
    }

}
