package mpa.gui.gameGui.panel;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import mpa.gui.gameGui.playingGUI.GuiObjectManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class ResourcesPanel
{

	private PanelBuilder resourcesBackgroundPanel;
	private PanelBuilder resourcesPanel;

	private Map<String, LabelBuilder> resourcesValueLabel = new HashMap<>();

	private String[] resources = { "WOOD", "WHEAT", "IRON", "STONE", "HERBS" };
	private int widthPanel;
	private int xPanel = 0;

	private PanelBuilder informationPanel;

	private LabelBuilder labelHPAndMP;
	private LabelBuilder labelLevel;
	private ImageBuilder buyHPPotion;

	de.lessvoid.nifty.tools.Color color = null;
	private ImageBuilder buyMPPotion;
	private PanelBuilder buttonsPotionsPanel;
	private ImageBuilder buyGranade;
	private LabelBuilder labelHPPotion;
	private LabelBuilder labelMPPotion;
	private LabelBuilder labelGranade;

	public ResourcesPanel()
	{
		widthPanel = 100 / resources.length;

		initInformationPanel();
		initButtonsPotionsPanel();

		initResources();
		initResourcePanel();

	}

	private void initLabels()
	{
		labelLevel = new LabelBuilder(GuiObjectManager.getInstance().getPlayingPlayer() + " Level")
		{
			{
				width("40%");
				height("15%");
				alignCenter();
				x("30%");
				y("5%");
				backgroundColor(new de.lessvoid.nifty.tools.Color(1f, 1f, 1f, 0.8f));

			}
		};
		labelHPAndMP = new LabelBuilder(GuiObjectManager.getInstance().getPlayingPlayer() + " HP_MP")
		{
			{
				width("40%");
				height("15%");
				alignCenter();
				x("30%");
				y("25%");
				backgroundColor(new de.lessvoid.nifty.tools.Color(1f, 1f, 1f, 0.8f));

			}
		};

		labelHPPotion = new LabelBuilder(GuiObjectManager.getInstance().getPlayingPlayer() + " HPPotion")
		{
			{
				width("40%");
				height("15%");
				alignCenter();
				x("30%");
				y("45%");
				backgroundColor(new de.lessvoid.nifty.tools.Color(1f, 1f, 1f, 0.8f));

			}
		};

		labelMPPotion = new LabelBuilder(GuiObjectManager.getInstance().getPlayingPlayer() + " MPPotion")
		{
			{
				width("40%");
				height("15%");
				alignCenter();
				x("30%");
				y("65%");
				backgroundColor(new de.lessvoid.nifty.tools.Color(1f, 1f, 1f, 0.8f));

			}
		};

		labelGranade = new LabelBuilder(GuiObjectManager.getInstance().getPlayingPlayer() + " Granade")
		{
			{
				width("40%");
				height("15%");
				x("30%");
				y("85%");
				backgroundColor(new de.lessvoid.nifty.tools.Color(1f, 1f, 1f, 0.8f));

			}
		};
	}

	private void initInformationPanel()
	{
		initLabels();
		informationPanel = new PanelBuilder("#informationPanel")
		{

			{
				childLayoutAbsoluteInside();
				width("20%");
				height("100%");
				alignLeft();
				valignTop();
				control(labelHPAndMP);
				control(labelLevel);
				control(labelHPPotion);
				control(labelMPPotion);
				control(labelGranade);

			}

		};

	}

	private void initButtonsPotionsPanel()
	{
		initImageBuyPotions();
		buttonsPotionsPanel = new PanelBuilder("#buttonsPotionsPanel")
		{

			{
				childLayoutAbsoluteInside();

				width("20%");
				height("100%");
				x("25%");
				y("15%");
				image(buyHPPotion);
				image(buyMPPotion);
				image(buyGranade);

			}

		};

	}

	private void initImageBuyPotions()
	{
		buyHPPotion = new ImageBuilder()
		{
			{
				filename("buyHPPotion.png");
				width("50%");
				height("30%");

				x("0%");
				y("10%");
				interactOnClick("onClickBuyHPPotion()");

			}

		};

		buyMPPotion = new ImageBuilder()
		{
			{
				filename("buyMPPotion.png");
				width("50%");
				height("30%");

				x("0%");
				y("40%");
				interactOnClick("onClickBuyMPPotion()");

			}

		};

		buyGranade = new ImageBuilder()
		{
			{
				filename("buyGranade.png");
				width("50%");
				height("30%");

				x("0%");
				y("75%");
				interactOnClick("onClickBuyGranade()");

			}

		};
	}

	// TODO UNIRE CON L'ALTRO PANNELLO DELLE RISORSE
	private void initResourcePanel()
	{
		resourcesBackgroundPanel = new PanelBuilder("#resourcesBackground")
		{

			{
				childLayoutAbsoluteInside();

				width("100%");
				height("12%");
				alignRight();
				valignTop();
				x("0%");
				y("88%");
				image(new ImageBuilder()
				{
					{
						filename("pannello.jpg");
						width("100%");
						height("100%");

					}
				});
				panel(informationPanel);
				panel(buttonsPotionsPanel);

			}

		};

		resourcesPanel = new PanelBuilder("resources")
		{

			{
				childLayoutAbsoluteInside();

				width("50%");
				height("100%");
				alignRight();
				valignTop();
				x("50%");
				y("0%");

			}

		};
		resourcesBackgroundPanel.panel(resourcesPanel);
		addPanels();

	}

	private void addPanels()
	{

		xPanel = 0;
		for (String resourceName : resources)
		{
			resourcesPanel.panel(getPanelBuilder(resourceName.toString(), resourceName.toString() + ".png"));
			xPanel += widthPanel;
		}
	}

	private ImageBuilder getImageBuilder(final String fileName)
	{
		return new ImageBuilder()
		{
			{
				filename(fileName);

				y("37%");
				width("40%");
				height("40%");

			}
		};
	}

	private PanelBuilder getPanelBuilder(final String resourceName, final String fileName)
	{

		return new PanelBuilder(resourceName + "Panel")
		{
			{
				childLayoutCenter();

				valignTop();
				x(Integer.toString(xPanel) + "%");
				y("0%");

				width(Integer.toString(widthPanel) + "%");
				height("100%");

				control(getLabelBuilder(resourceName));
				image(getImageBuilder(fileName));

				control(resourcesValueLabel.get(resourceName));
			}
		};
	}

	private LabelBuilder getLabelBuilder(final String text)
	{
		return new LabelBuilder(text + "Label")
		{
			{
				text(text);
				valignTop();

			}
		};
	}

	private void initResources()
	{

		for (String resoruceName : resources)
		{
			resourcesValueLabel.put(resoruceName.toString(), new LabelBuilder("Avaible" + resoruceName)
			{
				{
					valignBottom();
				}
			});
		}

	}

	public PanelBuilder get()
	{
		return resourcesPanel;
	}

	public Element build(Nifty nifty, Screen currentScreen, Element parent)
	{
		return resourcesBackgroundPanel.build(nifty, currentScreen, parent);
	}

	public void updateInformationPanel(Nifty nifty, int HP, int MP, String level, int numberOfHPPotions, int numberOfMPPotions, int numberOfGranades)
	{
		Color playingPlayerColor = GuiObjectManager.getInstance().getPlayingPlayerColor();

		if (color == null && playingPlayerColor != null)
		{
			color = new de.lessvoid.nifty.tools.Color((float) playingPlayerColor.getRed() / 255, (float) playingPlayerColor.getGreen() / 255,
					(float) playingPlayerColor.getBlue() / 255, 1);
			labelHPAndMP.color(color);
			labelLevel.color(color);
			labelHPPotion.color(color);
			labelMPPotion.color(color);
			labelGranade.color(color);
		}

		labelHPAndMP.text("HP: " + HP + " " + "MP: " + MP);
		labelLevel.text("Level: " + level);
		labelMPPotion.text("MP Potions : " + numberOfMPPotions);
		labelHPPotion.text("HP Potions : " + numberOfHPPotions);
		labelGranade.text("Granade : " + numberOfGranades);
		if (nifty.getCurrentScreen().findElementByName("#informationPanel") != null)
		{
			nifty.getCurrentScreen().findElementByName("#informationPanel").markForRemoval();
			nifty.removeElement(nifty.getCurrentScreen(), nifty.getCurrentScreen().findElementByName("#informationPanel"));
			Element findElementByName = nifty.getCurrentScreen().findElementByName("#resourcesBackground");
			findElementByName.add(informationPanel.build(nifty, nifty.getCurrentScreen(), findElementByName));
		}

	}

	public void updateResourceLabel(Nifty nifty, String resourceName, int value)
	{

		LabelBuilder labelBuilder = resourcesValueLabel.get(resourceName);
		labelBuilder.text(Integer.toString(value));

		// rebuildResourcesLabel(labelBuilder, resourceName, nifty);
		//
		// Element findElementByName = nifty.getCurrentScreen().findElementByName(resourceName +
		// "Panel");
		// findElementByName.add(labelBuilder.build(nifty, nifty.getCurrentScreen(),
		// findElementByName));
	}

	private void rebuildResourcesLabel(LabelBuilder labelBuilder, String resourceName, Nifty nifty)
	{
		if (nifty.getCurrentScreen().findElementByName("Avaible" + resourceName) != null)
		{
			nifty.getCurrentScreen().findElementByName("Avaible" + resourceName).markForRemoval();
			nifty.removeElement(nifty.getCurrentScreen(), nifty.getCurrentScreen().findElementByName("Avaible" + resourceName));
			Element findElementByName = nifty.getCurrentScreen().findElementByName(resourceName + "Panel");
			findElementByName.add(labelBuilder.build(nifty, nifty.getCurrentScreen(), findElementByName));
		}
	}
}
