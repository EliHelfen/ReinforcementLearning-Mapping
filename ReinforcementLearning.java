import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReinforcementLearning {

    static double alpha = 0.9;
    static double epsilon = 0.9;
    static double gamma = 0.9;
    static int alphaDecay = 1000;
    static int epsilonDecay = 200;
    static double successProbability = 0.8;
    static boolean qLearn = false;
    static int trials = 10000;
    static boolean unicode = false;
    static int verbosity = 1;
    static boolean p = false;
    
    public static void main(String[] args) {
        Environment env = new Environment(0, 0);
        Agent agent;

        Options run = new Options(args);
        if(!run.options[0].isEmpty()) {
            System.out.printf("Reading %s\n", run.options[0]);
            env = getFile(run.options[0]);
        }
        else {
            System.err.println("Must Provide File");
            System.exit(0);
        }
        setOptions(run);

        agent = new Agent(0,0, env);


        //perform learning episodes
        System.out.printf("Beginning %d learning episodes with %s . . . \n", trials, (qLearn) ? "Q-learn" : "SARSA");
        Learning(agent, env, gamma, alpha, alphaDecay, epsilon, epsilonDecay, qLearn, successProbability, trials, verbosity);
        System.out.println("Done with learning!");

        //perform final evaluation
        System.out.printf("Beginning 50 evaluation episodes . . . \n");
        double avgReward = evaluate(agent, env, successProbability);
        System.out.printf("Average Reward on evaluation: %f\n", avgReward);
        //print final learned policy
        print(agent, env, unicode, verbosity);
       
    }

    private static void print(Agent agent, Environment env, boolean u, int v) {
        env.show(agent, u);
        if (v > 1) {
            agent.showQValues();
        }
    }

    private static double evaluate(Agent agent, Environment env, double sP) {
        double sumreward = 0;
        for (int i = 0; i < 50; i++) {
            agent.reward = 0;
            agent.position = env.getCell(Feature.START);
            int actionsMade = 1;
            Direction action = agent.greedyAction(agent.position);
            while(episodeNotEnded(env, agent, actionsMade)) {
            agent.move(env, action, sP);
            actionsMade++;
            action = agent.greedyAction(agent.position);
            }
            sumreward += agent.reward;
        }
        return sumreward/50;
    }

    public static void Learning(Agent agent, Environment env, double g, double a, int nA, double e, int nE,boolean q, double sP, int T, int v) {
        if (v > 2) {
            System.out.printf("Episode, Avg Reward\n");
        }
        for (int t = 0; t < T; t++) {
            agent.position = env.getCell(Feature.START);
            Position state = agent.position;
            Direction action = agent.epsilonGreedyAction(agent.position, (e/(1+(t/nE))));
            int actionsMade = 1;
            while(episodeNotEnded(env,agent,actionsMade)) {
                int r = agent.move(env, action, sP);
                Direction actionPrime = agent.epsilonGreedyAction(agent.position, (e/(1+(t/nE))));
                if (q) agent.qvalues[state.y][state.x][action.ordinal()] += (a/(1+(t/nA)))*(r + g*agent.qvalues[agent.position.y][agent.position.x][agent.greedyAction(agent.position).ordinal()]-agent.qvalues[state.y][state.x][action.ordinal()]);
                else agent.qvalues[state.y][state.x][action.ordinal()] += (a/(1+(t/nA)))*(r + g*agent.qvalues[agent.position.y][agent.position.x][actionPrime.ordinal()]-agent.qvalues[state.y][state.x][action.ordinal()]);
                state = agent.position;
                actionsMade++;
                action = actionPrime;
            }
            if (v > 2) {
                if (t%(T/10) == 0) System.out.printf("%4d, %.3f\n", t, evaluate(agent, env, sP));
            }
            if (v > 3) {
                if (t%nA==0) {
                    System.out.printf("after episode %d, alpha to %f\n", t, (a/(1+(t/nA))));
                }
                if (t%nE==0) {
                    System.out.printf("after episode %d, epsilon to %f\n", t, (e/(1+(t/nE))));
                }
            }
        }
    }

    private static boolean episodeNotEnded(Environment env, Agent agent, int actionsMade) {
        if (env.getCell(agent.position).f == Feature.MINE || env.getCell(agent.position).f == Feature.GOAL || actionsMade > env.height*env.width) {// add if it has moved more than env.height*env.width
            return false;
        } else return true;
    }

    private static Environment getFile(String fileName) {
        int height = 0;
        int width = 0;
        List<Cell[]> cellList = new ArrayList<>();
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                height++;
                String line = scanner.nextLine();
                if(!line.isEmpty()||!(line.contains("#"))) {
                    width = line.length();
                    Cell[] cells = new Cell[width];
                    for (int i = 0; i < width; i++) {
                        cells[i] = new Cell(line.charAt(i));
                    }
                    cellList.add(cells);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File Not Found");
            System.exit(0);
        }
        Environment env = new Environment(width, height);
        for (int i = 0; i < height; i++) {
            env.setCellWall(cellList.get(i),i);
        }
        return env;
    }

    private static void setOptions(Options run) {
        if (!(run.options[1]==null)) {
            try {
                alpha = Double.parseDouble(run.options[1]);
            } catch (NumberFormatException e) {
                System.err.println(String.format("%s must be a double.", run.options[1]));
                System.exit(0);
            }
        }
        if (!(run.options[2]==null)) {
            try {
                epsilon = Double.parseDouble(run.options[2]);
            } catch (NumberFormatException e) {
                System.err.println(String.format("%s must be a double.", run.options[2]));
                System.exit(0);
            }
        }
        if (!(run.options[3]==null)) {
            try {
                gamma = Double.parseDouble(run.options[3]);
            } catch (NumberFormatException e) {
                System.err.println(String.format("%s must be a double.", run.options[3]));
                System.exit(0);
            }
        }
        if (!(run.options[4]==null)) {
            try {
                alphaDecay = Integer.parseInt(run.options[4]);
            } catch (NumberFormatException e) {
                System.err.println(String.format("%s must be a int.", run.options[4]));
                System.exit(0);
            }
        }
        if (!(run.options[5]==null)) {
            try {
                epsilonDecay = Integer.parseInt(run.options[5]);
            } catch (NumberFormatException e) {
                System.err.println(String.format("%s must be a int.", run.options[5]));
                System.exit(0);
            }
        }
        if (!(run.options[6]==null)) {
            try {
                successProbability = Double.parseDouble(run.options[6]);
            } catch (NumberFormatException e) {
                System.err.println(String.format("%s must be a double.", run.options[6]));
                System.exit(0);
            }
        }
        if (!(run.options[7]==null)) {
            qLearn = true;
        }
        if (!(run.options[8]==null)) {
            try {
                trials = Integer.parseInt(run.options[8]);
            } catch (NumberFormatException e) {
                System.err.println(String.format("%s must be a int.", run.options[8]));
                System.exit(0);
            }
        }
        if (!(run.options[9]==null)) {
            unicode = true;
        }
        if (!(run.options[10]==null)) {
            try {
                verbosity = Integer.parseInt(run.options[10]);
            } catch (NumberFormatException e) {
                System.err.println(String.format("%s must be a int.", run.options[10]));
                System.exit(0);
            }
        }
        if (!(run.options[11]==null)) {
            p=true;
        }
    }

    public static void play(Agent agent, Environment env, double sP) {
        
        boolean b = true;
        Scanner scan = new Scanner(System.in);
        while (b) {
            env.show(agent);
            String s = scan.nextLine();
            Direction move = Direction.UP;
            if (s.equals("exit")) {
                b = false;
            } else {
                switch (s.charAt(0)) {
                    case 'u': move = Direction.UP;
                    break;
                    case 'd': move = Direction.DOWN;
                    break;
                    case 'l': move = Direction.LEFT;
                    break;
                    case 'r': move = Direction.RIGHT;
                    break;
                }
                agent.move(env, move, successProbability);
                agent.print();
            }
        }
        scan.close();
    }
}
