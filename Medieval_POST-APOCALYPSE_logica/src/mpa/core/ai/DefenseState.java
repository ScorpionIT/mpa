package mpa.core.ai;

import javax.vecmath.Vector2f;

import mpa.core.logic.GameManager;
import mpa.core.logic.character.Player;
import mpa.core.logic.character.Player.Item;
import mpa.core.logic.tool.Potions;

public class DefenseState extends AIState
{

	Choice myChoice = null;

	enum Choice
	{
		FIGHT_BACK, RUN;
	}

	public DefenseState( OpponentAI opponentAI, Player bully )
	{
		if( opponentAI.canIFightWithHim( bully ) )
			myChoice = Choice.FIGHT_BACK;
		else
			myChoice = Choice.RUN;

		this.bully = bully;

	}

	@Override
	void action( OpponentAI opponentAI )
	{
		if( myChoice.ordinal() == Choice.FIGHT_BACK.ordinal() )
			return;

		int flashBangAmount = opponentAI.player.getPotionAmount( Potions.FLASH_BANG );

		if( flashBangAmount > 0 )
		{
			do
			{
				GameManager.getInstance().changeSelectedItem( opponentAI.player, Item.FLASH_BANG );
				GameManager.getInstance().playerAction( opponentAI.player, bully.getPosition() );
			} while( !bully.isFlashed()
					&& opponentAI.player.getPotionAmount( Potions.FLASH_BANG ) > 0 );

			Vector2f gatheringPlace = opponentAI.player.getHeadquarter().getGatheringPlace();
			GameManager.getInstance().computePath( opponentAI.player, gatheringPlace.x,
					gatheringPlace.y );

		}
		else
		{
			Vector2f gatheringPlace = opponentAI.player.getHeadquarter().getGatheringPlace();
			GameManager.getInstance().computePath( opponentAI.player, gatheringPlace.x,
					gatheringPlace.y );
		}

		bully = null;

	}

	@Override
	AIState changeState( OpponentAI opponentAI )
	{
		if( myChoice.ordinal() == Choice.FIGHT_BACK.ordinal() )
			return new CombatState( bully );

		AIState nextState = null;

		if( opponentAI.player.canUpgrade() )
			nextState = new StrengtheningState();
		else if( opponentAI.shouldBuyPotions() && opponentAI.player.canBuyPotions() )
			nextState = new ProductionState();
		else if( !opponentAI.knownAllTheWorld )
			nextState = new ExplorationState();
		else if( !opponentAI.knownBuildings.isEmpty() && opponentAI.areThereConquerableBuildings() )
			nextState = new ConquestState();
		else
			nextState = new WaitingState();

		return nextState;

	}
}
