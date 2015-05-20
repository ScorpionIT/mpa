package mpa.core.ai;

class WaitingState extends AIState
{

	public WaitingState()
	{
	}

	@Override
	void action( OpponentAI opponentAI )
	{
		System.out.println( "Waiting State " );
		try
		{
			Thread.sleep( 500 );
		} catch( InterruptedException e )
		{
			e.printStackTrace();
		}
	}

	@Override
	AIState changeState( OpponentAI opponentAI )
	{
		AIState nextState = null;
		if( !opponentAI.knownAllTheWorld )
			nextState = new ExplorationState();
		else if( !opponentAI.knownBuildings.isEmpty() && opponentAI.areThereConquerableBuildings()
				&& opponentAI.player.isThereAnyFreeSulbaltern() )
			nextState = new ConquestState();
		else if( opponentAI.player.canUpgrade() || opponentAI.player.canBuyPotions() )
			nextState = new ProductionState();
		else
			nextState = this;

		// else if( opponentAI.areThereWeakerPlayers() )
		// nextState = new CombatState();
		// else if( !opponentAI.knownAllTheWorld )
		// nextState = new ExplorationState();
		// else
		// nextState = new WaitingState();

		return nextState;
	}

}
