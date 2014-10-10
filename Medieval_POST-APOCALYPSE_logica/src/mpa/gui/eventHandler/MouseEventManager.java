package mpa.gui.eventHandler;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import mpa.gui.game.GamePanel;

public class MouseEventManager extends MouseAdapter
{
	GamePanel panel;

	public MouseEventManager( GamePanel panel )
	{
		this.panel = panel;
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		this.panel.setPlayerPosition( e.getX(), e.getY() );
	}
	
   

}
