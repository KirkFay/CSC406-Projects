package Assignment4;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class HorspoolMapFay {

	public static void main(String[] args) throws FileNotFoundException {
		if (args.length != 1) {
			System.out.print("Error: No file was specified");
			System.exit(0);
		}
		new HorspoolMapFay(args[0]);
	}

	private HorspoolMapFay(String inputFileName) throws FileNotFoundException {
		StringBuilder sBuilder = new StringBuilder();

		File inputFile = new File(inputFileName);

		Scanner sc = new Scanner(inputFile);
		PrintWriter pw = new PrintWriter(
				"/Users/kirkfay/Desktop/School/CSC406/CSC406/Assignment4/src/Assignment4/outputFile.txt");
		String pattern = new String();
		String text = new String();
		// Create Bad Match Table
		HashMap<Character, Integer> bmt = new HashMap<Character, Integer>();
		// initialize the characters
		char patternChar = '\0';
		char textChar = '\0';
		while (sc.hasNext()) {
			String tempLine = sc.nextLine();
			if (tempLine != "\n" || tempLine != "") {
				text = tempLine;
				pattern = sc.nextLine();
			} else {
				text = sc.nextLine();
				pattern = sc.nextLine();
			}

			for (int index = 0; index < pattern.length(); index++) {
				// formula to find the shift
				int shift = pattern.length() - 1 - index;
				if (shift == 0 && !bmt.containsKey(pattern.charAt(index)))
					bmt.put(pattern.charAt(index), pattern.length());

				else if (shift == 0 && bmt.containsKey(pattern.charAt(index)))
					break;

				else
					bmt.put(pattern.charAt(index), shift);
			}

			bmt.put('*', pattern.length());
			// Write to the stringBuilder in order to keep writing to File efficient
			sBuilder.append("text: " + text + "\tpattern: " + pattern + "\n");
			sBuilder.append("bmt table: " + bmt + "\n");

			
			int z = pattern.length() - 1;
			boolean foundMatch = false;
			while (z < text.length() && !foundMatch) {

				patternChar = pattern.charAt(pattern.length() - 1);
				textChar = text.charAt(z);

				if (patternChar != textChar && !pattern.contains(textChar + ""))
					z += bmt.get('*');
				else if (patternChar != textChar && pattern.contains(textChar + ""))
					z += bmt.get(textChar);
				else {

					int index = 1;
					char tempChar = patternChar;
					while (patternChar == textChar && !foundMatch) {
						patternChar = pattern.charAt(pattern.length() - index - 1);
						textChar = text.charAt(z - index);
						if (patternChar != textChar)
							z += bmt.get(tempChar);
						index++;
						if (pattern.length() - index - 1 == -1)
							foundMatch = true;
					}
				}

			}

			if (pattern.length() > text.length()) {
				sBuilder.append("pattern < text. not Found a match for the pattern. \n\n");
			
			}
			else if (foundMatch)
				sBuilder.append("Found a match for the pattern: " + pattern + " at pos: " + (z - pattern.length() + 1)
						+ "\n\n");
			else
				sBuilder.append("not Found match for pattern: " + pattern + "\n\n");
			
			if (sc.hasNext())
				sc.nextLine();
			bmt.clear();

		}
		pw.println(sBuilder.toString());
		pw.close();
		sc.close();

	}

}