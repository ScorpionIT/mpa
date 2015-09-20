package mpa.core.logic.building;

import javax.vecmath.Vector2f;

import mpa.core.logic.characters.Player;

public class Headquarter extends House
{
	private Vector2f collectionPoint;

	public Headquarter( float x, float y, Player player )
	{
		super( x, y, player );
	}

	public void provideOwner()
	{
		owner.putResources( "wheat", 3 + owner.getPlayerLevel().ordinal() );
		owner.putResources( "herbs", 1 + owner.getPlayerLevel().ordinal() );
		owner.putResources( "stone", 3 + owner.getPlayerLevel().ordinal() );
		owner.putResources( "iron", 2 + owner.getPlayerLevel().ordinal() );
		owner.putResources( "wood", 4 + owner.getPlayerLevel().ordinal() );
	}
}
