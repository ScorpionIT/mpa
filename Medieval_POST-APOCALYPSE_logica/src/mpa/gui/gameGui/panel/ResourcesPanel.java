package mpa.gui.gameGui.panel;

import java.util.HashMap;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;

public class ResourcesPanel
{

	private PanelBuilder resourcesPanel;

	private HashMap<String, LabelBuilder> resourcesValueLabel = new HashMap<>();

	private String[] resources = { "WOOD", "WHEAT", "IRON", "STONE", "HERBS" };
	private int widthPanel;
	private int xPanel = 0;

	public ResourcesPanel()
	{
		widthPanel = 100 / resources.length;
		initResources();
		initResourcePanel();

	}

	// TODO UNIRE CON L'ALTRO PANNELLO DELLE RISORSE
	private void initResourcePanel()
	{
		resourcesPanel = new PanelBuilder("resources")
		{

			{
				childLayoutAbsoluteInside();

				width("50%");
				height("10%");
				alignRight();
				valignTop();
				x("50%");
				y("90%");
				// backgroundColor("#FFD425");
				visibleToMouse(true);

			}

		};
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

				y("35%");
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
		return resourcesPanel.build(nifty, currentScreen, parent);
	}

	public LabelBuilder getResourceLabel(String resourceName)
	{
		return resourcesValueLabel.get(resourceName);
	}
}
