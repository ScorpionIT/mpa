package mpa.core.ai;

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
		if( bully != null )
			return new DefenseState( opponentAI, bully );

		return null;
	}

}
