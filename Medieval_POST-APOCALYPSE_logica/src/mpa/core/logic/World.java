package mpa.core.logic;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
	private Map<Vector2f, List<AbstractObject>> objectX = new HashMap<>();
	private Map<Vector2f, List<AbstractObject>> objectY = new HashMap<>();
	private List<AbstractPrivateProperty> allObjects = new ArrayList<>();
	private List<AbstractResourceProducer> resourceProducers = new ArrayList<AbstractResourceProducer>();
	private List<Headquarter> headquarters = new ArrayList<>();

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

	public Map<Vector2f, List<AbstractObject>> getObjectX()
	{
		return objectX;
	}

	public Map<Vector2f, List<AbstractObject>> getObjectY()
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

	public List<AbstractPrivateProperty> getAllObjects()
	{
		return allObjects;
	}

	public List<AbstractObject> getObjectsXInTheRange( float x )
	{
		Set<Vector2f> keySet = objectX.keySet();
		List<AbstractObject> abstractObjectsX = new ArrayList<>();
		for( Vector2f pos : keySet )
		{
			if( x >= pos.x && x <= pos.y )
			{
				abstractObjectsX.addAll( objectX.get( pos ) );
			}
		}
		return abstractObjectsX;
	}

	public List<AbstractObject> getObjectsInTheRange( float xMin, float xMax, float yMin, float yMax )
	{
		List<AbstractObject> objects = new ArrayList<>();
		List<AbstractObject> objectsX = getObjectsXInTheRange( ( int ) xMin, ( int ) xMax );
		List<AbstractObject> objectsY = getObjectsYInTheRange( ( int ) yMin, ( int ) yMax );

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

	public List<Headquarter> getHeadquarters()
	{
		return headquarters;
	}

	public List<AbstractObject> checkForCollision( float x, float y, float collisionRay )
	{
		List<AbstractObject> objectsXInTheRange = getObjectsXInTheRange( x );
		List<AbstractObject> objectsYInTheRange = getObjectsYInTheRange( y );

		List<AbstractObject> collisions = new ArrayList<>();
		List<AbstractObject> intersection = new ArrayList<>();

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

	public List<AbstractObject> checkForCollisionInTheRange( float xMin, float xMax, float yMin,
			float yMax, float collisionRay )
	{

		int extraRange = 10;
		List<AbstractObject> collisions = new ArrayList<>();
		Line2D.Float line = new Line2D.Float( xMin, yMin, xMax, yMax );
		for( AbstractPrivateProperty abstractPrivateProperty : allObjects )
		{
			float xUpperLeftCollisionRay = abstractPrivateProperty.getX()
					- abstractPrivateProperty.getWidth() / 2 - abstractPrivateProperty.getWidth()
					* extraRange / 100;
			float yUpperLeftCollisionRay = abstractPrivateProperty.getY()
					- abstractPrivateProperty.getHeight() / 2 - abstractPrivateProperty.getWidth()
					* extraRange / 100;

			Rectangle2D.Float rect = new Rectangle2D.Float( xUpperLeftCollisionRay,
					yUpperLeftCollisionRay, abstractPrivateProperty.getWidth()
							+ abstractPrivateProperty.getWidth() * extraRange / 100,
					abstractPrivateProperty.getHeight() + abstractPrivateProperty.getWidth()
							* extraRange / 100 );

			if( line.intersects( rect ) )
				collisions.add( abstractPrivateProperty );

		}
		return collisions;

	}

	public List<AbstractObject> getObjectsYInTheRange( float y )
	{
		Set<Vector2f> keySet = objectY.keySet();
		List<AbstractObject> abstractObjectsY = new ArrayList<>();
		for( Vector2f pos : keySet )
		{
			if( y >= pos.x && y <= pos.y )
			{
				abstractObjectsY.addAll( objectY.get( pos ) );
			}
		}
		return abstractObjectsY;
	}

	public List<AbstractObject> getObjectsXInTheRange( float xMin, float xMax )
	{
		Set<Vector2f> keySet = objectX.keySet();
		List<AbstractObject> abstractObjectsX = new ArrayList<>();
		for( Vector2f pos : keySet )
		{
			if( ( pos.x <= xMax && pos.x >= xMin ) || ( pos.y <= xMax && pos.y >= xMin ) )
				abstractObjectsX.addAll( objectX.get( pos ) );
			else if( ( xMin >= pos.x && xMin <= pos.y ) || ( xMax >= pos.x && xMax <= pos.y ) )
				abstractObjectsX.addAll( objectX.get( pos ) );
		}
		return abstractObjectsX;
	}

	public List<AbstractObject> getObjectsYInTheRange( int yMin, int yMax )
	{
		Set<Vector2f> keySet = objectY.keySet();
		List<AbstractObject> abstractObjectsY = new ArrayList<>();
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
		List<AbstractObject> objectsXInTheRange = getObjectsXInTheRange( xGoal );
		List<AbstractObject> objectsYInTheRange = getObjectsYInTheRange( yGoal );

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

	public List<AbstractResourceProducer> getResourceProducers()
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
