package mpa.core.logic.resource;

import java.util.ArrayList;
import java.util.List;
 


public class FieldManager extends Thread
{
	List<Field> fields;
	int time;
		
	public FieldManager( ArrayList<Field> fields, int time)
	{
		this.fields = fields;
		this.time = time;
	}

	@Override
	public void run()
	{
		try
        {
            while (true)
            {
                try
                {
                    sleep( this.time );
                }
                catch (final InterruptedException e)
                {

                }
               for( final Field field: this.fields)
               {
            	   field.advanceStatus();
               }
            }
        }
        catch (final Exception e)
        {
        }
		super.run();
	}
	
	
}
