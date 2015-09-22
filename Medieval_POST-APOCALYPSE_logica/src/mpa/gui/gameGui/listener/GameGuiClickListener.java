package mpa.gui.gameGui.listener;

import mpa.gui.gameGui.panel.NiftyHandler;
import mpa.gui.gameGui.playingGUI.GameGui;
import mpa.gui.gameGui.playingGUI.GuiObjectManager;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class GameGuiClickListener implements ActionListener
{

	private HandlerImplementation listener;
	private GameGui gameGui;

	private NiftyHandler niftyHandler;

	public GameGuiClickListener(HandlerImplementation listener, GameGui gameGui)
	{
		this.listener = listener;
		this.gameGui = gameGui;
		niftyHandler = gameGui.getNiftyHandler();
	}

	static int c = 1;

	@Override
	public void onAction(String name, boolean isPressed, float tpf)
	{

		Vector2f click = gameGui.getInputManager().getCursorPosition();

		Vector3f cursor = gameGui.getCamera().getWorldCoordinates(new Vector2f(click.x, click.y), 0.0f).clone();

		Vector3f dir = gameGui.getCamera().getWorldCoordinates(new Vector2f(click.x, click.y), 1.0f).subtractLocal(cursor).normalizeLocal();

		Ray ray = new Ray();

		ray.setOrigin(cursor);
		ray.setDirection(new Vector3f(dir.x, dir.y, dir.z));
		CollisionResults crs = new CollisionResults();
		gameGui.getGroundNode().collideWith(ray, crs);
		Vector2f contactPoint;

		if (!isPressed && (name.equals("Click") || name.equals("attack")) && !gameGui.canClick())
		{
			niftyHandler.removeVisiblePanel();
			return;
		}
		if (!isPressed && name.equals("Click") && crs.getClosestCollision() != null)
		{

			contactPoint = new Vector2f(crs.getClosestCollision().getContactPoint().x, crs.getClosestCollision().getContactPoint().z);
			String pickedObject = listener.getPickedObject(contactPoint);

			if (!isPressed && pickedObject.equals("GROUND"))
			{
				listener.computePath(contactPoint);

			}

			else if (!pickedObject.equals("GROUND"))
			{

				String[] split = pickedObject.split(":");
				String pickedObjectOwner = listener.getPickedObjectOwner(split[0], split[1]);
				String objectProductivity = Integer.toString(listener.getPickedObjectProductivity(split[0], split[1]));

				if (split[0].toLowerCase().equals("headquarter") && pickedObjectOwner.equals(GuiObjectManager.getInstance().getPlayingPlayer()))
				{
					niftyHandler.setHeadquarterPanel(split[0], split[1]);
				}
				else
				{
					niftyHandler.removeSelectedPanel();
					niftyHandler.setSelectedPanel(split[0], split[1], objectProductivity, pickedObjectOwner);
					niftyHandler.relocateSelectionPanel((int) click.x, gameGui.windowHeight() - (int) click.y);
				}
			}
		}
		if ("Wheel_DOWN".equals(name) && isPressed)
		{
			if (niftyHandler.isVisibleChoosePanel())
			{
				niftyHandler.changeChoosenElement(false);
			}

		}
		else if ("Wheel_UP".equals(name) && isPressed)
		{
			if (niftyHandler.isVisibleChoosePanel())
			{
				niftyHandler.changeChoosenElement(true);
			}
		}

		else if ("ChooseItem".equals(name) && niftyHandler.canCreateChooseItemPanel())
		{
			if (isPressed)
			{
				niftyHandler.relocateChoosePanel((int) click.x, gameGui.windowHeight() - (int) click.y);
			}
			else if (!isPressed)
			{
				String selectedItem = niftyHandler.getSelectedItem();
				listener.changeItem(selectedItem);

				niftyHandler.removeChooseItemPanel();
			}
		}

		if ("attack".equals(name) && isPressed && gameGui.canClick())
		{
			if (crs.getClosestCollision() != null)
			{

				contactPoint = new Vector2f(crs.getClosestCollision().getContactPoint().x, crs.getClosestCollision().getContactPoint().z);
				listener.playerAction(contactPoint);
			}
		}
	}
}
