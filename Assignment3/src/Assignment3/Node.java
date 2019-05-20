package Assignment3;

public class Node {

	int index;
	int profit;
	double weight;
	int nodeLabel;

	Node(int i, int p, double w, int nodeLab) {
		index = i;
		profit = p;
		weight = w;
		nodeLabel = nodeLab;
	}
	//Create a toString method to print out the values
	public String toString() {
		Node n = new Node(index,profit,weight,nodeLabel);
		return (nodeLabel + "\t\t" + index + "\t\t" + profit + "\t\t" + weight + "\t\t" + KSBB.bound(n) + "\t\t" + KSBB.maxProfit);
		}

}
