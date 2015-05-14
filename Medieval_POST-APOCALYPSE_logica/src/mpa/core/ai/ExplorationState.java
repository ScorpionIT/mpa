package mpa.core.ai;

import javax.vecmath.Vector2f;

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

	@Override
	void action( OpponentAI opponentAI )
	{
		Player p = opponentAI.player;
		float playerX = p.getX();
		float playerY = p.getY();

		if( pointToReach != null )
		{
			// if( pointToReach.x == playerX && pointToReach.y == playerY || p.getPath() == null
			// || p.getPath().isEmpty() )
			// System.out.println( "sono entrato nel primo if " );
			// System.out.println( ( ( int ) pointToReach.x ) + "=?" + ( ( int ) playerX ) + "; "
			// + ( ( int ) pointToReach.y ) + "=?" + ( ( int ) playerY ) );

			if( isWalking && ( ( int ) pointToReach.x ) == ( ( int ) playerX )
					&& ( ( int ) pointToReach.y ) == ( ( int ) playerY ) )
			{
				// System.out.println( "sono nel secondo if?!?!" );
				opponentAI.addBuildings( pointToReach );

				pointToReach = opponentAI.worldManager.getNextLocation( opponentAI.player );
				if( pointToReach != null )
				{
					GameManager.getInstance().computePath( opponentAI.player, pointToReach.x,
							pointToReach.y );
					isWalking = true;
				}
				else
				{
					opponentAI.knownAllTheWorld = true;
				}

			}
		}
		else
		{
			pointToReach = opponentAI.worldManager.getNextLocation( opponentAI.player );
			if( pointToReach != null )
			{
				isWalking = true;

				GameManager.getInstance().computePath( opponentAI.player, pointToReach.x,
						pointToReach.y );
			}
			else
			{
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
		// return this;
		return nextState;
	}
}
