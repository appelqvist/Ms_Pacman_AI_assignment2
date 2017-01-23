package pacman.entries.repacman;

import com.sun.prism.PixelFormat;
import dataRecording.DataTuple;
import pacman.game.Constants;
import sun.awt.image.ImageWatched;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Andréas Appelqvist on 2017-01-21.
 */
public class ID3 {

    public static String selectAttribute(LinkedList<DataTuple> dataTuples, LinkedList<String> attr) {

        //Is the average value
        double entropy = info(dataTuples);
        System.out.println("entropy: " + entropy);

        double bestGain = Double.MIN_VALUE;
        String attrWithHighestGain = "";

        for (String attribute : attr) {
            double infoAD = 0;

            for (String attrValue : Utils.findEveryPossibleAttributeValue(attribute)) {
                LinkedList<DataTuple> subList = Utils.getSubList(dataTuples, attribute, attrValue);
                double temp = (double) subList.size() / dataTuples.size();
                double temp2 = info(subList);
                infoAD += temp * temp2;   //Undefiend?
                double gain = entropy - infoAD;
                if (gain > bestGain) {
                    bestGain = gain;
                    attrWithHighestGain = attribute;
                }
            }
        }
        System.out.println("best attribute is:" + attrWithHighestGain);
        return attrWithHighestGain;
    }


    public static double info(LinkedList<DataTuple> D) {
        HashMap<Constants.MOVE, Integer> classes = new HashMap<>();
        for (Constants.MOVE classMove : Constants.MOVE.values()) {
            classes.put(classMove, 0);
        }

        int temp;
        for (DataTuple tuple : D) {
            temp = classes.get(tuple.DirectionChosen);
            temp++;
            classes.put(tuple.DirectionChosen, temp);
        }

        int total = D.size();
        double infoD = 0;

        for (Constants.MOVE classMove : Constants.MOVE.values()) {
            double nbrOfHits = classes.get(classMove);
            if (nbrOfHits > 0) {
                infoD += -(nbrOfHits / total) * Utils.toLog2(nbrOfHits / total);
            }
        }
        return infoD;
    }
}
