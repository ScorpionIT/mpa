package mpa.core.ai;

import javax.vecmath.Vector2f;

import mpa.core.logic.GameManager;
import mpa.core.logic.character.Player;

class StrengtheningState extends AIState
{
	private boolean isWalking = false;

	public StrengtheningState()
	{
		super();
	}

	@Override
	void action( OpponentAI opponentAI )
	{
		Player p = opponentAI.player;
		try
		{
			p.getWriteLock();

			if( isWalking )
				return;
			Vector2f collectionPoint = p.getHeadquarter().getCollectionPoint();
			if( p.getX() == collectionPoint.x && p.getY() == collectionPoint.y )
			{
				if( p.canUpgrade() )
					p.upgradeLevel();
			}

			else if( p.canUpgrade() )
			{
				GameManager.getInstance().computePath( p, collectionPoint.x, collectionPoint.y );
				isWalking = true;
			}
		} finally
		{
			p.leaveWriteLock();
		}
	}

	@Override
	AIState changeState( OpponentAI opponentAI )
	{
		AIState nextState = null;

		if( opponentAI.player.canUpgrade() )
			nextState = this;
		else if( opponentAI.player.canBuyPotions() )
			nextState = new ProductionState();
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
