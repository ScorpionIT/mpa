package mpa.gui.mapEditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mpa.core.logic.Pair;
import mpa.core.util.GameProperties;
import mpa.core.util.MapToXmlCreator;
import mpa.gui.mainMenu.MainMenuPanel;

public class MainMapEditorPanel extends JPanel
{

	private SettingsMapEditorPanel settingsMapEditorPanel;
	private int mapWidth = 0;
	private int mapHeight = 0;
	private int numberOfPlayers;
	private String mapName = null;
	private MapPreviewEditorPanel mapPreviewEditorPanel;
	private IconMapEditorPanel iconMapEditorPanel;
	private ButtonsEditorPanel buttonsEditorPanel;
	private boolean inizialized = false;
	private HashMap<String, Image> imageLabels = new HashMap<String, Image>();
	private final String imageFolder = GameProperties.getInstance().getPath("ImagesPreviewPath");
	private final String mapFolder = GameProperties.getInstance().getPath("CustomMapsPath");
	int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
	int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
	Image deleteCursor;

	private boolean deleteMode = false;

	// TODO creare un file proporties per le cartelle
	private SubmitButtonEditorPanel submitButtonEditorPanel;
	private JFrame mainFrame;
	private MainMenuPanel mainMenuPanel;

	public MainMapEditorPanel(MainMenuPanel mainMenuPanel, JFrame frame)
	{

		this.mainMenuPanel = mainMenuPanel;
		this.mainFrame = frame;

		this.setLayout(null);
		buttonsEditorPanel = new ButtonsEditorPanel(this);
		mapPreviewEditorPanel = new MapPreviewEditorPanel(this, imageLabels);
		settingsMapEditorPanel = new SettingsMapEditorPanel(this);
		submitButtonEditorPanel = new SubmitButtonEditorPanel(this);

		loadImages();

		iconMapEditorPanel = new IconMapEditorPanel(/* this, */imageLabels);

		this.add(iconMapEditorPanel);
		this.add(mapPreviewEditorPanel);
		this.add(settingsMapEditorPanel);
		this.add(buttonsEditorPanel);
		this.add(submitButtonEditorPanel);

	}

	public void setDeleteMode(boolean deleteMode)
	{
		this.deleteMode = deleteMode;
		if (this.deleteMode)
		{
			Toolkit kit;
			try
			{
				kit = Toolkit.getDefaultToolkit();

				Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(deleteCursor, new Point(0, 0), "DeleteCursor");

				this.setCursor(cursor);

			} catch (Exception exp)
			{
				exp.printStackTrace();
			}
		}
		else
		{
			this.setCursor(Cursor.getDefaultCursor());
		}
	}

	public boolean getDeleteMode()
	{
		return deleteMode;

	}

	public int getSettingsMapEditorHeight()
	{
		return settingsMapEditorPanel.getHeight();
	}

	public int getIconMapEditorWidth()
	{
		return iconMapEditorPanel.getWidth();
	}

	public void addSelectedIcon()
	{
		mapPreviewEditorPanel.addLabel(false);
	}

	public void repaintMapPreviewEditorPanel()
	{
		mapPreviewEditorPanel.repaint();
	}

	public ArrayList<Pair<String, Rectangle>> getIcons()
	{
		return iconMapEditorPanel.getImageLabelsPosition();
	}

	public boolean thereIsIntersection(Point rectangle, String objectName)
	{
		return mapPreviewEditorPanel.thereIsIntersection(rectangle, objectName);
	}

	public Component[] getMapPreviewEditorComponents()
	{
		return mapPreviewEditorPanel.getComponents();
	}

	@Override
	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(0, 0, mainFrame.getWidth(), mainFrame.getHeight());

		buttonsEditorPanel.setBounds(0, 0, this.getWidth() * 15 / 100, this.getHeight() * 5 / 100);
		try
		{
			deleteCursor = ImageIO.read(new File("./Assets/iconPanel/deleteIcon.png")).getScaledInstance(this.getWidth() * 2 / 100,
					this.getWidth() * 2 / 100, 0);
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		settingsMapEditorPanel.setBounds(0, this.getHeight() * 5 / 100, this.getWidth() * 15 / 100, this.getHeight() * 25 / 100);
		mapPreviewEditorPanel.setBounds(this.getWidth() * 15 / 100, 0, this.getWidth() - settingsMapEditorPanel.getWidth(), this.getHeight());
		if (!inizialized)
		{
			addIconMapEditorPanel();
			inizialized = true;
		}
		submitButtonEditorPanel.setBounds(0, iconMapEditorPanel.getY() + iconMapEditorPanel.getHeight(), this.getWidth() * 15 / 100,
				this.getHeight() * 5 / 100);
	}

	private void addIconMapEditorPanel()
	{
		iconMapEditorPanel.setBounds(0, this.buttonsEditorPanel.getHeight() + this.settingsMapEditorPanel.getHeight(), this.getWidth() * 15 / 100,
				this.getHeight() - this.buttonsEditorPanel.getHeight() - this.settingsMapEditorPanel.getHeight() - this.getHeight() * 5 / 100);
		IconMotion iconMotion = new IconMotion(this);

		iconMapEditorPanel.getIconPanel().addMouseListener(iconMotion);
		iconMapEditorPanel.getIconPanel().addMouseMotionListener(iconMotion);
	}

	public Rectangle getMapPreviewEditorPanelBounds()
	{
		return mapPreviewEditorPanel.getBounds();
	}

	public void setMapDimension(int width, int height)
	{
		this.mapWidth = width;
		this.mapHeight = height;
		mapPreviewEditorPanel.setMapDimension(width, height);
		mapPreviewEditorPanel.repaint();

	}

	public void paintImageInMapPreviewEditorPanel(Color color)
	{
		mapPreviewEditorPanel.paintSelectedObject(color);
	}

	public void undo()
	{
		mapPreviewEditorPanel.undo();
	}

	public void redo()
	{
		mapPreviewEditorPanel.redo();
	}

	public void addPlayer()
	{
		numberOfPlayers++;
	}

	public void removePlayer()
	{
		numberOfPlayers--;
	}

	private void loadImages()
	{

		try
		{

			File folder = new File(imageFolder);
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++)
			{
				if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".png"))
				{
					Image img = ImageIO.read(new File(imageFolder + "/" + listOfFiles[i].getName()));
					imageLabels.put(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4), img);

				}
			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public void convertMapToXml()
	{
		mapName = settingsMapEditorPanel.getMapName();
		boolean isNameValid = true;
		if (numberOfPlayers <= 0)
			JOptionPane.showMessageDialog(new Frame(), "Inserire oggetti nella mappa", "", JOptionPane.PLAIN_MESSAGE);
		else if (mapName == null || mapName.equals(""))
			JOptionPane.showMessageDialog(new Frame(), "Inserire un nome valido per la mappa", "", JOptionPane.PLAIN_MESSAGE);
		else
		{
			File folder = new File(mapFolder);
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++)
			{
				if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".xml"))
				{
					if (listOfFiles[i].getName().subSequence(0, listOfFiles[i].getName().length() - 4).equals(mapName))
					{
						JOptionPane.showMessageDialog(new Frame(), "Mappa già esistente", "", JOptionPane.PLAIN_MESSAGE);
						isNameValid = false;
						break;
					}
				}

			}
			if (isNameValid)
			{
				MapToXmlCreator.createXmlMap(mapPreviewEditorPanel.getAddedObjects(), mapName, mapWidth, mapHeight, numberOfPlayers);
			}
		}

	}

	public void setSelectedObject(String selectedObjectName, Point selectedObjectPosition)
	{
		mapPreviewEditorPanel.setSelectedObject(selectedObjectPosition, selectedObjectName);
		if (selectedObjectName == null)
			mapPreviewEditorPanel.repaint();
	}

	public void backButton()
	{
		mainFrame.setContentPane(mainMenuPanel);
		mainFrame.getContentPane().setVisible(true);
		mainFrame.setVisible(true);
		mainMenuPanel.repaint();
		mainFrame.setVisible(true);
	}

}
