package mpa.core.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.vecmath.Vector2f;

import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.building.Headquarter;
import mpa.core.logic.building.Tower;
import mpa.core.logic.resource.AbstractResourceProducer;
import mpa.core.maths.MyMath;

public class World
{
	private float width;
	private float height;
	private HashMap<Vector2f, ArrayList<AbstractObject>> objectX = new HashMap<>();
	private HashMap<Vector2f, ArrayList<AbstractObject>> objectY = new HashMap<>();
	private ArrayList<AbstractPrivateProperty> allObjects = new ArrayList<>();
	private ArrayList<AbstractResourceProducer> resourceProducers = new ArrayList<AbstractResourceProducer>();
	private ArrayList<Headquarter> headquarters = new ArrayList<>();

	public World( float width, float height )
	{
		super();
		this.width = width;
		this.height = height;
	}

	public boolean addObject( AbstractPrivateProperty obj )
	{
		float x = obj.getX();
		float y = obj.getY();
		float width = obj.getWidth();
		float height = obj.getHeight();
		float xMin = x - width / 2;
		float xMax = x + width / 2;
		float yMin = y - height / 2;
		float yMax = y + height / 2;
		Vector2f xPair = new Vector2f( xMin, xMax );
		Vector2f yPair = new Vector2f( yMin, yMax );
		if( !objectX.containsKey( xPair ) )
			objectX.put( xPair, new ArrayList<AbstractObject>() );
		objectX.get( xPair ).add( obj );
		if( !objectY.containsKey( yPair ) )
			objectY.put( yPair, new ArrayList<AbstractObject>() );
		objectY.get( yPair ).add( obj );

		allObjects.add( obj );

		if( obj instanceof AbstractResourceProducer )
			resourceProducers.add( ( AbstractResourceProducer ) obj );
		if( obj instanceof Headquarter )
			headquarters.add( ( Headquarter ) obj );
		return true;
	}

	public HashMap<Vector2f, ArrayList<AbstractObject>> getObjectX()
	{
		return objectX;
	}

	public HashMap<Vector2f, ArrayList<AbstractObject>> getObjectY()
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

	public ArrayList<AbstractPrivateProperty> getAllObjects()
	{
		return allObjects;
	}

	public ArrayList<AbstractObject> getObjectsXInTheRange( float x )
	{
		Set<Vector2f> keySet = objectX.keySet();
		ArrayList<AbstractObject> abstractObjectsX = new ArrayList<>();
		for( Vector2f pos : keySet )
		{
			if( x >= pos.x && x <= pos.y )
			{
				abstractObjectsX.addAll( objectX.get( pos ) );
			}
		}
		return abstractObjectsX;
	}

	public ArrayList<AbstractObject> getObjectsInTheRange( float xMin, float xMax, float yMin,
			float yMax )
	{
		ArrayList<AbstractObject> objects = new ArrayList<>();
		ArrayList<AbstractObject> objectsX = getObjectsXInTheRange( ( int ) xMin, ( int ) xMax );
		ArrayList<AbstractObject> objectsY = getObjectsYInTheRange( ( int ) yMin, ( int ) yMax );

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

	public ArrayList<Headquarter> getHeadquarters()
	{
		return headquarters;
	}

	public ArrayList<AbstractObject> checkForCollision( float x, float y, float collisionRay )
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
			if( MyMath.distanceFloat( x, y, obj.getX(), obj.getY() ) - obj.getCollisionRay()
					- collisionRay <= 0 )
				collisions.add( obj );

		return collisions;

	}

	public ArrayList<AbstractObject> checkForCollisionInTheRange( float xMin, float xMax,
			float yMin, float yMax, float collisionRay )
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

		// System.out.println( "la size di objectsInTheRange è " + objectsInTheRange.size() );

		for( AbstractObject obj : objectsInTheRange )
		{
			// System.out.println( "point to line :"
			// + MyMath.pointToLineDistance( xMin, yMin, xMax, yMax, obj.getX(), obj.getY() ) );
			// System.out.println( obj.getClass().getName() );
			if( MyMath.pointToLineDistance( xMin, yMin, xMax, yMax, obj.getX(), obj.getY() )
					- obj.getCollisionRay() - collisionRay <= 1f )
				collisions.add( obj );
		}
		System.out.println( collisions );

		return collisions;

	}

	public ArrayList<AbstractObject> getObjectsYInTheRange( float y )
	{
		Set<Vector2f> keySet = objectY.keySet();
		ArrayList<AbstractObject> abstractObjectsY = new ArrayList<>();
		for( Vector2f pos : keySet )
		{
			if( y >= pos.x && y <= pos.y )
			{
				abstractObjectsY.addAll( objectY.get( pos ) );
			}
		}
		return abstractObjectsY;
	}

	public ArrayList<AbstractObject> getObjectsXInTheRange( float xMin, float xMax )
	{
		Set<Vector2f> keySet = objectX.keySet();
		ArrayList<AbstractObject> abstractObjectsX = new ArrayList<>();
		for( Vector2f pos : keySet )
		{
			if( ( pos.x <= xMax && pos.x >= xMin ) || ( pos.y <= xMax && pos.y >= xMin ) )
				abstractObjectsX.addAll( objectX.get( pos ) );
			else if( ( xMin >= pos.x && xMin <= pos.y ) || ( xMax >= pos.x && xMax <= pos.y ) )
				abstractObjectsX.addAll( objectX.get( pos ) );
		}
		// System.out.println( "numero abstract object x " + abstractObjectsX.size() );
		return abstractObjectsX;
	}

	public ArrayList<AbstractObject> getObjectsYInTheRange( int yMin, int yMax )
	{
		Set<Vector2f> keySet = objectY.keySet();
		ArrayList<AbstractObject> abstractObjectsY = new ArrayList<>();
		for( Vector2f pos : keySet )
		{
			if( ( pos.x <= yMax && pos.x >= yMin ) || ( pos.y <= yMax && pos.y >= yMin ) )
				abstractObjectsY.addAll( objectY.get( pos ) );
			else if( ( yMin >= pos.x && yMin <= pos.y ) || ( yMax >= pos.x && yMax <= pos.y ) )
				abstractObjectsY.addAll( objectY.get( pos ) );
		}
		return abstractObjectsY;
	}

	@Override
	public String toString()
	{
		String s = new String( "ObjectX \n" );
		Set<Vector2f> keySet = objectX.keySet();
		for( Vector2f pos : keySet )
		{
			s += pos.x + " " + pos.y + "\n";
			for( AbstractObject obj : objectX.get( pos ) )
			{
				s += obj.getClass() + " ";
			}
			s += "\n";
			s += "ObjectY \n";
		}
		keySet = objectY.keySet();
		for( Vector2f pos : keySet )
		{
			s += pos.x + " " + pos.y + "\n";
			for( AbstractObject obj : objectY.get( pos ) )
			{
				s += obj.getClass() + " ";
			}
			s += "\n";
		}
		return s;
	}

	public AbstractObject pickedObject( float xGoal, float yGoal )
	{
		ArrayList<AbstractObject> objectsXInTheRange = getObjectsXInTheRange( xGoal );
		ArrayList<AbstractObject> objectsYInTheRange = getObjectsYInTheRange( yGoal );

		objectsXInTheRange.addAll( objectsYInTheRange );
		for( AbstractObject abstractObject : objectsXInTheRange )
		{
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

	public boolean addTower( Tower tower )
	{

		if( !checkForCollision( tower.getX(), tower.getY(), tower.getCollisionRay() ).isEmpty() )
			return false;

		addObject( tower );
		return true;
	}

	public void destroyObject( AbstractObject obj )
	{
		for( Vector2f p : objectX.keySet() )
			if( objectX.get( p ).contains( obj ) )
				objectX.get( p ).remove( obj );

		for( Vector2f p : objectY.keySet() )
			if( objectY.get( p ).contains( obj ) )
				objectY.get( p ).remove( obj );

	}
}
