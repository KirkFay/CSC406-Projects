package Assignment3;

public class Item implements Comparable<Item> {
	int value;
	double weight;

	public Item(int v, double w) {
		value = v;
		weight = w;

	}

	public double getAvg() {

		return value / weight;
	}

	@Override
	public int compareTo(Item o) {

		if (this.getAvg() > o.getAvg())
			return -1;
		else if (this.getAvg() == o.getAvg())
			return 0;
		else
			return 1;

	}
}