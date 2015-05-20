package mpa.gui.gameGui.panel;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.controls.scrollpanel.builder.ScrollPanelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class OpponentPropertiesPanel
{

	// private LabelBuilder objectName;
	ScrollPanelBuilder mainPanelBuilder;

	// private PanelBuilder selectionPanel;
	// private LabelBuilder objectOwner;
	// private boolean isVisibleSelectionPanel = false;
	// private ButtonBuilder occupyButton;
	// private LabelBuilder productivityLabel;

	public OpponentPropertiesPanel()
	{

		final ImageBuilder imageBuilder = new ImageBuilder()
		{
			{
				filename("selectedPanel.png");
				width("100%");
				height("100%");

			}
		};
		mainPanelBuilder = new ScrollPanelBuilder("#opponentPropertiesPanelId")
		{
			{
				image(imageBuilder);

			}
		};

		mainPanelBuilder.set("horizontal", "false");
		mainPanelBuilder.set("width", "60%");
		mainPanelBuilder.set("height", "60%");
		mainPanelBuilder.set("x", "20%");
		mainPanelBuilder.set("y", "20%");
		mainPanelBuilder.set("image", "imageBuilder");

		// occupyButton = new ButtonBuilder("#occupy", "Occupy")
		// {
		// {
		// childLayoutAbsoluteInside();
		// width("30%");
		// height("10%");
		//
		// x("15%");
		// y("50%");
		//
		// visible(false);
		// interactOnClick("onClickButtonOccupy()");
		//
		// }
		// };
		//
		// productivityLabel = new LabelBuilder("#productivityLabel")
		// {
		// {
		// x("10%");
		// y("30%");
		// }
		//
		// };
		// objectName = new LabelBuilder("#objectName")
		// {
		// {
		// x("10%");
		// y("10%");
		// }
		// };
		//
		// objectOwner = new LabelBuilder("#objectOwner")
		// {
		// {
		// x("10%");
		// y("20%");
		// }
		// };
		//
		// selectionPanel = new PanelBuilder("#selected")
		// {
		//
		// {
		// childLayoutAbsoluteInside();
		//
		// x("33%");
		// y("83%");
		// width("20%");
		// height("20%");
		//
		// image(new ImageBuilder()
		// {
		// {
		// filename("selectedPanel.png");
		// width("85%");
		// height("85%");
		//
		// }
		// });
		//
		// control(objectName);
		// control(objectOwner);
		// control(occupyButton);
		// control(productivityLabel);
		//
		// }
		// };

	}

	public Element build(Nifty nifty, Screen currentScreen, Element parent)
	{
		Element element = mainPanelBuilder.build(nifty, currentScreen, parent);
		return element;
	}

}
