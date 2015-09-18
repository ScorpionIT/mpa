package mpa.gui.gameGui.panel;

import java.util.HashMap;
import java.util.Set;

import mpa.core.logic.resource.Resources;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;

public class OpponentResourcesPanel
{

	private PanelBuilder resourcesPanel;

	private int xPanel = 0;
	private int height;
	private int width;
	private int y;
	private int x;
	private HashMap<String, Integer> resources;
	private int widthPanel;
	private String playerName;
	private HashMap<String, LabelBuilder> resourcesValueLabel = new HashMap<>();
	private LabelBuilder labelNameBuilder;
	private LabelBuilder labelHPAndMP;
	private Color playerColor;

	public OpponentResourcesPanel(HashMap<String, Integer> resources, int x, int y, int width, int height, String playerName, Color color)
	{
		this.resources = resources;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.playerName = playerName;
		// this.playerName = this.playerName.replaceAll( "\\s+", "" );
		playerColor = color;

		widthPanel = 100 / resources.size();
		initResources();
		initResourcePanel();

	}

	private void initResourcePanel()
	{

		labelNameBuilder = getLabelCenterBuilder(playerName, x + 20, 0, 15);

		labelHPAndMP = getLabelCenterBuilder(playerName + "HP_MP", x + 20, 15, 15);

		resourcesPanel = new PanelBuilder("#resources" + playerName)
		{

			{
				childLayoutAbsoluteInside();

				width(Integer.toString(width) + "%");
				height(Integer.toString(height) + "%");
				x(Integer.toString(x) + "%");
				y(Integer.toString(y) + "%");
				control(labelNameBuilder);
				control(labelHPAndMP);
				visibleToMouse(true);

			}

		};
		addPanels();

	}

	private void addPanels()
	{

		xPanel = 0;

		Set<String> keySet = resources.keySet();
		for (final String resource : keySet)
		{
			resourcesPanel.panel(getPanelBuilder(resource.toString(), resource.toString() + ".png"));
			xPanel += widthPanel;
		}
	}

	public void setPlayerLevel(final String level)
	{
		labelNameBuilder.text(playerName + " (" + level + ")");

	}

	public void setPlayerHPAndMP(int HP, int MP)
	{
		labelHPAndMP.text("HP: " + HP + " " + "MP: " + MP);

	}

	private ImageBuilder getImageBuilder(final String fileName)
	{
		return new ImageBuilder()
		{
			{
				filename(fileName);

				y("40%");
				width("40%");
				height("40%");

			}
		};
	}

	private PanelBuilder getPanelBuilder(final String resourceName, final String fileName)
	{

		return new PanelBuilder("#" + resourceName + "Panel" + playerName)
		{
			{
				childLayoutCenter();

				valignTop();
				x(Integer.toString(xPanel) + "%");
				y("38%");

				width(Integer.toString(widthPanel) + "%");
				height("72%");

				control(getLabelBuilder(resourceName));
				image(getImageBuilder(fileName));

				control(resourcesValueLabel.get(resourceName));
			}
		};
	}

	private LabelBuilder getLabelBuilder(final String text)
	{
		return new LabelBuilder("#" + text + "Label " + playerName)
		{
			{
				text(text);
				valignTop();

			}
		};
	}

	private LabelBuilder getLabelBuilder(final String text, final int x, final int y, final int width, final int height)
	{

		return new LabelBuilder("# " + "Label" + playerName)
		{
			{
				childLayoutAbsoluteInside();
				width(Integer.toString(width) + "%");
				height(Integer.toString(height) + "%");
				x(Integer.toString(x) + "%");
				y(Integer.toString(y) + "%");
				backgroundColor(new Color(1f, 1f, 1f, 0.8f));
				color(playerColor);

			}
		};
	}

	private LabelBuilder getLabelCenterBuilder(final String text, final int x, final int y, final int height)
	{

		return new LabelBuilder("# " + "Label" + playerName)
		{
			{
				childLayoutAbsoluteInside();
				alignCenter();
				height(Integer.toString(height) + "%");
				x(Integer.toString(x) + "%");
				y(Integer.toString(y) + "%");
				backgroundColor(new Color(1f, 1f, 1f, 0.8f));
				color(playerColor);

			}
		};
	}

	private void initResources()
	{

		Set<String> keySet = resources.keySet();
		for (final String resource : keySet)
		{
			resourcesValueLabel.put(resource.toString(), new LabelBuilder("#Avaible " + resource.toString() + " Label " + playerName)
			{
				{
					valignBottom();
					text(Integer.toString(resources.get(resource)));
					// color(Color.BLACK);

				}
			});
		}

	}

	public void updateResources(HashMap<String, Integer> resources)
	{
		this.resources = resources;

		Set<String> keySet = this.resources.keySet();
		for (String resource : keySet)
		{
			resourcesValueLabel.get(resource.toString()).text(Integer.toString(resources.get(resource)));
		}
	}

	public PanelBuilder getPanel()
	{
		return resourcesPanel;
	}

	public void setX(int x)
	{

	}

	public void setY(int y)
	{

	}

	public Element build(Nifty nifty, Screen currentScreen, Element parent)
	{
		return resourcesPanel.build(nifty, currentScreen, parent);
	}

	public LabelBuilder getResourceLabel(Resources resourceName)
	{
		return resourcesValueLabel.get(resourceName);
	}

}
