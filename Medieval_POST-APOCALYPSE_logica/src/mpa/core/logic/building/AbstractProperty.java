package mpa.core.logic.building;

import javax.vecmath.Vector2f;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.GameManager;

public class AbstractProperty extends AbstractObject
{

	protected Vector2f gatheringPlace = null;

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
}
