package mpa.core.ai;

import mpa.core.logic.character.Player;

abstract class AIState
{
	protected Player player;

	protected AIState( Player player )
	{
		this.player = player;
	}

	abstract protected void action();

	abstract protected AIState changeState();

}
