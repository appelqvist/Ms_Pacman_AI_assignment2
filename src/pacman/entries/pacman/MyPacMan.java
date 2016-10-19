package pacman.entries.pacman;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.bind.v2.model.core.ID;
import dataRecording.DataTuple;
import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Game;
import sun.reflect.generics.tree.Tree;

import java.io.*;
import java.util.LinkedList;

/**
 * Created by Andréas Appelqvist on 2016-10-17.
 */

public class MyPacMan extends Controller<Constants.MOVE> {
    private String pathToTree = "src/pacman/entries/pacman/decisionTree";
    private TreeNode decisionTreeRoot;
    private String LOG = "MyController: ";

    public MyPacMan() {

        /*try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pathToTree));
            this.decisionTreeRoot = (TreeNode) ois.readObject();
            ois.close();
            System.out.println(LOG + "Load was successfully.");
        } catch (FileNotFoundException e) {
            //File not found Create a decisionTree.
            System.out.println(LOG + "No decision tree to load, creating a new..");
            this.decisionTreeRoot = createNewTree();

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
        }*/

        createNewTree();
    }

    @Override
    /**
     * This is called when it is needed to make a move.
     */
    public Constants.MOVE getMove(Game game, long timeDue) {
        return Constants.MOVE.NEUTRAL;
    }

    private TreeNode createNewTree() {
        LinkedList<String> attributes = getAttributes();
        LinkedList<DataTuple> data = getUserData();
        LinkedList<DataTuple> testData = new LinkedList<>();
        LinkedList<DataTuple> trainingData = new LinkedList<>();

        //Splitting up the full data into two sets. about 2 times more training tuple than test tuples.
        for (DataTuple tuple : data) {
            if (Math.random() * 10 < 3) {
                testData.add(tuple);
            } else {
                trainingData.add(tuple);
            }
        }
        System.out.println(testData.size() + " " + trainingData.size());
        TreeNode n = new TreeNode(ID3.selectBestAttribute(trainingData, attributes));

        return null;
    }

    private TreeNode GenerateTree(LinkedList<DataTuple> data, LinkedList<String> atr) {
        TreeNode node = new TreeNode();
        if (data.size() > 0 && allTupleSameClass(data)) {
            node.setAsLeaf();
            node.setMove(data.getFirst().DirectionChosen);
            node.setLabel("" + data.getFirst().DirectionChosen);
            return node;
        } else if (atr.size() == 0) {
            Constants.MOVE majMove = getMajorityClass(data);
            node.setAsLeaf();
            node.setMove(majMove);
            node.setLabel(""+majMove);
            return node;
        } else{
            String bestAtr = ID3.selectBestAttribute(data, atr);
            node.setLabel(bestAtr);
            atr.remove(bestAtr);
            //For every value in
        }

        return null;
    }

    private Constants.MOVE getMajorityClass(LinkedList<DataTuple> data) {
        int up = 0, down = 0, left = 0, right = 0;
        int biggest = -1;
        Constants.MOVE move = Constants.MOVE.NEUTRAL;

        for (DataTuple tuple : data) {
            if (tuple.DirectionChosen == Constants.MOVE.UP) {
                up++;
                if (up > biggest) {
                    biggest = up;
                    move = Constants.MOVE.UP;
                }
            } else if (tuple.DirectionChosen == Constants.MOVE.DOWN) {
                down++;
                if (down > biggest) {
                    biggest = down;
                    move = Constants.MOVE.DOWN;
                }
            } else if (tuple.DirectionChosen == Constants.MOVE.LEFT) {
                left++;
                if (left > biggest) {
                    biggest = left;
                    move = Constants.MOVE.LEFT;
                }
            } else if (tuple.DirectionChosen == Constants.MOVE.RIGHT){
                right++;
                if(right > biggest){
                    biggest = right;
                    move = Constants.MOVE.RIGHT;
                }
            }
        }
        return move;
    }

    private boolean allTupleSameClass(LinkedList<DataTuple> data) {
        Constants.MOVE move = data.getFirst().DirectionChosen;
        for (DataTuple tuple : data) {
            if (tuple.DirectionChosen != move) {
                return false;
            }
        }
        return true;
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
            return null;
        }
    }

    private LinkedList<String> getAttributes() {
        LinkedList<String> attributes = new LinkedList<>();
        attributes.add("pillsLeft");
        attributes.add("powerPillsLeft");

        attributes.add("distanceToBlinky");
        attributes.add("distanceToInky");
        attributes.add("distanceToPinky");
        attributes.add("distanceToSue");

        attributes.add("dirToBlinky");
        attributes.add("dirToInky");
        attributes.add("dirToPinky");
        attributes.add("dirToSue");

        attributes.add("isBlinkyEdible");
        attributes.add("isInkyEdible");
        attributes.add("isPinkyEdible");
        attributes.add("isSueEdible");
        return attributes;
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
