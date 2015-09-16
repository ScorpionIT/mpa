package mpa.gui.gameGui.panel;

import mpa.gui.gameGui.listener.ListenerImplementation;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.listbox.builder.ListBoxBuilder;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class SelectionHeadquarterPanel
{
	private ListBoxBuilder listBoxBuilder;
	private ListenerImplementation gameController;
	private PanelBuilder selectionPanel;
	private int panelWidth;
	private int panelHeight;
	private final String listId = "#listBoxEnemy";
	private TextFieldBuilder textFieldMinionNumber;
	private final String textFieldId = "#textFieldMinion ";

	public SelectionHeadquarterPanel(int panelWidth, int panelHeight, final int xPanel, final int yPanel, ListenerImplementation gameController)
	{
		this.panelWidth = panelWidth;
		this.panelHeight = panelHeight;
		this.gameController = gameController;

		textFieldMinionNumber = new TextFieldBuilder(textFieldId)
		{
			{
				x("10%");
				y("30%");
				width("40%");
				height("10%");

			}
		};
		listBoxBuilder = new ListBoxBuilder(listId)
		{
			{
				displayItems(3);
				selectionModeMutliple();
				optionalHorizontalScrollbar();
				optionalVerticalScrollbar();
				selectionModeSingle();
				x("10%");
				y("50%");

				width("40%");
				height("10%");
			}
		};

		selectionPanel = new PanelBuilder("#selectedHeadquarter")
		{

			{
				childLayoutAbsoluteInside();

				width(Integer.toString(SelectionHeadquarterPanel.this.panelWidth));
				height(Integer.toString(SelectionHeadquarterPanel.this.panelHeight));
				x(Integer.toString(xPanel));
				y(Integer.toString(yPanel));

				image(new ImageBuilder()
				{
					{
						filename("selectedPanel.png");
						width("100%");
						height("100%");

					}
				});

				control(new LabelBuilder("#Insert Minions' number")
				{
					{
						text("Insert Minions' number");
						x("10%");
						y("20%");
						width("40%");
						height("10%");
					}

				});

				control(new LabelBuilder("#Choose Enemy")
				{
					{
						text("Choose Enemy");
						x("10%");
						y("40%");
						width("40%");
						height("10%");
					}

				});
				control(new LabelBuilder("#Create")
				{
					{
						text("Create");
						x("70%");
						y("80%");
						width("30%");
						height("20%");
						interactOnClick("onClickCreateMinion()");

					}

				});

				control(listBoxBuilder);
				control(textFieldMinionNumber);
			}
		};

	}

	public Element build(Nifty nifty, Screen currentScreen, Element parent)
	{
		Element element = selectionPanel.build(nifty, currentScreen, parent);
		return element;
	}

	public String getListBoxId()
	{
		return listId;
	}

	public String getTextFieldId()
	{
		return textFieldId;
	}

	public void setVisible(boolean visible)
	{
		selectionPanel.visible(visible);
	}

}
