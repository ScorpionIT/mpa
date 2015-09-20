package mpa.core.ai;

import java.util.List;

import javax.vecmath.Vector2f;

import mpa.core.logic.GameManager;
import mpa.core.logic.characters.Player;

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
			// System.out.println( "Exploration State" );
			// System.out.println( "da visitare ho ancora " +
			// opponentAI.worldManager.openList.size() );
			// System.out.println( "mentre ne ho visitati "
			// + opponentAI.worldManager.closedList.size() );
			if( pointToReach != null )
			{
				// System.out.println( "sto cercandoo di andare in  " + pointToReach.toString() );
				if( pointToReach.x == 500 && pointToReach.y == 300 )
				{
					List<Vector2f> path = opponentAI.player.getPath();
					// if( path != null )
					// System.out.println( "e la size del path è " + path.size() );
				}
			}
		}
		Player p = opponentAI.player;

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
			else if( isWalking && pointToReach != null && !pointToReach.equals( p.getPosition() )
					&& ( p.getPath() == null || p.getPath().isEmpty() ) )
			{
				// System.out.println( "sono " + opponentAI.player.getName()
				// + " e sto facendo bruttissimo" );
				pointToReach = opponentAI.worldManager.giveMeAnotherLocation( pointToReach,
						opponentAI.player );
				GameManager.getInstance().computePath( opponentAI.player, pointToReach.x,
						pointToReach.y );
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
				if( pointToReach.x == 500 && pointToReach.y == 300 )
				{
					// System.out.println( "sto calcolando il path per quel punto" );
					List<Vector2f> path = opponentAI.player.getPath();
					// if( path != null )
					// System.out.println( "e la size del path è " + path.size() );
				}
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
		else if( opponentAI.player.canUpgrade() )
			nextState = new StrengtheningState();
		else if( !opponentAI.knownBuildings.isEmpty() && opponentAI.areThereConquerableBuildings()
				&& opponentAI.player.isThereAnyFreeSulbaltern()
				&& opponentAI.canGoToThisState( ConquestState.class ) )
			nextState = new ConquestState();
		else if( opponentAI.shouldICreateTowers() && opponentAI.canIcreateTowers() )
			nextState = new FortificationState();
		else if( opponentAI.areThereWeakerPlayers() )
			nextState = new CombatState();
		else if( opponentAI.shouldBuyPotions() && opponentAI.player.canBuyPotions()
				&& opponentAI.canGoToThisState( ProductionState.class ) )
			nextState = new ProductionState();
		else if( !opponentAI.knownAllTheWorld )
			nextState = this;
		else
		{
			nextState = new WaitingState();
			opponentAI.resetStateCounters();
		}

		return nextState;
	}
}
