package Assignment4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class AVLInsertFay {

	Boolean h = false;

	public static void main(String[] args) throws FileNotFoundException {
		if (args.length != 1) {
			System.out.print("Error: Did you specify a file?");
			System.exit(0);
		}
		new AVLInsertFay(args[0]);
	}

	private AVLInsertFay(String inputFileName) throws FileNotFoundException {
		
			Scanner inputScnr = new Scanner(new File(inputFileName));
			PrintWriter fileWriter = new PrintWriter("/Users/kirkfay/Desktop/School/CSC406/CSC406/Assignment4/src/Assignment4/outputFile.txt");
			AVLNodeFay p = new AVLNodeFay(inputScnr.nextInt(), 0, null, null);

			while (inputScnr.hasNext()) {
				fileWriter.println(printTree(p));
				p = insert(inputScnr.nextInt(), p);

			}

			fileWriter.close();
			inputScnr.close();
		
	}

	private AVLNodeFay leftRightRotation(AVLNodeFay p) {
		AVLNodeFay p1;
		AVLNodeFay p2;

		p1 = p.left;
		p2 = p1.right;
		p1.right = p2.left;
		p.left = p2.right;
		p2.right = p;
		p2.left = p1;

		if (p2.bal == 0) {
			p.bal = 0;
			p1.bal = 0;
		}

		if (p2.bal == -1)
			p.bal = 1;

		if (p2.bal == 1)
			p1.bal = -1;

		p = p2;

		return p;
	}

	private AVLNodeFay rightLeftRotation(AVLNodeFay p) {
		AVLNodeFay p1;
		AVLNodeFay p2;
		p1 = p.right;
		p2 = p1.left;
		p1.left = p2.right;
		p.right = p2.left;
		p2.left = p;
		p2.right = p1;
		if (p2.bal == 0) {
			p.bal = 0;
			p1.bal = 0;
		}
		if (p2.bal == -1)
			p1.bal = +1;
		if (p2.bal == +1)
			p.bal = -1;

		p = p2;

		return p;
	}

	private AVLNodeFay rightRightRotation(AVLNodeFay p) {
		AVLNodeFay p1;
		p1 = p.right;
		p.right = p1.left;
		p1.left = p;
		p = p1;

		return p;
	}

	private AVLNodeFay leftLeftRotation(AVLNodeFay p) {
		AVLNodeFay p1;
		p1 = p.left;
		p.left = p1.right;
		p1.right = p;
		p = p1;

		return p;
	}

	private String printTree(AVLNodeFay p) {

		if (p == null)
			return "()";

		return (p.key + "(" + printTree(p.left) + printTree(p.right) + ")");

	}
	

	private AVLNodeFay insert(int k, AVLNodeFay p) {
		AVLNodeFay p1 = new AVLNodeFay(k, 0, null, null);

		if (p == null) {
			p = new AVLNodeFay(k, 0, null, null);
			h = true;
		}

		else if (k < p.key) {
			p.left = insert(k, p.left);
			if (h)
				switch (p.bal) {
				case 0: {
					p.bal = -1;
					break;
				}
				case +1: {
					p.bal = 0;
					h = false;
					break;
				}
				case -1: {
					p1 = p.left;
					if (p1.bal == -1) {
						p = leftLeftRotation(p);
						p.bal = 0;
						p.left.bal = 0;
						p.right.bal = 0;
						h = false;
					} else {
						p = leftRightRotation(p);
						p.bal = 0;
						h = false;
					}
				}
				}
		} else {

			p.right = insert(k, p.right);
			if (h)
				switch (p.bal) {
				case 0: {
					p.bal = 1;
					break;
				}
				case -1: {
					p.bal = 0;
					h = false;
					break;
				}
				case 1: {
					p1 = p.right;
					if (p1.bal == 1) {
						p = rightRightRotation(p);
						p.bal = 0;
						p.left.bal = 0;
						p.right.bal = 0;
						h = false;
					} else {
						p = rightLeftRotation(p);
						p.bal = 0;
						h = false;
					}
				}
				}

		}
		return p;
	}

}