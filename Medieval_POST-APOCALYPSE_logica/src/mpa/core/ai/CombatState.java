package mpa.core.ai;

import java.util.List;

import javax.vecmath.Vector2f;

import mpa.core.logic.GameManager;
import mpa.core.logic.building.Tower;
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

	private void preRoutine( OpponentAI opponentAI )
	{
		if( playerToAttack instanceof Player && playerToAttack != null )
		{
			List<Tower> towers = ( ( Player ) opponentAI.player ).getTowers();
			Tower toDestroy = null;
			float minDistance = Float.MAX_VALUE;

			for( Tower t : towers )
			{
				float dinstaceOpponentToTower = MyMath.distanceFloat( playerToAttack.getPosition(),
						t.getPosition() );
				if( dinstaceOpponentToTower < minDistance )
				{
					minDistance = dinstaceOpponentToTower;
					toDestroy = t;
				}
			}
			while( !GameManager.getInstance().createTowerCrushers( opponentAI.player, toDestroy )
					.isEmpty() );

			while( !GameManager.getInstance()
					.createMinions( opponentAI.player, 1, ( ( Player ) playerToAttack ) ).isEmpty() );

		}

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
		if( playerToAttack != null && playerToAttack.amIAlive() )
		{
			// preRoutine( opponentAI );
			float distanceMeToHim = MyMath.distanceFloat( opponentAI.player.getPosition(),
					playerToAttack.getPosition() );

			if( distanceMeToHim < opponentAI.player.getRangeOfPhysicallAttack() )
			{
				opponentAI.player.stopMoving();
				GameManager.getInstance().changeSelectedItem( opponentAI.player, Item.WEAPON );
				GameManager.getInstance().playerAction( opponentAI.player,
						playerToAttack.getPosition() );
			}
			if( distanceMeToHim < opponentAI.player.getRangeOfDistanceAttack()
					&& opponentAI.player.getPotionAmount( Potions.GRANADE ) > 0 )
			{
				opponentAI.player.stopMoving();
				GameManager.getInstance().changeSelectedItem( opponentAI.player, Item.GRANADE );
				GameManager.getInstance().playerAction( opponentAI.player,
						playerToAttack.getPosition() );
			}

			if( pointToReach == null )
			{
				computePath( opponentAI.player, playerToAttack, true );
			}
			else
			{
				float distanceOldPositionToNew = MyMath.distanceFloat( pointToReach,
						playerToAttack.getPosition() );

				if( distanceOldPositionToNew > 20f )
					computePath( opponentAI.player, playerToAttack, true );
			}
		}
		else if( ( playerToAttack == null ) || ( !playerToAttack.amIAlive() ) )
		{
			if( !bullies.isEmpty() )
			{
				float minDistance = Float.MAX_VALUE;
				Enemy toAttack = null;
				for( Enemy enemy : bullies )
				{
					float distanceFromEnemy = MyMath.distanceFloat(
							opponentAI.player.getPosition(), enemy.getEnemy().getPosition() );
					if( distanceFromEnemy < minDistance )
					{
						minDistance = distanceFromEnemy;
						toAttack = enemy;
					}
				}
				if( toAttack != null )
				{
					playerToAttack = toAttack.getEnemy();
					computePath( opponentAI.player, playerToAttack, true );
					return;
				}
			}
			float minDistance = Float.MAX_VALUE;
			for( Player p : opponentAI.knownPlayers )
			{
				if( p == opponentAI.player )
					continue;
				float distanceMeToHim = MyMath.distanceFloat( opponentAI.player.getPosition(),
						p.getPosition() );
				if( distanceMeToHim < minDistance )
				{
					minDistance = distanceMeToHim;
					playerToAttack = p;
				}

			}
			if( playerToAttack != null )
				computePath( opponentAI.player, playerToAttack, true );
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
		else if( opponentAI.shouldICreateTowers() && opponentAI.canIcreateTowers() )
			nextState = new FortificationState();
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
