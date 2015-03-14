package mpa.core.logic;

public class PathNode
{
	// private Pair<Float, Float> parent;
	// private Pair<Float, Float> currentNode;
	// private double cost;
	//
	// public PathNode(Pair<Float, Float> parent, Pair<Float, Float> currentNode, double cost)
	// {
	// this.parent = parent;
	// this.currentNode = currentNode;
	// this.cost = cost;
	// }
	//
	// public Pair<Float, Float> getParent()
	// {
	// return parent;
	// }
	//
	// public Pair<Float, Float> getCurrentNode()
	// {
	// return currentNode;
	// }
	//
	// public double getCost()
	// {
	// return cost;
	// }
	//
	// public void setParent(Pair<Float, Float> parent)
	// {
	// this.parent = parent;
	// }
	//
	// public void setCost(double cost)
	// {
	// this.cost = cost;
	// }

	private Pair<Integer, Integer> parent;
	private Pair<Integer, Integer> currentNode;
	private double cost;

	public PathNode(Pair<Integer, Integer> parent, Pair<Integer, Integer> currentNode, double cost)
	{

		this.parent = parent;
		this.currentNode = currentNode;
		this.cost = cost;
	}

	public Pair<Integer, Integer> getParent()
	{
		return parent;
	}

	public Pair<Integer, Integer> getCurrentNode()
	{
		return currentNode;
	}

	public double getCost()
	{
		return cost;
	}

	public void setParent(Pair<Integer, Integer> parent)
	{
		this.parent = parent;
	}

	public void setCost(double cost)
	{
		this.cost = cost;
	}

}
