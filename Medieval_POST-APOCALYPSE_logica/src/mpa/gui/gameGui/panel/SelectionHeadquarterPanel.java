package mpa.gui.gameGui.panel;

import mpa.gui.gameGui.listener.ListenerImplementation;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.listbox.builder.ListBoxBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class SelectionHeadquarterPanel
{
	private ListBoxBuilder listBoxBuilder;
	private ListenerImplementation playerController;
	private PanelBuilder selectionPanel;
	private int windowWidth;
	private int windowHeight;
	private final String listId = "#listBoxEnemy";

	public SelectionHeadquarterPanel(int windowWidth, int windowHeight, ListenerImplementation playerController)
	{
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.playerController = playerController;

		listBoxBuilder = new ListBoxBuilder(listId)
		{
			{
				displayItems(5);
				selectionModeMutliple();
				optionalHorizontalScrollbar();
				optionalVerticalScrollbar();
				selectionModeSingle();
				width("50%"); // standard nifty width attribute
				height("50%"); // standard nifty width attribute
			}
		};

		selectionPanel = new PanelBuilder("#selected")
		{

			{
				childLayoutAbsoluteInside();

				width(Integer.toString(SelectionHeadquarterPanel.this.windowWidth));
				height(Integer.toString(SelectionHeadquarterPanel.this.windowHeight));
				x("0%");
				y("0%");

				image(new ImageBuilder()
				{
					{
						filename("selectedPanel.png");
						width("85%");
						height("85%");

					}
				});

				visible(false);
				control(listBoxBuilder);
			}
		};

	}

	public void addListBox(Screen screen)
	{
	}

	public Element build(Nifty nifty, Screen currentScreen, Element parent)
	{
		Element element = selectionPanel.build(nifty, currentScreen, parent);
		return element;
	}

	public String getId()
	{
		return listId;
	}

}
