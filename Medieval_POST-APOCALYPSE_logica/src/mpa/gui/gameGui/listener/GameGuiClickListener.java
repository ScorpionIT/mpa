package mpa.gui.gameGui.listener;

import java.util.ArrayList;

import mpa.gui.gameGui.panel.NiftyHandler;
import mpa.gui.gameGui.playingGUI.GameGui;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class GameGuiClickListener implements ActionListener
{

	private ListenerImplementation listener;
	private GameGui gameGui;

	private NiftyHandler niftyHandler;

	public GameGuiClickListener(ListenerImplementation listener, GameGui gameGui)
	{
		this.listener = listener;
		this.gameGui = gameGui;
		niftyHandler = gameGui.getNiftyHandler();
	}

	static int c = 1;

	@Override
	public void onAction(String name, boolean isPressed, float tpf)
	{

		// System.out.println( "ci sono entrato  per la " + c++ );
		Vector2f click = gameGui.getInputManager().getCursorPosition();

		Vector3f cursor = gameGui.getCamera().getWorldCoordinates(new Vector2f(click.x, click.y), 0.0f).clone();

		// System.out.println( "la pos di cursor è " + cursor );

		Vector3f dir = gameGui.getCamera().getWorldCoordinates(new Vector2f(click.x, click.y), 1.0f).subtractLocal(cursor).normalizeLocal();
		// System.out.println( "la dir è " + dir );

		Ray ray = new Ray();

		ray.setOrigin(cursor);
		ray.setDirection(new Vector3f(dir.x, dir.y, dir.z));
		CollisionResults crs = new CollisionResults();
		gameGui.getGroundNode().collideWith(ray, crs);
		Vector2f contactPoint;

		if (!isPressed && name.equals("Click"))
		{

			contactPoint = new Vector2f(crs.getClosestCollision().getContactPoint().x, crs.getClosestCollision().getContactPoint().z);
			String pickedObject = listener.getPickedObject(contactPoint);

			System.out.println("ho cliccato su " + pickedObject);
			if (!isPressed && pickedObject.equals("GROUND"))
			{
				if (!gameGui.canClick())
				{
					if (niftyHandler.isVisibleSelectionPanel())
					{
						niftyHandler.removeSelectedPanel();
					}
				}
				else
				{
					System.out.println("ci entro?");
					listener.computePath(contactPoint);
				}
			}
			else if (!pickedObject.equals("GROUND") && !niftyHandler.isVisibleChoosePanel() && !niftyHandler.isVisibleOpponentPropertiesPanel())
			{

				String[] split = pickedObject.split(":");
				String pickedObjectOwner = listener.getPickedObjectOwner(split[0], split[1]);
				String objectProductivity = Integer.toString(listener.getPickedObjectProductivity(split[0], split[1]));

				niftyHandler.removeSelectedPanel();
				niftyHandler.setSelectedPanel(split[0], split[1], objectProductivity, pickedObjectOwner);
				niftyHandler.relocateSelectionPanel((int) click.x, gameGui.windowHeight() - (int) click.y);
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

		else if ("ChooseItem".equals(name))
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

		if ("attack".equals(name) && isPressed)
		{

			// String player = GuiObjectManager.getInstance().getPlayingPlayer();

			// TODO cambiare azione in base all'item selezionato
			if (crs.getClosestCollision() != null)
			{

				contactPoint = new Vector2f(crs.getClosestCollision().getContactPoint().x, crs.getClosestCollision().getContactPoint().z);
				// contactPoint = new Vector2f(crs.getClosestCollision().getContactPoint().x,
				// crs.getClosestCollision().getContactPoint().z);

				ArrayList<String> hitPlayers = listener.playerAction(contactPoint);

				if (hitPlayers.isEmpty())
					System.out.println("non ho colpito niente");
				else
					for (String pl : hitPlayers)
						System.out.println("ho colpito " + pl);

			}
		}
	}
}
