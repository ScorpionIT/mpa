package mpa.core.ai;

import javax.vecmath.Vector2f;

import mpa.core.logic.GameManager;
import mpa.core.logic.characters.Player;
import mpa.core.logic.potions.Potions;

class ProductionState extends AIState
{
	private boolean isWalking = false;
	private boolean imOk = false;

	ProductionState()
	{
		super();
	}

	Potions whatShouldIProduce( Player p )
	{
		int[] potions = new int[Potions.values().length];
		/* i = 1 => HP, 2 => MP, 3 => GRANADE, 4 => flash bang */
		potions[0] = p.getPotionAmount( Potions.HP );
		potions[1] = p.getPotionAmount( Potions.MP );
		potions[2] = p.getPotionAmount( Potions.GRANADE );
		potions[3] = p.getPotionAmount( Potions.FLASH_BANG );

		int index = 0;
		int min = Integer.MAX_VALUE;
		// System.out.print( "possiedo al momento " );
		for( int i = 0; i < potions.length; i++ )
		{
			if( potions[i] == 0 )
			{
				index = i;
				min = 0;
				break;
			}
			else if( potions[i] < min )
			{
				min = potions[i];
				index = i;
			}
		}

		switch( index )
		{
			case 0:
				if( p.getPlayerLevel().canBuy( Potions.HP, p.getPotionAmount( Potions.HP ) + 1 ) )
					return Potions.HP;
			case 1:
				if( p.getPlayerLevel().canBuy( Potions.MP, p.getPotionAmount( Potions.MP ) + 1 ) )
					return Potions.MP;
			case 2:
				if( p.getPlayerLevel().canBuy( Potions.GRANADE,
						p.getPotionAmount( Potions.GRANADE ) + 1 ) )
					return Potions.GRANADE;
			case 3:
				if( p.getPlayerLevel().canBuy( Potions.FLASH_BANG,
						p.getPotionAmount( Potions.FLASH_BANG ) + 1 ) )
					return Potions.FLASH_BANG;

			default:
				imOk = true;
				return null;
		}
	}

	@Override
	void action( OpponentAI opponentAI )
	{
		if( opponentAI.player.getName().equals( "Paola Maledetta 2" ) )
			System.out.println( "Production state" );
		Vector2f gatheringPlace = opponentAI.player.getHeadquarter().getGatheringPlace();
		Player player = opponentAI.player;
		Vector2f position = player.getPosition();

		if( !isWalking && !position.equals( gatheringPlace ) )
		{
			GameManager.getInstance().computePath( opponentAI.player, gatheringPlace.x,
					gatheringPlace.y );
			isWalking = true;
		}
		else if( isWalking && !position.equals( gatheringPlace ) )
		{
			return;
		}
		else if( position.equals( gatheringPlace ) )
		{
			Potions toProduce;

			while( ( toProduce = whatShouldIProduce( player ) ) != null )
			{
				if( !player.buyPotion( toProduce ) )
					break;
			}

			isWalking = false;
			imOk = true;

		}

	}

	@Override
	AIState changeState( OpponentAI opponentAI )
	{
		AIState nextState = super.changeState( opponentAI );

		if( nextState != null )
			return nextState;

		if( isWalking && !imOk )
			nextState = this;
		else if( opponentAI.player.canUpgrade() )
			nextState = new StrengtheningState();
		else if( !opponentAI.knownBuildings.isEmpty() && opponentAI.areThereConquerableBuildings()
				&& opponentAI.canGoToThisState( ConquestState.class ) )
			nextState = new ConquestState();
		else if( opponentAI.shouldICreateTowers() && opponentAI.canIcreateTowers() )
			nextState = new FortificationState();
		else if( opponentAI.areThereWeakerPlayers() )
			nextState = new CombatState();
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
