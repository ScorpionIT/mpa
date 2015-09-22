package mpa.core.multiplayer.processingChain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.vecmath.Vector2f;

import mpa.core.logic.GameManagerProxy;

public class UpdateInfoHandler extends ProcessingChain
{

	public UpdateInfoHandler( ProcessingChain next )
	{
		super( next );
	}

	@Override
	public String[] processRequest( String request )
	{
		if( request.equals( "UpdateInfo" ) )
		{
			List<String> message = new ArrayList<>();

			// dead minions
			message.add( new String( "DeadMinions:" ) );
			List<String> deadMinions = GameManagerProxy.getInstance().takeDeadMinions();
			for( String minion : deadMinions )
				message.add( minion );

			message.add( new String( "DeadPlayers:" ) );
			List<String> deadPlayers = GameManagerProxy.getInstance().takeDeadPlayers();
			for( String player : deadPlayers )
				message.add( player );

			message.add( new String( "DeadTowerCrushers:" ) );
			List<String> deadTowerCrushers = GameManagerProxy.getInstance().takeDeadTowerCrushers();
			for( String tC : deadTowerCrushers )
				message.add( tC );

			message.add( new String( "Towers:" ) );
			Map<String, Vector2f> towers = GameManagerProxy.getInstance().getTowers();
			for( String tower : towers.keySet() )
			{
				Vector2f position = towers.get( tower );
				message.add( tower + ":" + String.valueOf( position.x ) + ","
						+ String.valueOf( position.y ) );
			}

			message.add( new String( "PlayerAttacks:" ) );
			List<String> playerAttacks = GameManagerProxy.getInstance().takePlayerAttacks();
			for( String player : playerAttacks )
				message.add( player );

			message.add( new String( "MinionAttacks:" ) );
			List<String> minionAttacks = GameManagerProxy.getInstance().takeMinionAttacks();
			for( String minion : minionAttacks )
				message.add( minion );

			message.add( new String( "TowerCrusherAttacks:" ) );
			List<String> towerCrusherAttacks = GameManagerProxy.getInstance()
					.takeTowerCrusherAttacks();
			for( String tC : towerCrusherAttacks )
				message.add( tC );

			message.add( new String( "PlayersPositions:" ) );
			Map<String, Vector2f[]> playersPositions = GameManagerProxy.getInstance()
					.getPlayersPositions();
			for( String player : playersPositions.keySet() )
			{
				Vector2f[] position = playersPositions.get( player );
				String isMoving = ( GameManagerProxy.getInstance().isMovingPlayer( player ) ) ? new String(
						"true" ) : new String( "false" );
				String hp = String.valueOf( GameManagerProxy.getInstance().getPLayerHP( player ) );
				message.add( new String( player + ":" + isMoving + ":" + hp + ":"
						+ String.valueOf( position[0].x ) + "," + String.valueOf( position[0].y ) )
						+ ":"
						+ String.valueOf( position[1].x )
						+ ","
						+ String.valueOf( position[1].y ) );
			}

			message.add( new String( "MinionsPositions:" ) );
			Map<String, Vector2f[]> minionsPositions = GameManagerProxy.getInstance()
					.getMinionsPositions();
			for( String minion : minionsPositions.keySet() )
			{
				Vector2f[] position = minionsPositions.get( minion );
				String isMoving = ( GameManagerProxy.getInstance().isMovingMinion( minion ) ) ? new String(
						"true" ) : new String( "false" );
				String boss = GameManagerProxy.getInstance().getMinionBoss( minion );
				message.add( new String( minion + ":" + String.valueOf( position[0].x ) + ","
						+ String.valueOf( position[0].y ) )
						+ ":"
						+ String.valueOf( position[1].x )
						+ ","
						+ String.valueOf( position[1].y ) + ":" + isMoving + ":" + boss );
			}

			message.add( new String( "TowerCrushersPositions:" ) );
			Map<String, Vector2f[]> towerCrushersPositions = GameManagerProxy.getInstance()
					.getTowerCrushersPositions();
			for( String tC : towerCrushersPositions.keySet() )
			{
				Vector2f[] position = towerCrushersPositions.get( tC );
				String isMoving = ( GameManagerProxy.getInstance().isMovingTowerCrusher( tC ) ) ? new String(
						"true" ) : new String( "false" );
				String boss = GameManagerProxy.getInstance().getTowerCrusherBoss( tC );
				String hp = String
						.valueOf( GameManagerProxy.getInstance().getTowerCrusherLife( tC ) );
				message.add( new String( tC + ":" + String.valueOf( position[0].x ) + ","
						+ String.valueOf( position[0].y ) )
						+ ":"
						+ String.valueOf( position[1].x )
						+ ","
						+ String.valueOf( position[1].y ) + ":" + isMoving + ":" + boss + ":" + hp );
			}
			message.add( new String( "EndOfInfo." ) );
			int index = 0;
			String[] reply = new String[message.size()];
			for( String s : message )
				reply[index++] = s;
			return reply;
		}
		else if( hasNext() )
			return next.processRequest( request );
		else
			return super.processRequest( request );
	}
}
