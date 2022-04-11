
// import java.io.File;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.Stream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LSRCompute {

    static int table[][];
    static char nodes[];
    static boolean SSmode = false;
    static int shortest[];
    static int previous[];
    static boolean visited[];
    static int source;
    static int closest;
    static Stack<Integer> path;

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

            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++)
                    if (table[i][j] == 0)
                        table[i][j] = 1000;
            }

        } catch (IOException e) {
            System.out.println("File Not Found");
        }
    }

    public static void dijkstraAlgorithm(int[][] matrix) {
        visited = new boolean[nodes.length];
        shortest = new int[nodes.length];
        previous = new int[nodes.length];
        shortest = matrix[source];

        for (int i = 0; i < nodes.length; i++) {
            visited[i] = false;
            previous[i] = source;
        }
        visited[source] = true;
        shortest[source] = 0;

        int Dist;

        for (int num = 0; num < nodes.length - 1; num++) {

            // shorter = shorterPath();
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
            // Print if single step mode is on
            if (SSmode && visited[closest]) {
                path = new Stack<Integer>();
                System.out.print("Found " + nodes[closest] + ": ");
                try {

                    int current = closest;
                    path.push(current);
                    // while (previous[current] != source && current != source) {
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

                }
            }

        }
    }

    public static void pressEnterKeyToContinue() {
        System.out.println("\t\t [Press Enter key to continue...]");
        Scanner s = new Scanner(System.in);
        s.nextLine();
    }

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
                    // System.out.println("\n");
                }

            } catch (Exception e) {

            }

        }
    }
}