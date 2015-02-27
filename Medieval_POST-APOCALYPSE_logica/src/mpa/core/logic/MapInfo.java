package mpa.core.logic;

import java.awt.Point;
import java.util.ArrayList;

public class MapInfo
{
	private float width;
	private float height;
	private String name;
	private int numberOfPlayers;
	private ArrayList<Point> headQuarters = new ArrayList<Point>();

	public float getWidth()
	{
		return width;
	}

	public void setWidth( float width )
	{
		this.width = width;
	}

	public float getHeight()
	{
		return height;
	}

	public void setHeight( float height )
	{
		this.height = height;
	}

	public String getName()
	{
		return name;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public int getNumberOfPlayers()
	{
		return numberOfPlayers;
	}

	public void setNumberOfPlayers( int numberOfPlayers )
	{
		this.numberOfPlayers = numberOfPlayers;
	}

	public boolean addHeadQuarter( Point position )
	{
		if( headQuarters.size() == numberOfPlayers )
			return false;

		headQuarters.add( position );
		return true;
	}

	public ArrayList<Point> getHeadQuarters()
	{
		return headQuarters;
	}
}
