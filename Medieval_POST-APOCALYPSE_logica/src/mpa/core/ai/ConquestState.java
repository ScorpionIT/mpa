package mpa.core.ai;

import javax.vecmath.Vector2f;

import mpa.core.logic.GameManager;
import mpa.core.logic.building.AbstractPrivateProperty;
import mpa.core.logic.character.DependentCharacter;
import mpa.core.logic.character.Player;
import mpa.core.logic.resource.AbstractResourceProducer;
import mpa.core.logic.resource.Cave;
import mpa.core.logic.resource.Field;
import mpa.core.logic.resource.Mine;
import mpa.core.logic.resource.Wood;
import mpa.core.maths.MyMath;

public class ConquestState extends AIState
{
	private boolean walking = false;
	private AbstractPrivateProperty buildingToOccupy = null;

	public ConquestState()
	{
		super();
	}

	@Override
	void action( OpponentAI opponentAI )
	{
		Player player = opponentAI.player;

		// System.out.println( "action dell'exploration" );

		if( buildingToOccupy != null && walking )
		{
			// System.out.println( "sono dentro l'if " );
			Vector2f gathPlace = buildingToOccupy.getGatheringPlace();
			if( ( ( int ) player.getX() ) == ( ( int ) gathPlace.x )
					&& ( ( int ) player.getY() ) == ( ( int ) gathPlace.y ) )
			{
				System.out.println( "sono dentro l'if cazzuto " );
				if( GameManager.getInstance().occupyProperty( player, buildingToOccupy ) )
				{
					// System.out.println( "dovrei aver occupato!!" );
					// System.out.println();
					// System.out.println();
					// System.out.println();
					walking = false;
					buildingToOccupy = null;
				}
			}
			else
				return;
		}
		int caves = 0;
		int fields = 0;
		int mines = 0;
		int woods = 0;

		for( DependentCharacter dependentCharacter : player.getSubalterns() )
		{
			AbstractPrivateProperty building = dependentCharacter.getAbstractPrivateProperty();

			if( building != null )
			{
				if( building instanceof Wood )
					woods++;
				else if( building instanceof Field )
					fields++;
				else if( building instanceof Mine )
					mines++;
				else if( building instanceof Cave )
					caves++;
			}
		}

		Class<?> bestChoice = null;

		if( caves < fields )
			bestChoice = Cave.class;
		else if( fields < mines )
			bestChoice = Field.class;
		else if( mines < woods )
			bestChoice = Mine.class;
		else
			bestChoice = Wood.class;

		float shortestDistance = Float.MAX_VALUE;
		AbstractResourceProducer _building = null;
		float _distance = Float.MAX_VALUE;
		AbstractResourceProducer _building2 = null;

		for( AbstractResourceProducer building : opponentAI.getKnownResourceProducers() )
		{
			if( building.getOwner() == player )
				continue;

			float distance = MyMath.distanceFloat( player.getX(), player.getY(), building.getX(),
					building.getY() );

			if( distance < shortestDistance && !building.getClass().equals( bestChoice.getClass() ) )
			{
				_building = building;
				shortestDistance = distance;
			}
			else if( distance < _distance && building.getClass().equals( bestChoice.getClass() ) )
			{
				_building2 = building;
				_distance = distance;
			}

		}

		if( _building2 != null )
		{
			buildingToOccupy = _building2;
			Vector2f gatheringPlace = _building2.getGatheringPlace();
			GameManager.getInstance().computePath( player, gatheringPlace.x, gatheringPlace.y );
			// System.out.println( "ho calcolato il percorso? 1" );
			// System.err.println( "il nuovo posto sarebbe un " + _building2.getClass().getName()
			// + " in pos " + gatheringPlace.x + ", " + gatheringPlace.y );
			// System.out.println();
			walking = true;
		}
		else if( _building != null )
		{
			buildingToOccupy = _building;
			Vector2f gatheringPlace = _building.getGatheringPlace();
			GameManager.getInstance().computePath( player, gatheringPlace.x, gatheringPlace.y );
			// System.out.println( "ho calcolato il percorso? 2" );
			// System.err.println( "il nuovo posto sarebbe un " + _building.getClass().getName()
			// + " in pos " + gatheringPlace.x + ", " + gatheringPlace.y );
			// System.out.println();
			walking = true;
		}
	}

	@Override
	AIState changeState( OpponentAI opponentAI )
	{
		AIState nextState = null;
		if( walking && buildingToOccupy != null )
			nextState = this;
		else
			nextState = new ExplorationState();
		// else if( opponentAI.player.canUpgrade() || opponentAI.player.canBuyPotions() )
		// nextState = new ProductionState();
		//
		// else if( opponentAI.areThereWeakerPlayers() )
		// nextState = new CombatState();
		// else if( !opponentAI.knownAllTheWorld )
		// nextState = new ExplorationState();
		// else
		// nextState = new WaitingState();

		return nextState;
	}
}
