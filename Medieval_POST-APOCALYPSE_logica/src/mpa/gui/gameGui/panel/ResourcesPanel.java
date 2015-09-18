package mpa.gui.gameGui.panel;

import java.awt.Color;
import java.util.HashMap;

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

	private HashMap<String, LabelBuilder> resourcesValueLabel = new HashMap<>();

	private String[] resources = { "WOOD", "WHEAT", "IRON", "STONE", "HERBS" };
	private int widthPanel;
	private int xPanel = 0;

	private PanelBuilder informationPanel;

	private LabelBuilder labelHPAndMP;
	private LabelBuilder labelLevel;
	de.lessvoid.nifty.tools.Color color = null;

	public ResourcesPanel()
	{
		widthPanel = 100 / resources.length;

		labelHPAndMP = new LabelBuilder(GuiObjectManager.getInstance().getPlayingPlayer() + " HP_MP")
		{
			{
				width("100%");
				height("40%");
				x("20%");
				y("60%");
				backgroundColor(new de.lessvoid.nifty.tools.Color(1f, 1f, 1f, 0.8f));

			}
		};
		labelLevel = new LabelBuilder(GuiObjectManager.getInstance().getPlayingPlayer() + " Level")
		{
			{
				width("100%");
				height("40%");
				x("20%");
				y("10%");
				backgroundColor(new de.lessvoid.nifty.tools.Color(1f, 1f, 1f, 0.8f));

			}
		};
		initInformationPanel();
		initResources();
		initResourcePanel();

	}

	private void initInformationPanel()
	{
		informationPanel = new PanelBuilder("#informationPanel")
		{

			{
				childLayoutAbsoluteInside();

				width("20%");
				height("50%");
				alignLeft();
				valignTop();
				control(labelHPAndMP);
				control(labelLevel);

			}

		};

	}

	// TODO UNIRE CON L'ALTRO PANNELLO DELLE RISORSE
	private void initResourcePanel()
	{
		resourcesBackgroundPanel = new PanelBuilder("resourcesBackground")
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

	public void setPlayerHPAndMP(int HP, int MP)
	{
		labelHPAndMP.text("HP: " + HP + " " + "MP: " + MP);

	}

	public void setPlayerLevel(final String level)
	{
		labelLevel.text("Level: " + level);

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

	public void updateInformationPanel(Nifty nifty, int HP, int MP, String level)
	{
		Color playngPlayerColor = GuiObjectManager.getInstance().getPlayngPlayerColor();

		if (color == null && playngPlayerColor != null)
		{
			color = new de.lessvoid.nifty.tools.Color((float) playngPlayerColor.getRed() / 255, (float) playngPlayerColor.getGreen() / 255,
					(float) playngPlayerColor.getBlue() / 255, 1);
			labelHPAndMP.color(color);
			labelLevel.color(color);
		}

		labelHPAndMP.text("HP: " + HP + " " + "MP: " + MP);
		labelLevel.text("Level: " + level);

		if (nifty.getCurrentScreen().findElementByName("#informationPanel") != null)
		{

			nifty.getCurrentScreen().findElementByName("#informationPanel").markForRemoval();
			nifty.removeElement(nifty.getCurrentScreen(), nifty.getCurrentScreen().findElementByName("#informationPanel"));
			Element findElementByName = nifty.getCurrentScreen().findElementByName("resourcesBackground");
			findElementByName.add(informationPanel.build(nifty, nifty.getCurrentScreen(), findElementByName));
		}
	}

	public LabelBuilder getResourceLabel(String resourceName)
	{
		return resourcesValueLabel.get(resourceName);
	}
}
