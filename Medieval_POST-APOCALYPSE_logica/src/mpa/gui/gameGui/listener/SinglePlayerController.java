package mpa.gui.gameGui.listener;

import java.util.List;
import java.util.Map;
import java.util.Set;

import mpa.core.logic.GameManagerProxy;
import mpa.gui.gameGui.playingGUI.GuiObjectManager;

import com.jme3.math.Vector2f;

public class SinglePlayerController extends HandlerImplementation
{
	private GameManagerProxy gManagerProxy = GameManagerProxy.getInstance();

	@Override
	public void setPause()
	{
		gManagerProxy.setPause();
	}

	@Override
	public Map<String, Map<String, Integer>> getPlayersResourceAmount()
	{
		return gManagerProxy.getPlayersResourceAmount();
	}

	@Override
	public String getPickedObject( Vector2f click )
	{
		return GameManagerProxy.getInstance().getPickedObject( click );
	}

	@Override
	public String getPickedObjectOwner( String objectType, String objectID )
	{
		return gManagerProxy.getObjectOwner( objectType, objectID );
	}

	@Override
	public int getPickedObjectProductivity( String objectType, String objectID )
	{
		return gManagerProxy.getObjectProductivity( objectType, objectID );
	}

	@Override
	public void changeItem( String item )
	{
		gManagerProxy.changeSelectedItem( GuiObjectManager.getInstance().getPlayingPlayer(), item );
	}

	@Override
	public void playerAction( Vector2f direction )
	{
		gManagerProxy.playerAction( GuiObjectManager.getInstance().getPlayingPlayer(),
				new javax.vecmath.Vector2f( direction.x, direction.y ) );
	}

	@Override
	public boolean occupyProperty( String property )
	{
		return gManagerProxy.occupyProperty( GuiObjectManager.getInstance().getPlayingPlayer(),
				property );
	}

	@Override
	public boolean createTower( String property )
	{
		javax.vecmath.Vector2f towerAvaiblePosition = gManagerProxy
				.getTowerAvaiblePosition( property );
		if( towerAvaiblePosition != null )
		{
			String tower = gManagerProxy.createTower( GuiObjectManager.getInstance()
					.getPlayingPlayer(), towerAvaiblePosition, property );

			if( !tower.equals( "" ) )
			{
				return true;
			}
		}
		return false;

	}

	@Override
	public boolean createMinions( String boss, String target, int quantity )
	{
		List<String> createMinions = gManagerProxy.createMinions( boss, quantity, target );
		System.out.println( "SONO QUI " + createMinions );
		if( createMinions != null && !createMinions.isEmpty() )
			return true;
		else
			return false;
		// for (String ID : minions)
		// GuiObjectManager.getInstance().addMinion(ID, boss);

	}

	@Override
	public void updateInformation()
	{
		List<String> deadMinions = gManagerProxy.takeDeadMinions();
		List<String> deadPlayers = gManagerProxy.takeDeadPlayers();
		List<String> deadTowerCrushers = gManagerProxy.takeDeadTowerCrushers();
		Map<String, javax.vecmath.Vector2f> towers = gManagerProxy.getTowers();
		List<String> attackingPlayers = gManagerProxy.takePlayerAttacks();
		List<String> attackingMinions = gManagerProxy.takeMinionAttacks();
		List<String> attackingTowerCrushers = gManagerProxy.takeTowerCrusherAttacks();
		Map<String, javax.vecmath.Vector2f[]> playersPositions = gManagerProxy
				.getPlayersPositions();

		Map<String, javax.vecmath.Vector2f[]> minionsPositions = gManagerProxy
				.getMinionsPositions();
		Map<String, javax.vecmath.Vector2f[]> towerCrusherPositions = gManagerProxy
				.getTowerCrushersPositions();

		for( String m : deadMinions )
			GuiObjectManager.getInstance().killMinion( m );
		for( String p : deadPlayers )
			GuiObjectManager.getInstance().killPlayer( p );
		for( String towerCrusherID : deadTowerCrushers )
			GuiObjectManager.getInstance().killTowerCrusher( towerCrusherID );

		for( String p : playersPositions.keySet() )
		{

			javax.vecmath.Vector2f[] positions = playersPositions.get( p );
			GuiObjectManager.getInstance().updatePlayerPosition( p, positions[0], positions[1],
					gManagerProxy.isMovingPlayer( p ), gManagerProxy.getPLayerHP( p ) );
		}

		for( String m : minionsPositions.keySet() )
		{
			// if (!deadMinions.contains(m))
			{
				javax.vecmath.Vector2f[] positions = minionsPositions.get( m );
				GuiObjectManager.getInstance().updateMinionPosition( m, positions[0], positions[1],
						gManagerProxy.isMovingMinion( m ), gManagerProxy.getMinionBoss( m ) );
			}
		}

		for( String towerCrusherID : towerCrusherPositions.keySet() )
		{
			System.out.println( "!!!!!!!!!!!!!!" );
			System.out.println( towerCrusherID );
			System.out.println( "!!!!!!!!!!!!!!" );
			javax.vecmath.Vector2f[] positions = towerCrusherPositions.get( towerCrusherID );
			GuiObjectManager.getInstance().updateTowerCrusherPosition( towerCrusherID,
					positions[0], positions[1],
					gManagerProxy.isMovingTowerCrusher( towerCrusherID ),
					gManagerProxy.getTowerCrusherBoss( towerCrusherID ),
					gManagerProxy.getTowerCrusherLife( towerCrusherID ) );
		}
		for( String playerName : attackingPlayers )
		{
			GuiObjectManager.getInstance().startPlayerAttackAnimation( playerName );
		}
		for( String idMinion : attackingMinions )
		{
			GuiObjectManager.getInstance().startMinionAttackAnimation( idMinion );

		}

		for( String towerID : towers.keySet() )
		{
			GuiObjectManager.getInstance().updateTower( towers.get( towerID ), towerID,
					gManagerProxy.getLifeTower( towerID ) );
		}
		for( String towerCrusherID : attackingTowerCrushers )
		{
			GuiObjectManager.getInstance().startTowerCrusherAttackAnimation( towerCrusherID );
		}
	}

	@Override
	public void createStateInformation()
	{
		Map<String, javax.vecmath.Vector2f[]> initPs = gManagerProxy.getPlayers();
		for( String p : initPs.keySet() )
		{
			javax.vecmath.Vector2f[] positions = initPs.get( p );
			System.out.println( "positions= " + positions );
			System.out.println( GuiObjectManager.getInstance() );
			GuiObjectManager.getInstance().addPlayer( p, positions[2], positions[0], positions[1],
					gManagerProxy.getPLayerHP( p ) );
		}
		Map<String, javax.vecmath.Vector2f> fields = gManagerProxy.getFields();
		Map<String, javax.vecmath.Vector2f> caves = gManagerProxy.getCaves();
		Map<String, javax.vecmath.Vector2f> woods = gManagerProxy.getWoods();
		Map<String, javax.vecmath.Vector2f> mines = gManagerProxy.getMines();

		for( String s : fields.keySet() )
			GuiObjectManager.getInstance().addResource( "FIELD", Integer.parseInt( s ),
					fields.get( s ) );
		for( String s : caves.keySet() )
			GuiObjectManager.getInstance().addResource( "CAVE", Integer.parseInt( s ),
					caves.get( s ) );
		for( String s : woods.keySet() )
			GuiObjectManager.getInstance().addResource( "WOOD", Integer.parseInt( s ),
					woods.get( s ) );
		for( String s : mines.keySet() )
			GuiObjectManager.getInstance().addResource( "MINE", Integer.parseInt( s ),
					mines.get( s ) );

		GuiObjectManager.getInstance().setWorldDimension( gManagerProxy.worldDimension() );
	}

	@Override
	public void computePath( Vector2f click )
	{
		gManagerProxy.computePath( GuiObjectManager.getInstance().getPlayingPlayer(), click.x,
				click.y );
	}

	@Override
	public Map<String, Integer> getPlayerResourcesAmount( String playerName )
	{
		return gManagerProxy.getPlayerResourcesAmout( playerName );
	}

	@Override
	public int getNumberOfPlayer()
	{
		return gManagerProxy.getNumberOfPlayer();
	}

	@Override
	public void computePath( Vector2f click, String playerName )
	{
		gManagerProxy.computePath( playerName, click.x, click.y );
	}

	@Override
	public String getPlayerLevel( String player )
	{
		return gManagerProxy.getPlayerLevel( player );
	}

	@Override
	public int getPlayerHP( String playerName )
	{
		return gManagerProxy.getPLayerHP( playerName );
	}

	@Override
	public int getPlayerMP( String playerName )
	{
		return gManagerProxy.getPlayerMP( playerName );
	}

	@Override
	public Set<String> getPlayersName()
	{
		return gManagerProxy.getPlayersName();
	}

	@Override
	public boolean createTowerCrusher( String boss, String target )
	{
		String towerCrusher = gManagerProxy.createTowerCrushers( boss, target );
		// GuiObjectManager.getInstance().addTowerCrusher(towerCrusher.get(0));
		if( towerCrusher == null )
			return false;
		else
			return true;

	}

	@Override
	public boolean buyHPPotion( String playerName )
	{
		return gManagerProxy.buyPotion( playerName, "HP" );
	}

	@Override
	public boolean buyMPPotion( String playerName )
	{
		return gManagerProxy.buyPotion( playerName, "MP" );
	}

	@Override
	public boolean buyGranade( String playerName )
	{
		return gManagerProxy.buyPotion( playerName, "GRANADE" );
	}

	@Override
	public int getPlayerHPPotion( String playerName )
	{
		return gManagerProxy.getPotionAmount( playerName, "HP" );
	}

	@Override
	public int getPlayerMPPotion( String playerName )
	{
		return gManagerProxy.getPotionAmount( playerName, "MP" );
	}

	@Override
	public int getPlayerGranade( String playerName )
	{
		return gManagerProxy.getPotionAmount( playerName, "GRANADE" );
	}

	@Override
	public String getMinionBoss( String ID )
	{
		return gManagerProxy.getMinionBoss( ID );
	}

	@Override
	public void endGame()
	{
		gManagerProxy.endGame();
	}

}
