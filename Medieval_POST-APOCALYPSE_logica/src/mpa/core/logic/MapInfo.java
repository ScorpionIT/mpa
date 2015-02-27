package mpa.core.logic;

import java.util.ArrayList;

public class MapInfo
{
	private float width;
	private float height;
	private String name;
	private int numberOfPlayers;
	private ArrayList<mpa.core.logic.Point> headQuarters = new ArrayList<mpa.core.logic.Point>();
	private ArrayList<mpa.core.logic.Point> caves = new ArrayList<mpa.core.logic.Point>();
	private ArrayList<mpa.core.logic.Point> woods = new ArrayList<mpa.core.logic.Point>();
	private ArrayList<mpa.core.logic.Point> fields = new ArrayList<mpa.core.logic.Point>();
	private ArrayList<mpa.core.logic.Point> mills = new ArrayList<mpa.core.logic.Point>();

	public float getWidth()
	{
		return width;
	}

	public void setWidth(float width)
	{
		this.width = width;
	}

	public float getHeight()
	{
		return height;
	}

	public void setHeight(float height)
	{
		this.height = height;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getNumberOfPlayers()
	{
		return numberOfPlayers;
	}

	public void setNumberOfPlayers(int numberOfPlayers)
	{
		this.numberOfPlayers = numberOfPlayers;
	}

	public boolean addHeadQuarter(mpa.core.logic.Point position)
	{
		if (headQuarters.size() == numberOfPlayers)
			return false;

		headQuarters.add(position);
		return true;
	}

	public void addCave(mpa.core.logic.Point position)
	{
		caves.add(position);
	}

	public void addField(mpa.core.logic.Point position)
	{
		fields.add(position);
	}

	public void addWood(mpa.core.logic.Point position)
	{
		woods.add(position);
	}

	public void addMill(mpa.core.logic.Point position)
	{
		mills.add(position);
	}

	public ArrayList<mpa.core.logic.Point> getHeadQuarters()
	{
		return headQuarters;
	}
}
