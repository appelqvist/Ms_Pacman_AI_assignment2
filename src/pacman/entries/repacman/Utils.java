package pacman.entries.repacman;

import dataRecording.DataTuple;
import pacman.game.Constants;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by Andréas Appelqvist on 2017-01-22.
 */
public class Utils {

    /**
     * Calculate the log2(x)
     * @param x
     * @return
     */
    public static double toLog2(double x) {
        return Math.log(x) / Math.log(2);
    }

    /**
     * Check if all the tuples in the dataset has the same class.
     * @param data
     * @return true if it is, false if not.
     */
    public static boolean allTupleSameClass(LinkedList<DataTuple> data) {
        Constants.MOVE move = data.getFirst().DirectionChosen;
        for (DataTuple tuple : data) {
            if (tuple.DirectionChosen != move) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a sublist of data where the attribute has value.
     *
     * @param data
     * @param attribute
     * @param value
     * @return
     */
    public static LinkedList<DataTuple> getSubList(LinkedList<DataTuple> data, String attribute, String value) {
        LinkedList<DataTuple> sublist = new LinkedList<>();
        try {
            for (DataTuple tuple : data) {
                Field f = DataTuple.class.getDeclaredField(attribute);
                if (attribute.contains("Dist") && DataTuple.DiscreteTag.DiscretizeDouble((Integer) f.get(tuple)).toString().equals(value)) {
                    sublist.add(tuple);
                } else if (!attribute.contains("Dist") && f.get(tuple).toString().equals(value)) {
                    sublist.add(tuple);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sublist;
    }

    /**
     * Gives an array with every possible value for a given attribute.
     *
     * @return Stack<String>
     */
    public static Stack<String> findEveryPossibleAttributeValue(String attribute) {
        Stack<String> values = new Stack<>();
        if (attribute.contains("Edible")) {
            values.push("true");
            values.push("false");
        } else if (attribute.contains("Dir")) {
            for (Constants.MOVE move : Constants.MOVE.values()) {
                values.push(move.toString());
            }
        } else if (attribute.contains("Dist")) {
            values.push("HIGH");
            values.push("LOW");
            values.push("NONE");
        } else {
            System.out.println("pang här då va?");
            //System.exit(0);
            //Throw a exception here.
        }
        return values;
    }

    /**
     * Find the move that is the most occurred.
     * @param data
     * @return
     */
    public static Constants.MOVE majorityClass(LinkedList<DataTuple> data) {
        HashMap<Constants.MOVE, Integer> tuples = new HashMap<>(); //K MOVE, NBR OF OCCUR.
        for(Constants.MOVE move : Constants.MOVE.values()){
            tuples.put(move,0); //init
        }

        for(DataTuple t : data){
            tuples.put(t.DirectionChosen, tuples.get(t.DirectionChosen) + 1 );
        }

        Constants.MOVE majM = Constants.MOVE.NEUTRAL;
        for(Constants.MOVE m : tuples.keySet()){
            if(tuples.get(m) > tuples.get(majM)){
                majM = m;
            }
        }

        return majM;
    }
}
