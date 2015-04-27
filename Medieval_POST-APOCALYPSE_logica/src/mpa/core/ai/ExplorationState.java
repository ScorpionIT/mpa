package mpa.core.ai;

import java.util.ArrayList;

import javax.vecmath.Vector2f;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.GameManager;

class ExplorationState extends AIState
{
	boolean newBuildingsAdded = false;

	ExplorationState()
	{
		super();
	}

	@Override
	void action( OpponentAI opponentAI )
	{
		if( opponentAI.player.getPath().isEmpty() )
		{
			float playerX = opponentAI.player.getX();
			float playerY = opponentAI.player.getY();
			float ray = opponentAI.worldManager.ray;
			ArrayList<AbstractObject> objectsInTheRange = GameManager
					.getInstance()
					.getWorld()
					.getObjectsInTheRange( playerX - ray, playerX + ray, playerY - ray,
							playerY + ray );

			if( !objectsInTheRange.isEmpty() )
			{

				for( AbstractObject abstractObject : objectsInTheRange )
					opponentAI.addBuilding( abstractObject );

				newBuildingsAdded = true;
			}

			Vector2f goal = opponentAI.worldManager.getNextLocation( opponentAI.player );
			if( goal != null )
				GameManager.getInstance().computePath( opponentAI.player, goal.x, goal.y );
			else
				opponentAI.knownAllTheWorld = true;
		}
	}

	@Override
	AIState changeState( OpponentAI opponentAI )
	{
		AIState nextState = null;

		// if( newBuildingsAdded && opponentAI.player.isThereAnyFreeSulbaltern() )
		if( !opponentAI.knownBuildings.isEmpty() && opponentAI.areThereConquerableBuildings() )
		{
			nextState = new ConquestState();
		}
		else if( opponentAI.player.canUpgrade() || opponentAI.player.canBuyPotions() )
		{
			nextState = new ProductionState();
		}
		else if( opponentAI.areThereWeakerPlayers() )
		{
			nextState = new CombatState();
		}
		else if( !opponentAI.knownAllTheWorld )
		{
			nextState = this;
		}
		else
			nextState = new WaitingState();

		return nextState;

	}
}
