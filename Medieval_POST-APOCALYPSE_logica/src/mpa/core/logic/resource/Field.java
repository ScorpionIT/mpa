package mpa.core.logic.resource;

import mpa.core.logic.character.Player;

enum FieldState
{
	PLOWING, SEEDING, GROWTH, HARVEST
}; // RACCOLTO, CRESCITA, ARATURA, SEMINA

public class Field extends AbstractResource
{
	private FieldState currentFieldState;
	private boolean free;
	private int foodCollected;
	private int maximumFoodCapacity;
	private int food;

	public Field(int x, int y, Player player)
	{
		super(x, y, 0, player); // TODO
		this.currentFieldState = FieldState.PLOWING;
		this.free = true;
		this.foodCollected = 0;
		this.maximumFoodCapacity = 10; // TODO
		this.food = 10;

	}

	public FieldState getCurrentFieldState()
	{
		return currentFieldState;
	}

	public void advanceStatus()
	{
		if (this.currentFieldState.ordinal() < FieldState.values().length)
		{
			this.currentFieldState = FieldState.values()[currentFieldState.ordinal() + 1];
		}
		else
		{
			this.currentFieldState = FieldState.values()[0];

			if (this.foodCollected + this.food <= this.maximumFoodCapacity)
				this.foodCollected += this.food;

		}
	}

	public void setCurrentFieldState(FieldState currentFieldState)
	{
		this.currentFieldState = currentFieldState;
	}

	public boolean isFree()
	{
		return free;
	}

	public void setFree(boolean free)
	{
		this.free = free;
	}

	public void setFood(int food)
	{
		this.food = food;
	}

}
