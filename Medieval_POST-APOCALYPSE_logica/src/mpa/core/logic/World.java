package mpa.core.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class World
{
	private float width;
	private float height;
	private HashMap<Pair<Float, Float>, ArrayList<AbstractObject>> objectX = new HashMap<>();
	private HashMap<Pair<Float, Float>, ArrayList<AbstractObject>> objectY = new HashMap<>();
	private ArrayList<AbstractObject> allObjects = new ArrayList<>();

	// private Map<Integer, Point> headquartedPosition;
	public World( float width, float height )
	{
		super();
		this.width = width;
		this.height = height;
	}

	public boolean addObject( AbstractObject obj )
	{
		float x = obj.getX();
		float y = obj.getY();
		float width = obj.getWidth();
		float height = obj.getHeight();
		float xMin = x - width / 2;
		float xMax = x + width / 2;
		float yMin = y - height / 2;
		float yMax = y + height / 2;
		Pair<Float, Float> xPair = new Pair( xMin, xMax );
		Pair<Float, Float> yPair = new Pair( yMin, yMax );
		if( !objectX.containsKey( xPair ) )
			objectX.put( xPair, new ArrayList<AbstractObject>() );
		objectX.get( xPair ).add( obj );
		if( !objectY.containsKey( yPair ) )
			objectY.put( yPair, new ArrayList<AbstractObject>() );
		objectY.get( yPair ).add( obj );

		allObjects.add( obj );
		return true;
	}

	// per aggiungere giocatori a partita iniziata
	// public Headquarter addHeadquarter(int i) throws Exception
	// {
	// Headquarter headquarter;
	// headquarter = new Headquarter(headquartedPosition.get(i).x, headquartedPosition.get(i).y);
	// this.addObject(headquarter);
	// return headquarter;
	// }
	// public void setHeadquartedPosition(Map<Integer, Point> headquartedPosition)
	// {
	// this.headquartedPosition = headquartedPosition;
	// }
	public HashMap<Pair<Float, Float>, ArrayList<AbstractObject>> getObjectX()
	{
		return objectX;
	}

	public HashMap<Pair<Float, Float>, ArrayList<AbstractObject>> getObjectY()
	{
		return objectY;
	}

	public float getWidth()
	{
		return width;
	}

	public float getHeight()
	{
		return height;
	}

	public ArrayList<AbstractObject> getAllObjects()
	{
		return allObjects;
	}

	public ArrayList<AbstractObject> getObjectsXInTheRange( float x )
	{
		Set<Pair<Float, Float>> keySet = objectX.keySet();
		ArrayList<AbstractObject> abstractObjectsX = new ArrayList<>();
		for( Pair<Float, Float> pair : keySet )
		{
			if( x >= pair.getFirst() && x <= pair.getSecond() )
			{
				abstractObjectsX.addAll( objectX.get( pair ) );
			}
		}
		// System.out.println("getObjectsXInTheRange " + abstractObjectsX.size());
		return abstractObjectsX;
	}

	public ArrayList<AbstractObject> getObjectsYInTheRange( float y )
	{
		Set<Pair<Float, Float>> keySet = objectY.keySet();
		ArrayList<AbstractObject> abstractObjectsY = new ArrayList<>();
		for( Pair<Float, Float> pair : keySet )
		{
			if( y >= pair.getFirst() && y <= pair.getSecond() )
			{
				abstractObjectsY.addAll( objectY.get( pair ) );
			}
		}
		// System.out.println("getObjectsYInTheRange " + abstractObjectsY.size());
		return abstractObjectsY;
	}

	public ArrayList<AbstractObject> getObjectsXInTheRange( int xMin, int xMax )
	{
		Set<Pair<Float, Float>> keySet = objectX.keySet();
		ArrayList<AbstractObject> abstractObjectsX = new ArrayList<>();
		for( Pair<Float, Float> pair : keySet )
		{
			if( ( pair.getFirst() <= xMax && pair.getFirst() >= xMin )
					|| ( pair.getSecond() <= xMax && pair.getSecond() >= xMin ) )
				abstractObjectsX.addAll( objectX.get( pair ) );
			else if( ( xMin >= pair.getFirst() && xMin <= pair.getSecond() )
					|| ( xMax >= pair.getFirst() && xMax <= pair.getSecond() ) )
				abstractObjectsX.addAll( objectX.get( pair ) );
		}
		System.err.println( "numero abstract object x " + abstractObjectsX.size() );
		return abstractObjectsX;
	}

	public ArrayList<AbstractObject> getObjectsYInTheRange( int yMin, int yMax )
	{
		Set<Pair<Float, Float>> keySet = objectY.keySet();
		ArrayList<AbstractObject> abstractObjectsY = new ArrayList<>();
		for( Pair<Float, Float> pair : keySet )
		{
			if( ( ( pair.getFirst() < yMax && pair.getFirst() > yMin ) || ( pair.getSecond() < yMax && pair
					.getSecond() > yMin ) ) )
				abstractObjectsY.addAll( objectY.get( pair ) );
			else if( ( yMin >= pair.getFirst() && yMin <= pair.getSecond() )
					|| ( yMax >= pair.getFirst() && yMax <= pair.getSecond() ) )
				abstractObjectsY.addAll( objectY.get( pair ) );
		}
		System.err.println( "numero abstract object y " + abstractObjectsY.size() );
		return abstractObjectsY;
	}

	@Override
	public String toString()
	{
		String s = new String( "ObjectX \n" );
		Set<Pair<Float, Float>> keySet = objectX.keySet();
		for( Pair<Float, Float> pair : keySet )
		{
			s += pair.getFirst() + " " + pair.getSecond() + "\n";
			for( AbstractObject obj : objectX.get( pair ) )
			{
				s += obj.getClass() + " ";
			}
			s += "\n";
			s += "ObjectY \n";
		}
		keySet = objectY.keySet();
		for( Pair<Float, Float> pair : keySet )
		{
			s += pair.getFirst() + " " + pair.getSecond() + "\n";
			for( AbstractObject obj : objectY.get( pair ) )
			{
				s += obj.getClass() + " ";
			}
			s += "\n";
		}
		return s;
	}
	// public boolean isPositionEmpty(int x, int y)
	// {
	// return (map[x][y] instanceof EmptyObject);
	// }
}
