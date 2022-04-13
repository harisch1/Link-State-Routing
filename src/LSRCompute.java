
// Compiled using JDK-18
import java.util.Scanner;
import java.util.Stack;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LSRCompute {

    static int table[][]; // stores the adjacency matrix
    static char nodes[]; // for mapping nodes back to respective characters
    static boolean SSmode = false; // Single step flag
    static int shortest[]; // Distance of the shortest patch for each node from the source
    static int previous[]; // Previous node in the shortest path
    static boolean visited[]; // Check if a node has been visited
    static int source; // Source node
    static int closest; // The new discovered node
    static Stack<Integer> path; // Trace of the shortest path

    public static void main(String[] args) throws Exception {

        if (args[2].equals("SS"))
            SSmode = true;
        buildMatrix(args[0]);
        if (String.valueOf(nodes).contains(args[1])) {
            source = (int) args[1].charAt(0) - (int) 'A';
            dijkstraAlgorithm(table);
            printTable();
        } else
            System.out.println("Source node (" + args[1] + ") not found in Routing Graphs.");

    }

    // For file parsing and matrix initialization
    static void buildMatrix(String filePath) {
        try {
            // Read user input
            BufferedReader objReader = new BufferedReader(new FileReader(filePath));
            String input = new String();
            String strCurrentLine;
            int len = 0;
            while ((strCurrentLine = objReader.readLine()) != null) {
                input += strCurrentLine + "-";
                len++;
            }
            objReader.close();

            nodes = new char[len];
            table = new int[len][len];

            String[] inputArray = input.split("-", len);

            // Construct an adjutancy matrix for an easier computation of router path
            for (String s : inputArray) {
                s = s.replace("-", "");
                String[] indNode = s.split(" ", len);

                int c = 0;

                for (String n : indNode) {
                    if (n.length() > 1) {
                        n = n.strip();
                        if (n.length() == 2)
                            c = ((int) n.charAt(0)) - ((int) 'A');
                        else {
                            table[c][(int) n.charAt(0) - (int) 'A'] = Integer.parseInt(n.substring(2));
                        }
                    }
                }
                nodes[c] = s.charAt(0);
            }

            // initialize the nodes without direct connection to a large number
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++)
                    if (table[i][j] == 0)
                        table[i][j] = 1000;
            }

        } catch (IOException e) {
            System.out.println("File Not Found");
        }
    }

    // Dijkstra's algorithm for finding shortest path
    public static void dijkstraAlgorithm(int[][] matrix) {
        visited = new boolean[nodes.length];
        shortest = new int[nodes.length];
        previous = new int[nodes.length];
        shortest = matrix[source];

        // initializations
        for (int i = 0; i < nodes.length; i++) {
            visited[i] = false;
            previous[i] = source;
        }
        visited[source] = true;
        shortest[source] = 0;

        int Dist; // new distance from the source to the destination

        for (int num = 0; num < nodes.length - 1; num++) {

            int shorter = 1000;
            for (int i = 0; i < shortest.length; i++) {
                if (!visited[i] && shortest[i] < shorter) {
                    shorter = shortest[i];
                    closest = i;
                }
            }
            visited[closest] = true;
            for (int j = 0; j < nodes.length; j++) {
                if (matrix[closest][j] != source && !visited[j]) {
                    Dist = matrix[closest][j] + shorter;
                    if (Dist < shortest[j]) {
                        shortest[j] = Dist;
                        previous[j] = closest;
                    }
                }
            }
            /*
             * Print if single step mode is on
             * when a new node is found
             * it runs only when the new node has been visited to avoid any repetitions.
             */
            if (SSmode && visited[closest]) {
                SingleStepPrint();
            }

        }
    }

    // Uses a stack for the trace of each path
    public static void SingleStepPrint() {
        path = new Stack<Integer>();
        System.out.print("Found " + nodes[closest] + ": ");
        try {

            int current = closest;
            path.push(current);
            for (int i = 0; i < previous.length - 1; i++) {
                if (current == source)
                    break;
                current = previous[current];
                path.push(current);
            }
            while (path.size() > 1) {
                System.out.print(nodes[path.pop()] + ">");
            }
            System.out.print(nodes[path.pop()] + " Cost = " + shortest[closest]);
            pressEnterKeyToContinue();

        } catch (Exception e) {
            System.out.print(e);
        }
    }

    // Awaits user input until the next line is displayed
    public static void pressEnterKeyToContinue() {
        System.out.println("\t\t [Press Enter key to continue...]");
        Scanner s = new Scanner(System.in);
        s.nextLine();
        s.close();
    }

    /*
     * For printing the final path using a stack to trace back the path using
     * previous node array
     */
    public static void printTable() {
        int j;
        System.out.println();
        System.out.println("Source: " + nodes[source]);
        for (int i = 0; i < nodes.length; i++) {
            try {
                path = new Stack<Integer>();
                if (i != source) {
                    System.out.print(nodes[i] + ": Path = ");
                    path.push(i);
                    j = i;
                    do {
                        j = previous[j];
                        path.push(j);

                    } while (j != source);

                    while (path.size() > 1) {
                        System.out.print(nodes[path.pop()] + ">");
                    }
                    System.out.println(nodes[path.pop()] + " Cost = " + shortest[i]);
                }

            } catch (Exception e) {

            }

        }
    }
}