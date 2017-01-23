package pacman.entries.repacman;

import com.sun.xml.internal.bind.v2.model.core.ID;
import com.sun.xml.internal.bind.v2.schemagen.Util;
import dataRecording.DataTuple;
import pacman.controllers.Controller;
import pacman.entries.pacman.TreeNode;
import pacman.game.Constants;
import pacman.game.Game;
import sun.awt.image.ImageWatched;
import sun.reflect.generics.tree.Tree;

import javax.xml.crypto.Data;
import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

import static java.lang.String.valueOf;

/**
 * Created by Andréas Appelqvist on 2017-01-21.
 * Remake on the previous pacman i did around oct 2016
 */
public class MyNewPacMan extends Controller<Constants.MOVE> {

    private String pathTree = "src/pacman/entries/repacman/decisionTree";
    private String LOG = "AI_CONTROLLER - ";
    private Node root;

    public MyNewPacMan() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pathTree));
            this.root = (Node) ois.readObject();
            ois.close();
            System.out.println(LOG + "Load was successfully.");
        } catch (FileNotFoundException e) {
            //File not found Create a decisionTree.
            System.out.println(LOG + "No decision tree to load, creating a new..");
            root = createNewTree();
            root.print();

            /*
            if (saveDecisionTree()) {
                System.out.println(LOG + "The new tree has been saved");
            } else {
                System.err.println(LOG + "The new tree has not been saved");
            }
            */

        } catch (IOException e) {
            System.err.println(LOG + "Exception when read file");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println(LOG + "Exception when read file");
            e.printStackTrace();
        }
        System.out.println(LOG + "Decision Tree:");
    }

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        //Requiered by the framework to make a move.
        return null;
    }

    private Node createNewTree() {
        LinkedList<String> attr = getAttributeList();
        //Use the attr to build the decisiontree
        LinkedList[] dataset = getUserData();
        LinkedList<DataTuple> training = dataset[0];
        LinkedList<DataTuple> test = dataset[1];
        System.out.println("Träning: " + training.size() + ",  Test:" + test.size());
        return generateTree(training, attr);
    }

    private LinkedList<String> getAttributeList() {
        //Get the attributes from the attributelist
        LinkedList<String> attr = new LinkedList<>();
        String line = "";
        Scanner sc = null;
        try {
            sc = new Scanner(new File("src/pacman/entries/repacman/attributelist"));
            line = sc.nextLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (line.charAt(0) != '/') {
            if (line.charAt(0) != '#') {
                attr.add(line);
            }
            line = sc.nextLine();
        }
        return attr;
    }

    private Node generateTree(LinkedList<DataTuple> data, LinkedList<String> attr) {
        Node n = new Node();
        if(data.size() > 0 && Utils.allTupleSameClass(data)){  //2.
            System.out.println("ALL SAME CLASS");
            Constants.MOVE move = data.getFirst().DirectionChosen;
            n.setLeaf(move.toString(), move);
            return n;
        }else if(attr.size() == 0){                               //3
            System.out.println("ATTR IS EMPTY");
            Constants.MOVE move = Utils.majorityClass(data);
            n.setLeaf(move.toString(), move);
            return n;
        }else{                                                  //4.
            System.out.println("DEEPER");
            String bestAtr = ID3.selectAttribute(data, attr);
            System.out.println("best: "+bestAtr);
            n.setAttribute(bestAtr);
            attr.remove(bestAtr);
            Stack<String> values = Utils.findEveryPossibleAttributeValue(bestAtr);
            for (String v : values){
                LinkedList<DataTuple> sublist = Utils.getSubList(data, bestAtr, v);
                if(sublist.isEmpty()){
                    Node child = new Node();
                    Constants.MOVE move = Utils.majorityClass(data);
                    child.setLeaf(move.toString(), move);
                    n.addChild(v,child);
                }else{
                    n.addChild(v, generateTree(sublist, (LinkedList<String>)attr.clone()));
                }
            }
        }
        return n;
    }

    private boolean saveDecisionTree() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathTree));
            oos.writeObject(this.root);
            oos.flush();
            oos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private LinkedList<DataTuple>[] getUserData() {
        LinkedList<DataTuple> training = new LinkedList<>();
        LinkedList<DataTuple> test = new LinkedList<>();
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("myData/trainingData.txt"));
            int counter = 0;
            while ((line = br.readLine()) != null) {
                if (counter < 3) {
                    training.add(new DataTuple(line));
                } else {
                    test.add(new DataTuple(line));
                    counter = 0;
                }
                counter ++;
            }
            return new LinkedList[]{training, test};

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}