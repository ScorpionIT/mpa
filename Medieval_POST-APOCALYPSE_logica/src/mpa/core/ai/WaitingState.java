package mpa.core.ai;

class WaitingState extends AIState
{

	public WaitingState()
	{
	}

	@Override
	void action( OpponentAI opponentAI )
	{
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
		return this;
	}

}
