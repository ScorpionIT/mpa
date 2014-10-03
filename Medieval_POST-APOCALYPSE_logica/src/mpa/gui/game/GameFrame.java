package mpa.gui.game;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mpa.gui.menu.GameEditorFrame;

public class GameFrame extends JFrame
{
	private JPanel panel;

	public GameFrame() throws HeadlessException
	{
		super();
		panel = new GamePanel();
		
		panel.addMouseListener( new MouseListener()
		{
			
			@Override
			public void mousePressed(MouseEvent arg0)
			{
					
			}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				// TODO Auto-generated method stub
				
			}
			
			
		});
		
		this.setContentPane(panel);
		this.setLocation(600, 300);
		this.setSize(500, 500);
		this.setTitle("Aru Q");
		
		this.setVisible(true);
	
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String argv[])
	{
		JFrame frame = new GameFrame();
		
	}

}
