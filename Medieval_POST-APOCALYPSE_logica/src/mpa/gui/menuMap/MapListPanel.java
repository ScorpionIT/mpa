package mpa.gui.menuMap;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MapListPanel extends JPanel
{

	private final String maps = new String("./maps");

	private JPanel mainPanel;

	private JList<String> mapList;
	private String[] data;
	JScrollPane scrollPane;

	public MapListPanel(MainMenuPanel mainPanel)
	{

		this.setOpaque(false);

		this.mainPanel = mainPanel;
		File folder = new File(maps);
		File[] listOfFiles = folder.listFiles();
		data = new String[listOfFiles.length];
		ArrayList<String> maps = new ArrayList<String>();
		for (int i = 0; i < listOfFiles.length; i++)
		{
			if (listOfFiles[i].isFile())
			{
				data[i] = listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4);
			}
		}
		for (int i = 0; i < data.length; i++)
		{
			System.out.println(data[i]);

		}

		mapList = new JList<>(data);
		mapList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mapList.addListSelectionListener(new ListSelectionListener()
		{

			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				if (!e.getValueIsAdjusting())
				{
					((MainMenuPanel) MapListPanel.this.mainPanel).setMap(MapListPanel.this.maps + "/" + mapList.getSelectedValue() + ".xml");

				}
			}
		});

		this.mainPanel = mainPanel;
		this.setLayout(null);

		mapList.setOpaque(false); // TODO
		mapList.setBounds(0, 0, mapList.getPreferredSize().width, mapList.getPreferredSize().width);
		scrollPane = new JScrollPane();
		scrollPane.setLayout(null);
		scrollPane.add(mapList);
		scrollPane.setOpaque(false);

		scrollPane.setBounds(0, 0, this.getWidth() - 15, this.getHeight());
		this.add(scrollPane);

		this.setVisible(true);

	}

	@Override
	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(x, y, width, height);
		scrollPane.setBounds(0, 0, this.getWidth() - 15, this.getHeight());
		mapList.setBounds(0, 0, this.getWidth() - 15, this.getHeight());
	}

}
