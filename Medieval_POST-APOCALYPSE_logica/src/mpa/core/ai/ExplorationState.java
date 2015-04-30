package mpa.core.ai;

import java.util.ArrayList;

import javax.vecmath.Vector2f;

import mpa.core.logic.AbstractObject;
import mpa.core.logic.GameManager;
import mpa.core.logic.character.Player;

class ExplorationState extends AIState
{
	private boolean newBuildingsAdded = false;
	private boolean isWalking = false;
	private Vector2f pointToReach;

	ExplorationState()
	{
		super();
	}

	@Override
	void action( OpponentAI opponentAI )
	{
		Player p = opponentAI.player;
		if( isWalking )
		{
			if( pointToReach.x == p.getX() && pointToReach.y == p.getY() )
			{
				isWalking = false;

				float playerX = p.getX();
				float playerY = p.getY();
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
			}
			else
				return;
		}
		else
		{

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

		if( isWalking || !opponentAI.knownAllTheWorld )
			nextState = this;
		// else if( !opponentAI.knownBuildings.isEmpty() &&
		// opponentAI.areThereConquerableBuildings() )
		// nextState = new ConquestState();
		// else if( opponentAI.player.canUpgrade() )
		// nextState = new StrengtheningState();
		// else if( opponentAI.player.canBuyPotions() )
		// nextState = new ProductionState();
		// else if( opponentAI.areThereWeakerPlayers() )
		// nextState = new CombatState();
		// else
		// nextState = new WaitingState();

		return nextState;
	}
}
