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
		if( opponentAI.player.getName().equals( "Paola Maledetta 2" ) )
		{
			System.out.println( "Exploration State" );
			System.out.println( "da visitare ho ancora " + opponentAI.worldManager.openList.size() );
			System.out.println( "mentre ne ho visitati "
					+ opponentAI.worldManager.closedList.size() );
			if( pointToReach != null )
				System.out.println( "sto cercandoo di andare in  " + pointToReach.toString() );
		}
		Player p = opponentAI.player;
		float playerX = p.getX();
		float playerY = p.getY();

		if( pointToReach != null )
		{
			// if( isWalking && ( ( int ) pointToReach.x ) == ( ( int ) playerX )
			// && ( ( int ) pointToReach.y ) == ( ( int ) playerY ) )
			if( isWalking && pointToReach.equals( p.getPosition() ) )
			{
				opponentAI.addBuildings( pointToReach );

				// pointToReach = opponentAI.worldManager.getNextLocation( opponentAI.player );
				// if( opponentAI.player.getName().equals( "Paola Maledetta 2" ) )
				// {
				// System.out.println( "ho un nuovo punto da raggiungere "
				// + pointToReach.toString() );
				// }
				pointToReach = null;
				isWalking = false;
				// if( pointToReach != null )
				// {
				// GameManager.getInstance().computePath( opponentAI.player, pointToReach.x,
				// pointToReach.y );
				// isWalking = true;
				// }
				// else
				// {
				// opponentAI.knownAllTheWorld = true;
				// isWalking = false;
				// }

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
		AIState nextState = super.changeState( opponentAI );

		if( nextState != null )
			return nextState;

		if( isWalking )
			nextState = this;
		// if( opponentAI.player.canUpgrade() )
		// nextState = new StrengtheningState();
		// else if( opponentAI.shouldBuyPotions() && opponentAI.player.canBuyPotions() )
		// nextState = new ProductionState();
		// else if( !opponentAI.knownBuildings.isEmpty() &&
		// opponentAI.areThereConquerableBuildings()
		// && opponentAI.player.isThereAnyFreeSulbaltern() )
		// nextState = new ConquestState();
		else if( !opponentAI.knownAllTheWorld )
			nextState = this;
		else
			nextState = new WaitingState();

		// else if( opponentAI.areThereWeakerPlayers() )
		// nextState = new CombatState();

		return nextState;
	}
}
