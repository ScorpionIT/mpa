package mpa.core.ai;

import mpa.core.logic.character.Player;

class ProductionState extends AIState
{
	private boolean isWalking = false;

	ProductionState()
	{
		super();
	}

	@Override
	void action( OpponentAI opponentAI )
	{
		Player p = opponentAI.player;

	}

	@Override
	AIState changeState( OpponentAI opponentAI )
	{
		AIState nextState = null;

		if( isWalking || opponentAI.player.canBuyPotions() )
			nextState = this;
		else if( !opponentAI.knownAllTheWorld )
			nextState = new ExplorationState();
		else if( opponentAI.areThereWeakerPlayers() )
			nextState = new CombatState();
		else if( !opponentAI.knownBuildings.isEmpty() && opponentAI.areThereConquerableBuildings() )
			nextState = new ConquestState();
		else
			nextState = new WaitingState();

		return nextState;
	}
}
