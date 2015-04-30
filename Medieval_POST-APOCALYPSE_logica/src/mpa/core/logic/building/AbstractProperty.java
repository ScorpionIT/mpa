package mpa.core.logic.building;

import javax.vecmath.Vector2f;

import mpa.core.logic.AbstractObject;

public class AbstractProperty extends AbstractObject
{

	protected Vector2f gatheringPlace;

	AbstractProperty( float x, float y, float width, float height )
	{
		super( x, y, width, height );

		gatheringPlace = new Vector2f( x + width / 2 + 10, y + height / 2 + 10 );

	}

	public Vector2f getGatheringPlace()
	{
		return gatheringPlace;
	}
}
