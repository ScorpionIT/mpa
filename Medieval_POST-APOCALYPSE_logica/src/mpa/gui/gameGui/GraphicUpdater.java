package mpa.gui.gameGui;

public class GraphicUpdater extends Thread
{
	private GameGui gameGui;
	private final long INTERVAL = 10;
	private final float MOVEMENT_LENGTH = 1;

	public GraphicUpdater(GameGui gameGui)
	{
		super();
		this.gameGui = gameGui;
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				sleep(INTERVAL);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			gameGui.takeLock();

			com.jme3.math.Vector3f camLocation = gameGui.getCamPosition();

			if (gameGui.cursorOnTheBottomEdge)
			{
				camLocation.setZ(camLocation.z - MOVEMENT_LENGTH);
				gameGui.setCamera(camLocation);
			}
			else if (gameGui.cursorOnTheTopEdge)
			{
				camLocation.setZ(camLocation.z + MOVEMENT_LENGTH);
				gameGui.setCamera(camLocation);
			}

			if (gameGui.cursorOnTheLeftEdge)
			{
				camLocation.setX(camLocation.x + MOVEMENT_LENGTH);
				gameGui.setCamera(camLocation);
			}
			else if (gameGui.cursorOnTheRightEdge)
			{
				camLocation.setX(camLocation.x - MOVEMENT_LENGTH);
				gameGui.setCamera(camLocation);
			}

			gameGui.leaveLock();
		}
	}
}
