package mpa.core.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import mpa.core.logic.resource.AbstractResourceProducer;
import mpa.core.maths.MyMath;

public class World
{
	private float width;
	private float height;
	private HashMap<Pair<Float, Float>, ArrayList<AbstractObject>> objectX = new HashMap<>();
	private HashMap<Pair<Float, Float>, ArrayList<AbstractObject>> objectY = new HashMap<>();
	private ArrayList<AbstractObject> allObjects = new ArrayList<>();
	private ArrayList<AbstractResourceProducer> resourceProducers = new ArrayList<AbstractResourceProducer>();

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

		if( obj instanceof AbstractResourceProducer )
			resourceProducers.add( ( AbstractResourceProducer ) obj );
		return true;
	}

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
		return abstractObjectsX;
	}

	public ArrayList<AbstractObject> getObjectsInTheRange( float xMin, float xMax, float yMin,
			float yMax )
	{
		ArrayList<AbstractObject> objects = new ArrayList<>();
		ArrayList<AbstractObject> objectsX = getObjectsXInTheRange( ( int ) xMin, ( int ) xMax );
		ArrayList<AbstractObject> objectsY = getObjectsXInTheRange( ( int ) yMin, ( int ) yMax );

		for( AbstractObject objX : objectsX )
		{
			Iterator<AbstractObject> it = objectsY.iterator();
			while( it.hasNext() )
			{
				AbstractObject objectY = it.next();
				if( objX == objectY )
				{
					objects.add( objX );
					it.remove();
				}
			}
		}

		return objects;
	}

	public ArrayList<AbstractObject> checkForCollision( float x, float y )
	{
		ArrayList<AbstractObject> objectsXInTheRange = getObjectsXInTheRange( x );
		ArrayList<AbstractObject> objectsYInTheRange = getObjectsYInTheRange( y );

		ArrayList<AbstractObject> collisions = new ArrayList<>();
		ArrayList<AbstractObject> intersection = new ArrayList<>();

		for( AbstractObject objX : objectsXInTheRange )
		{
			Iterator<AbstractObject> it = objectsYInTheRange.iterator();

			while( it.hasNext() )
			{
				AbstractObject objY = it.next();
				if( objX == objY )
				{
					intersection.add( objX );
					it.remove();
				}
			}
		}

		for( AbstractObject obj : intersection )
			if( MyMath.distanceFloat( x, y, obj.getX(), obj.getY() ) - obj.getCollisionRay() <= 0 )
				collisions.add( obj );

		return collisions;

	}

	public ArrayList<AbstractObject> checkForCollisionInTheRange( float xMin, float xMax,
			float yMin, float yMax )
	{
		if( xMin > xMax )
		{
			float tmp = xMax;
			xMax = xMin;
			xMin = tmp;
		}
		if( yMin > yMax )
		{
			float tmp = yMax;
			yMax = yMin;
			yMin = tmp;
		}
		ArrayList<AbstractObject> collisions = new ArrayList<>();

		ArrayList<AbstractObject> objectsInTheRange = getObjectsInTheRange( xMin, xMax, yMin, yMax );

		for( AbstractObject obj : objectsInTheRange )
		{
			if( MyMath.pointToLineDistance( xMin, yMin, xMax, yMax, obj.getX(), obj.getY() )
					- obj.getCollisionRay() - 10 <= 6f )
				collisions.add( obj );
		}

		return collisions;

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
		// System.err.println("numero abstract object x " + abstractObjectsX.size());
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
		// System.err.println("numero abstract object y " + abstractObjectsY.size());
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

	// public ArrayList<AbstractObject> getObjectsInTheRange( int xMin, int xMax, int yMin, int yMax
	// )
	// {
	// ArrayList<AbstractObject> intersection = new ArrayList<>();
	//
	// for( AbstractObject objectX : objectsX )
	// {
	// Iterator<AbstractObject> it = objectsY.iterator();
	// while( it.hasNext() )
	// {
	// AbstractObject objectY = it.next();
	// if( objectX == objectY )
	// {
	// intersection.add( objectX );
	// it.remove();
	// }
	// }
	// }
	// }

	public AbstractObject pickedObject( float xGoal, float yGoal )
	{
		ArrayList<AbstractObject> objectsXInTheRange = getObjectsXInTheRange( xGoal );
		ArrayList<AbstractObject> objectsYInTheRange = getObjectsYInTheRange( yGoal );

		objectsXInTheRange.addAll( objectsYInTheRange );
		for( AbstractObject abstractObject : objectsXInTheRange )
		{
			// System.out.println();
			// System.out.println();
			// System.out.println(abstractObject);
			// System.out.println();
			// System.out.println();
			if( MyMath.distanceFloat( ( int ) abstractObject.getX(), ( int ) abstractObject.getY(),
					( int ) xGoal, ( int ) yGoal ) - abstractObject.getCollisionRay() <= 0 )
			{
				return abstractObject;
			}

		}
		return null;
	}

	public ArrayList<AbstractResourceProducer> getResourceProducers()
	{
		return resourceProducers;
	}
}
