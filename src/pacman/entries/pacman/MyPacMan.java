package pacman.entries.pacman;

import dataRecording.DataTuple;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;
import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Game;

import javax.xml.crypto.Data;
import java.io.*;
import java.util.LinkedList;

import static java.lang.String.valueOf;

/**
 * Created by Andréas Appelqvist on 2016-10-17.
 */

public class MyPacMan extends Controller<Constants.MOVE> {
    private String pathToTree = "src/pacman/entries/pacman/decisionTree";
    private TreeNode decisionTreeRoot;
    private String LOG = "MyController: ";

    public MyPacMan() {

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pathToTree));
            this.decisionTreeRoot = (TreeNode) ois.readObject();
            ois.close();
            System.out.println(LOG + "Load was successfully.");
        } catch (FileNotFoundException e) {
            //File not found Create a decisionTree.
            System.out.println(LOG + "No decision tree to load, creating a new..");
             decisionTreeRoot = createNewTree();

            if (saveDecisionTree()) {
                System.out.println(LOG + "The new tree has been saved");
            } else {
                System.err.println(LOG + "The new tree has not been saved");
            }

        } catch (IOException e) {
            System.err.println(LOG + "Exception when read file");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println(LOG + "Exception when read file");
            e.printStackTrace();
        }

        System.out.println("Decision Tree:");
        this.decisionTreeRoot.print();

    }

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        System.out.println("Vill ha ett move?");
        return decisionTreeRoot.getLeaf(new DataTuple(game, Constants.MOVE.NEUTRAL)).getMove();
    }

    private TreeNode createNewTree() {
        LinkedList<String> attributes = toolBox.getAttributes();
        LinkedList<DataTuple> data = getUserData();
        LinkedList<DataTuple> testData = new LinkedList<>();
        LinkedList<DataTuple> trainingData = new LinkedList<>();

        System.out.println(data.size());
        //Splitting up the full data into two sets. about 2 times more training tuple than test tuples.
        for (DataTuple tuple : data) {
            if (Math.random() * 10 < 3) {
                testData.add(tuple);
            } else {
                trainingData.add(tuple);
            }
        }
        TreeNode n = generateTree(trainingData, attributes);
        n.getMove();
        System.out.println(trainingData.size()+ ":"+ testData.size());
        System.out.println(toolBox.getAccuracy(n ,testData));
        return n;
    }

    private TreeNode generateTree(LinkedList<DataTuple> data, LinkedList<String> atr) {
        TreeNode node = new TreeNode();
        if (data.size() > 0 && toolBox.allTupleSameClass(data)) {
            node.setAsLeaf();
            node.setMove(data.getFirst().DirectionChosen);
            node.setLabel("" + data.getFirst().DirectionChosen);
            return node;
        } else if (atr.isEmpty()) {
            Constants.MOVE majMove = toolBox.getMajorityClass(data);
            node.setAsLeaf();
            node.setMove(majMove);
            node.setLabel("");
            return node;
        } else {
            String bestAtr = ID3.selectBestAttribute(data, atr);
            node.setLabel(bestAtr);
            atr.remove(bestAtr);
            String[] atrValue = toolBox.getAttributeValue(bestAtr);
            for (String value : atrValue) {
                //Seperate all tuples in D so that attribute A takes the value
                LinkedList<DataTuple> subset = toolBox.getSubset(data,bestAtr,value);
                if(subset.isEmpty()){
                    TreeNode child = new TreeNode();
                    Constants.MOVE move = toolBox.getMajorityClass(data);
                    child.setLabel(valueOf(move));
                    child.setMove(move);
                    child.setAsLeaf();
                    node.addChildren(child,value);
                }else{
                    node.addChildren(generateTree(subset,atr), value);
                }
            }
        }
        return node;
    }

    private LinkedList<DataTuple> getUserData() {
        LinkedList<DataTuple> data = new LinkedList<>();
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("myData/trainingData.txt"));
            while ((line = br.readLine()) != null) {
                data.add(new DataTuple(line));
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean saveDecisionTree() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(pathToTree));
            oos.writeObject(this.decisionTreeRoot);
            oos.flush();
            oos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
