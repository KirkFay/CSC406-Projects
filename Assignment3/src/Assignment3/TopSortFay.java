package Assignment3;

import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.io.FileNotFoundException;
import java.util.Stack;
import java.util.Iterator;

public class TopSortFay {
	private static int numberOfNodes = 0;
	private static int[] inDegree;
	private static LinkedList[] adjacencyList;

	public static void main(String[] args) throws FileNotFoundException {
		if (args.length != 1) {
			System.out.print("Not enough input arguments... Did you specify a file?");
			System.exit(0);
		}

		createDiGraph(new File(args[0]));
		sort(new File("/Users/kirkfay/Desktop/School/CSC406/CSC406/Assignment3/src/Assignment3/ouput.txt"));
	}

	public static File sort(File outputFile) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(outputFile);
		Stack<Integer> s = new Stack<Integer>();
		int u;
		for (u = 0; u < inDegree.length; u++)
			if (inDegree[u] == 0)
				s.push(u);
		int i = 1;
		int v = 0;
		while (!s.isEmpty()) {
			u = s.pop();
			pw.print(u + 1 + " ");
			i++;
			if (adjacencyList[u] != null) {
				Iterator<Integer> listIterator = adjacencyList[u].iterator();
				while (listIterator.hasNext()) {
					v = (int) listIterator.next() - 1;
					inDegree[v]--;
					if (inDegree[v] == 0)
						s.push(v);
				}
			}
		}
		if (i > numberOfNodes) {
			pw.close();
			return outputFile;

		} else {
			pw.print("G is cyclic");
			pw.close();
			return outputFile;
		}

	}

	public static void createDiGraph(File dataFile) throws FileNotFoundException {
		Scanner fileScanner = new Scanner(dataFile);

		numberOfNodes = fileScanner.nextInt();
		adjacencyList = new LinkedList[numberOfNodes];
		inDegree = new int[numberOfNodes];

		int nodeOne = fileScanner.nextInt();
		int nodeTwo = fileScanner.nextInt();
		for (int temp = 1; temp <= numberOfNodes; temp++)
			while (temp == nodeOne) {
				// Check if the LinkedList is null, if it is initialize
				if (adjacencyList[temp - 1] == null) {
					adjacencyList[temp - 1] = new LinkedList<Integer>();
				}
				// Check if the adjacencyList has the second node, if it does add it and
				// subtract the inDegree
				if (!adjacencyList[temp - 1].contains(nodeTwo)) {
					adjacencyList[temp - 1].add(nodeTwo);
					inDegree[nodeTwo - 1]++;
				}
				// Scan in the two nodes
				if (fileScanner.hasNext()) {
					nodeOne = fileScanner.nextInt();
					nodeTwo = fileScanner.nextInt();
				} else
					nodeOne = -1;

			}
		fileScanner.close();

	}

}