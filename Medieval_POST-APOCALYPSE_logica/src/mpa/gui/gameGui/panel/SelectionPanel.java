package mpa.gui.gameGui.panel;

import mpa.core.logic.character.Player;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class SelectionPanel
{
	private LabelBuilder objectName;
	private PanelBuilder selectionPanel;
	private LabelBuilder objectOwner;
	private boolean isVisibleSelectionPanel = false;
	private ButtonBuilder occupyButton;
	private LabelBuilder productivityLabel;

	public SelectionPanel()
	{
		occupyButton = new ButtonBuilder("#occupy", "Occupy")
		{
			{
				childLayoutAbsoluteInside();
				width("30%");
				height("10%");

				x("15%");
				y("50%");

				visible(false);
				interactOnClick("onClickButtonOccupy()");

			}
		};

		productivityLabel = new LabelBuilder("#productivityLabel")
		{
			{
				x("10%");
				y("34%");
			}

		};
		objectName = new LabelBuilder("#objectName")
		{
			{
				x("10%");
				y("10%");
			}
		};

		objectOwner = new LabelBuilder("#objectOwner")
		{
			{
				x("10%");
				y("22%");
			}
		};

		selectionPanel = new PanelBuilder("#selected")
		{

			{
				childLayoutAbsoluteInside();

				x("33%");
				y("83%");
				width("20%");
				height("20%");

				image(new ImageBuilder()
				{
					{
						filename("selectedPanel.png");
						width("85%");
						height("85%");

					}
				});

				control(objectName);
				control(objectOwner);
				control(occupyButton);
				control(productivityLabel);

			}
		};

	}

	public void emptyPanel()
	{
		occupyButton.visible(false);
		objectName.visible(false);
		objectOwner.visible(false);
		productivityLabel.visible(false);

	}

	public void setObjectName(String objectName)
	{
		this.objectName.text(objectName);
		this.objectName.visible(true);
	}

	public void setObjectOwner(Player player)
	{
		if (player == null)
		{
			this.occupyButton.visible(true);
			objectOwner.visible(false);
		}
		else
		{
			this.objectOwner.text("Owner: " + player.getName());
			objectOwner.visible(true);
			occupyButton.visible(false);
		}

	}

	public void setProductivityLabel(int productivity)
	{
		this.productivityLabel.text("Productivity: " + Integer.toString(productivity));
		this.productivityLabel.visible(true);
	}

	public Element build(Nifty nifty, Screen currentScreen, Element parent)
	{
		Element element = selectionPanel.build(nifty, currentScreen, parent);
		return element;
	}

	public boolean getIsVisible()
	{
		return isVisibleSelectionPanel;
	}
}
