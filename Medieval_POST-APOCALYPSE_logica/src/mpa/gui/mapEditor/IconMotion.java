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
	Point selectedObjectPosition;
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

		if (mainMapEditorPanel.getDeleteMode())
			return;
		for (int i = 0; i < components.size(); i++)
		{

			Point point = e.getPoint();
			if (components.get(i).getSecond().contains(point))
			{
				Rectangle rect = components.get(i).getSecond();

				selectedObjectPosition = new Point(rect.x, rect.y);
				selectedObjectName = components.get(i).getFirst();
				mainMapEditorPanel.setSelectedObject(selectedObjectName, selectedObjectPosition);
				dragging = true;
				break;

			}
		}
	}

	public void mouseReleased(MouseEvent e)
	{

		if (dragging && !mainMapEditorPanel.thereIsIntersection(selectedObjectPosition, selectedObjectName))
		{
			selectedObjectPosition.x = e.getX() - mainMapEditorPanel.getIconMapEditorWidth();
			selectedObjectPosition.y = e.getY() + mainMapEditorPanel.getSettingsMapEditorHeight();
			if (selectedObjectPosition.x > 0 && selectedObjectPosition.y > 0)
			{
				mainMapEditorPanel.addSelectedIcon();
			}
		}
		else
			mainMapEditorPanel.setSelectedObject(null, null);

		dragging = false;

	}

	public void mouseDragged(MouseEvent e)
	{
		if (dragging)
		{
			selectedObjectPosition.x = e.getX() - mainMapEditorPanel.getIconMapEditorWidth();
			selectedObjectPosition.y = e.getY() + mainMapEditorPanel.getSettingsMapEditorHeight();

			if (selectedObjectPosition.x > 0 && selectedObjectPosition.y > 0)
			{

				if (mainMapEditorPanel.thereIsIntersection(selectedObjectPosition, selectedObjectName))
					mainMapEditorPanel.paintImageInMapPreviewEditorPanel(Color.red);
				else
					mainMapEditorPanel.paintImageInMapPreviewEditorPanel(Color.green);
			}

			// mainMapEditorPanel.repaintMapPreviewEditorPanel();
		}
	}
}
