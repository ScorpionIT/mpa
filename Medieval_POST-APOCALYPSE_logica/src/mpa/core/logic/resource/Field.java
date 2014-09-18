package mpa.core.logic.resource;

enum FieldState { PLOWING, SEEDING, GROWTH, HARVEST }; //RACCOLTO, CRESCITA, ARATURA, SEMINA 

public class Field extends AbstractResource 
{
	private FieldState currentFieldState;
	private boolean free;
	private int foodCollected;
	private int maximumFoodCollected;
	
	public Field( int x, int y, int providing, FieldState fieldState, int maximumFoodCollected )
	{
		super(x, y, providing);
		this.currentFieldState = fieldState;
		this.free = true;
		this.foodCollected = 0;
		this.maximumFoodCollected = maximumFoodCollected;
		
	}

	public FieldState getCurrentFieldState()
	{
		return currentFieldState;
	}
	
	public void advanceStatus()
	{
		if( this.currentFieldState.ordinal() < FieldState.values().length )
			this.currentFieldState= this.currentFieldState.values()[currentFieldState.ordinal() + 1];
		else 
		{
			this.currentFieldState = FieldState.values()[0];
			
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

}
