package mpa.core.ai;

import javax.vecmath.Vector2f;

import mpa.core.logic.GameManager;
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
		Vector2f gatheringPlace = opponentAI.player.getHeadquarter().getGatheringPlace();
		Player player = opponentAI.player;
		Vector2f position = player.getPosition();

		if( !isWalking && !position.equals( gatheringPlace ) )
		{
			GameManager.getInstance().computePath( opponentAI.player, gatheringPlace.x,
					gatheringPlace.y );
			isWalking = true;
		}
		else if( isWalking && !position.equals( gatheringPlace ) )
		{
			return;
		}
		else if( position.equals( gatheringPlace ) )
		{

		}

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
