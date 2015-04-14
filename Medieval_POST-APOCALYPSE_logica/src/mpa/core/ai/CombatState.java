package mpa.core.ai;

import mpa.core.logic.character.Player;

class CombatState extends AIState
{
	private Player playerToAttack;

	CombatState( Player player, Player playerToAttack )
	{
		super( player );
		this.playerToAttack = playerToAttack;
	}

	@Override
	protected void action()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected AIState changeState()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
