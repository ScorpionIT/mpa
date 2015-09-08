//package mpa.gui.gameGui.listener;
//
//import java.util.ArrayList;
//
//import mpa.core.logic.GameManager;
//import mpa.core.logic.GameManagerProxy;
//import mpa.gui.gameGui.playingGUI.GameGui;
//import mpa.gui.gameGui.playingGUI.GuiObjectManager;
//
//import com.jme3.collision.CollisionResults;
//import com.jme3.input.controls.ActionListener;
//import com.jme3.math.Ray;
//import com.jme3.math.Vector2f;
//import com.jme3.math.Vector3f;
//
//public class GameGuiClickListener implements ActionListener
//{
//
//	private ListenerImplementation listener;
//	private GameGui gameGui;
//	private boolean choosePanel = false;
//
//	public GameGuiClickListener( ListenerImplementation listener, GameGui gameGui )
//	{
//		this.listener = listener;
//		this.gameGui = gameGui;
//	}
//
//	static int c = 1;
//
//	@Override
//	public void onAction( String name, boolean isPressed, float tpf )
//	{
//		// NiftyHandler niftyHandler = gameGui.getNiftyHandler();
//		// System.out.println( "ci sono entrato  per la " + c++ );
//		Vector2f click = gameGui.getInputManager().getCursorPosition();
//
//		Vector3f cursor = gameGui.getCamera()
//				.getWorldCoordinates( new Vector2f( click.x, click.y ), 0.0f ).clone();
//
//		// System.out.println( "la pos di cursor è " + cursor );
//
//		Vector3f dir = gameGui.getCamera()
//				.getWorldCoordinates( new Vector2f( click.x, click.y ), 1.0f )
//				.subtractLocal( cursor ).normalizeLocal();
//		// System.out.println( "la dir è " + dir );
//
//		Ray ray = new Ray();
//
//		ray.setOrigin( cursor );
//		ray.setDirection( new Vector3f( dir.x, dir.y, dir.z ) );
//
//		CollisionResults crs = new CollisionResults();
//		gameGui.getGroundNode().collideWith( ray, crs );
//
//		if( "Wheel_DOWN".equals( name ) )
//		{
//			if( choosePanel )
//			{
//				niftyHandler.changeChoosenElement( false );
//			}
//
//		}
//		else if( "Wheel_UP".equals( name ) )
//		{
//			if( choosePanel )
//			{
//				niftyHandler.changeChoosenElement( true );
//			}
//		}
//
//		else if( "Click".equals( name ) && isPressed )
//		{
//
//			if( crs.getClosestCollision() != null )
//			{
//				gameGui.getNiftyHandler().emptySelectedPanel();
//				Vector2f contactPoint = new Vector2f(
//						crs.getClosestCollision().getContactPoint().x, crs.getClosestCollision()
//								.getContactPoint().z );
//
//				if( listener.getPickedObject( click ) == "GROUND" )
//				{
//					GameManagerProxy.getInstance().computePath(
//							GuiObjectManager.getInstance().getPlayingPlayer(), contactPoint.x,
//							contactPoint.y );
//				}
//				else
//				{
//					// TODO
//					gameGui.getNiftyHandler().setSelectedPanel(
//							GameManager.getInstance().getWorld()
//									.pickedObject( contactPoint.x, contactPoint.y ) );
//				}
//			}
//		}
//		else if( "ChooseItem".equals( name ) )
//		{
//			if( isPressed )
//			{
//				choosePanel = true;
//				niftyHandler.relocateChoosePanel( ( int ) click.x, gameGui.windowHeight()
//						- ( int ) click.y );
//			}
//			else
//			{
//				choosePanel = false;
//				String selectedItem = niftyHandler.getSelectedItem();
//				listener.changeItem( selectedItem );
//
//				niftyHandler.removeChoosePanel();
//				niftyHandler.initChoosenElementPanel();
//			}
//		}
//
//		else if( "attack".equals( name ) && isPressed )
//		{
//
//			// Player player = gameGui.getPlayingPlayer();
//
//			// TODO cambiare azione in base all'item selezionato
//			if( crs.getClosestCollision() != null )
//			{
//
//				Vector2f contactPoint = new Vector2f(
//						crs.getClosestCollision().getContactPoint().x, crs.getClosestCollision()
//								.getContactPoint().z );
//
//				ArrayList<String> hitPlayers = listener.playerAction( contactPoint );
//
//				if( hitPlayers.isEmpty() )
//					System.out.println( "non ho colpito niente" );
//				// else
//				// for( Player pl : p )
//				// System.out.println( "ho colpito " + pl.getName() );
//
//			}
//		}
//	}
// }
