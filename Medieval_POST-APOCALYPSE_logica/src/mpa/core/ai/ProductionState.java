package mpa.core.ai;

import mpa.core.logic.character.Player;
import mpa.core.maths.MyMath;

class ProductionState extends AIState
{

	ProductionState( Player player )
	{
		super( player );
	}

	@Override
	protected void action()
	{
		/*
		 * if( enough resources to raise the level and current level is not the maximum level )
		 * player.raiseLevel(); else { while( enough resources to make potions ) if(
		 * player.getNumberOfDamagePotions > player.getNumberOfHPPotions +
		 * player.getNumberOfMPPotions ) player.makeDamagePotions(); else if(
		 * player.getNumberOfHPPotions > player.getNumberOfMPPotions ) player.makeHPPotions(); else
		 * player.makeMPPotions(); }
		 */
	}

	@Override
	protected AIState changeState()
	{
		float distance = Float.MAX_VALUE;
		Player playerToAttack = null;

		for( Player opponent : player.getKnownPlayers() )
		{
			float distance2 = MyMath.distanceFloat( player.getX(), player.getY(), opponent.getX(),
					opponent.getY() );
			if( ( player.getPlayerLevel().ordinal() > opponent.getPlayerLevel().ordinal() && ( playerToAttack == null || playerToAttack
					.getPlayerLevel().ordinal() < opponent.getPlayerLevel().ordinal() ) )
					&& distance2 < distance )
			{
				distance = distance2;
				playerToAttack = opponent;
			}
		}

		if( playerToAttack != null )
			return new CombatState( player, playerToAttack );
		else
			return new ExplorationState( player );

	}
}
