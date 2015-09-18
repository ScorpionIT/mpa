package mpa.core.ai;

import java.util.ArrayList;

import mpa.core.logic.GameManager;
import mpa.core.logic.character.AbstractCharacter;
import mpa.core.logic.character.Minion;
import mpa.core.logic.character.Player;

abstract class AIState
{
	protected AbstractCharacter bully = null;
	protected ArrayList<Enemy> bullies = new ArrayList<>();

	protected AIState()
	{
	}

	void heIsAttackingYou( AbstractCharacter p, Player mySelf )
	{
		bullies.add( 0, new Enemy( p, mySelf ) );
	}

	protected void cleanBulliesList()
	{
		ArrayList<Enemy> deadEnemies = new ArrayList<>();
		for( Enemy enemy : bullies )
			if( !enemy.getEnemy().amIAlive() )
				deadEnemies.add( enemy );

		for( Enemy enemy : deadEnemies )
			bullies.remove( enemy );
	}

	abstract void action( OpponentAI opponentAI );

	AIState changeState( OpponentAI opponentAI )
	{
		boolean shouldIFight = false;
		Player prey = null;

		for( Player p : GameManager.getInstance().getPlayersAround( opponentAI.player,
				opponentAI.worldManager.ray ) )
		{
			if( !opponentAI.knownPlayers.contains( p ) )
				opponentAI.knownPlayers.add( p );
			if( opponentAI.isOpponentPlayerWeaker( p ) && !shouldIFight )
			{
				shouldIFight = true;
				prey = p;
				bullies.add( new Enemy( prey, opponentAI.player ) );
				break;
			}

		}

		for( Minion m : GameManager.getInstance().getMinionsAlive() )
		{
			bullies.add( new Enemy( m, opponentAI.player ) );
			shouldIFight = true;
		}

		if( shouldIFight && this instanceof CombatState )
		{
			( ( CombatState ) this ).changePrey( prey );
			return this;
		}
		if( shouldIFight )
		{
			return new CombatState( prey );
		}

		if( bullies != null && !bullies.isEmpty() )
			return new DefenseState( opponentAI, bullies );

		return null;
	}
}
