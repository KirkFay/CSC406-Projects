package Assignment3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class KSBB {

	// Declare Instance Variables
	static int maxProfit;
	private static int numberOfItems;
	private static double maxWeight;
	private static Item[] vw;
	private static PrintWriter fileWriter;

	public static void main(String[] args) throws FileNotFoundException {

		// Check for the correct number of arguments
		if (args.length != 1) {
			System.out.println("Incorrect number of input arguments! \n");
			System.exit(0);
		}
		// Create and output file and read in the input file
		File outputFile = new File(
				"/Users/kirkfay/Desktop/School/CSC406/CSC406/Assignment3/src/Assignment3/KnapSackOutPut.txt");
		fileWriter = new PrintWriter(outputFile);
		Scanner inputScan = new Scanner(new File(args[0]));

		numberOfItems = inputScan.nextInt();
		maxWeight = inputScan.nextInt();
		vw = new Item[numberOfItems];

		fileWriter.println("numItems \t maxWeight \n");
		fileWriter.println(numberOfItems + "\t" + maxWeight);
		fileWriter.println("Initial unsorted items\n\n");
		fileWriter.println("value \t Weight \t Value/Weight\n");

		int x = 0;
		int y = 0;
		int z = 0;
		while (inputScan.hasNext()) {
			y = inputScan.nextInt();
			z = inputScan.nextInt();

			Item tempItem = new Item(y, z);
			vw[x] = tempItem;
			fileWriter.println(y + "\t" + z + "\t" + (y / z));
			x++;
		}
		inputScan.close();
		// Check for the constraints
		// 1. at least one index i = 1,2,... ,n, Wi < W
		// 2. If ∑_(i=1)^n▒wi < W then all items may be loaded and the problem is
		// trivial.

		if (firstConstraint() && secondConstraint())
			new KSBB();
		else

			System.out.println("This case is trivial!");
		System.exit(0);
	}

	// Method to check first Contraint detailed above
	public static boolean firstConstraint() {

		for (Item tempItem : vw) {
			if (tempItem.weight < maxWeight) {
				return true;
			}

		}
		return false;
	}

	// Method to check secondConstraint detailed above
	public static boolean secondConstraint() {
		double tempWeight = 0;
		for (Item tempItem : vw) {
			tempWeight += tempItem.weight;

		}

		return (tempWeight > maxWeight);
	}

	KSBB() {
		Arrays.sort(vw);

		for (Item z : vw) {

			fileWriter.println(z.value + "\t" + z.weight + "\t" + z.getAvg());
		}

		knapsackSolver();

	}

	static double bound(Node v) {
		if (v.weight >= maxWeight)
			return 0;
		int totalBound = v.profit;
		int tempIndex = v.index + 1;
		double totalWeight = v.weight;
		while ((tempIndex < numberOfItems) && (totalWeight + vw[tempIndex].weight <= maxWeight)) {
			totalWeight += vw[tempIndex].weight;
			totalBound += vw[tempIndex].value;
			tempIndex++;
		}
		if (tempIndex < numberOfItems)
			totalBound += (maxWeight - totalWeight) * (vw[tempIndex].value / vw[tempIndex].weight);
		return totalBound;
	}

	int knapsackSolver() {
		LinkedList<Node> nodeQ = new LinkedList<Node>();
		Node u = new Node(-1, 0, 0, 1);
		nodeQ.add(u);
		maxProfit = 0;
		int i = 0;
		fileWriter.println("Following is a trace of code: \n\n");
		fileWriter.println("Action \t nodeLabel \t Level \t Profit \t Weight \t Bound \t maxP\n");

		while (!nodeQ.isEmpty()) {
			u = nodeQ.pop();

			fileWriter.println("dequeue\t\t" + u.toString());
			if (u.profit >= maxProfit) {
				Node v1 = new Node(u.index + 1, vw[i].value + u.profit, vw[i].weight + u.weight, u.nodeLabel + 1);
				Node v2 = new Node(u.index + 1, u.profit, u.weight, u.nodeLabel + 2);
				if (v1.weight <= maxWeight) {
					if (v1.profit > maxProfit) {
						maxProfit = v1.profit;
						if (bound(v1) > maxProfit) {
							nodeQ.add(v1);
							fileWriter.println("addRT\t\t" + v1.toString());
						}
					}
				}
				if (v2.weight <= maxWeight) {
					if (v2.profit > maxProfit) {
						maxProfit = v2.profit;
					}

					if (bound(v2) > maxProfit) {
						nodeQ.add(v2);
						fileWriter.println("addLT\t\t" + v2.toString());
					}

				}

			}

		}

		fileWriter.println("Max Profit: " + maxProfit);
		fileWriter.close();
		return maxProfit;

	}

}
