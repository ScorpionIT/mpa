package mpa.gui.gameGui.panel;

import java.util.ArrayList;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class ChoosePanel
{

	private PanelBuilder choosePanel;
	private String[] listObject = { "WEAPON", "GRANADE", "FLASH_BANG", "HP_POTION", "MP_POTION" };
	private ArrayList<LabelBuilder> elementLabels = new ArrayList();

	private final int width = 130;
	private final int height = 150;

	private int selectedElementIndex = 0;

	public ChoosePanel()
	{
		initChoosePanel();

	}

	private void initChoosePanel()
	{
		choosePanel = new PanelBuilder("#choosePanel")
		{

			{
				childLayoutAbsoluteInside();

				width(Integer.toString(width));
				height(Integer.toString(height));
				alignRight();
				valignTop();
				visibleToMouse(true);

				style("nifty-panel-no-shadow");

			}

		};

		int yIncrement = 100 / (listObject.length + 1);

		int currentY = yIncrement / 2;
		for (String obj : listObject)
		{

			LabelBuilder labelBuilder = getLabelBuilder(obj, 25, currentY);
			// labelBuilder.backgroundColor("#f008");
			elementLabels.add(labelBuilder);
			choosePanel.control(labelBuilder);
			currentY += yIncrement;
		}
		initSelectedElment();

	}

	private LabelBuilder getLabelBuilder(final String text, final int x, final int y)
	{
		return new LabelBuilder(text + "Label")
		{
			{
				childLayoutCenter();
				text(text);
				width("100%");

				y(Integer.toString(y) + "%");
			}
		};
	}

	public void changePosition(int x, int y)
	{
		choosePanel.x(Integer.toString(x));
		choosePanel.y(Integer.toString(y));
	}

	public PanelBuilder get()
	{
		return choosePanel;
	}

	public Element build(Nifty nifty, Screen currentScreen, Element parent)
	{
		return choosePanel.build(nifty, currentScreen, parent);
	}

	public void changeChoosenElement(boolean back)
	{
		if (back)
		{
			elementLabels.get(selectedElementIndex).backgroundColor("#0000");
			selectedElementIndex -= 1;
			if (selectedElementIndex < 0)
				selectedElementIndex = elementLabels.size() - 1;
			elementLabels.get(selectedElementIndex).backgroundColor("#f008");

		}
		else
		{
			elementLabels.get(selectedElementIndex).backgroundColor("#0000");
			selectedElementIndex += 1;
			if (selectedElementIndex > elementLabels.size() - 1)
				selectedElementIndex = 0;
			elementLabels.get(selectedElementIndex).backgroundColor("#f008");
		}
	}

	public void initSelectedElment()
	{

		elementLabels.get(selectedElementIndex).backgroundColor("#0000");
		selectedElementIndex = 0;
		elementLabels.get(selectedElementIndex).backgroundColor("#f008");
	}

	public String getSelectedElement()
	{
		return listObject[selectedElementIndex];

	}

	public void setVisible(boolean visible)
	{
		choosePanel.visible(visible);
	}

	public int getHeight()
	{
		return height;
	}

	public int getWidth()
	{
		return width;
	}

}
