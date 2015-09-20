package mpa.core.logic;

import java.util.ArrayList;
import java.util.List;

public class MapInfo
{
	private float width;
	private float height;
	private String name;
	private int numberOfPlayers;
	private List<Pair<Float, Float>> headQuarters = new ArrayList<Pair<Float, Float>>();
	private List<Pair<Float, Float>> caves = new ArrayList<Pair<Float, Float>>();
	private List<Pair<Float, Float>> woods = new ArrayList<Pair<Float, Float>>();
	private List<Pair<Float, Float>> fields = new ArrayList<Pair<Float, Float>>();
	private List<Pair<Float, Float>> mines = new ArrayList<Pair<Float, Float>>();

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

	public boolean addHeadQuarter( Pair<Float, Float> position )
	{
		if( headQuarters.size() == numberOfPlayers )
			return false;

		headQuarters.add( position );
		return true;
	}

	public void addCave( Pair<Float, Float> position )
	{
		caves.add( position );
	}

	public void addField( Pair<Float, Float> position )
	{
		fields.add( position );
	}

	public void addWood( Pair<Float, Float> position )
	{
		woods.add( position );
	}

	public void addMine( Pair<Float, Float> position )
	{
		mines.add( position );
	}

	public List<Pair<Float, Float>> getHeadQuarters()
	{
		return headQuarters;
	}

	public List<Pair<Float, Float>> getCaves()
	{
		return caves;
	}

	public List<Pair<Float, Float>> getMines()
	{
		return mines;
	}

	public List<Pair<Float, Float>> getWoods()
	{
		return woods;
	}

	public List<Pair<Float, Float>> getFields()
	{
		return fields;
	}

	@Override
	public String toString()
	{
		String s = "Name: " + name;
		s += "\nWidth : " + width + " ; Height: " + height;
		s += "\nHeadQuarters: ";
		for( Pair<Float, Float> position : headQuarters )
		{
			s += "\nHeadQuarter: " + position.getFirst() + " - " + position.getSecond();
		}

		s += "\nFields: ";
		for( Pair<Float, Float> position : fields )
		{
			s += "\nField: " + position.getFirst() + " - " + position.getSecond();
		}

		s += "\nCaves: ";
		for( Pair<Float, Float> position : caves )
		{
			s += "\nCave: " + position.getFirst() + " - " + position.getSecond();
		}
		s += "\nWoods: ";
		for( Pair<Float, Float> position : woods )
		{
			s += "\nWoods: " + position.getFirst() + " - " + position.getSecond();
		}

		return s;
	}
}
