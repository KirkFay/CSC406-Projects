package Assignment3;

import java.util.Arrays;
import java.util.Iterator;

public class tempIter {

	public static void main(String[] args) {
		Integer[] inDegree = { 1, 2, 3 };
		Iterable<Integer> it = Arrays.asList(inDegree);
		Iterator<Integer> temp = it.iterator();
		while (temp.hasNext()) {

			System.out.println(temp.next());
		}

	}

}
