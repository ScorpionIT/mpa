package mpa.gui.menuMap;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mpa.core.logic.MapFromXMLCreator;
import mpa.core.logic.MapLoader;
import mpa.core.logic.World;
import mpa.core.logic.WorldFromXMLCreator;
import mpa.gui.menu.GameEditorComboBox;
import mpa.gui.menu.GameEditorPanel;

public class MapMenu extends JFrame
{
	private final String maps = new String(System.getProperty("user.home") + "/Medieval Post APOCALYPSE/MappeGioco/");
	private MapLoader mapLoader = new MapLoader();
	private JPanel panel;

	public MapMenu()
	{
		super();
		this.panel = new GameEditorPanel();
		this.setContentPane(panel);
		this.setLocation(600, 300);
		this.setSize(500, 500);
		this.setTitle("Aru Q");
		File mapDir = new File(maps);
		if (!mapDir.exists())
			new File(maps).mkdirs();

		panel.setLayout(new BorderLayout());

		JComboBox<String> comboBox = new GameEditorComboBox(maps);
		panel.add(comboBox, BorderLayout.NORTH);

		comboBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{

				@SuppressWarnings("rawtypes")
				JComboBox comboBox = (JComboBox) event.getSource();

				loadMap(comboBox.getSelectedItem().toString());

			}
		});

		this.setVisible(true);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	private void loadMap(String path)
	{
		mapLoader.loadMapInfo(new MapFromXMLCreator(),
				"/home/parcuri/Dropbox/Workspace_Java/Ho Perso Al Gioco/mpa/Medieval_POST-APOCALYPSE_logica/map.xml");
		System.out.println("ho caricato la mappa info");
		System.out.println(mapLoader.getMapInfo().toString());

		World world = mapLoader.loadWorld(new WorldFromXMLCreator());
		System.out.println("ho caricato il mondo");

	}

	public static void main(String[] args)
	{
		JFrame jFrame = new MapMenu();
	}
}
