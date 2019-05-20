package Assignment2;
/*
 *Author: Kirk Fay
 *Class: CSC406
 *Version: 2/28/19 
 */

//Start import statements
import java.io.File;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import org.jgrapht.alg.KruskalMinimumSpanningTree;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

public class MainFay {
	//take in an inputfile as a string
	private String inputFileName;
	//create an int array in order to hold the nodes as they get traversed
	private static int[] tour;
	private SimpleWeightedGraph<Integer, DefaultWeightedEdge> graph;
	// set a variable for the number of integers
	private static int nv;
	// set the default weighted edge to mstEdges
	//here we have a Set of all the weighted edges that we find
	private Set<DefaultWeightedEdge> mstEdges;

	/*
	 * Constructor that takes in the name of the input file
	 */

	public MainFay(String inputString) throws FileNotFoundException {
		inputFileName = inputString;
		readInput();
		mst();
		depthFirstOrder();
	}

	/*
	 * Start the main function and make sure there are the correct number of command
	 * line arguments, we need an input and an output file Run a timer in order to
	 * see how long computation takes. Output the time
	 */

	public static void main(String[] args) throws FileNotFoundException {
		if (args.length != 2) {
			System.out.print("Not enough variables!");
			System.exit(0);
		}
		TimerFay timer = new TimerFay();
		timer.start();
	    new MainFay(args[0]);
		// ARGS[1]
		writeTour(args[1], tour);
		timer.stop();
		System.out.print("Time to completion for this operation is " + timer.getDuration() + " milliseconds");
	}

	/*
	 * Create a readInput method that takes in the file and reads in the necessary
	 * variables for the edges/edge weight. Create a simple weighted graph using
	 * these values
	 */

	public void readInput() throws FileNotFoundException {
		nv = 0;
		File fileName = new File(inputFileName);
		Scanner newScanner = new Scanner(fileName);
		Pattern inputPattern = Pattern.compile("DIMENSION: ");
		// while the input pattern isn't there, keep moving the scanner along to the
		// next line
		while (newScanner.findInLine(inputPattern) == null) {
			newScanner.nextLine();
		}
		nv = newScanner.nextInt();
		// set the new size of tour
		tour = new int[nv];
		graph = new SimpleWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		for (int i = 1; i <= nv; i++)
			graph.addVertex(i);
		while (newScanner.hasNext()) {
			if (newScanner.next().equals("NODE_COORD_SECTION"))
				while (newScanner.hasNextInt()) {
					int v1 = newScanner.nextInt();
					int v2 = newScanner.nextInt();
					int v3 = newScanner.nextInt();
					DefaultWeightedEdge edge = graph.addEdge(v1, v2);
					graph.setEdgeWeight(edge, v3);
				}
		}
		newScanner.close();
	}

	/*
	 * Using the graph we read in, we create a minimum spanning tree using the
	 * included method in the jgrapht library. Add all the edges to mstEdges
	 */
	public void mst() {
		KruskalMinimumSpanningTree<Integer, DefaultWeightedEdge> tree = new KruskalMinimumSpanningTree<Integer, DefaultWeightedEdge>(
				graph);
		//All the edges of the mst
		mstEdges = tree.getEdgeSet();
	}

	/*
	 * Create a graph without the weighted edges
	 */
	public void depthFirstOrder() throws FileNotFoundException {
		// A simple graph, also called a strict graph is an unweighted, undirected graph
		// containing no graph loops or multiple edges
		SimpleGraph<Integer, DefaultEdge> tree = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);
		// Add vertices
		for (int i = 1; i <= nv; i++)
			tree.addVertex(i);
		// add edges without weights
		for (DefaultWeightedEdge edge : mstEdges)
			tree.addEdge(graph.getEdgeSource(edge), graph.getEdgeTarget(edge));
		// Traverse the graph to find visited nodes
		DepthFirstIterator<Integer, DefaultEdge> traverse = new DepthFirstIterator<Integer, DefaultEdge>(tree);
		int i = 0;
		while (traverse.hasNext()) {
			tour[i] = traverse.next();
			i++;
		}
	}

	/*
	 * Put visited nodes into output file with opt.tour extension
	 */
	static void writeTour(String outputFile, int[] tour) throws FileNotFoundException {
		PrintWriter newWriter = new PrintWriter(outputFile);
		newWriter.println("NAME: " + outputFile);
		newWriter.println("TYPE: tour");
		newWriter.println("DIMENSION: " + nv);
		newWriter.println("TOUR_SECTION");

		for (int i = 0; i < tour.length; i++)
			newWriter.println(tour[i]);
		// end the file with -1 
		newWriter.print("-1");
		newWriter.close();
	}

}

