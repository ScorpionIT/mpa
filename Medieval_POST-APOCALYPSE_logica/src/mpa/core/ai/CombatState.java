package mpa.core.ai;

import java.util.Collections;

import javax.vecmath.Vector2f;

import mpa.core.logic.GameManager;
import mpa.core.logic.character.AbstractCharacter;
import mpa.core.logic.character.Player;
import mpa.core.logic.character.Player.Item;
import mpa.core.logic.fights.CombatManager;
import mpa.core.logic.tool.Potions;
import mpa.core.maths.MyMath;

class CombatState extends AIState
{
	private AbstractCharacter playerToAttack;
	private Vector2f pointToReach = null;
	int a = 0;

	CombatState()
	{
		super();
	}

	CombatState( AbstractCharacter playerToAttack )
	{
		this.playerToAttack = playerToAttack;
	}

	void changePrey( AbstractCharacter abs )
	{
		playerToAttack = abs;
	}

	private void computePath( Player me, AbstractCharacter him, boolean physicall )
	{
		Vector2f parallelVector = MyMath.computeDirection( me.getPosition(), him.getPosition() );
		float modulus = MyMath.distanceFloat( me.getX(), me.getY(), him.getX(), him.getY() );
		float actualDistance;

		if( physicall )
			actualDistance = modulus - me.getRangeOfPhysicallAttack() - 3;
		else
			actualDistance = modulus - me.getRangeOfDistanceAttack() - 3;

		pointToReach = new Vector2f( me.getX() + parallelVector.x * actualDistance, me.getY()
				+ parallelVector.y * actualDistance );

		GameManager.getInstance().computePath( me, pointToReach.x, pointToReach.y );

	}

	@Override
	void action( OpponentAI opponentAI )
	{
		// System.out.println( "sono nel COMBAT STATE" );

		if( playerToAttack != null && !playerToAttack.amIAlive() )
			playerToAttack = null;
		if( playerToAttack == null )
		{
			float distance = Float.MAX_VALUE;

			if( bullies.isEmpty() )
			{
				for( Player opponent : opponentAI.knownPlayers )
				{
					if( opponent == opponentAI.player )
						continue;
					float distance2 = MyMath.distanceFloat( opponentAI.player.getX(),
							opponentAI.player.getY(), opponent.getX(), opponent.getY() );
					if( ( opponentAI.player.getPlayerLevel().ordinal() >= opponent.getPlayerLevel()
							.ordinal() ) && distance2 < distance )
					{
						distance = distance2;
						playerToAttack = opponent;
					}
				}
			}
			else
			{
				for( Enemy opponent : bullies )
				{
					float distance2 = MyMath.distanceFloat( opponentAI.player.getX(),
							opponentAI.player.getY(), opponent.getEnemy().getX(), opponent
									.getEnemy().getY() );
					if( distance2 < distance )
					{
						distance = distance2;
						playerToAttack = opponent.getEnemy();
					}
				}
			}

		}
		cleanBulliesList();
		if( playerToAttack == null )
			return;
		else if( !playerToAttack.amIAlive() && !bullies.isEmpty() )
		{

			Collections.sort( bullies );
			playerToAttack = bullies.remove( 0 ).getEnemy();
			pointToReach = null;
		}

		if( pointToReach == null )
		{
			float distanceBetweenUs = MyMath.distanceFloat( opponentAI.player.getPosition(),
					playerToAttack.getPosition() );
			if( distanceBetweenUs < AbstractCharacter.getPace() )
			{
				Vector2f direction = MyMath.computeDirection( opponentAI.player.getPosition(),
						playerToAttack.getPosition() );
				pointToReach = new Vector2f( opponentAI.player.getX() + direction.x
						* distanceBetweenUs / 2, distanceBetweenUs / 2 );
				opponentAI.player.stopMoving();
				opponentAI.player.setPosition( pointToReach );
			}
			else
			{
				computePath( opponentAI.player, playerToAttack, true );
			}

			return;
		}
		else
		{
			Vector2f currentPosition = playerToAttack.getPosition();

			if( MyMath.distanceFloat( currentPosition.x, currentPosition.y, pointToReach.x,
					pointToReach.y ) > Math.max( opponentAI.player.getRangeOfDistanceAttack(),
					opponentAI.player.getRangeOfPhysicallAttack() ) * 1.5 )
			{
				computePath( opponentAI.player, playerToAttack, true );
				return;

			}
			if( MyMath.distanceFloat( opponentAI.player.getX(), opponentAI.player.getY(),
					playerToAttack.getX(), playerToAttack.getY() ) <= opponentAI.player
					.getRangeOfDistanceAttack() )
			{
				opponentAI.player.stopMoving();
				opponentAI.player.setSelectedItem( Item.GRANADE );
				GameManager.getInstance().playerAction( opponentAI.player,
						playerToAttack.getPosition() );
			}
			else if( MyMath.distanceFloat( opponentAI.player.getX(), opponentAI.player.getY(),
					playerToAttack.getX(), playerToAttack.getY() ) <= opponentAI.player
					.getRangeOfPhysicallAttack() )
			{
				opponentAI.player.stopMoving();
				opponentAI.player.setSelectedItem( Item.WEAPON );
				GameManager.getInstance().playerAction( opponentAI.player,
						playerToAttack.getPosition() );
			}
			else
			{
				float distanceBetweenUs = MyMath.distanceFloat( opponentAI.player.getPosition(),
						playerToAttack.getPosition() );
				if( distanceBetweenUs < AbstractCharacter.getPace() )
				{
					Vector2f direction = MyMath.computeDirection( opponentAI.player.getPosition(),
							playerToAttack.getPosition() );
					pointToReach = new Vector2f( opponentAI.player.getX() + direction.x
							* distanceBetweenUs / 2, distanceBetweenUs / 2 );
					opponentAI.player.stopMoving();
					opponentAI.player.setPosition( pointToReach );
				}
				else
				{
					computePath( opponentAI.player, playerToAttack, true );
				}

			}

		}

	}

	@Override
	AIState changeState( OpponentAI opponentAI )
	{
		AIState nextState = super.changeState( opponentAI );

		if( nextState != null )
			return nextState;

		if( playerToAttack != null
				&& playerToAttack.amIAlive()
				&& ( opponentAI.player.getMP() >= Math.min( CombatManager.getInstance()
						.getMP_REQUIRED_FOR_DISTANCE_ATTACK(), CombatManager.getInstance()
						.getMP_REQUIRED_FOR_PHYSICALL_ATTACK() ) || opponentAI.player
						.getPotionAmount( Potions.MP ) > 0 ) )
			nextState = this;
		else if( opponentAI.player.canUpgrade() )
			nextState = new StrengtheningState();
		else if( opponentAI.shouldBuyPotions() && opponentAI.player.canBuyPotions()
				&& opponentAI.canGoToThisState( ProductionState.class ) )
			nextState = new ProductionState();
		else if( !opponentAI.knownBuildings.isEmpty() && opponentAI.areThereConquerableBuildings()
				&& opponentAI.player.isThereAnyFreeSulbaltern()
				&& opponentAI.canGoToThisState( ConquestState.class ) )
			nextState = new ConquestState();
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
