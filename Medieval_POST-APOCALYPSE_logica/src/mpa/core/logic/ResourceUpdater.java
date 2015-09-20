package mpa.core.logic;

import java.util.List;

import mpa.core.logic.building.Headquarter;
import mpa.core.logic.resource.AbstractResourceProducer;

public class ResourceUpdater extends MyThread
{
	private List<AbstractResourceProducer> resources = GameManager.getInstance().getWorld()
			.getResourceProducers();
	private List<Headquarter> headquarters = GameManager.getInstance().getWorld().getHeadquarters();

	private static final int INTERVAL = 2000;

	@Override
	public void run()
	{
		boolean provide = true;
		while( true )
		{
			super.run();
			try
			{
				sleep( INTERVAL );
			} catch( InterruptedException e )
			{
				e.printStackTrace();
			}

			for( AbstractResourceProducer resource : resources )
			{
				resource.providePlayer();
			}

			if( provide )
			{
				for( Headquarter headquarter : headquarters )
					headquarter.provideOwner();
				provide = false;
			}
			else
				provide = true;
		}
	}
}
