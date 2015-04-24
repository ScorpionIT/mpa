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
import mpa.gui.menuMap.MpaPanel;

public class MapPreviewEditorPanel extends MpaPanel
{
	private Image backgroundImage;
	private Stack<Pair<String, Rectangle>> addedObjects = new Stack<>();
	private Stack<Pair<String, Rectangle>> removedObjects = new Stack<>();
	private HashMap<String, Image> images;

	private Point selectedObjectPosition = null;
	private Color selectedObjectColor;
	private String selectedObjectName;
	private MainMapEditorPanel mainMapEditorPanel;

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
			if (selectedObjectName.equals("Headquarter"))
				mainMapEditorPanel.addPlayer();
			removedObjects.removeAllElements();
			Rectangle rect = new Rectangle((int) (selectedObjectPosition.getX()), (int) selectedObjectPosition.getY(), (int) W((float) images.get(
					selectedObjectName).getWidth(null)), (int) H((float) images.get(selectedObjectName).getHeight(null)));
			addedObjects.push(new Pair<String, Rectangle>(selectedObjectName, rect));
			selectedObjectColor = null;
			selectedObjectName = null;
			selectedObjectPosition = null;
			this.repaint();
		}

	}

	public boolean thereIsIntersection(Rectangle selectedLabelBounds, String selectedObjectName)
	{
		for (int i = 0; i < addedObjects.size(); i++)
		{
			Rectangle rect = addedObjects.get(i).getSecond();
			selectedLabelBounds.setBounds((int) selectedLabelBounds.getX(), (int) selectedLabelBounds.getY(),
					(int) W((float) images.get(selectedObjectName).getWidth(null)), (int) H((float) images.get(selectedObjectName).getHeight(null)));

			if (rect.intersects(selectedLabelBounds))
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

			g.drawRect((int) selectedObjectPosition.getX(), (int) selectedObjectPosition.getY(), (int) W((float) images.get(selectedObjectName)
					.getWidth(null)), (int) H((float) images.get(selectedObjectName).getHeight(null)));

			g.drawImage(images.get(selectedObjectName), (int) selectedObjectPosition.getX(), (int) selectedObjectPosition.getY(),
					(int) W((float) images.get(selectedObjectName).getWidth(null)), (int) H((float) images.get(selectedObjectName).getHeight(null)),
					this);
		}

		for (Pair<String, Rectangle> element : addedObjects)
		{

			Image image = images.get(element.getFirst());
			g.setColor(Color.green);
			g.drawRect((int) element.getSecond().getX(), (int) element.getSecond().getY(), (int) W((float) image.getWidth(null)),
					(int) H((float) image.getHeight(null)));
			g.drawImage(image, (int) element.getSecond().getX(), (int) element.getSecond().getY(), (int) W((float) image.getWidth(null)),
					(int) H((float) image.getHeight(null)), this);

		}

	}

	public void removeLabel(Rectangle elementBounds)
	{
		this.addedObjects.pop();
		this.repaint();
	}

	public void undo()
	{
		if (addedObjects.size() > 0)
		{
			Pair<String, Rectangle> removedObject = addedObjects.pop();
			if (removedObject.getFirst().equals("Headquarter"))
				mainMapEditorPanel.removePlayer();
			removedObjects.push(removedObject);
			this.repaint();
		}
	}

	public void redo()
	{
		if (removedObjects.size() > 0)
		{
			Pair<String, Rectangle> readdedObject = removedObjects.pop();
			if (readdedObject.getFirst().equals("Headquarter"))
				mainMapEditorPanel.addPlayer();
			addedObjects.push(readdedObject);
			this.repaint();
		}

	}

	@Override
	public void setMapDimension(float width, float height)
	{
		super.setMapDimension(width, height);

		// resize Object
		for (int i = 0; i < addedObjects.size(); i++)
		{
			Pair<String, Rectangle> element = addedObjects.get(i);
			Image image = images.get(element.getFirst());

			element.getSecond().setBounds((int) graphicX((float) element.getSecond().getX()), (int) graphicX((float) element.getSecond().getY()),
					(int) W((float) image.getWidth(null)), (int) H((float) image.getHeight(null)));

			Rectangle map = new Rectangle(0, 0, (int) width, (int) height);
			if (!map.contains(element.getSecond()))
			{
				addedObjects.remove(element);
				i--;

			}
		}
	}

	public void setSelectedObject(Point elementPosition, Color selectedObjectColor, String selectedObjectName)
	{

		this.selectedObjectColor = selectedObjectColor;
		this.selectedObjectName = selectedObjectName;
		this.selectedObjectPosition = elementPosition;
		this.repaint();

	}
}
