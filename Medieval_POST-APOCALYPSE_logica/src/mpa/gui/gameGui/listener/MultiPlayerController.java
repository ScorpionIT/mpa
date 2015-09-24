package mpa.gui.gameGui.listener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.vecmath.Vector2f;

import mpa.gui.gameGui.playingGUI.GuiObjectManager;

public class MultiPlayerController extends HandlerImplementation
{
	private Socket socket;
	private DataOutputStream outToServer;
	private BufferedReader inFromServer;

	public MultiPlayerController(Socket socket)
	{
		this.socket = socket;
		boolean isOk = true;
		do
		{
			try
			{
				outToServer = new DataOutputStream(socket.getOutputStream());
				inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}
		} while (!isOk);
	}

	@Override
	public void setPause()
	{
		return;
	}

	@Override
	public Map<String, Map<String, Integer>> getPlayersResourceAmount()
	{
		Map<String, Map<String, Integer>> reply = new HashMap<String, Map<String, Integer>>();

		boolean isOk = true;
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				// BufferedReader inFromServer = new BufferedReader( new
				// InputStreamReader(
				// socket.getInputStream() ) );

				outToServer.writeBytes("getPlayerResourcesAmount" + '\n');

				int numberOfPlayer = Integer.parseInt(inFromServer.readLine());

				Map<String, Integer> resources = null;
				String player = null;
				for (int index = 0; index <= numberOfPlayer;)
				{
					String response = inFromServer.readLine();
					String[] fields = response.split("=");

					if (fields[0].equals("Player"))
					{
						if (index != 0)
							reply.put(player, resources);

						resources = new HashMap<String, Integer>();
						index++;
						player = fields[1];
					}
					else
					{
						resources.put(fields[0], Integer.parseInt(fields[1]));
					}
					isOk = true;
				}

			} catch (IOException e)
			{
				isOk = false;
			}
		} while (!isOk);
		return reply;
	}

	@Override
	public String getPickedObject(com.jme3.math.Vector2f click)
	{

		boolean isOk = true;
		String reply = null;
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				// BufferedReader inFromServer = new BufferedReader( new
				// InputStreamReader(
				// socket.getInputStream() ) );

				outToServer.writeBytes("PickedObject" + String.valueOf(click.x) + "," + String.valueOf(click.y) + '\n');
				reply = inFromServer.readLine();
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}
		} while (!isOk);

		return reply;
	}

	@Override
	public void changeItem(String item)
	{
		boolean isOk = true;
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				outToServer.writeBytes("ChangeItem:" + GuiObjectManager.getInstance().getPlayingPlayer() + ":" + item + '\n');
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}

		} while (!isOk);
	}

	@Override
	public void playerAction(com.jme3.math.Vector2f direction)
	{
		boolean isOk = true;
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				outToServer.writeBytes("Do:" + GuiObjectManager.getInstance().getPlayingPlayer() + ":" + String.valueOf(direction.x) + ","
						+ String.valueOf(direction.y) + '\n');
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}

		} while (!isOk);
	}

	@Override
	public boolean occupyProperty(String property)
	{
		boolean isOk = true;
		String reply = null;
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				outToServer.writeBytes("Occupy:" + GuiObjectManager.getInstance().getPlayingPlayer() + ":" + property + '\n');

				BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				reply = inFromServer.readLine();
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}

		} while (!isOk);
		if (reply.equals("OK"))
			return true;
		else
			return false;
	}

	@Override
	public boolean createTower(String property)
	{
		boolean isOk = true;
		do
		{
			try
			{
				outToServer.writeBytes("CreateTower:" + GuiObjectManager.getInstance().getPlayingPlayer() + property + '\n');
				String reply = inFromServer.readLine();
				isOk = true;
				if (reply.split(":")[0].equals("true"))
					return true;
				else
					return false;
			} catch (IOException e)
			{
				isOk = false;
			}
		} while (!isOk);
		return false;
	}

	@Override
	public boolean createMinions(String boss, String target, int quantity)
	{

		boolean isOk = true;
		String reply = null;
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				outToServer.writeBytes("Bless:" + "Minions" + boss + ":" + String.valueOf(quantity) + ":" + target + '\n');

				// BufferedReader inFromServer = new BufferedReader( new
				// InputStreamReader(
				// socket.getInputStream() ) );

				reply = inFromServer.readLine();
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}

		} while (!isOk);
		if (reply.equals("OK"))
			return true;
		else
			return false;
	}

	@Override
	public void updateInformation()
	{
		List<String> deadMinions = new ArrayList<>();
		List<String> deadPlayers = new ArrayList<>();
		List<String> deadTowerCrushers = new ArrayList<>();
		List<String> attackingPlayers = new ArrayList<>();
		List<String> attackingMinions = new ArrayList<>();
		List<String> attackingTowerCrushers = new ArrayList<>();

		for (String m : deadMinions)
			GuiObjectManager.getInstance().killMinion(m);
		for (String p : deadPlayers)
			GuiObjectManager.getInstance().killPlayer(p);
		for (String towerCrusherID : deadTowerCrushers)
			GuiObjectManager.getInstance().killTowerCrusher(towerCrusherID);

		try
		{
			String message = inFromServer.readLine();
			int state = 0; // 0 deadM 1 deadP 2 deadTC 3 Towers 4 AttackingP 5
							// AttackingM 6
							// AttackingT 7 PlayerPosition 8 MinionPosition 9
							// TowerCrusherPosition
			while (!message.equals("EndOfInfo."))
			{
				if (state == 0)
				{
					if (message.equals("DeadPlayers:"))
					{
						state++;
						for (String minion : deadMinions)
							GuiObjectManager.getInstance().killMinion(minion);
					}
					else if (!message.equals("DeadMinions:"))
					{
						deadMinions.add(message);
					}

				}
				else if (state == 1)
				{
					if (message.equals("DeadTowerCrushers:"))
					{
						state++;
						for (String player : deadPlayers)
							GuiObjectManager.getInstance().killPlayer(player);
					}
					else if (!message.equals("DeadPlayers:"))
					{
						deadPlayers.add(message);
					}
				}
				else if (state == 2)
				{
					if (message.equals("Towers:"))
					{
						state++;
						for (String tC : deadTowerCrushers)
							GuiObjectManager.getInstance().killTowerCrusher(tC);
					}
					else if (!message.equals("DeadTowerCrushers:"))
					{
						deadTowerCrushers.add(message);
					}
				}
				else if (state == 3)
				{
					if (message.equals("PlayerAttacks:"))
					{
						state++;
					}
					else if (!message.equals("Towers:"))
					{
						String[] strings = message.split(":");
						String[] positions = strings[1].split(",");
						GuiObjectManager.getInstance().updateTower(
								new javax.vecmath.Vector2f(Float.parseFloat(positions[0]), Float.parseFloat(positions[1])), strings[0],
								Integer.parseInt(strings[2]));
					}
				}
				else if (state == 4)
				{
					if (message.equals("MinionAttacks:"))
					{
						state++;
						for (String player : attackingPlayers)
							GuiObjectManager.getInstance().startPlayerAttackAnimation(player);
					}
					else if (!message.equals("PlayerAttacks:"))
						attackingPlayers.add(message);
				}
				else if (state == 5)
				{
					if (message.equals("TowerCrusherAttacks:"))
					{
						state++;
						for (String minion : attackingMinions)
							GuiObjectManager.getInstance().startMinionAttackAnimation(minion);
					}
					else if (!message.equals("MinionAttacks:"))
						attackingMinions.add(message);
				}
				else if (state == 6)
				{
					if (message.equals("PlayersPositions:"))
					{
						state++;
						for (String tC : attackingTowerCrushers)
							GuiObjectManager.getInstance().startTowerCrusherAttackAnimation(tC);
					}
					else if (!message.equals("TowerCrusherAttacks:"))
						attackingMinions.add(message);
				}
				else if (state == 7)
				{
					if (message.equals("MinionsPositions:"))
						state++;
					else if (!message.equals("PlayersPositions:"))
					{
						String[] fields = message.split(":");
						boolean isMoving = fields[1].equals("true");
						int hp = Integer.parseInt(fields[2]);
						String[] position = fields[3].split(",");
						String[] lookAt = fields[4].split(",");
						GuiObjectManager.getInstance().updatePlayerPosition(fields[0],
								new javax.vecmath.Vector2f(Float.parseFloat(position[0]), Float.parseFloat(position[1])),
								new javax.vecmath.Vector2f(Float.parseFloat(lookAt[0]), Float.parseFloat(lookAt[1])), isMoving, hp);
					}
				}
				else if (state == 8)
				{
					if (message.equals("TowerCrushersPositions:"))
						state++;
					else if (!message.equals("MinionsPositions:"))
					{
						String[] fields = message.split(":");
						boolean isMoving = fields[3].equals("true");
						String[] position = fields[1].split(",");
						String[] lookAt = fields[2].split(",");
						GuiObjectManager.getInstance().updateMinionPosition(fields[0],
								new javax.vecmath.Vector2f(Float.parseFloat(position[0]), Float.parseFloat(position[1])),
								new javax.vecmath.Vector2f(Float.parseFloat(lookAt[0]), Float.parseFloat(lookAt[1])), isMoving, fields[4]);
					}
				}
				else if (state == 9)
				{
					if (!message.equals("TowerCrushersPositions:"))
					{
						String[] fields = message.split(":");
						boolean isMoving = fields[3].equals("true");
						int hp = Integer.parseInt(fields[5]);
						String[] position = fields[1].split(",");
						String[] lookAt = fields[2].split(",");
						GuiObjectManager.getInstance().updateTowerCrusherPosition(fields[0],
								new javax.vecmath.Vector2f(Float.parseFloat(position[0]), Float.parseFloat(position[1])),
								new javax.vecmath.Vector2f(Float.parseFloat(lookAt[0]), Float.parseFloat(lookAt[1])), isMoving, fields[4], hp);
					}
				}

				message = inFromServer.readLine();

			}
		} catch (IOException e)
		{}
	}

	@Override
	public void createStateInformation()
	{

		try
		{
			System.out.println("HO SCRITTO ");
			outToServer.writeBytes("PLAYERS" + '\n');
			String reply = inFromServer.readLine();

			while (!reply.equals("END"))
			{
				String[] split = reply.split(":");

				String[] positionSplit = split[1].split(",");
				Vector2f position = new Vector2f(Float.parseFloat(positionSplit[0]), Float.parseFloat(positionSplit[1]));

				String[] directionSplit = split[2].split(",");
				Vector2f direction = new Vector2f(Float.parseFloat(directionSplit[0]), Float.parseFloat(directionSplit[1]));

				String[] headquarterSplit = split[3].split(",");
				javax.vecmath.Vector2f hqPosition = new javax.vecmath.Vector2f(Float.parseFloat(headquarterSplit[0]),
						Float.parseFloat(headquarterSplit[1]));

				GuiObjectManager.getInstance().addPlayer(split[0], hqPosition, position, direction, Integer.parseInt(split[4]));

				reply = inFromServer.readLine();
			}

			outToServer.writeBytes("FIELD" + '\n');
			reply = inFromServer.readLine();
			while (!reply.equals("END"))
			{
				String[] split = reply.split(":");
				String[] positionSplit = split[1].split(",");
				Vector2f position = new Vector2f(Float.parseFloat(positionSplit[0]), Float.parseFloat(positionSplit[1]));
				GuiObjectManager.getInstance().addResource("FIELD", Integer.parseInt(split[0]), position);
				reply = inFromServer.readLine();
			}

			outToServer.writeBytes("CAVE" + '\n');
			reply = inFromServer.readLine();
			while (!reply.equals("END"))
			{
				String[] split = reply.split(":");
				String[] positionSplit = split[1].split(",");
				Vector2f position = new Vector2f(Float.parseFloat(positionSplit[0]), Float.parseFloat(positionSplit[1]));
				GuiObjectManager.getInstance().addResource("CAVE", Integer.parseInt(split[0]), position);
				reply = inFromServer.readLine();
			}

			outToServer.writeBytes("WOOD" + '\n');
			reply = inFromServer.readLine();
			while (!reply.equals("END"))
			{
				String[] split = reply.split(":");
				String[] positionSplit = split[1].split(",");
				Vector2f position = new Vector2f(Float.parseFloat(positionSplit[0]), Float.parseFloat(positionSplit[1]));
				GuiObjectManager.getInstance().addResource("WOOD", Integer.parseInt(split[0]), position);
				reply = inFromServer.readLine();
			}

			outToServer.writeBytes("MINE" + '\n');
			reply = inFromServer.readLine();
			while (!reply.equals("END"))
			{
				String[] split = reply.split(":");
				String[] positionSplit = split[1].split(",");
				Vector2f position = new Vector2f(Float.parseFloat(positionSplit[0]), Float.parseFloat(positionSplit[1]));
				GuiObjectManager.getInstance().addResource("MINE", Integer.parseInt(split[0]), position);
				reply = inFromServer.readLine();
			}

			outToServer.writeBytes("WORLD_DIMENSION" + '\n');

			reply = inFromServer.readLine();
			System.out.println(reply);
			GuiObjectManager.getInstance().setWorldDimension(Float.parseFloat(reply));
		} catch (NumberFormatException | IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void computePath(com.jme3.math.Vector2f click)
	{
		boolean isOk = true;
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				outToServer.writeBytes("GoThere:" + GuiObjectManager.getInstance().getPlayingPlayer() + ":" + String.valueOf(click.x) + ","
						+ String.valueOf(click.y) + '\n');
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}

		} while (!isOk);
	}

	@Override
	public String getPickedObjectOwner(String objectType, String objectID)
	{
		boolean isOk = true;
		String reply = null;
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				outToServer.writeBytes("InfoOn:" + "OWNER:" + objectType + "," + objectID + '\n');

				BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				reply = inFromServer.readLine();
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}

		} while (!isOk);

		return reply;
	}

	@Override
	public int getPickedObjectProductivity(String objectType, String objectID)
	{
		boolean isOk = true;
		String reply = null;
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				outToServer.writeBytes("InfoOn:" + "PRODUCTIVITY:" + objectType + "," + objectID + '\n');

				BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				reply = inFromServer.readLine();
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}

		} while (!isOk);

		return Integer.parseInt(reply);
	}

	@Override
	public Map<String, Integer> getPlayerResourcesAmount(String playerName)
	{
		boolean isOk = true;
		Map<String, Integer> reply = new HashMap<String, Integer>();
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				outToServer.writeBytes("PlayerInfo:" + playerName + ":" + "RESOURCES" + '\n');

				// BufferedReader inFromServer = new BufferedReader( new
				// InputStreamReader(
				// socket.getInputStream() ) );

				String message = inFromServer.readLine();

				while (!message.equals("."))
				{
					String[] amount = message.split("=");
					reply.put(amount[0], Integer.parseInt(amount[1]));
					message = inFromServer.readLine();
				}
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}

		} while (!isOk);

		return reply;
	}

	@Override
	public int getNumberOfPlayer()
	{
		boolean isOk = true;
		int reply = 0;
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				outToServer.writeBytes("NumberOfPlayer" + '\n');

				// BufferedReader inFromServer = new BufferedReader( new
				// InputStreamReader(
				// socket.getInputStream() ) );

				reply = Integer.parseInt(inFromServer.readLine());
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}

		} while (!isOk);

		return reply;
	}

	@Override
	public void computePath(com.jme3.math.Vector2f click, String playerName)
	{
		// NOT TO DO

	}

	@Override
	public String getPlayerLevel(String player)
	{
		boolean isOk = true;
		String reply = null;
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				outToServer.writeBytes("PlayerInfo:" + player + ":" + "LEVEL" + '\n');

				// BufferedReader inFromServer = new BufferedReader( new
				// InputStreamReader(
				// socket.getInputStream() ) );

				reply = inFromServer.readLine();
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}

		} while (!isOk);

		return reply;
	}

	@Override
	public int getPlayerHP(String player)
	{
		boolean isOk = true;
		int reply = 0;
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				outToServer.writeBytes("PlayerInfo:" + player + ":" + "HP" + '\n');

				// BufferedReader inFromServer = new BufferedReader( new
				// InputStreamReader(
				// socket.getInputStream() ) );

				reply = Integer.parseInt(inFromServer.readLine());
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}

		} while (!isOk);

		return reply;

	}

	@Override
	public int getPlayerMP(String player)
	{
		boolean isOk = true;
		int reply = 0;
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				outToServer.writeBytes("PlayerInfo:" + player + ":" + "MP" + '\n');

				// BufferedReader inFromServer = new BufferedReader( new
				// InputStreamReader(
				// socket.getInputStream() ) );

				reply = Integer.parseInt(inFromServer.readLine());
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}

		} while (!isOk);

		return reply;
	}

	@Override
	public Set<String> getPlayersName()
	{
		boolean isOk = true;
		Set<String> reply = new HashSet<>();
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				outToServer.writeBytes("PlayersNames" + '\n');

				// BufferedReader inFromServer = new BufferedReader( new
				// InputStreamReader(
				// socket.getInputStream() ) );

				String message = inFromServer.readLine();

				while (!message.equals("."))
				{
					reply.add(message);
					message = inFromServer.readLine();
				}
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}

		} while (!isOk);

		return reply;

	}

	@Override
	public boolean createTowerCrusher(String boss, String target)
	{
		boolean isOk = true;
		String reply = null;
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				outToServer.writeBytes("Bless:" + "TowerCrusher" + boss + ":" + ":" + target + '\n');

				// BufferedReader inFromServer = new BufferedReader( new
				// InputStreamReader(
				// socket.getInputStream() ) );

				reply = inFromServer.readLine();
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}

		} while (!isOk);
		if (reply.equals("OK"))
			return true;
		else
			return false;
	}

	@Override
	public boolean buyHPPotion(String playerName)
	{
		boolean isOk = true;
		String reply = null;
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				outToServer.writeBytes("Buy:" + playerName + ":" + "HP" + '\n');

				// BufferedReader inFromServer = new BufferedReader( new
				// InputStreamReader(
				// socket.getInputStream() ) );

				reply = inFromServer.readLine();
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}

		} while (!isOk);

		if (reply.equals("OK"))
			return true;
		else
			return false;
	}

	@Override
	public boolean buyMPPotion(String playerName)
	{
		boolean isOk = true;
		String reply = null;
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				outToServer.writeBytes("Buy:" + playerName + ":" + "MP" + '\n');

				// BufferedReader inFromServer = new BufferedReader( new
				// InputStreamReader(
				// socket.getInputStream() ) );

				reply = inFromServer.readLine();
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}

		} while (!isOk);

		if (reply.equals("OK"))
			return true;
		else
			return false;
	}

	@Override
	public boolean buyGranade(String playerName)
	{
		boolean isOk = true;
		String reply = null;
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				outToServer.writeBytes("Buy:" + playerName + ":" + "GRANADE" + '\n');

				// BufferedReader inFromServer = new BufferedReader( new
				// InputStreamReader(
				// socket.getInputStream() ) );

				reply = inFromServer.readLine();
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}

		} while (!isOk);

		if (reply.equals("OK"))
			return true;
		else
			return false;
	}

	@Override
	public int getPlayerHPPotion(String playerName)
	{
		boolean isOk = true;
		int reply = 0;
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				outToServer.writeBytes("GetPotionAmount:" + playerName + ":" + "HP" + '\n');

				// BufferedReader inFromServer = new BufferedReader( new
				// InputStreamReader(
				// socket.getInputStream() ) );

				reply = Integer.parseInt(inFromServer.readLine());
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}

		} while (!isOk);

		return reply;
	}

	@Override
	public int getPlayerMPPotion(String playerName)
	{
		boolean isOk = true;
		int reply = 0;
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				outToServer.writeBytes("GetPotionAmount:" + playerName + ":" + "MP" + '\n');

				// BufferedReader inFromServer = new BufferedReader( new
				// InputStreamReader(
				// socket.getInputStream() ) );

				reply = Integer.parseInt(inFromServer.readLine());
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}

		} while (!isOk);

		return reply;

	}

	@Override
	public int getPlayerGranade(String playerName)
	{
		boolean isOk = true;
		int reply = 0;
		do
		{
			try
			{
				// DataOutputStream outToServer = new DataOutputStream(
				// socket.getOutputStream() );
				outToServer.writeBytes("GetPotionAmount:" + playerName + ":" + "GRANADE" + '\n');

				// BufferedReader inFromServer = new BufferedReader( new
				// InputStreamReader(
				// socket.getInputStream() ) );

				reply = Integer.parseInt(inFromServer.readLine());
				isOk = true;
			} catch (IOException e)
			{
				isOk = false;
			}

		} while (!isOk);

		return reply;

	}

	@Override
	public String getMinionBoss(String ID)
	{
		try
		{
			outToServer.writeBytes("InfoPlayer::MINION," + ID + '\n');
			return inFromServer.readLine();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void endGame()
	{
		return;

	}

	@Override
	public boolean upgradeLevel(String playingPlayer)
	{
		return false;
	}

}
