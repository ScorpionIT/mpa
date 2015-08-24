package mpa.gui.gameGui.listener;

import java.util.ArrayList;

import mpa.core.logic.GameManager;
import mpa.core.logic.character.Player;
import mpa.core.logic.character.Player.Item;
import mpa.gui.gameGui.GameGui;
import mpa.gui.gameGui.panel.NiftyHandler;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class GameGuiClickListener implements ActionListener
{

	private GameGui gameGui;
	private boolean choosePanel = false;

	public GameGuiClickListener( GameGui gameGui )
	{

		this.gameGui = gameGui;
	}

	static int c = 1;

	@Override
	public void onAction( String name, boolean isPressed, float tpf )
	{
		NiftyHandler niftyHandler = gameGui.getNiftyHandler();
		// System.out.println( "ci sono entrato  per la " + c++ );
		Vector2f click = gameGui.getInputManager().getCursorPosition();

		Vector3f cursor = gameGui.getCamera()
				.getWorldCoordinates( new Vector2f( click.x, click.y ), 0.0f ).clone();

		// System.out.println( "la pos di cursor è " + cursor );

		Vector3f dir = gameGui.getCamera()
				.getWorldCoordinates( new Vector2f( click.x, click.y ), 1.0f )
				.subtractLocal( cursor ).normalizeLocal();
		// System.out.println( "la dir è " + dir );

		Ray ray = new Ray();

		ray.setOrigin( cursor );
		ray.setDirection( new Vector3f( dir.x, dir.y, dir.z ) );

		CollisionResults crs = new CollisionResults();
		gameGui.getGroundNode().collideWith( ray, crs );

		if( "Wheel_DOWN".equals( name ) )
		{
			if( choosePanel )
			{
				niftyHandler.changeChoosenElement( false );
			}

		}
		else if( "Wheel_UP".equals( name ) )
		{
			if( choosePanel )
			{
				niftyHandler.changeChoosenElement( true );
			}
		}

		else if( "Click".equals( name ) && isPressed )
		{

			if( crs.getClosestCollision() != null )
			{
				gameGui.getNiftyHandler().emptySelectedPanel();
				Vector2f contactPoint = new Vector2f(
						crs.getClosestCollision().getContactPoint().x, crs.getClosestCollision()
								.getContactPoint().z );

				if( GameManager.getInstance().getWorld()
						.pickedObject( contactPoint.x, contactPoint.y ) == null )
				{
					GameManager.getInstance().computePath( gameGui.getPlayingPlayer(),
							contactPoint.x, contactPoint.y );

					gameGui.setPath( gameGui.getPlayingPlayer().getPath() );
					gameGui.drawPath();
				}
				else
				{

					gameGui.getNiftyHandler().setSelectedPanel(
							GameManager.getInstance().getWorld()
									.pickedObject( contactPoint.x, contactPoint.y ) );
				}
			}
		}
		else if( "ChooseItem".equals( name ) )
		{
			if( isPressed )
			{
				choosePanel = true;
				niftyHandler.relocateChoosePanel( ( int ) click.x, gameGui.windowHeight()
						- ( int ) click.y );
			}
			else
			{
				choosePanel = false;
				Item selectedItem = niftyHandler.getSelectedItem();
				GameManager.getInstance().changeSelectedItem( gameGui.getPlayingPlayer(),
						selectedItem );

				niftyHandler.removeChoosePanel();
				niftyHandler.initChoosenElementPanel();
			}
		}

		else if( "attack".equals( name ) )
		{

			Player player = gameGui.getPlayingPlayer();

			// TODO cambiare azione in base all'item selezionato

			if( crs.getClosestCollision() != null )
			{

				javax.vecmath.Vector2f contactPoint = new javax.vecmath.Vector2f( crs
						.getClosestCollision().getContactPoint().x, crs.getClosestCollision()
						.getContactPoint().z );
				player.setPlayerDirection( contactPoint );

				ArrayList<Player> p = GameManager.getInstance().playerAction(
						gameGui.getPlayingPlayer(), contactPoint );

				if( p.isEmpty() )
					System.out.println( "non ho colpito niente" );
				else
					for( Player pl : p )
						System.out.println( "ho colpito " + pl.getName() );

			}
		}
	}
	// @Override
	// public void onAnalog( String arg0, float arg1, float arg2 )
	// {
	// if( "Click".equals( arg0 ) )
	// {
	//
	// Vector2f click = gameGui.getInputManager().getCursorPosition();
	//
	// Vector3f cursor = gameGui.getCamera()
	// .getWorldCoordinates( new Vector2f( click.x, click.y ), 0.0f ).clone();
	//
	// System.out.println( "la pos di cursor è " + cursor );
	//
	// Vector3f dir = gameGui.getCamera()
	// .getWorldCoordinates( new Vector2f( click.x, click.y ), 1.0f )
	// .subtractLocal( cursor ).normalizeLocal();
	// System.out.println( "la dir è " + dir );
	//
	// Ray ray = new Ray();
	//
	// ray.setOrigin( cursor );
	// ray.setDirection( new Vector3f( dir.x, dir.y, dir.z ) );
	//
	// CollisionResults crs = new CollisionResults();
	// gameGui.getGroundNode().collideWith( ray, crs );
	//
	// if( crs.getClosestCollision() != null )
	// {
	// gameGui.getNiftyHandler().emptySelectedPanel();
	// Vector2f contactPoint = new Vector2f(
	// crs.getClosestCollision().getContactPoint().x, crs.getClosestCollision()
	// .getContactPoint().z );
	//
	// if( GameManager.getInstance().getWorld()
	// .pickedObject( contactPoint.x, contactPoint.y ) == null )
	// {
	// GameManager.getInstance().computePath(
	// gameGui.getPlayers().get( gameGui.getPlayerIndex() ), contactPoint.x,
	// contactPoint.y );
	//
	// gameGui.setPath( gameGui.getPlayers().get( gameGui.getPlayerIndex() ).getPath() );
	// gameGui.drawPath();
	// }
	// else
	// {
	//
	// gameGui.getNiftyHandler().setSelectedPanel(
	// GameManager.getInstance().getWorld()
	// .pickedObject( contactPoint.x, contactPoint.y ) );
	// }
	// }
	// }
	// }

}
