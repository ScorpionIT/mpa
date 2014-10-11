package mpa.gui.game;

import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mpa.gui.eventHandler.MouseEventManager;

@SuppressWarnings("serial")
public class GameFrame extends JFrame
{
	private JPanel panel;
	private MouseEventManager mouseEventManager;

	public GameFrame() throws HeadlessException
	{
		super();
		panel = new GamePanel();
		
		this.setContentPane(panel);
		this.setLocation(600, 300);
		this.setSize(500, 500);
		this.setTitle("Aru Q");
		
		this.setVisible(true);
	
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.mouseEventManager = new MouseEventManager( (GamePanel) this.panel );
		
		this.panel.addMouseListener( this.mouseEventManager );
		
	}
	
	
	
	public void refresh()
	{
			this.panel.repaint();
	}

}
