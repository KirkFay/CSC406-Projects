package Assignment1;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.Reader;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;

// Need a writer to write out to a text file

public class SatSolverFay {

	private SatSolverFay(String inputFileName) throws IOException {

		File outputFile = new File("./tempSatFormulaFile.cnf");

		// create a new solver object using the given factory method
		ISolver solver = SolverFactory.newDefault();

		// Create a reader in order to take in values
		Reader reader = new DimacsReader(solver);

		FileWriter fileWriter = new FileWriter(outputFile, true);
		PrintWriter outputWriter = new PrintWriter(fileWriter);

		// filename is given on the command line. It should be in the CNF //format.
		try {

			IProblem problem = reader.parseInstance(inputFileName);
			int[] vars = problem.findModel();
			// Need to check if there's a value in vars, if not, then it's unsatisfiable
			if (vars == null) {
				outputWriter.println("Unsatisfiable");
				outputWriter.close();
			} else {
				// Write to output file if the conditions were met
				outputWriter.println("Satisfiable");
				// if vars is positive the solution is true otherwise its negative
				// if vars is positive the solution is true otherwise its negative
				for (int x = 1; x <= vars.length; x++) {

					if (vars[x - 1] > 0)
						outputWriter.println("x" + x + ": 1(true) ");

					else
						outputWriter.println("x" + x + ": -1(false)");

				}

				outputWriter.close();

			}
		} catch (Exception e) {
			outputWriter.write("Unsatisfiable");
			outputWriter.close();
		}
	}

	static void main(String[] args) throws IOException {
		TimerFay timer = new TimerFay();
		if (args.length <= 0) {
			System.out.println("ERROR: File is null!");
			System.exit(0);
		}

		// Start the timer
		timer.start();
		// Pass the arguments to SatSolverFay
		new SatSolverFay(args[0]);

		// Stop the timer
		timer.stop();

		// See how long it took
		System.out.println("This operation took " + timer.getDuration() + " milliseconds to complete");

	}

}
