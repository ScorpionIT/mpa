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

		System.out.println( "Conquest State" );
		if( buildingToOccupy != null && walking )
		{
			Vector2f gathPlace = buildingToOccupy.getGatheringPlace();
			if( ( ( int ) player.getX() ) == ( ( int ) gathPlace.x )
					&& ( ( int ) player.getY() ) == ( ( int ) gathPlace.y ) )
			{
				System.out.println( "sono dentro l'if cazzuto " );
				GameManager.getInstance().occupyProperty( player, buildingToOccupy );
				walking = false;
				buildingToOccupy = null;
			}
			else
				return;
		}

		if( !player.isThereAnyFreeSulbaltern() )
			return;

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
			walking = true;
		}
		else if( _building != null )
		{
			buildingToOccupy = _building;
			Vector2f gatheringPlace = _building.getGatheringPlace();
			GameManager.getInstance().computePath( player, gatheringPlace.x, gatheringPlace.y );
			walking = true;
		}
	}

	@Override
	AIState changeState( OpponentAI opponentAI )
	{
		AIState nextState = null;
		if( walking && buildingToOccupy != null )
			nextState = this;
		else if( !opponentAI.knownAllTheWorld )
			nextState = new ExplorationState();
		else if( opponentAI.player.canUpgrade() )
			nextState = new StrengtheningState();
		else
			nextState = new WaitingState();
		// else if( opponentAI.player.canBuyPotions() )
		// nextState = new ProductionState();

		// else if( opponentAI.areThereWeakerPlayers() )
		// nextState = new CombatState();
		// else if( !opponentAI.knownAllTheWorld )
		// nextState = new ExplorationState();
		// else
		// nextState = new WaitingState();

		return nextState;
	}
}
