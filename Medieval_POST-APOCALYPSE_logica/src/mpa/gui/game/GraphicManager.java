package mpa.gui.game;

import javax.swing.JFrame;

public class GraphicManager extends Thread
{

	private GameFrame gameFrame;
	private int time;
	
	public GraphicManager()
	{
		this.gameFrame = new GameFrame();
		this.time = 5;
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
               this.gameFrame.refresh();
            }
        }
        catch (final Exception e)
        {
        }
		super.run();
	}

	

}
