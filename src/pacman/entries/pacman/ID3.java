package pacman.entries.pacman;

import dataRecording.DataTuple;
import pacman.game.Constants;
import java.util.LinkedList;
import static java.lang.String.valueOf;

/**
 * Created by Andréas Appelqvist on 2016-10-19.
 */
public class ID3 {

    public static String selectBestAttribute(LinkedList<DataTuple> dataSet, LinkedList<String> attributes) {
        double avr = calcAverage(dataSet);

        double bestScore = -10000;
        String bestAttribute = "init";
        int[] classCount;

        LinkedList<DataTuple> subset;

        for (String atr : attributes) { //ex dirBlinky

            double atrScore = 0;
            String[] values = toolBox.getAttributeValue(atr);

            for (String value : values) { //DOWN //UP //LEFT //RIGHT
                subset = toolBox.getSubset(dataSet, atr, value);
                classCount = toolBox.countClass(subset);

                double temp = 0;
                for (int i = 0; i < classCount.length; i++) {
                    if(classCount[i] > 0){
                    temp -= (((double) classCount[i] / subset.size())
                            * binaryLogarithm((double) classCount[i] / subset.size()));
                    }
                }

                atrScore += ((double) subset.size() / dataSet.size()) * temp;
            }

            if ((avr - atrScore) > bestScore) {
                bestScore = avr - atrScore;
                bestAttribute = atr;
            }
        }
        return bestAttribute;
    }

    private static double calcAverage(LinkedList<DataTuple> dataSet) {

        int[] classCount = toolBox.countClass(dataSet);

        int total = dataSet.size(), up = classCount[0], down = classCount[1],
                left = classCount[2], right = classCount[3];

        double average = 0;

        if (up > 0) {
            average -= ((double) up / total) * (binaryLogarithm((double) up / total));
        }
        if (down > 0) {
            average -= ((double) down / total) * (binaryLogarithm((double) down / total));
        }
        if (left > 0) {
            average -= ((double) left / total) * (binaryLogarithm((double) left / total));
        }
        if (right > 0) {
            average -= ((double) right / total) * (binaryLogarithm((double) right / total));
        }

        return average;
    }

    private static double binaryLogarithm(double decimal) {
        return (Math.log10(decimal) / Math.log10(2));
    }


}
