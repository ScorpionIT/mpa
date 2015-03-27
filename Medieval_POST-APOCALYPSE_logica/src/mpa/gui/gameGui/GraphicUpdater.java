package mpa.gui.gameGui;

public class GraphicUpdater extends Thread
{
	private GameGui gameGui;
	private final long INTERVAL = 50;
	private final float MOVEMENT_LENGTH = 10;

	public GraphicUpdater( GameGui gameGui )
	{
		super();
		this.gameGui = gameGui;
	}

	@Override
	public void run()
	{
		while( true )
		{
			try
			{
				sleep( INTERVAL );
			} catch( InterruptedException e )
			{
				e.printStackTrace();
			}
			gameGui.takeLock();

			com.jme3.math.Vector3f camLocation = gameGui.getCamPosition();

			System.out.println( gameGui.cursorOnTheBottomEdge );
			if( gameGui.cursorOnTheBottomEdge )
				camLocation.setZ( camLocation.z - MOVEMENT_LENGTH );
			else if( gameGui.cursorOnTheTopEdge )
				camLocation.setZ( camLocation.z + MOVEMENT_LENGTH );
			if( gameGui.cursorOnTheLeftEdge )
				camLocation.setX( camLocation.x + MOVEMENT_LENGTH );
			else if( gameGui.cursorOnTheRightEdge )
				camLocation.setX( camLocation.x - MOVEMENT_LENGTH );

			gameGui.setCamera( camLocation );

			System.out.println( gameGui.getCamPosition() );

			gameGui.leaveLock();
		}
	}
}
