package mpa.core.logic.resource;

enum FieldState {HARVEST,GROWTH, PLOWING, SEEDING}; //RACCOLTO, CRESCITA, ARATURA, SEMINA 

public class Field extends AbstractResource 
{
	private FieldState currentFieldState;
	private boolean free;
	
	public Field(int x, int y, int providing, FieldState fieldState)
	{
		super(x, y, providing);
		this.currentFieldState = fieldState;
		this.free = true;
	}

	public FieldState getCurrentFieldState()
	{
		return currentFieldState;
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
