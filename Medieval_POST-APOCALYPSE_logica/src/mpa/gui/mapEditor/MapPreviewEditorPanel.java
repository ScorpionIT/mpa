package mpa.gui.mapEditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

import javax.imageio.ImageIO;

import mpa.core.logic.Pair;
import mpa.core.util.GameProperties;
import mpa.gui.menuMap.MpaPanel;

public class MapPreviewEditorPanel extends MpaPanel
{
	private Image backgroundImage;
	private Stack<Pair<String, Point>> addedObjects = new Stack<>();
	private Stack<Pair<String, Point>> removedObjects = new Stack<>();
	private HashMap<String, Image> images;

	private Point selectedObjectPosition = null;
	private Color selectedObjectColor;
	private String selectedObjectName;
	private MainMapEditorPanel mainMapEditorPanel;
	private boolean market = false;

	public MapPreviewEditorPanel(MainMapEditorPanel mainMapEditorPanel, HashMap<String, Image> imageLabels)
	{
		this.mainMapEditorPanel = mainMapEditorPanel;
		this.images = imageLabels;
		this.setLayout(null);
		try
		{

			backgroundImage = ImageIO.read(new File("Assets/Textures/grass-pattern.png"));
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}

		this.setVisible(true);
	}

	private void setBackgroundPatternImage(Graphics g)
	{
		int backgroundWidth = backgroundImage.getWidth(this);
		int backgroundHeight = backgroundImage.getWidth(this);
		int maxI = this.getWidth() / backgroundWidth;
		int maxJ = this.getHeight() / backgroundHeight;

		for (int i = 0; i <= maxI; i++)
		{
			for (int j = 0; j <= maxJ; j++)
			{
				g.drawImage(backgroundImage, i * backgroundWidth, j * backgroundHeight, backgroundWidth, backgroundHeight, this);
			}
		}

	}

	public void addLabel()
	{
		if (selectedObjectPosition != null)
		{
			if (selectedObjectName.toLowerCase().equals("headquarter"))
				mainMapEditorPanel.addPlayer();
			else if (selectedObjectName.toLowerCase().equals("market"))
				market = true;
			removedObjects.removeAllElements();

			addedObjects.push(new Pair<String, Point>(selectedObjectName, selectedObjectPosition));
			selectedObjectColor = null;
			selectedObjectName = null;
			selectedObjectPosition = null;
			this.repaint();
		}

	}

	public boolean thereIsIntersection(Point selectedLabelBounds, String selectedObjectName)
	{
		for (int i = 0; i < addedObjects.size(); i++)
		{
			Rectangle rect = new Rectangle((int) (addedObjects.get(i).getSecond().getX()), (int) addedObjects.get(i).getSecond().getY(),
					(int) W((float) GameProperties.getInstance().getObjectWdth(addedObjects.get(i).getFirst())), (int) H((float) GameProperties
							.getInstance().getObjectWdth(addedObjects.get(i).getFirst())));

			Rectangle rect1 = new Rectangle((int) selectedLabelBounds.getX(), (int) selectedLabelBounds.getY(), (int) W((float) GameProperties
					.getInstance().getObjectWdth(selectedObjectName)), (int) H((float) GameProperties.getInstance().getObjectHeight(
					selectedObjectName)));

			if (rect.intersects(rect1))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		setBackgroundPatternImage(g);

		if (selectedObjectPosition != null)
		{
			g.setColor(selectedObjectColor);

			g.drawRect((int) (this.selectedObjectPosition.getX()), (int) this.selectedObjectPosition.getY(), (int) W((float) GameProperties
					.getInstance().getObjectWdth(this.selectedObjectName)),
					(int) H((float) GameProperties.getInstance().getObjectHeight(this.selectedObjectName)));

			g.drawImage(images.get(this.selectedObjectName), (int) (this.selectedObjectPosition.getX()), (int) this.selectedObjectPosition.getY(),
					(int) W((float) GameProperties.getInstance().getObjectWdth(this.selectedObjectName)), (int) H((float) GameProperties
							.getInstance().getObjectHeight(this.selectedObjectName)), this);
		}

		for (Pair<String, Point> element : addedObjects)
		{

			Image image = images.get(element.getFirst());
			g.setColor(Color.green);
			g.drawRect((int) element.getSecond().getX(), (int) element.getSecond().getY(), (int) W((float) GameProperties.getInstance()
					.getObjectWdth(element.getFirst())), (int) H((float) GameProperties.getInstance().getObjectHeight(element.getFirst())));
			g.drawImage(image, (int) element.getSecond().getX(), (int) element.getSecond().getY(), (int) W((float) GameProperties.getInstance()
					.getObjectWdth(element.getFirst())), (int) H((float) GameProperties.getInstance().getObjectHeight(element.getFirst())), this);

		}

	}

	public void undo()
	{
		if (addedObjects.size() > 0)
		{
			Pair<String, Point> removedObject = addedObjects.pop();
			if (removedObject.getFirst().toLowerCase().equals("headquarter"))
				mainMapEditorPanel.removePlayer();
			else if (removedObject.getFirst().toLowerCase().equals("market"))
				market = false;
			removedObjects.push(removedObject);
			this.repaint();
		}
	}

	public void redo()
	{
		if (removedObjects.size() > 0)
		{
			Pair<String, Point> readdedObject = removedObjects.pop();
			if (readdedObject.getFirst().toLowerCase().equals("headquarter"))
				mainMapEditorPanel.addPlayer();
			else if (readdedObject.getFirst().toLowerCase().equals("market"))
				market = true;
			addedObjects.push(readdedObject);
			this.repaint();
		}

	}

	@Override
	public void setMapDimension(float width, float height)
	{
		super.setMapDimension(width, height);

		Rectangle map = new Rectangle(0, 0, (int) width, (int) height);
		// resize Object
		for (int i = 0; i < addedObjects.size(); i++)
		{
			Pair<String, Point> element = addedObjects.get(i);
			element.getSecond().setLocation((int) graphicX((float) element.getSecond().getX()), (int) graphicX((float) element.getSecond().getY()));

			Rectangle objectBounds = new Rectangle((int) element.getSecond().getX(), (int) element.getSecond().getY(), (int) W((float) GameProperties
					.getInstance().getObjectWdth(element.getFirst())), (int) H((float) GameProperties.getInstance().getObjectHeight(
					element.getFirst())));

			if (!map.contains(objectBounds))
			{
				addedObjects.remove(element);
				i--;

			}
		}
	}

	public boolean isThereAMarket()
	{
		return market;
	}

	public void setSelectedObject(Point elementPosition, String selectedObjectName)
	{

		this.selectedObjectName = selectedObjectName;
		this.selectedObjectPosition = elementPosition;

	}

	public void paintSelectedObject(Color color)
	{
		this.selectedObjectColor = color;
		this.repaint();

	}
}
