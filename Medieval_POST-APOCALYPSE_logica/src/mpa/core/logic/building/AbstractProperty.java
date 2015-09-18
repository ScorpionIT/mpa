package mpa.core.logic.building;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.vecmath.Vector2f;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.GameManager;
import mpa.core.util.GameProperties;

public class AbstractProperty extends AbstractObject
{

	protected Vector2f gatheringPlace = null;
	protected List<Vector2f> gatheringPlaces = null;
	protected Map<Vector2f, Tower> towers = new HashMap<>();

	AbstractProperty( float x, float y, float width, float height )
	{
		super( x, y, width, height );

	}

	public Vector2f getGatheringPlace()
	{
		if( gatheringPlace == null )
		{
			float gatheringPlaceX = x - getCollisionRay() - 10;
			float gatheringPlaceY = y + getCollisionRay() + 10;

			if( gatheringPlaceX <= 0 )
			{
				gatheringPlaceX = x + getCollisionRay() + 10;
			}
			if( gatheringPlaceY >= GameManager.getInstance().getWorld().getHeight() )
			{
				gatheringPlaceY = y - getCollisionRay() - 10;
			}

			gatheringPlace = new Vector2f( gatheringPlaceX, gatheringPlaceY );
		}
		return gatheringPlace;
	}

	private void calculateGatheringPlaces()
	{

		gatheringPlaces = new ArrayList<>();

		float collisionRayTower = GameProperties.getInstance().getCollisionRay( "tower" );

		gatheringPlaces.add( new Vector2f( x - getCollisionRay() - collisionRayTower / 2, y
				- getCollisionRay() - collisionRayTower / 2 ) );
		gatheringPlaces.add( new Vector2f( x + getCollisionRay() + collisionRayTower / 2, y
				+ getCollisionRay() + collisionRayTower / 2 ) );
		gatheringPlaces.add( new Vector2f( x + getCollisionRay() + collisionRayTower / 2, y
				- getCollisionRay() - collisionRayTower / 2 ) );
		gatheringPlaces.add( new Vector2f( x - getCollisionRay() - collisionRayTower / 2, y
				+ getCollisionRay() + collisionRayTower / 2 ) );

		Iterator<Vector2f> gatheringPlaceIterator = gatheringPlaces.iterator();
		while( gatheringPlaceIterator.hasNext() )
		{
			Vector2f gatheringPlace = gatheringPlaceIterator.next();
			if( ( gatheringPlace.x - collisionRayTower / 2 <= 0 )
					|| ( gatheringPlace.y - collisionRayTower / 2 <= 0 )
					|| ( gatheringPlace.x + collisionRayTower / 2 >= GameManager.getInstance()
							.getWorld().getWidth() )
					|| ( gatheringPlace.y + collisionRayTower / 2 >= GameManager.getInstance()
							.getWorld().getHeight() ) )
			{
				gatheringPlaceIterator.remove();
			}
		}

		for( Vector2f gatheringPlace : gatheringPlaces )
		{
			towers.put( gatheringPlace, null );
		}

	}

	public List<Vector2f> getAvaibleGatheringPlaces()
	{
		if( gatheringPlaces == null )
			calculateGatheringPlaces();
		ArrayList<Vector2f> avaibleGatheringPlaces = new ArrayList<>();
		Set<Vector2f> keySet = towers.keySet();
		for( Vector2f vector2f : keySet )
		{
			if( towers.get( vector2f ) == null )
			{
				avaibleGatheringPlaces.add( vector2f );
			}
		}
		return avaibleGatheringPlaces;
	}

	public void addTower( Tower tower, Vector2f position )
	{
		towers.put( position, tower );

	}

	public void removeTower( Vector2f position )
	{
		towers.put( position, null );

	}

	public void removeTower( Tower tower )
	{
		for( Vector2f towerPosition : towers.keySet() )
		{
			if( towers.get( towerPosition ) != null && towers.get( towerPosition ).equals( tower ) )
			{
				towers.put( towerPosition, null );
				return;

			}
		}
	}
}
