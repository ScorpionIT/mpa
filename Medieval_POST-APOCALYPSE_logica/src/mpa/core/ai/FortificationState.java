package mpa.core.ai;

import javax.vecmath.Vector2f;

import mpa.core.logic.GameManager;
import mpa.core.logic.building.AbstractPrivateProperty;

public class FortificationState extends AIState
{
	boolean isWalking = false;
	Vector2f gatheringPlace = null;
	AbstractPrivateProperty building = null;

	public FortificationState()
	{
		super();
	}

	@Override
	void action(OpponentAI opponentAI)
	{
		if (gatheringPlace == null || !isWalking)
		{
			if (opponentAI.player.getHeadquarter().getNumberOfTowers() == 0)
			{
				Vector2f hqGatheringPlace = opponentAI.player.getHeadquarter().getGatheringPlace();
				GameManager.getInstance().computePath(opponentAI.player, hqGatheringPlace.x, hqGatheringPlace.y);
				isWalking = true;
				gatheringPlace = hqGatheringPlace;
				building = opponentAI.player.getHeadquarter();
			}
			else
			{
				AbstractPrivateProperty _building = null;
				int minTowers = Integer.MAX_VALUE;
				for (AbstractPrivateProperty property : opponentAI.player.getProperties())
					if (property.getNumberOfTowers() < minTowers)
					{
						minTowers = property.getNumberOfTowers();
						_building = property;
					}

				if (_building != null)
				{
					building = _building;

					gatheringPlace = building.getGatheringPlace();
					GameManager.getInstance().computePath(opponentAI.player, gatheringPlace.x, gatheringPlace.y);
					isWalking = true;
				}
			}
		}
		else if (gatheringPlace.equals(opponentAI.player.getPosition()))
		{
			GameManager.getInstance().createTower(opponentAI.player, building.getAGatheringPlace(), building);
			isWalking = false;
			gatheringPlace = null;
			building = null;
		}
	}

	@Override
	AIState changeState(OpponentAI opponentAI)
	{
		AIState nextState = super.changeState(opponentAI);

		if (nextState != null)
			return nextState;

		if (isWalking)
			nextState = this;
		else if (opponentAI.player.canUpgrade())
			nextState = new StrengtheningState();
		else if (!opponentAI.knownBuildings.isEmpty() && opponentAI.areThereConquerableBuildings() && opponentAI.player.isThereAnyFreeSulbaltern()
				&& opponentAI.canGoToThisState(ConquestState.class))
			nextState = new ConquestState();
		else if (opponentAI.areThereWeakerPlayers())
			nextState = new CombatState();
		else if (opponentAI.shouldBuyPotions() && opponentAI.player.canBuyPotions() && opponentAI.canGoToThisState(ProductionState.class))
			nextState = new ProductionState();
		else if (!opponentAI.knownAllTheWorld && opponentAI.canGoToThisState(ExplorationState.class))
			nextState = new ExplorationState();
		else
		{
			nextState = new WaitingState();
			opponentAI.resetStateCounters();
		}
		return nextState;
	}
}
