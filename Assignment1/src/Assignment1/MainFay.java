package Assignment1;

import java.io.IOException;

public class MainFay {

	public static void main(String[] args) throws Exception, IOException {
		// Check and make sure the number of arguments is correct
		if (args.length != 3) {
			System.out.println("Invalid number of variables!");
			System.exit(0);
		}

		// create a new instance of GridReductionFay and pass in the variables from main
		new GridReductionFay(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));

	}

}
