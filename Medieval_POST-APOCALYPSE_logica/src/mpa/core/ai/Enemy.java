package mpa.core.ai;

import javax.vecmath.Vector2f;

import mpa.core.logic.characters.AbstractCharacter;
import mpa.core.logic.characters.Player;
import mpa.core.maths.MyMath;

public class Enemy implements Comparable<Enemy>
{
	AbstractCharacter mySelf;
	Player opponent;

	public Enemy( AbstractCharacter mySelf, Player opponent )
	{
		super();
		this.mySelf = mySelf;
		this.opponent = opponent;
	}

	public Vector2f getPosition()
	{
		return mySelf.getPosition();
	}

	public AbstractCharacter getEnemy()
	{
		return mySelf;
	}

	@Override
	public int compareTo( Enemy him )
	{
		float meToPlayer = MyMath.distanceFloat( mySelf.getPosition(), opponent.getPosition() );
		float himToPlayer = MyMath.distanceFloat( him.getPosition(), opponent.getPosition() );

		if( meToPlayer < himToPlayer )
			return -1;
		else if( meToPlayer > himToPlayer )
			return 1;
		else
			return 0;
	}
}
