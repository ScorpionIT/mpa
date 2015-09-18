package mpa.core.ai;

import java.util.ArrayList;

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
		// if( opponentAI.player.getName().equals( "Paola Maledetta 2" ) )
		// System.out.println( "Conquest State" );
		if( buildingToOccupy != null && walking )
		{
			Vector2f gathPlace = buildingToOccupy.getGatheringPlace();
			if( ( ( int ) player.getX() ) == ( ( int ) gathPlace.x )
					&& ( ( int ) player.getY() ) == ( ( int ) gathPlace.y ) )
			{
				// System.out.println( "sono dentro l'if cazzuto " );
				GameManager.getInstance().occupyProperty( player, buildingToOccupy );
				walking = false;
				buildingToOccupy = null;
			}

			return;
		}

		final int caves = 0;
		final int fields = 1;
		final int mines = 2;
		final int woods = 3;

		int[] resourcesAmount = new int[4];

		for( DependentCharacter dependentCharacter : player.getSubalterns() )
		{
			AbstractPrivateProperty building = dependentCharacter.getAbstractPrivateProperty();

			if( building != null )
			{
				if( building instanceof Wood )
					resourcesAmount[woods]++;
				else if( building instanceof Field )
					resourcesAmount[fields]++;
				else if( building instanceof Mine )
					resourcesAmount[mines]++;
				else if( building instanceof Cave )
					resourcesAmount[caves]++;
			}
		}

		Class<?> bestChoice = null;
		int minAmount = Integer.MAX_VALUE;
		int indexBestChoice = 0;

		for( int i = 0; i < 4; i++ )
			if( resourcesAmount[i] < minAmount )
			{
				minAmount = resourcesAmount[i];
				indexBestChoice = i;
				if( resourcesAmount[i] == 0 && i == fields )
					break;
			}

		int maxAmount = 0;
		int resourceToFreeIndex = 0;
		Class<?> resourceToFree = null;
		for( int i = 0; i < 4; i++ )
			if( resourcesAmount[i] > maxAmount )
			{
				maxAmount = resourcesAmount[i];
				resourceToFreeIndex = i;
			}

		switch( indexBestChoice )
		{
			case caves:
				bestChoice = Cave.class;
				break;
			case fields:
				bestChoice = Field.class;
				break;
			case mines:
				bestChoice = Mine.class;
				break;
			case woods:
				bestChoice = Wood.class;
				break;
		}

		switch( resourceToFreeIndex )
		{
			case caves:
				resourceToFree = Cave.class;
				break;
			case fields:
				resourceToFree = Field.class;
				break;
			case mines:
				resourceToFree = Mine.class;
				break;
			case woods:
				resourceToFree = Wood.class;
				break;
		}
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

		if( !player.isThereAnyFreeSulbaltern() )
		{
			if( minAmount == 0 )
			{
				ArrayList<DependentCharacter> subalterns = player.getSubalterns();

				for( DependentCharacter dC : subalterns )
				{
					AbstractPrivateProperty privateProperty = dC.getAbstractPrivateProperty();
					if( privateProperty != null
							&& privateProperty.getClass().equals( resourceToFree.getClass() ) )
					{
						player.freeSubaltern( dC );
						break;
					}
				}
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
		else if( !opponentAI.player.getFreeSubalterns().isEmpty() )
		{
			int min = Integer.MAX_VALUE;
			AbstractPrivateProperty chosen = null;
			for( AbstractPrivateProperty abstractPrivateProperty : opponentAI.player
					.getProperties() )
			{
				if( opponentAI.player.getName().equals( "Paola Maledetta 2" ) )
				{
					// System.out.println( "il numero di edifici conquistati è "
					// + opponentAI.player.getProperties().size() );
					// System.out.println( "il mio chosen per il momento è " + chosen );
				}
				if( abstractPrivateProperty.getNumberOfControllers() < min )
				{
					chosen = abstractPrivateProperty;
					min = abstractPrivateProperty.getNumberOfControllers();
				}
			}
			if( chosen != null )
			{
				if( opponentAI.player.getName().equals( "Paola Maledetta 2" ) )
				{
					// System.out.println( "alla fine ho scelto " + chosen );
				}
				GameManager.getInstance().addWorker( opponentAI.player, chosen );
			}
		}
	}

	@Override
	AIState changeState( OpponentAI opponentAI )
	{
		AIState nextState = super.changeState( opponentAI );

		if( nextState != null )
			return nextState;

		if( walking && buildingToOccupy != null )
			nextState = this;
		else if( opponentAI.player.canUpgrade() )
			nextState = new StrengtheningState();
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
			opponentAI.resetStateCounters();
			nextState = new WaitingState();
		}
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
