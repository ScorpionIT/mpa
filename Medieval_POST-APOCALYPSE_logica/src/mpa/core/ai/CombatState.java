package mpa.core.ai;

import javax.vecmath.Vector2f;

import mpa.core.logic.GameManager;
import mpa.core.logic.character.Player;
import mpa.core.logic.character.Player.Item;
import mpa.core.logic.fights.CombatManager;
import mpa.core.logic.tool.Potions;
import mpa.core.maths.MyMath;

class CombatState extends AIState
{
	private Player playerToAttack;
	private Vector2f pointToReach = null;

	CombatState()
	{
		super();
	}

	CombatState( Player playerToAttack )
	{
		this.playerToAttack = playerToAttack;
	}

	@Override
	void action( OpponentAI opponentAI )
	{
		if( playerToAttack == null )
		{
			float distance = Float.MAX_VALUE;

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
			if( playerToAttack == null )
				return;
			else
				System.out.println( "sono " + opponentAI.player.getName() + " e sto per attaccare "
						+ playerToAttack.getName() );
		}

		if( pointToReach == null )
		{
			pointToReach = playerToAttack.getPosition();
			pointToReach.set( pointToReach.x - opponentAI.player.getRangeOfPhysicallAttack() / 2,
					pointToReach.y - opponentAI.player.getRangeOfPhysicallAttack() / 2 );
			GameManager.getInstance().computePath( opponentAI.player, pointToReach.x,
					pointToReach.y );
			return;

		}
		else
		{
			Vector2f currentPosition = playerToAttack.getPosition();

			if( MyMath.distanceFloat( currentPosition.x, currentPosition.y, pointToReach.x,
					pointToReach.y ) > Math.max( opponentAI.player.getRangeOfDistanceAttack(),
					opponentAI.player.getRangeOfPhysicallAttack() ) * 2 )
			{
				pointToReach = playerToAttack.getPosition();
				pointToReach.set( pointToReach.x - opponentAI.player.getRangeOfPhysicallAttack()
						/ 2, pointToReach.y - opponentAI.player.getRangeOfPhysicallAttack() / 2 );
				GameManager.getInstance().computePath( opponentAI.player, pointToReach.x,
						pointToReach.y );
				return;

			}
			if( MyMath.distanceFloat( opponentAI.player.getX(), opponentAI.player.getY(),
					playerToAttack.getX(), playerToAttack.getY() ) <= opponentAI.player
					.getRangeOfDistanceAttack() )
			{
				opponentAI.player.stopMoving();
				opponentAI.player.setDirection( MyMath.computeDirection(
						opponentAI.player.getPosition(), playerToAttack.getPosition() ) );
				opponentAI.player.setSelectedItem( Item.GRANADE );
				GameManager.getInstance().playerAction( opponentAI.player,
						opponentAI.player.getCurrentVector() );
			}
			if( MyMath.distanceFloat( opponentAI.player.getX(), opponentAI.player.getY(),
					playerToAttack.getX(), playerToAttack.getY() ) <= opponentAI.player
					.getRangeOfPhysicallAttack() )
			{
				opponentAI.player.stopMoving();
				opponentAI.player.setDirection( MyMath.computeDirection(
						opponentAI.player.getPosition(), playerToAttack.getPosition() ) );
				opponentAI.player.setSelectedItem( Item.WEAPON );
				GameManager.getInstance().playerAction( opponentAI.player,
						opponentAI.player.getCurrentVector() );
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
				&& playerToAttack.getHP() > 0
				&& ( opponentAI.player.getMP() >= Math.min( CombatManager.getInstance()
						.getMP_REQUIRED_FOR_DISTANCE_ATTACK(), CombatManager.getInstance()
						.getMP_REQUIRED_FOR_PHYSICALL_ATTACK() ) || opponentAI.player
						.getPotionAmount( Potions.MP ) > 0 ) )
			nextState = this;
		else if( opponentAI.player.canUpgrade() )
			nextState = new StrengtheningState();
		else if( opponentAI.shouldBuyPotions() && opponentAI.player.canBuyPotions() )
			nextState = new ProductionState();
		else if( !opponentAI.knownBuildings.isEmpty() && opponentAI.areThereConquerableBuildings()
				&& opponentAI.player.isThereAnyFreeSulbaltern() )
			nextState = new ConquestState();
		else if( !opponentAI.knownAllTheWorld )
			nextState = new ExplorationState();
		else
			nextState = new WaitingState();

		return nextState;
	}

}
