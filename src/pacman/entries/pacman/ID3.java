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
            String[] values = getAttributeValue(atr);

            for (String value : values) { //DOWN //UP //LEFT //RIGHT
                subset = getSubset(dataSet, atr, value);
                classCount = countClass(subset);

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

    /**
     * Gives a int array with:
     * int[0] = counts UP
     * int[1] = counts DOWN
     * int[2] = counts LEFT
     * int[3] = counts RIGHT
     *
     * @param dataSet
     * @return int[] size 4 with the counts on every class.
     */
    private static int[] countClass(LinkedList<DataTuple> dataSet) {
        int[] classCount = new int[4];

        for (DataTuple data : dataSet) {
            if (data.DirectionChosen == Constants.MOVE.UP)
                classCount[0]++;
            else if (data.DirectionChosen == Constants.MOVE.DOWN)
                classCount[1]++;
            else if (data.DirectionChosen == Constants.MOVE.LEFT)
                classCount[2]++;
            else if (data.DirectionChosen == Constants.MOVE.RIGHT)
                classCount[3]++;
        }
        return classCount;
    }

    private static double calcAverage(LinkedList<DataTuple> dataSet) {

        int[] classCount = countClass(dataSet);

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

    public static String[] getAttributeValue(String atr) {
        String[] a;
        if (atr.contains("dirTo")) {
            a = new String[4];
            a[0] = "UP";
            a[1] = "DOWN";
            a[2] = "LEFT";
            a[3] = "RIGHT";
        } else if (atr.contains("Edible")) {
            a = new String[2];
            a[0] = "true";
            a[1] = "false";
        } else {
            a = new String[3];
            a[0] = "LOW";
            a[1] = "MEDIUM";
            a[2] = "HIGH";
        }
        return a;
    }

    public static LinkedList<DataTuple> getSubset(LinkedList<DataTuple> data, String byAtribute, String whenValue) {
        LinkedList<DataTuple> subset = new LinkedList<>();
        for (DataTuple tuple : data) {
            if (byAtribute.contains("Blinky")) {
                if (byAtribute.contains("dir")) {
                    if (valueOf(tuple.blinkyDir).equals(whenValue))
                        subset.add(tuple);
                } else if (byAtribute.contains("distance")) {
                    if (valueOf(tuple.discretizeDistance(tuple.blinkyDist)).equals(whenValue))
                        subset.add(tuple);
                } else if (byAtribute.contains("Edible")) {
                    if (("" + tuple.isBlinkyEdible).equals(whenValue))
                        subset.add(tuple);
                }
            } else if (byAtribute.contains("Inky")) {
                if (byAtribute.contains("dir")) {
                    if (valueOf(tuple.inkyDir).equals(whenValue))
                        subset.add(tuple);
                } else if (byAtribute.contains("distance")) {
                    if (valueOf(tuple.discretizeDistance(tuple.inkyDist)).equals(whenValue))
                        subset.add(tuple);
                } else if (byAtribute.contains("Edible")) {
                    if (("" + tuple.isInkyEdible).equals(whenValue))
                        subset.add(tuple);
                }
            } else if (byAtribute.contains("Pinky")) {
                if (byAtribute.contains("dir")) {
                    if (valueOf(tuple.pinkyDir).equals(whenValue))
                        subset.add(tuple);
                } else if (byAtribute.contains("distance")) {
                    if (valueOf(tuple.discretizeDistance(tuple.pinkyDist)).equals(whenValue))
                        subset.add(tuple);
                } else if (byAtribute.contains("Edible")) {
                    if (("" + tuple.isPinkyEdible).equals(whenValue))
                        subset.add(tuple);
                }
            } else if (byAtribute.contains("Sue")) {
                if (byAtribute.contains("dir")) {
                    if (valueOf(tuple.sueDir).equals(whenValue))
                        subset.add(tuple);
                } else if (byAtribute.contains("distance")) {
                    if (valueOf(tuple.discretizeDistance(tuple.sueDist)).equals(whenValue))
                        subset.add(tuple);
                } else if (byAtribute.contains("Edible")) {
                    if (("" + tuple.isSueEdible).equals(whenValue))
                        subset.add(tuple);
                }
            } else {
                if (byAtribute.contains("power")) {
                    if ((valueOf(tuple.discretizeNumberOfPowerPills(tuple.numOfPowerPillsLeft))).equals(whenValue))
                        subset.add(tuple);
                } else {
                    if ((valueOf(tuple.discretizeNumberOfPills(tuple.numOfPillsLeft))).equals(whenValue))
                        subset.add(tuple);
                }
            }
        }
        return subset;
    }

}
