package pacman.entries.repacman;

import pacman.game.Constants;

import java.io.Serializable;
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


    public void setLeaf(String attribute, Constants.MOVE move){
        this.isLeaf = true;
        this.attribute = attribute;
        this.move = move;
    }

    public void setAttribute(String attribute){
        this.attribute = attribute;
    }

    public void addChild(String attribute, Node node){
        children.put(attribute,node);
    }

    public void print() {
        System.out.println("ROOT");
        print("  ");
    }
/*
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
    */

    private void print(String indent) {

        if (isLeaf) {
            System.out.print(indent);
            System.out.println("  └─ Return " + move.toString());
        }
        Map.Entry<String, Node>[] nodes = children.entrySet().toArray(new Map.Entry[0]);
        for (int i = 0; i < nodes.length; i++) {
            System.out.print(indent);
            if (i == nodes.length - 1) {
                System.out.println("└─ \"" + attribute + "\" = " + nodes[i].getKey() + ":");
                nodes[i].getValue().print(indent + "    ");
            } else {
                System.out.println("├─ \"" + attribute + "\" = " + nodes[i].getKey() + ":");
                nodes[i].getValue().print(indent + "│   ");
            }
        }
    }
}
