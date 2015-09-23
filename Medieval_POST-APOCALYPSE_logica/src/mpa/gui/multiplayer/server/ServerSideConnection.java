package mpa.gui.multiplayer.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.vecmath.Vector2f;

import mpa.core.logic.GameManagerProxy;
import mpa.core.logic.Pair;
import mpa.core.multiplayer.processingChain.BuyPotionHandler;
import mpa.core.multiplayer.processingChain.ChangeItemHandler;
import mpa.core.multiplayer.processingChain.ComputePathHandler;
import mpa.core.multiplayer.processingChain.CreateMinionsHandler;
import mpa.core.multiplayer.processingChain.CreateTower;
import mpa.core.multiplayer.processingChain.GetPickedObjectHandler;
import mpa.core.multiplayer.processingChain.GetPickedObjectInfo;
import mpa.core.multiplayer.processingChain.GetPotionAmountHandler;
import mpa.core.multiplayer.processingChain.GetResourcesAmountHandler;
import mpa.core.multiplayer.processingChain.OccupyPropertyHandler;
import mpa.core.multiplayer.processingChain.PlayerActionHandler;
import mpa.core.multiplayer.processingChain.PlayerInfoHandler;
import mpa.core.multiplayer.processingChain.PlayersInfosHandler;
import mpa.core.multiplayer.processingChain.ProcessingChain;
import mpa.core.multiplayer.processingChain.UpdateInfoHandler;

public class ServerSideConnection extends Thread {
	public enum ConnectionState {
		GETTING_MAP, GAME_SETTING, PLAYING
	}

	private ConnectionState state = ConnectionState.GETTING_MAP;

	private String mapPath;
	private boolean keepConnectionOn = true;
	private DataOutputStream outToClient;
	private BufferedReader inFromClient;
	private ProcessingChain headOfTheChain = null;
	private WelcomeServer welcomeServer;

	private Socket socket;

	private boolean allReadyPlayers = false;
	private boolean playing = false;

	private boolean gettingFirstInformation = true;

	public ServerSideConnection(Socket socket, String mapPath,
			WelcomeServer welcomeServer) {
		this.socket = socket;
		this.mapPath = mapPath;
		this.welcomeServer = welcomeServer;
		boolean isOk = true;

		do {
			try {
				outToClient = new DataOutputStream(socket.getOutputStream());
				inFromClient = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				isOk = true;
			} catch (IOException e) {
				isOk = false;
			}
		} while (!isOk);
	}

	@Override
	public void run() {
		try {
			while (keepConnectionOn) {
				System.out.println(state);
				switch (state) {
				case GETTING_MAP:
					if (inFromClient.readLine().equals("Enter"))
						outToClient.writeBytes("OK" + '\n');
					if (inFromClient.readLine().equals("GetMap"))
						givingTheMap();
					break;
				case GAME_SETTING:
					String readLine = inFromClient.readLine();
					String[] split = readLine.split(",");
					if (split[0].equals("GET")) {
						if (welcomeServer.occupy(
								new Pair<Float, Float>(Float
										.parseFloat(split[1]), Float
										.parseFloat(split[2])), socket
										.getInetAddress())) {

							outToClient.writeBytes("OK" + '\n');

						} else
							outToClient.writeBytes("NO" + '\n');

					}
					readLine = inFromClient.readLine();
					String[] split2 = readLine.split(":");
					if (split2[0].equals("READY")) {
						welcomeServer.addReadyPlayer(split2[1],
								socket.getInetAddress());
						state = ConnectionState.PLAYING;
					}
					break;
				case PLAYING:
					if (allReadyPlayers) {
						outToClient.writeBytes("LETSGO" + '\n');
						allReadyPlayers = false;
						playing = true;
					} else if (playing) {
						if (gettingFirstInformation) {
							initGame();
							gettingFirstInformation = false;

						} else {
							String request = inFromClient.readLine();
							if (headOfTheChain == null)
								initChain();
							String[] processRequest = headOfTheChain
									.processRequest(request);
							for (String message : processRequest)
								outToClient.writeBytes(message + '\n');
						}
					}
					break;
				}
			}
			outToClient.writeBytes("CLOSING");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initGame() {

		try {
			if (inFromClient.readLine().equals("PLAYERS")) {
				List<String> message = new ArrayList<>();
				message.add(new String("PlayersPositions:"));
				Map<String, javax.vecmath.Vector2f[]> playersPositions = GameManagerProxy
						.getInstance().getPlayersPositions();
				for (String player : playersPositions.keySet()) {
					javax.vecmath.Vector2f[] position = playersPositions
							.get(player);

					javax.vecmath.Vector2f heaquarterPosition = GameManagerProxy
							.getInstance().getHeaquarterPosition(player);
					String hp = String.valueOf(GameManagerProxy.getInstance()
							.getPLayerHP(player));
					message.add(new String(player + ":"
							+ String.valueOf(position[0].x) + ","
							+ String.valueOf(position[0].y))
							+ ":"
							+ String.valueOf(position[1].x)
							+ ","
							+ String.valueOf(position[1].y)
							+ ":"
							+ String.valueOf(heaquarterPosition.x)
							+ ","
							+ String.valueOf(heaquarterPosition.y) + ":" + hp);

				}
				message.add("END");

				for (String string : message) {
					outToClient.writeBytes(string + '\n');
				}

			}

			if (inFromClient.readLine().equals("FIELD")) {
				Map<String, Vector2f> fields = GameManagerProxy.getInstance()
						.getFields();
				List<String> createMessages = createMessages(fields);
				for (String string : createMessages) {
					outToClient.writeBytes(string + '\n');
				}

			}

			if (inFromClient.readLine().equals("CAVE")) {
				Map<String, Vector2f> caves = GameManagerProxy.getInstance()
						.getCaves();
				List<String> createMessages = createMessages(caves);
				for (String string : createMessages) {
					outToClient.writeBytes(string + '\n');
				}

			}
			if (inFromClient.readLine().equals("WOOD")) {
				Map<String, Vector2f> woods = GameManagerProxy.getInstance()
						.getWoods();
				List<String> createMessages = createMessages(woods);
				for (String string : createMessages) {
					outToClient.writeBytes(string + '\n');
				}

			}
			if (inFromClient.readLine().equals("MINE")) {
				Map<String, Vector2f> mines = GameManagerProxy.getInstance()
						.getMines();
				List<String> createMessages = createMessages(mines);
				for (String string : createMessages) {
					outToClient.writeBytes(string + '\n');
				}

			}

			if (inFromClient.equals("WORLD_DIMESION")) {
				float worldDimension = GameManagerProxy.getInstance()
						.worldDimension();
				String dimension = new String(
						String.valueOf(worldDimension) + '\n');
				outToClient.writeBytes(dimension);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<String> createMessages(Map<String, Vector2f> objects) {
		java.util.List<String> message = new ArrayList<String>();
		for (String ID : objects.keySet()) {
			Vector2f position = objects.get(ID);
			message.add(new String(ID + ":" + String.valueOf(position.x) + ","
					+ String.valueOf(position.y)));
		}
		message.add("END");
		return message;
	}

	private void initChain() {

		BuyPotionHandler buyPotionHandler = new BuyPotionHandler(null);
		ChangeItemHandler changeItemHandler = new ChangeItemHandler(
				buyPotionHandler);
		ComputePathHandler computePathHandler = new ComputePathHandler(
				changeItemHandler);
		CreateMinionsHandler createMinionsHandler = new CreateMinionsHandler(
				computePathHandler);
		CreateTower createTower = new CreateTower(createMinionsHandler);
		GetPickedObjectHandler getPickedObjectHandler = new GetPickedObjectHandler(
				createTower);
		GetPickedObjectInfo getPickedObjectInfo = new GetPickedObjectInfo(
				getPickedObjectHandler);
		GetPotionAmountHandler getPotionAmountHandler = new GetPotionAmountHandler(
				getPickedObjectInfo);
		GetResourcesAmountHandler getResourcesAmountHandler = new GetResourcesAmountHandler(
				getPotionAmountHandler);
		OccupyPropertyHandler occupyPropertyHandler = new OccupyPropertyHandler(
				getResourcesAmountHandler);
		PlayerActionHandler playerActionHandler = new PlayerActionHandler(
				occupyPropertyHandler);
		PlayersInfosHandler playersInfosHandler = new PlayersInfosHandler(
				playerActionHandler);
		UpdateInfoHandler updateInfoHandler = new UpdateInfoHandler(
				playersInfosHandler, welcomeServer);
		headOfTheChain = new PlayerInfoHandler(updateInfoHandler);

	}

	private void givingTheMap() {

		try {
			BufferedReader br = new BufferedReader(new FileReader(mapPath));
			String line = br.readLine();

			outToClient.writeBytes("BEGIN" + '\n');
			while (line != null) {
				outToClient.writeBytes(line + '\n');
				line = br.readLine();
			}
			outToClient.writeBytes("END" + '\n');
			br.close();

		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			// TODO: handle exception
		}

		state = ConnectionState.GAME_SETTING;

	}

	public void stopThread() {
		keepConnectionOn = false;
	}

	public void setAllPlayersReasy() {
		allReadyPlayers = true;

	}

}
