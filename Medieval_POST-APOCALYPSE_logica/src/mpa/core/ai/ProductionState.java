package mpa.core.ai;

import javax.vecmath.Vector2f;

import mpa.core.logic.GameManager;
import mpa.core.logic.character.Player;
import mpa.core.logic.tool.Potions;

class ProductionState extends AIState
{
	private boolean isWalking = false;

	ProductionState()
	{
		super();
	}

	@Override
	void action( OpponentAI opponentAI )
	{
		Player p = opponentAI.player;

		try
		{
			p.getWriteLock();

			Vector2f collectionPoint = p.getHeadquarter().getCollectionPoint();
			boolean canBuyPotions = p.canBuyPotions();

			if( canBuyPotions && collectionPoint.x != p.getX() && p.getY() != collectionPoint.y )
			{
				GameManager.getInstance().computePath( p, collectionPoint.x, collectionPoint.y );
				isWalking = true;
				return;
			}
			else if( canBuyPotions && collectionPoint.x == p.getX()
					&& collectionPoint.y == p.getY() )
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
		} finally
		{
			p.leaveWriteLock();
		}

	}

	@Override
	AIState changeState( OpponentAI opponentAI )
	{
		AIState nextState = null;

		if( isWalking || opponentAI.player.canBuyPotions() )
			nextState = this;
		else if( !opponentAI.knownAllTheWorld )
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
