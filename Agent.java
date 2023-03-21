import java.util.ArrayList;
import java.util.List;

public class Agent {
    Position position;
    int reward;
    double[][][] qvalues;
    public Agent(Position p, Environment e) {
        this.position = p;
        this.reward = 0;
        this.qvalues = new double[e.height][e.width][4];
    }

    public Agent(int y, int x, Environment e) {
        this(new Position(y,x), e);
    }

    public Direction epsilonGreedyAction(Position p, double e) {
        //qvalues = 0-UP, 1-DOWN, 2-LEFT, 3-RIGHT
        double rand = Math.random();
        int d = 0;
        if (rand < 1-e) {
            return greedyAction(p);
        } else {
            d = (int) (Math.random()*4);
        }
        switch (d) {
            case 0: return Direction.UP;
            case 1: return Direction.DOWN;
            case 2: return Direction.LEFT;
            case 3: return Direction.RIGHT;
            default: return null;//never reach this
        }
    }

    public Direction greedyAction(Position p) {
        int d = 0;
        double best = -10000;
        List<Integer> bestIndexes = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            if (qvalues[p.y][p.x][i]>best) {
                bestIndexes.clear();
                best = qvalues[p.y][p.x][i];
            }
            if (qvalues[p.y][p.x][i] >= best) {
                bestIndexes.add(i);
            }
        }
        if (bestIndexes.size() > 1) {
            d = bestIndexes.get((int) Math.random()*bestIndexes.size());
        } else d = bestIndexes.get(0);
        switch (d) {
            case 0: return Direction.UP;
            case 1: return Direction.DOWN;
            case 2: return Direction.LEFT;
            case 3: return Direction.RIGHT;
            default: return null;//never reach this
        }
    }

    public int move(Environment e, Direction d, double sP) {
        double rand = Math.random();
        //INCLUDE IF CLIMBING FROM CLIFF
        if (e.getCell(position).f == Feature.CLIFF) {
            position = e.getCell(Feature.START);
            int oneStepReward = -10;
            reward += oneStepReward;
            return oneStepReward;
        }
        //System.out.printf("Attempt Move %s\n",d.name());
        if(rand < sP) {
            Position attempt = position.move(d);
            //System.out.println("Probability Success");
            if (e.checkMove(attempt)) {
                position = attempt;
            }
        } else {
            //System.out.print("Probability Fail ");
            if((((int)(rand*100))%2) > 1) {
                //System.out.printf("Moving %s %s\n",d, getLeft(d));
                Position attempt = position.move(d);
                attempt = attempt.move(getLeft(d));//move left
                if (e.checkMove(attempt)) {
                    position = attempt;
                    
                }
            } else {
                //System.out.printf("Moving %s %s\n",d, getRight(d));
                Position attempt = position.move(d);
                attempt = attempt.move(getRight(d));//move right
                if (e.checkMove(attempt)) {
                    position = attempt;
                }
            }
        }
        int oneStepReward = e.rewardPosition(position);
        reward += oneStepReward;
        return oneStepReward;
    }

    private Direction getLeft(Direction d) {
        switch (d) {
            case UP: return Direction.LEFT;
            case DOWN: return Direction.RIGHT;
            case LEFT: return Direction.DOWN;
            case RIGHT: return Direction.UP;
            default: return d;
        }
    }

    private Direction getRight(Direction d) {
        switch (d) {
            case UP: return Direction.RIGHT;
            case DOWN: return Direction.LEFT;
            case LEFT: return Direction.UP;
            case RIGHT: return Direction.DOWN;
            default: return d;
        }
    }

    public void printSymbol() {
        System.out.print("A");
    }

    public void print() {
        position.print();
        //System.out.printf("Reward: %d\n",reward);
    }

    public void showPolicy(int i, int j, boolean u) {
        double max = -1000;
        for (int k = 0; k < qvalues[i][j].length; k++) {
            max = (qvalues[i][j][k] > max) ? qvalues[i][j][k] : max;
        }
        int[] maxIndex = new int[qvalues[i][j].length];
        for (int k = 0; k < qvalues[i][j].length; k++) {
            if (qvalues[i][j][k] == max) {
                maxIndex[k] = 1;
            }
        }
        if (maxIndex[0] == 1 && maxIndex[1] == 1 && maxIndex[2] == 1 && maxIndex[3] == 1) {
            System.out.printf("%s", (!u) ? "+" : "\u65291");
        } else if (maxIndex[0] == 1 && maxIndex[1] == 1 && maxIndex[2] == 1) {
            System.out.printf("%s", (!u) ? "<" : "\u22a3");
        } else if (maxIndex[0] == 1 && maxIndex[2] == 1 && maxIndex[3] == 1) {
            System.out.printf("%s", (!u) ? "^" : "\u22a5");
        } else if (maxIndex[0] == 1 && maxIndex[1] == 1 && maxIndex[3] == 1) {
            System.out.printf("%s", (!u) ? ">" : "\u22a3");
        } else if (maxIndex[1] == 1 && maxIndex[2] == 1 && maxIndex[3] == 1) {
            System.out.printf("%s", (!u) ? "v" : "\u22a4");
        } else if (maxIndex[0] == 1 && maxIndex[1] == 1) {
            System.out.printf("%s", (!u) ? "|" : "\u2195");
        } else if (maxIndex[0] == 1 && maxIndex[2] == 1) {
            System.out.printf("%s", (!u) ? "\\" : "\u2196");
        } else if (maxIndex[0] == 1 && maxIndex[3] == 1) {
            System.out.printf("%s", (!u) ? "/" : "\u2197");
        } else if (maxIndex[1] == 1 && maxIndex[2] == 1) {
            System.out.printf("%s", (!u) ? "/" : "\u2199");
        } else if (maxIndex[1] == 1 && maxIndex[3] == 1) {
            System.out.printf("%s", (!u) ? "\\" : "\u2198");
        } else if (maxIndex[2] == 1 && maxIndex[3] == 1) {
            System.out.printf("%s", (!u) ? "-" : "\u2194");
        } else if (maxIndex[0] == 1) {
            System.out.printf("%s", (!u) ? "^" : "\u2191");
        } else if (maxIndex[1] == 1) {
            System.out.printf("%s", (!u) ? "v" : "\u2193");
        } else if (maxIndex[2] == 1) {
            System.out.printf("%s", (!u) ? "<" : "\u2190");
        } else if (maxIndex[3] == 1) {
            System.out.printf("%s", (!u) ? ">" : "\u2192");
        } 
    }

    public void showQValues() {
        for (int i = 0; i < qvalues.length; i++) {
            for (int j = 0; j < qvalues[i].length; j++) {
                for (int k = 0; k < qvalues[i][j].length; k++) {
                    String s = "";
                    switch (k) {
                        case 0: s = "U";
                        break;
                        case 1: s = "D";
                        break;
                        case 2: s = "L";
                        break;
                        case 3: s = "R";
                        break;
                    }
                    System.out.printf("(%d, %d) %s: %.2f\n", i, j, s, qvalues[i][j][k]);
                }
                System.out.println();
            }
        }
    }

    
}
