package GhostBuster_HMM_Filter.Game.Controllers;


/*
  This is the sample controller class which handles the whole netWorking issues
  @author  Ahsanul Ameen Sabit
 * */

import GhostBuster_HMM_Filter.Game.StartGame;
import javafx.scene.control.Control;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class GameControl extends Control {

    private BoardImplementation checkerBoard;
    private final Translate pos;
    private int hash;
    private static Double[][] table; // probability table of ghost finder
    private static int gx, gy; // ghost position

    public GameControl() {
        table = new Double[StartGame.BOARD_SIZE][StartGame.BOARD_SIZE];
        pos = new Translate();
        setSkin(new GameControlSkin(this));
        checkerBoard = new BoardImplementation();
        getChildren().addAll(checkerBoard);

        // Places background squares, imageview, texts
        for(int x = 0; x < StartGame.BOARD_SIZE; x++) {
            for(int y = 0; y < StartGame.BOARD_SIZE; y++) {
                checkerBoard.placeBoard(x, y);
                checkerBoard.placeText(x, y);
                checkerBoard.placeImageViews(x, y);
            }
        }
        init();

        //when pressing space bar .. optional
        setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.SPACE) System.out.println("|| Game Reset! ||");
            checkerBoard = new BoardImplementation();
            getChildren().addAll(checkerBoard);

            // Places background squares, imageview, texts
            for(int x = 0; x < StartGame.BOARD_SIZE; x++) {
                for(int y = 0; y < StartGame.BOARD_SIZE; y++) {
                    checkerBoard.placeBoard(x, y);
                    checkerBoard.placeImageViews(x, y);
                    checkerBoard.placeText(x, y);
                }
            }
            init();
        });

        // when mouse is clicked on the board
        setOnMouseClicked(event -> {
            try {
                hash = event.getTarget().hashCode(); //clean totally
                ImageView [][]selectView = checkerBoard.getImageviews();
                Rectangle[][] targetSelect = checkerBoard.getBoard();

                for(int x=0; x < StartGame.BOARD_SIZE; x++) {
                    for(int y=0; y < StartGame.BOARD_SIZE ; y++) {
                        assert selectView[x][y] != null;
                        if(selectView[x][y].hashCode() == hash || targetSelect[x][y].hashCode() == hash) {
                            sense(x, y);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void sense(int x, int y) {
        System.out.println("Sensing : (" + x + ", " + y + ")");
        assert(isSafe(x, y));
        int dist = findHammingDistance(x, y);
        Paint color = getColor(dist);
        checkerBoard.assignColor(x, y, color);
        updateSensedProbabilities(x, y);
    }

    private void updateSensedProbabilities(int hx, int hy) {
        Double[][] currentTable = new Double[StartGame.BOARD_SIZE][StartGame.BOARD_SIZE];
        for(int x = 0; x < StartGame.BOARD_SIZE; ++x) {
            System.arraycopy(table[x], 0, currentTable[x], 0, StartGame.BOARD_SIZE);
        }

        for(int x = 0; x < StartGame.BOARD_SIZE; ++x) {
            for(int y = 0; y < StartGame.BOARD_SIZE; ++y) {
                updateSensedProbability(hx, hy, x, y, currentTable);
            }
        }

        normalize(); //fixme is a must
        checkerBoard.setCurrentTable(table);
    }

    private void normalize() {
        double sum = 0;
        for(int x = 0; x < StartGame.BOARD_SIZE; ++x) {
            for(int y = 0; y < StartGame.BOARD_SIZE; ++y) {
                sum += table[x][y];
            }
        }
        //System.out.println("Sum : " + sum);
        //normalize
        for(int x = 0; x < StartGame.BOARD_SIZE; ++x) {
            for(int y = 0; y < StartGame.BOARD_SIZE; ++y) {
                table[x][y] /= sum;
            }
        }
    }

    private void updateSensedProbability(int hx, int hy, int x, int y, Double[][] currentTable) {
        Paint color_before = getColor(findHammingDistance(hx, hy));
        // swap (x, y) with (gx, gy)
        x = x ^ gx ^ (gx = x);
        y = y ^ gy ^ (gy = y);
        // if ghost in cell(x, y)
        Paint color_next = getColor(findHammingDistance(hx, hy));
        double probRij = (color_before == color_next) ? 1 : 0;
        // restore (gx, gy) with (x, y)
        x = x ^ gx ^ (gx = x);
        y = y ^ gy ^ (gy = y);
        table[x][y] = currentTable[x][y] * probRij;
    }

    private Paint getColor(int distance) {
        int n = StartGame.BOARD_SIZE;
        int diff = (2 * (n - 1)) / 4;
        if(0 <= distance && distance <= diff - 1) {
            // RED
            return Color.RED;
        } else if(diff <= distance && distance <= 2 * diff - 1) {
            // ORANGE
            return Color.ORANGE;
        } else if(2 * diff <= distance && distance <= 2 * (n - 1)) {
            // GREEN
            return Color.GREEN;
        } else {
            System.out.println(distance);
            System.out.println("Invalid color");
            assert (false);
        }
        return null;
    }

    public int findHammingDistance(int x, int y) {
        return Math.abs(x - gx) + Math.abs(y - gy);
    }

    void init() {
        initTable();
        checkerBoard.leaveTheGhost(gx, gy);
        gx = ThreadLocalRandom.current().nextInt(0, StartGame.BOARD_SIZE);
        gy = ThreadLocalRandom.current().nextInt(0, StartGame.BOARD_SIZE);
    }

    void initTable() {
        double prob = 1.0 / Math.pow(StartGame.BOARD_SIZE, 2);
        for(int x = 0; x < StartGame.BOARD_SIZE; ++x) {
            for(int y = 0; y < StartGame.BOARD_SIZE; ++y) {
                table[x][y] = prob;
            }
        }
        checkerBoard.setCurrentTable(table);
    }

    public void updateTableTimeStamp() {
        Double[][] oldTable = new Double[StartGame.BOARD_SIZE][StartGame.BOARD_SIZE];
        for(int x = 0; x < StartGame.BOARD_SIZE; ++x) {
            System.arraycopy(table[x], 0, oldTable[x], 0, StartGame.BOARD_SIZE);
        }

        for(int x = 0; x < StartGame.BOARD_SIZE; ++x) {
            for(int y = 0; y < StartGame.BOARD_SIZE; ++y) {
                updateCellTimeStamp(x, y, oldTable);
            }
        }
        checkerBoard.clearHighLights();
        normalize();
        checkerBoard.setCurrentTable(table);
        // and finally move the ghost
        moveGhost();
    }

    private void updateCellTimeStamp(int x, int y, Double[][] oldTable) {
        int xx, yy;
        double extraProbTBLR = 0, extraProbDia = 0;
        int tblrCnt = 0, diaCnt = 0;

        for(int[] add : Logics.dir8) {
            xx = x + add[0];
            yy = y + add[1];
            if(!isSafe(xx, yy)) {
               if(hammingDist(x, y, xx, yy) == 2) {
                   extraProbDia += getMoveProbability(x, y, xx, yy);
                   diaCnt++;
               }
               else {
                   extraProbTBLR += getMoveProbability(x, y, xx, yy);
                   tblrCnt++;
               }
            }
        }
        double newProb = oldTable[x][y] * (getMoveProbability(x, y, x, y) + extraProbDia / (5.0 - diaCnt));
        for(int[] add : Logics.dir8) {
            xx = x + add[0];
            yy = y + add[1];
            if(isSafe(xx, yy)) {
                if(hammingDist(x, y, xx, yy) == 2) {
                    newProb += oldTable[xx][yy] * (getMoveProbability(x, y, xx, yy) + extraProbDia / (5.0 - diaCnt));
                }
                else {
                    newProb += oldTable[xx][yy] * (getMoveProbability(x, y, xx, yy) + extraProbTBLR / (4.0 - tblrCnt));
                }
            }
        }
        //System.out.println(newProb);
        table[x][y] = newProb;
    }

    private void moveGhost() {
        int xx = 0, yy = 0;
        double extraProbTBLR = 0, extraProbDia = 0;
        int tblrCnt = 0, diaCnt = 0;

        for(int[] add : Logics.dir8) {
            xx = gx + add[0];
            yy = gy + add[1];
            if(!isSafe(xx, yy)) {
                if(findHammingDistance(xx, yy) == 2) {
                    extraProbDia += getMoveProbability(gx, gy, xx, yy);
                    diaCnt++;
                }
                else {
                    extraProbTBLR += getMoveProbability(gx, gy, xx, yy);
                    tblrCnt++;
                }
            }
        }
        HashMap<String, Double> directionProbabilities = new HashMap<>();
        directionProbabilities.put(0 + "|" + 0, getMoveProbability(gx, gy, gx, gy) + extraProbDia / (5.0 - diaCnt));
        for(int[] add : Logics.dir8) {
            xx = gx + add[0];
            yy = gy + add[1];
            if(isSafe(xx, yy)) {
                if(findHammingDistance(xx, yy) == 2) {
                    directionProbabilities.put(add[0] + "|" + add[1], getMoveProbability(gx, gy, xx, yy) + extraProbDia / (5.0 - diaCnt));
                }
                else {
                    directionProbabilities.put(add[0] + "|" + add[1], getMoveProbability(gx, gy, xx, yy) + extraProbTBLR / (4.0 - tblrCnt));
                }
            }
        }

        // -----------------------------
        double p = Math.random();
        double cumulativeProbability = 0.0;
        for (Map.Entry<String, Double> entry : directionProbabilities.entrySet()) {
            String dir = entry.getKey();
            Double prob = entry.getValue();
            cumulativeProbability += prob;
            if(p <= cumulativeProbability) {
                System.out.println(dir);
                xx = Integer.parseInt(dir.split("\\|")[0]);
                yy = Integer.parseInt(dir.split("\\|")[1]);
                break;
            }
        }
        // move ghost to a new position
        gx += xx;
        gy += yy;
    }

    public void bustTheGhost() {
        if(checkerBoard.isSelected(gx, gy)) {
            System.out.println("Ghost Busted! \uD83D\uDE03");
        } else {
            System.out.println("Mistaken... \uD83D\uDE22");
        }
        checkerBoard.callTheGhost(gx, gy);
        System.out.println("Ghost(" + gx + ", " + gy + ")");
    }

    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        checkerBoard.resize(width, height);
    }

    @Override
    public void relocate(double x, double y) {
        super.relocate(x, y);
        pos.setX(x);
        pos.setY(x);
    }

    private static boolean isSafe(int x, int y) {
        return x >= 0 && x < StartGame.BOARD_SIZE && y >= 0 && y < StartGame.BOARD_SIZE;
    }

    private int hammingDist(int x, int y, int xx, int yy) {
        return Math.abs(x - xx) + Math.abs(y - yy);
    }

    private double getMoveProbability(int x, int y, int xx, int yy) {
        int diff = hammingDist(x, y, xx, yy);
        if(diff == 1) return 0.24;
        else if(diff == 2 || diff == 0) return 0.008;
        else {
            System.out.println("Error_1");
            return 0;
        }
    }
}