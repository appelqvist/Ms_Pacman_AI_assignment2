package pacman.entries.pacman;

import dataRecording.DataTuple;
import pacman.game.Constants;

import java.util.LinkedList;

import static java.lang.String.valueOf;

/**
 * Created by Andréas Appelqvist on 2016-10-21.
 */
public class toolBox {

    public static LinkedList<String> getAttributes() {
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

    public static Constants.MOVE getMajorityClass(LinkedList<DataTuple> data) {
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
            } else if (tuple.DirectionChosen == Constants.MOVE.RIGHT) {
                right++;
                if (right > biggest) {
                    biggest = right;
                    move = Constants.MOVE.RIGHT;
                }
            }
        }
        return move;
    }

    public static String[] getAttributeValue(String atr) {
        String[] a;
        if (atr.contains("dirTo")) {
            a = new String[5];
            a[0] = "UP";
            a[1] = "DOWN";
            a[2] = "LEFT";
            a[3] = "RIGHT";
            a[4] = "NONE";
        } else if (atr.contains("Edible")) {
            a = new String[2];
            a[0] = "true";
            a[1] = "false";
        } else {
            a = new String[4];
            a[0] = "LOW";
            a[1] = "MEDIUM";
            a[2] = "HIGH";
            a[3] = "NONE";
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
    public static int[] countClass(LinkedList<DataTuple> dataSet) {
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

    public static double getAccuracy(TreeNode root ,LinkedList<DataTuple> testData){
        int sameClass = 0;
        System.out.println(root.label);
        for(DataTuple tuple : testData){
            TreeNode child = root.getLeaf(tuple);
            if(child.getMove().equals(tuple.DirectionChosen))
                sameClass++;
        }
        System.out.println(sameClass + " av " + testData.size());
        return (double)sameClass/testData.size();
    }

    public static boolean allTupleSameClass(LinkedList<DataTuple> data) {
        Constants.MOVE move = data.getFirst().DirectionChosen;
        for (DataTuple tuple : data) {
            if (tuple.DirectionChosen != move) {
                return false;
            }
        }
        return true;
    }
}
