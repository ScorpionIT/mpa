package mpa.core.ai;

import mpa.core.logic.character.Player;
import mpa.core.logic.tool.Potions;

class ProductionState extends AIState
{

	ProductionState()
	{
		super();
	}

	@Override
	void action( OpponentAI opponentAI )
	{
		Player p = opponentAI.player;

		if( p.canUpgrade() )
			p.upgradeLevel();
		else
		{
			Potions potionNotToBuy = null;
			int max = Integer.MIN_VALUE;
			for( Potions potion : Potions.values() )
			{
				int amount = p.getPotionAmount( potion );

				if( amount > max )
				{
					potionNotToBuy = potion;
					max = amount;
				}
			}

			for( Potions value : Potions.values() )
				while( p.canBuyPotion( value ) )
				{
					if( value != potionNotToBuy )
					{
						p.buyPotion( value );

						if( p.getPotionAmount( value ) == p.getPotionAmount( potionNotToBuy )
								&& p.canBuyPotion( value ) )
						{
							potionNotToBuy = value;
						}
					}
				}
		}

	}

	@Override
	AIState changeState( OpponentAI opponentAI )
	{
		AIState nextState = null;

		if( !opponentAI.knownAllTheWorld )
			nextState = new ExplorationState();
		else if( opponentAI.areThereWeakerPlayers() )
			nextState = new CombatState();
		else if( !opponentAI.knownBuildings.isEmpty() && opponentAI.areThereConquerableBuildings() )
			nextState = new ConquestState();
		else
			nextState = new WaitingState();

		return nextState;
	}
}
