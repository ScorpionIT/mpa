package mpa.core.ai;

import mpa.core.logic.character.Player;
import mpa.core.maths.MyMath;

class CombatState extends AIState
{
	private Player playerToAttack;

	CombatState()
	{
		super();
	}

	@Override void action( OpponentAI opponentAI )
	{
		if( playerToAttack == null )
		{
			float distance = Float.MAX_VALUE;
			Player playerToAttack = null;

			for( Player opponent : opponentAI.knownPlayers )
			{
				float distance2 = MyMath.distanceFloat( opponentAI.player.getX(),
						opponentAI.player.getY(), opponent.getX(), opponent.getY() );
				if( ( opponentAI.player.getPlayerLevel().ordinal() > opponent.getPlayerLevel()
						.ordinal() && ( playerToAttack == null || playerToAttack.getPlayerLevel()
						.ordinal() < opponent.getPlayerLevel().ordinal() ) )
						&& distance2 < distance )
				{
					distance = distance2;
					playerToAttack = opponent;
				}
			}
		}
	}

	@Override AIState changeState( OpponentAI opponentAI )
	{
		// TODO Auto-generated method stub
		return null;
	}

}
