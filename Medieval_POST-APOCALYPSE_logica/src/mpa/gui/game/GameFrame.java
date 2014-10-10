package mpa.gui.game;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import mpa.gui.eventHandler.MouseEventManager;
import mpa.gui.menu.GameEditorFrame;

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
