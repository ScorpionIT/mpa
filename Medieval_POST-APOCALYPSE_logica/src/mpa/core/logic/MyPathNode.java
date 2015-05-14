package mpa.core.logic;

import javax.vecmath.Vector2f;

public class MyPathNode
{
	private Vector2f startingPoint;
	private Vector2f arrival;
	private float cost;

	public MyPathNode( Vector2f startingPoint, Vector2f arrival, float cost )
	{
		super();
		this.startingPoint = startingPoint;
		this.arrival = arrival;
		this.cost = cost;
	}

	public float getCost()
	{
		return cost;
	}

	public Vector2f getStartingPoint()
	{
		return startingPoint;
	}

	public Vector2f getArrival()
	{
		return arrival;
	}

	public void setCost( float cost )
	{
		this.cost = cost;
	}

	public void setArrival( Vector2f arrival )
	{
		this.arrival = arrival;
	}

	public void setStartingPoint( Vector2f startingPoint )
	{
		this.startingPoint = startingPoint;
	}

}
