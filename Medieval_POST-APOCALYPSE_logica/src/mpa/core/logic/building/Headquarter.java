package mpa.core.logic.building;

import javax.vecmath.Vector2f;

import mpa.core.logic.character.Player;

public class Headquarter extends House
{
	private Vector2f collectionPoint;

	public Headquarter( float x, float y, Player player )
	{
		super( x, y, player );
	}

	public Vector2f getCollectionPoint()
	{
		return collectionPoint;
	}
}
