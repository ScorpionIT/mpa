package mpa.gui.multiplayer.server;

import java.awt.Image;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mpa.gui.menuMap.MapListPanel;
import mpa.gui.menuMap.MapPreview;

public class CreateServerMainPanel extends JPanel
{
	private JLabel backButton = new JLabel();
	private JLabel launchButton = new JLabel();
	private MapPreview mapPreview;
	private MapListPanel mapListPanel;
	private JComboBox<String> numberOfBots;
	private Image backgroundImage;
	private JFrame mainFrame;

	public CreateServerMainPanel( int x, int y, int width, int height, final JFrame mainFrame )
	{
		this.mainFrame = mainFrame;
		

	}

}
