package mpa.core.logic.resource;

enum FieldState {HARVEST,GROWTH, PLOWING, SEEDING}; //RACCOLTO, CRESCITA, ARATURA, SEMINA 

public class Field extends AbstractResource
{
	FieldState currentFieldState;
	
	public Field(int x, int y, int providing, FieldState fieldState)
	{
		super(x, y, providing);
		this.currentFieldState = fieldState;
		
	}

}
