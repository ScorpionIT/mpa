package mpa.core.logic.building;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Vector2f;

import mpa.core.logic.GameManager;
import mpa.core.logic.characters.AbstractCharacter;
import mpa.core.logic.characters.Minion;
import mpa.core.logic.characters.Player;
import mpa.core.logic.characters.TowerCrusher;
import mpa.core.logic.resources.Resources;
import mpa.core.maths.MyMath;

public class Tower extends AbstractPrivateProperty
{

	private int damage = 5;
	private int life = 50;
	private float range = 20f;
	private static Map<Resources, Integer> PRICE = new HashMap<>();
	private AbstractPrivateProperty property = null;

	public Tower( float x, float y, float width, float height, Player owner,
			AbstractPrivateProperty abstractPrivateProperty )
	{
		super( x, y, width, height, owner );
		this.property = abstractPrivateProperty;

		PRICE.put( Resources.IRON, 100 );
		PRICE.put( Resources.STONE, 120 );
		PRICE.put( Resources.WOOD, 150 );
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

	public ArrayList<AbstractCharacter> attack()
	{
		try
		{
			readLock.lock();
			ArrayList<AbstractCharacter> hitCharacters = new ArrayList<>();

			GameManager.getInstance().takeLock();

			for( Player p : GameManager.getInstance().getPlayers() )
			{
				if( p != owner
						&& MyMath.distanceFloat( x, y, p.getX(), p.getY() ) <= range
								+ owner.getPlayerLevel().ordinal() )
					hitCharacters.add( p );
			}

			for( Minion m : GameManager.getInstance().getMinionsAlive() )
			{
				if( m.getBoss() != owner
						&& MyMath.distanceFloat( x, y, m.getX(), m.getY() ) <= range
								+ owner.getPlayerLevel().ordinal() )
				{
					hitCharacters.add( m );
				}
			}

			for( TowerCrusher t : GameManager.getInstance().getTowerCrushers() )
			{
				if( t.getBoss() != owner
						&& MyMath.distanceFloat( x, y, t.getX(), t.getY() ) <= range
								+ owner.getPlayerLevel().ordinal() )
					hitCharacters.add( t );
			}

			return hitCharacters;
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

	@Override
	public ArrayList<Vector2f> getAvaibleGatheringPlaces()
	{
		return null;
	}

	public int getLife()
	{
		return life;
	}

	public AbstractProperty getProperty()
	{
		return property;
	}
}
