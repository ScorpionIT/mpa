package mpa.gui.mapEditor;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.event.MouseInputAdapter;

import mpa.core.logic.Pair;

class IconMotion extends MouseInputAdapter
{
	Rectangle selectedObjectBounds;
	boolean dragging;
	private MainMapEditorPanel mainMapEditorPanel;
	private ArrayList<Pair<String, Rectangle>> components;
	private String selectedObjectName;

	public IconMotion(MainMapEditorPanel mainMapEditorPanel)
	{
		this.mainMapEditorPanel = mainMapEditorPanel;
		dragging = false;
		components = mainMapEditorPanel.getIcons();

	}

	public void mousePressed(MouseEvent e)
	{

		if (!mainMapEditorPanel.isMapDimensionValid())
			return;
		for (int i = 0; i < components.size(); i++)
		{

			Point point = e.getPoint();
			if (components.get(i).getSecond().contains(point))
			{

				Rectangle rect = components.get(i).getSecond();
				selectedObjectBounds = new Rectangle(rect.x, rect.y, rect.width, rect.height);
				selectedObjectName = components.get(i).getFirst();
				dragging = true;
				break;
			}
		}
	}

	public void mouseReleased(MouseEvent e)
	{

		if (!mainMapEditorPanel.thereIsAnIntersection(selectedObjectBounds, selectedObjectName) && dragging)
			mainMapEditorPanel.addSelectedIcon();
		else
			mainMapEditorPanel.paintImageInMapPreviewEditorPanel(null, null, null);

		dragging = false;

	}

	public void mouseDragged(MouseEvent e)
	{

		if (dragging)
		{
			selectedObjectBounds.x = e.getX() - mainMapEditorPanel.getIconMapEditorWidth();
			selectedObjectBounds.y = e.getY() + mainMapEditorPanel.getSettingsMapEditorHeight();

			if (selectedObjectBounds.x > 0 && selectedObjectBounds.y > 0)
			{

				if (mainMapEditorPanel.thereIsAnIntersection(selectedObjectBounds, selectedObjectName))
					mainMapEditorPanel.paintImageInMapPreviewEditorPanel(new Point(selectedObjectBounds.x, selectedObjectBounds.y), Color.red,
							selectedObjectName);
				else
					mainMapEditorPanel.paintImageInMapPreviewEditorPanel(new Point(selectedObjectBounds.x, selectedObjectBounds.y), Color.green,
							selectedObjectName);
			}

			mainMapEditorPanel.repaintMapPreviewEditorPanel();
		}
	}
}
