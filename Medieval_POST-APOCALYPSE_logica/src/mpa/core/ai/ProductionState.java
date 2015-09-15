package mpa.core.ai;

import javax.vecmath.Vector2f;

import mpa.core.logic.GameManager;
import mpa.core.logic.character.Player;
import mpa.core.logic.tool.Potions;

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
		for( int i = 0; i < potions.length; i++ )
		{
			if( potions[i] == 0 )
			{
				index = i;
				break;
			}
			else if( potions[i] < min )
			{
				min = potions[i];
				index = i;
			}
		}

		if( min >= 5 )
		{
			imOk = true;
			return null;
		}

		switch( index )
		{
			case 0:
				return Potions.HP;
			case 1:
				return Potions.MP;
			case 2:
				return Potions.GRANADE;
			case 3:
				return Potions.FLASH_BANG;
			default:
				imOk = true;
				return null;
		}
	}

	@Override
	void action( OpponentAI opponentAI )
	{
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
				player.buyPotion( toProduce );
			}

		}

	}

	@Override
	AIState changeState( OpponentAI opponentAI )
	{
		AIState nextState = super.changeState( opponentAI );

		if( nextState != null )
			return nextState;

		if( ( isWalking || opponentAI.shouldBuyPotions() && opponentAI.player.canBuyPotions() )
				&& !imOk )
			nextState = this;
		else if( opponentAI.player.canUpgrade() )
			nextState = new StrengtheningState();
		else if( !opponentAI.knownAllTheWorld )
			nextState = new ExplorationState();
		// else if( opponentAI.areThereWeakerPlayers() )
		// nextState = new CombatState();
		else if( !opponentAI.knownBuildings.isEmpty() && opponentAI.areThereConquerableBuildings() )
			nextState = new ConquestState();
		else
			nextState = new WaitingState();

		return nextState;
	}
}
