package mpa.core.ai;

import mpa.core.logic.GameManager;
import mpa.core.logic.character.Player;

abstract class AIState
{
	protected Player bully = null;

	protected AIState()
	{
	}

	void heIsAttackingYou( Player p )
	{
		bully = p;
	}

	abstract void action( OpponentAI opponentAI );

	AIState changeState( OpponentAI opponentAI )
	{
		boolean shouldIFight = false;
		Player prey = null;
		for( Player p : GameManager.getInstance().getPlayersAround( opponentAI.player,
				opponentAI.worldManager.ray ) )
		{
			if( !opponentAI.knownPlayers.contains( p ) )
				opponentAI.knownPlayers.add( p );
			if( opponentAI.isOpponentPlayerWeaker( p ) && !shouldIFight )
			{
				shouldIFight = true;
				prey = p;
			}

		}

		if( shouldIFight && this instanceof CombatState )
			return this;
		if( shouldIFight )
			return new CombatState( prey );
		if( bully != null )
			return new DefenseState( opponentAI, bully );

		return null;
	}
}
