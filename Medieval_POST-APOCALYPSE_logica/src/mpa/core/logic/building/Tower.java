package mpa.core.logic.building;

import java.util.ArrayList;

import mpa.core.logic.GameManager;
import mpa.core.logic.character.Player;
import mpa.core.maths.MyMath;

public class Tower extends AbstractPrivateProperty
{

	private int damage = 5;
	private int life = 50;
	private float range = 20f;

	public Tower( float x, float y, float width, float height, Player owner )
	{
		super( x, y, width, height, owner );
	}

	public int getDamage()
	{
		try
		{
			readLock.lock();
			return damage + 2 * owner.getPlayerLevel().ordinal();
		} finally
		{
			readLock.unlock();
		}
	}

	public ArrayList<Player> attack()
	{
		try
		{
			readLock.lock();
			ArrayList<Player> hitPlayers = new ArrayList<>();

			GameManager.getInstance().takeLock();

			for( Player p : GameManager.getInstance().getPlayers() )
			{
				if( p != owner && MyMath.distanceFloat( x, y, p.getX(), p.getY() ) <= 0 )
					hitPlayers.add( p );
			}

			return hitPlayers;
		} finally
		{
			readLock.unlock();
		}
	}

	public boolean inflictDamage( int damage )
	{
		try
		{
			writeLock.lock();
			life -= damage - 2 * owner.getPlayerLevel().ordinal();

			if( life <= 0 )
				return true;

			return false;
		} finally
		{
			writeLock.unlock();
		}
	}
}
