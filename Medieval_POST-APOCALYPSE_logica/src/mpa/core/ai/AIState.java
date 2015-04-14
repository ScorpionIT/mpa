package mpa.core.ai;

abstract class AIState
{
	protected AIState()
	{
	}

	abstract void action( OpponentAI opponentAI );

	abstract AIState changeState(OpponentAI opponentAI );

}
