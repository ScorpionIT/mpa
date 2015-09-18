package mpa.gui.gameGui.panel;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;

public class SelectionPanel
{
	private LabelBuilder objectNameLabel;
	private PanelBuilder selectionPanel;
	private LabelBuilder objectOwner;
	private boolean isVisibleSelectionPanel = false;
	private ImageBuilder occupyButton;
	private LabelBuilder productivityLabel;
	private int panelWidth;
	private int panelHeight;
	private ImageBuilder createTowerButton;
	private ImageBuilder attackTowerButton;
	private String objectName;

	public SelectionPanel(int windowWidth, int windowHeight)
	{
		panelWidth = 20 * windowWidth / 100;
		panelHeight = 20 * windowHeight / 100;
		occupyButton = new ImageBuilder()
		{
			{
				filename("occupy.png");
				width("35%");
				height("20%");

				x("23%");
				y("50%");
				visible(false);
				interactOnClick("onClickButtonOccupy()");

			}

		};

		createTowerButton = new ImageBuilder()
		{
			{
				filename("createTower.png");
				width("35%");
				height("35%");

				x("23%");
				y("50%");
				visible(false);
				interactOnClick("onClickButtonCreateTowerButton()");

			}

		};

		attackTowerButton = new ImageBuilder()
		{
			{
				filename("createTower.png");
				width("35%");
				height("35%");

				x("23%");
				y("50%");
				visible(false);
				interactOnClick("onClickButtonAttackTowerButton()");

			}

		};

		productivityLabel = new LabelBuilder("#productivityLabel")
		{
			{
				x("10%");
				color(Color.BLACK);
			}

		};
		objectNameLabel = new LabelBuilder("#objectName")
		{
			{
				x("10%");
				y("10%");
				color(Color.BLACK);
			}
		};

		objectOwner = new LabelBuilder("#objectOwner")
		{
			{
				x("10%");
				y("25%");
				color(Color.BLACK);
			}
		};

		selectionPanel = new PanelBuilder("#selected")
		{

			{
				childLayoutAbsoluteInside();

				width(Integer.toString(panelWidth));
				height(Integer.toString(panelHeight));

				image(new ImageBuilder()
				{
					{
						filename("selectedPanel.png");
						width("85%");
						height("85%");

					}
				});
				control(objectNameLabel);
				control(objectOwner);
				image(occupyButton);
				control(productivityLabel);
				image(createTowerButton);
				image(attackTowerButton);

			}
		};

	}

	public void emptyPanel()
	{
		occupyButton.visible(false);
		objectNameLabel.visible(false);
		objectOwner.visible(false);
		productivityLabel.visible(false);

	}

	public void setObjectName(String objectName)
	{
		this.objectName = objectName;
		this.objectNameLabel.text(objectName);
		this.objectNameLabel.visible(true);

	}

	public void setObjectOwner(String playerName)
	{
		if (playerName == null)
		{
			productivityLabel.y("33%");
			this.occupyButton.visible(true);
			objectOwner.visible(false);
		}
		else
		{
			productivityLabel.y("37%");
			this.objectOwner.text("Owner: " + playerName);

			if (/* playerName.equals(GuiObjectManager.getInstance().getPlayingPlayer()) && */(objectName.toLowerCase().equals("tower")))
				attackTowerButton.visible(true);

			objectOwner.visible(true);

			occupyButton.visible(false);
			if (!objectName.toLowerCase().equals("headquarter"))
			{
				createTowerButton.visible(true);
			}
		}

	}

	public void setProductivityLabel(String productivity)
	{
		if (!productivity.equals("-1"))
		{
			this.productivityLabel.text("Productivity: " + productivity);
			this.productivityLabel.visible(true);
		}
		else
		{
			this.productivityLabel.visible(false);
		}
	}

	public Element build(Nifty nifty, Screen currentScreen, Element parent)
	{
		Element element = selectionPanel.build(nifty, currentScreen, parent);
		return element;
	}

	public int getHeight()
	{
		return panelHeight;
	}

	public void changePosition(int x, int y)
	{
		selectionPanel.x(Integer.toString(x));
		selectionPanel.y(Integer.toString(y));

	}

	public int getWidth()
	{
		return panelWidth;
	}

	public boolean isVisible()
	{
		return isVisibleSelectionPanel;
	}

	public void setVisible(boolean visible)
	{
		isVisibleSelectionPanel = visible;
	}

}
