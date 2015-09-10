package mpa.core.ai;

import mpa.core.logic.character.Player;

class ProductionState extends AIState
{
	private boolean isWalking = false;

	ProductionState()
	{
		super();
	}

	private void sell()
	{
		// Market m = Market.getInstance();

	}

	@Override
	void action(OpponentAI opponentAI)
	{
		Player p = opponentAI.player;

		// try
		// {
		// p.getWriteLock();
		//
		// Vector2f collectionPoint = Market.getInstance().getGatheringPlace();
		// boolean canBuyPotions = p.canBuyPotions();
		//
		// if (collectionPoint.x != p.getX() && p.getY() != collectionPoint.y)
		// {
		// GameManager.getInstance().computePath(p, collectionPoint.x, collectionPoint.y);
		// isWalking = true;
		// return;
		// }
		// else if (collectionPoint.x != p.getX() && p.getY() != collectionPoint.y)
		// {
		//
		// if (!canBuyPotions)
		// {
		// sell();
		// }
		// else
		// {
		//
		// Potions potionNotToBuy = null;
		// int max = Integer.MIN_VALUE;
		// for (Potions potion : Potions.values())
		// {
		// int amount = p.getPotionAmount(potion);
		//
		// if (amount > max)
		// {
		// potionNotToBuy = potion;
		// max = amount;
		// }
		// }
		//
		// for (Potions value : Potions.values())
		// while (p.canBuyPotion(value))
		// {
		// if (value != potionNotToBuy)
		// {
		// p.buyPotion(value);
		//
		// if (p.getPotionAmount(value) == p.getPotionAmount(potionNotToBuy) &&
		// p.canBuyPotion(value))
		// {
		// potionNotToBuy = value;
		// }
		// }
		// }
		// }
		// }
		// } finally
		// {
		// p.leaveWriteLock();
		// }

	}

	@Override
	AIState changeState(OpponentAI opponentAI)
	{
		AIState nextState = null;

		if (isWalking || opponentAI.player.canBuyPotions())
			nextState = this;
		else if (!opponentAI.knownAllTheWorld)
			nextState = new ExplorationState();
		else if (opponentAI.areThereWeakerPlayers())
			nextState = new CombatState();
		else if (!opponentAI.knownBuildings.isEmpty() && opponentAI.areThereConquerableBuildings())
			nextState = new ConquestState();
		else
			nextState = new WaitingState();

		return nextState;
	}
}
