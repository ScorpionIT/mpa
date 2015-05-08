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
	private Vector2f pointToReach = null;

	ExplorationState()
	{
		super();
	}

	private void addFoundBuildings( Vector2f player, float ray, OpponentAI opponentAI )
	{
		ArrayList<AbstractObject> objectsInTheRange = GameManager
				.getInstance()
				.getWorld()
				.getObjectsInTheRange( player.x - ray, player.x + ray, player.y - ray,
						player.y + ray );

		if( !objectsInTheRange.isEmpty() )
		{

			for( AbstractObject abstractObject : objectsInTheRange )
			{
				opponentAI.addBuilding( abstractObject );
				// System.out.println( "ho aggiunto un edificio" );
				// System.out.println();
				// System.out.println();
				// System.out.println();
			}

		}

	}

	@Override
	void action( OpponentAI opponentAI )
	{
		Player p = opponentAI.player;
		float playerX = p.getX();
		float playerY = p.getY();
		float ray = opponentAI.worldManager.ray;

		if( pointToReach != null )
		{
			System.out.println( "player " + p + " in: " + playerX + ", " + playerY + "; "
					+ pointToReach.x + ", " + pointToReach.y );
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();

			if( pointToReach.x == playerX && pointToReach.y == playerY || p.getPath().isEmpty() )
			{
				addFoundBuildings( new Vector2f( playerX, playerY ), ray, opponentAI );
				// opponentAI.worldManager.pointReached();

				pointToReach = opponentAI.worldManager.getNextLocation( opponentAI.player );
				if( pointToReach != null )
				{
					isWalking = true;

					while( Math.abs( pointToReach.x - p.getX() ) <= 2
							&& Math.abs( pointToReach.y - p.getY() ) <= 2 )
					{
						addFoundBuildings( new Vector2f( playerX, playerY ), ray, opponentAI );
						pointToReach = opponentAI.worldManager.getNextLocation( opponentAI.player );
					}
					GameManager.getInstance().computePath( opponentAI.player, pointToReach.x,
							pointToReach.y );
				}
				else
				{
					// opponentAI.worldManager.pointReached();
					opponentAI.knownAllTheWorld = true;
				}

			}
		}
		else
		{
			// opponentAI.worldManager.pointReached();
			pointToReach = opponentAI.worldManager.getNextLocation( opponentAI.player );
			if( pointToReach != null )
			{
				isWalking = true;

				while( Math.abs( pointToReach.x - p.getX() ) <= 2
						&& Math.abs( pointToReach.y - p.getY() ) <= 2 )
				{
					addFoundBuildings( new Vector2f( playerX, playerY ), ray, opponentAI );
					pointToReach = opponentAI.worldManager.getNextLocation( opponentAI.player );
				}
				GameManager.getInstance().computePath( opponentAI.player, pointToReach.x,
						pointToReach.y );

			}
			else
			{
				// opponentAI.worldManager.pointReached();
				opponentAI.knownAllTheWorld = true;
			}

		}
	}

	@Override
	AIState changeState( OpponentAI opponentAI )
	{
		AIState nextState = null;

		if( !opponentAI.knownBuildings.isEmpty() && opponentAI.areThereConquerableBuildings() )
			nextState = new ConquestState();

		// else if( opponentAI.player.canUpgrade() )
		// nextState = new StrengtheningState();
		// else if( opponentAI.player.canBuyPotions() )
		// nextState = new ProductionState();
		// else if( opponentAI.areThereWeakerPlayers() )
		// nextState = new CombatState();
		// else
		// nextState = new WaitingState();

		else if( !opponentAI.knownAllTheWorld )
			nextState = this;
		else
			nextState = new WaitingState();

		// if( nextState instanceof ExplorationState )
		// System.out.println( "ho scelto esplorazione " );
		// else
		// System.out.println( "ho scelto conquista" );
		// if( nextState instanceof ExplorationState )
		// System.out.println( "prox stato è Exp" );
		// else if( nextState instanceof ConquestState )
		// System.out.println( "prox stato è conq" );
		// else if( nextState instanceof WaitingState )
		// System.out.println( "prox stato è wait" );
		// System.out.println();
		// System.out.println();
		// System.out.println();
		// System.out.println();
		return this;
		// return nextState;
	}
}
