package mpa.gui.gameGui.panel;

import mpa.gui.gameGui.listener.HandlerImplementation;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.listbox.builder.ListBoxBuilder;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class SelectionHeadquarterPanel
{
	private ListBoxBuilder listBoxBuilder;
	private HandlerImplementation gameController;
	private PanelBuilder selectionPanel;
	private int panelWidth;
	private int panelHeight;
	private final String listId = "#listBoxEnemy";
	private TextFieldBuilder textFieldMinionNumber;
	private final String textFieldId = "#textFieldMinion ";
	private ImageBuilder createTowerButton;

	public SelectionHeadquarterPanel(int panelWidth, int panelHeight, final int xPanel, final int yPanel, HandlerImplementation gameController)
	{
		this.panelWidth = panelWidth;
		this.panelHeight = panelHeight;
		this.gameController = gameController;

		textFieldMinionNumber = new TextFieldBuilder(textFieldId)
		{
			{
				x("15%");
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
				x("15%");
				y("50%");

				width("40%");
				height("10%");
			}
		};

		createTowerButton = new ImageBuilder()
		{
			{
				filename("createTower1.png");
				width("50%");
				height("20%");

				x("15%");
				y("80%");
				interactOnClick("onClickButtonCreateTowerButton()");

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
						filename("backgroundHeadquarterPanel.png");
						width("100%");
						height("100%");

					}
				});

				image(new ImageBuilder("#Insert Minions' number")
				{
					{
						filename("insertMinionsNumber.png");
						x("15%");
						y("20%");
						width("30%");
						height("8%");
					}

				});

				image(new ImageBuilder("#Choose Enemy")
				{
					{
						filename("chooseEnemy.png");
						x("15%");
						y("41%");
						width("20%");
						height("8%");
					}

				});
				image(new ImageBuilder("#Create")
				{
					{
						filename("createMinions.png");
						x("65%");
						y("80%");
						width("20%");
						height("10%");
						interactOnClick("onClickCreateMinion()");

					}

				});

				image(new ImageBuilder()
				{
					{
						filename("createTower1.png");
						width("20%");
						height("10%");

						x("15%");
						y("80%");
						interactOnClick("onClickButtonCreateTowerHeadquarterButton()");

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
