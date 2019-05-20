package Assignment1;
/*
 * Author: Kirk Fay
 * Class: CSC406
 * Last updated: Thursday, February 14th 2019
 */

//Begin import statements
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.Reader;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;

public class GridReductionFay {

	private PrintWriter writer;
	private int n, m, nColor;
	private TimerFay timer = new TimerFay();
	 GridReductionFay(int n, int m, int nColor) throws IOException {
		
		// Pass the provided values from the constructor through to the instance
		// variables
		this.n = n;
		this.m = m;
		this.nColor = nColor;

		// start the timer
		timer.start();
		// call the solve function
		solve();
		// End the timer
		timer.stop();
		// See how long it took in milliseconds and print it to standard output
		System.out.println("This operation took " + timer.getDuration() + " milliseconds to complete");
	}

	/*
	 * This solve function simply passes the necessary CNF files to the reduceToSAT
	 * and output functions
	 */
	private void solve() throws IOException {
		reduceToSAT("outputFileName.cnf");
		output("outputFileName.cnf");

	}

	/*
	 * Take in a String for the outputFileName and pass that through to the
	 * writeFileHeader method Check that there is exactly one color per cell. Check
	 * the grid for condition 2 (Each sub-grid (with dimensions of 2 or greater per
	 * side) cannot have corners of any single color.) Output the results to the
	 * outputFileName
	 */
	private void reduceToSAT(String outputFileName) throws IOException {
		writeFileHeader(outputFileName);
		exactlyOneColorPerCell();
		subgridCornerConstraint();
		output(outputFileName);

	}

	/*
	 * starting with a 2x2 grid, this counts subgrids and increments to the next
	 * size up starting with the rows
	 */
	private void writeFileHeader(String outputFileName) {
		try {
			int numberOfSubGrids = 0;
			for (int z = 2; z <= m; z++)
				for (int y = 2; y <= n; y++) {
					// Check to see if the subgrid is a square
					if (z == y)
						// Columns - (ColumnWidth -1) * rows - (rowWidth -1)
						numberOfSubGrids += (n - (z - 1)) * (m - (y - 1));
					else
						// rows - (ColumnWidth - 1) * Columns - (rowWidth -1)
						numberOfSubGrids += (m - (z - 1)) * (n - (y - 1));

				}

			writer = new PrintWriter(outputFileName);
			// Print out to the file in the correct CNF file format
			writer.println("p cnf " + (n * m * nColor) + " "
					+ ((1 + (nColor * (nColor - 1) / 2)) * (n * m) + (nColor * numberOfSubGrids)));

		} catch (FileNotFoundException e) {
			System.out.println("Could not find outputFileName. Maybe try another location?");
		}
	}

	/*
	 * Check and see that there is only one color per cell Run through each column
	 * and then each row in that column checking the constraints as you pass through
	 * them
	 */
	private void exactlyOneColorPerCell() {
		for (int x = 1; x <= n; x++)
			for (int y = 1; y <= m; y++) {
				constraint1A(x, y);
				constraint1B(x, y);
			}
	}

	/*
	 * Verifies the corners of the sub-grid are not all color1 and The corners of
	 * the subgrid are not all color2 and The corners of the subgrid are not all
	 * color3 and The corners of the subgrid are not all color4
	 */
	private void subgridCornerConstraint() {
		for (int x = 1; x < n; x++)
			for (int y = 1; y < m; y++)
				// prevent going over bounds with + x
				for (int rowOffset = 1; rowOffset + x <= n; rowOffset++)
					for (int colOffset = 1; colOffset + y <= m; colOffset++)

						for (int z = 1; z <= nColor; z++)
							writer.println(-encode(x, y, z) + " " + -encode(x + rowOffset, y, z) + "  "
									+ -encode(x, y + colOffset, z) + " " + -encode(x + rowOffset, y + colOffset, z)
									+ " 0");
		writer.close();
	}

	/*
	 * CONTSTRAINT 1A: At least one color is assigned to a cell (i, j)
	 */
	private void constraint1A(int p, int q) {
		for (int i = 1; i <= nColor; i++)
			writer.print(encode(p, q, i) + " ");
		writer.println("0");

	}

	/*
	 * CONTSTRAINT 1B: At most one color is assigned to a cell (i, j)
	 */
	private void constraint1B(int p, int q) {
		for (int x = 1; x <= n; x++)
			for (int index = x + 1; index <= nColor; index++) {
				writer.println(-encode(p, q, x) + " " + -encode(p, q, index) + " 0");
			}

	}

	/*
	 * convert a triplet grid variable (p,q,r) to an integer X which represents the
	 * equivalent SAT variable as used by the satSolver
	 */
	private int encode(int x, int y, int z) {
		return nColor * ((x - 1) * m + (y - 1)) + z;
	}

	private void output(String satFilename) throws IOException {

		// create a new solver object using the given factory method
		ISolver solver = SolverFactory.newDefault();

		// Create a reader in order to take in values
		Reader reader = new DimacsReader(solver);
		PrintWriter outputWriter = new PrintWriter("satFileName.cnf");

		// filename is given on the command line. It should be in the CNF format.
		try {

			IProblem problem = reader.parseInstance(satFilename);
			int[] vars = problem.findModel();
			// Need to check if there's a value in vars, if not, then it's unsatisfiable
			if (vars == null) {
				outputWriter.println("Unsatisfiable");
				outputWriter.close();
			} else {
				// Write to output file if the conditions were met
				outputWriter.println("Satisfiable");
				// if vars is positive the solution is true otherwise its negative
				for (int x = 0; x < vars.length; x++) {
					if (vars[x] > 0)
						outputWriter.println("x" + (x + 1) + ": 1(true) ");
					else
						outputWriter.println("x" + (x + 1) + ": -1(false)");

				}
				outputWriter.close();
			}
			// If the file is not found catch the exception and print the error to standard
			// output.
		} catch (

		Exception e) {
			System.out.println("File Not Found!");
		}
	}

}
