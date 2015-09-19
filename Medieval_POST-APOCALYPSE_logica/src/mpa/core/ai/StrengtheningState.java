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

		if( opponentAI.player.getName().equals( "Paola Maledetta 2" ) )
			System.out.println( "Strengthening State" );
		Player p = opponentAI.player;

		Vector2f collectionPoint = p.getHeadquarter().getGatheringPlace();
		if( /*
			 * ( p.getPath() == null || p.getPath().isEmpty() ) &&
			 */collectionPoint.equals( p.getPosition() ) )
		{
			if( p.canUpgrade() )
			{
				p.upgradeLevel();

				// System.out.println();
				// System.out.println();
				// System.out.println( "ho uppato di livello" );
				// System.out.println();
				// System.out.println();
			}
			isWalking = false;
		}
		else if( isWalking )
			return;
		else if( p.canUpgrade() )
		{
			GameManager.getInstance().computePath( p, collectionPoint.x, collectionPoint.y );
			isWalking = true;
		}

	}

	@Override
	AIState changeState( OpponentAI opponentAI )
	{
		AIState nextState = super.changeState( opponentAI );

		if( nextState != null )
			return nextState;

		if( opponentAI.player.canUpgrade() || isWalking )
			nextState = this;
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
		else if( !opponentAI.knownAllTheWorld
				&& opponentAI.canGoToThisState( ExplorationState.class ) )
			nextState = new ExplorationState();
		else
		{
			nextState = new WaitingState();
			opponentAI.resetStateCounters();
		}
		return nextState;
	}

}
